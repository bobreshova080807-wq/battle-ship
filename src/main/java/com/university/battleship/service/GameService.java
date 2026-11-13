package com.university.battleship.service;

import com.university.battleship.event.*;
import com.university.battleship.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Сервис игры в морской бой.
 *
 * <p>Отвечает за:
 * <ul>
 *   <li>Применение выстрела</li>
 *   <li>Обработку взрывов мин</li>
 *   <li>Определение победителя</li>
 * </ul>
 *
 * <p>Механика не печатает в консоль — возвращает список событий ({@link GameEvent}).
 */
public final class GameService {

    private Random random;

    /**
     * Создаёт сервис с заданным генератором случайных чисел.
     *
     * @param random генератор (с задаваемым зерном для воспроизводимости)
     */
    public GameService(Random random) {
        this.random = random;
    }

    /**
     * Применяет выстрел игрока по полю противника.
     *
     * <p>Обрабатывает все возможные результаты:
     * <ul>
     *   <li>Промах (вода)</li>
     *   <li>Попадание в корабль (HIT или SUNK)</li>
     *   <li>Попадание в подводную лодку (скрыта)</li>
     *   <li>Попадание в мину (взрыв по площади)</li>
     *   <li>Повторный выстрел (ALREADY_SHOT)</li>
     * </ul>
     *
     * @param shooter игрок, который стреляет
     * @param victim  игрок, по которому стреляют
     * @param coord   координаты выстрела
     * @return список событий, произошедших в результате выстрела
     */
    public List<GameEvent> applyShot(Player shooter, Player victim, Coordinate coord){
        List<GameEvent> events = new ArrayList<>();

        // Проверяем, не стреляли ли уже сюда
        if(shooter.knowledge().isShot(coord)){
            events.add(new ShotEvent(shooter.name(), coord, ShotResult.ALREADY_SHOT));
            return events;
        }

        Board victimBoard = victim.board();
        CellState state = victimBoard.getState(coord);

        ShotResult result;

        switch (state){
            case WATER -> {
                result = ShotResult.MISS;
                victimBoard.updateCell(coord, CellState.MISS);
                shooter.knowledge().update(coord, CellState.MISS);
            }
            case SHIP, MINESWEEPER -> {
                Ship ship = victimBoard.getFleet().getShipAt(coord);
                if (ship == null){
                    result = ShotResult.MISS;
                    victimBoard.updateCell(coord, CellState.MISS);
                    shooter.knowledge().update(coord, CellState.MISS);
                }
                boolean sunk = ship.hit(coord);

                if (sunk){
                    result = ShotResult.SUNK;
                    victimBoard.updateCell(coord, CellState.SUNK);
                    shooter.knowledge().update(coord, CellState.SUNK);

                    for (Coordinate cell: ship.getCells()){
                        victimBoard.updateCell(cell, CellState.SUNK);
                        shooter.knowledge().update(cell, CellState.SUNK);
                    }

                    events.add(new ShipSunkEvent(shooter.name(), victim.name(), ship.getCells(), ship.getType()));
                } else {
                  result = ShotResult.HIT;
                  victimBoard.updateCell(coord, CellState.HIT);
                  shooter.knowledge().update(coord, CellState.HIT);
                }
            }

            case SUBMARINE -> {
                result = ShotResult.SUBMARINE_MISSED;
                victimBoard.updateCell(coord, CellState.MISS);
                shooter.knowledge().update(coord, CellState.MISS);
            }

            case MINE -> {
                Mine mine = victimBoard.getFleet().getMineAt(coord);
                result = ShotResult.MINE_HIT;
                victimBoard.updateCell(coord, CellState.MINE_EXPLODED);
                shooter.knowledge().update(coord, CellState.MINE_EXPLODED);

                List<Coordinate> hurt = explodeMine(victim, mine, shooter);
                events.add(new MineExplodedEvent(shooter.name(), mine.getCoord(), mine.getRadius(), hurt));
            }

            default -> {
                result = ShotResult.MISS;
            }

        }

        events.add(new ShotEvent(shooter.name(), coord, result));
        return events;
    }

    /**
     * Взрыв мины по площади.
     * Повреждает корабли в радиусе взрыва.
     *
     * @param victim  игрок, чья мина взорвалась
     * @param mine    взорвавшаяся мина
     * @param shooter игрок, который подорвался на мине
     * @return список координат клеток кораблей, повреждённых взрывом
     */
    private List<Coordinate> explodeMine(Player victim, Mine mine, Player shooter){
        List<Coordinate> hurt = new ArrayList<>();
        List<Coordinate> area = mine.getCoord().inRadius(mine.getRadius());

        for (Coordinate cell: area){
            Ship ship = victim.fleet().getShipAt(cell);

            // Проверяем, что корабль существует и не потоплен
            if ((ship != null) && !ship.isSunk()){
                boolean sunk = ship.hit(cell);
                victim.board().updateCell(cell, sunk ? CellState.SUNK : CellState.HIT);
                shooter.board().updateCell(cell, sunk ? CellState.SUNK : CellState.HIT);
                hurt.add(cell);

                if (sunk){
                    for (Coordinate c: ship.getCells()){
                        victim.board().updateCell(c, CellState.SUNK);
                        shooter.board().updateCell(c, CellState.SUNK);
                    }
                }
            }
        }
        return hurt;
    }

    /**
     * Проверяет, обезврежена ли мина тральщиком.
     *
     * <p>Тральщик обезвреживает мины, находящиеся в радиусе 1 клетки
     * от любой своей клетки (включая диагонали). Это означает, что мина
     * считается обезвреженной, если она расположена в области 3×3
     * вокруг любой клетки тральщика.
     *
     * <p>Метод перебирает все корабли флота игрока и для каждого тральщика
     * проверяет, попадает ли координата мины в его радиус действия.
     *
     * @param victim    игрок, чей флот проверяется (владелец тральщика)
     * @param mineCoord координаты мины для проверки
     * @return true, если мина находится в радиусе действия хотя бы одного тральщика
     */
    public boolean isMineClearedByMinesweeper(Player victim, Coordinate mineCoord){
        for (Ship ship: victim.fleet().getShips()){
            if (ship.isMinesweeper()){
                for (Coordinate cell: ship.getCells()){
                    if (cell.inRadius(1).contains(mineCoord)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Проверяет, закончена ли игра.
     *
     * @param player1 первый игрок
     * @param player2 второй игрок
     * @return имя победителя или null, если игра продолжается
     */
    public String checkWinner(Player player1, Player player2){
        if (player1.isDefeated()){
            return player2.name();
        }
        if (player2.isDefeated()){
            return player1.name();
        }
        return null;
    }

}
