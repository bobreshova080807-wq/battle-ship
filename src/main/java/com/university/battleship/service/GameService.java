package com.university.battleship.service;

import com.university.battleship.event.*;
import com.university.battleship.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public final class GameService {

    private final Random random;

    public GameService(Random random) {
        this.random = random;
    }

    public List<GameEvent> applyShot(Player shooter, Player victim, Coordinate coord) {
        List<GameEvent> events = new ArrayList<>();

        if (shooter.knowledge().isShot(coord)) {
            events.add(new ShotEvent(shooter.name(), coord, ShotResult.ALREADY_SHOT));
            return events;
        }

        Board victimBoard = victim.board();
        CellState state = victimBoard.getState(coord);

        ShotResult result;

        switch (state) {
            case WATER -> {
                result = ShotResult.MISS;
                victimBoard.updateCell(coord, CellState.MISS);
                shooter.knowledge().update(coord, CellState.MISS);
            }

            case SHIP -> {
                Ship ship = victim.fleet().getShipAt(coord);
                if (ship == null) {
                    result = ShotResult.MISS;
                    victimBoard.updateCell(coord, CellState.MISS);
                    shooter.knowledge().update(coord, CellState.MISS);
                } else {
                    boolean sunk = ship.hit(coord);
                    if (sunk) {
                        result = ShotResult.SUNK;
                        victimBoard.updateCell(coord, CellState.SUNK);
                        shooter.knowledge().update(coord, CellState.SUNK);
                        for (Coordinate cell : ship.getCells()) {
                            victimBoard.updateCell(cell, CellState.SUNK);
                            shooter.knowledge().update(cell, CellState.SUNK);
                        }
                        events.add(new ShipSunkEvent(
                                shooter.name(), victim.name(),
                                ship.getCells(), ship.getType()
                        ));
                    } else {
                        result = ShotResult.HIT;
                        victimBoard.updateCell(coord, CellState.HIT);
                        shooter.knowledge().update(coord, CellState.HIT);
                    }
                }
            }

            case MINESWEEPER -> {
                // Попадание в тральщик — тральщик тонет, стреляющий выдаёт мину
                Ship ship = victim.fleet().getShipAt(coord);
                if (ship != null) {
                    ship.hit(coord);  // Тральщик тонет (он 2 клетки, но для простоты — сразу)
                }
                victimBoard.updateCell(coord, CellState.SUNK);
                shooter.knowledge().update(coord, CellState.SUNK);

                // Выдаём координату одной из активных мин стреляющего
                Optional<Coordinate> revealedMine = shooter.fleet().revealOneMine();
                if (revealedMine.isPresent()) {
                    Coordinate mineCoord = revealedMine.get();
                    // Противник помечает эту клетку в своих знаниях
                    victim.knowledge().update(mineCoord, CellState.KNOWN_MINE);
                    events.add(new MineRevealedEvent(
                            shooter.name(), victim.name(), mineCoord
                    ));
                    result = ShotResult.MINESWEEPER_HIT;
                } else {
                    // У стреляющего нет активных мин
                    events.add(new MinesweeperNoMinesEvent(
                            shooter.name(), victim.name(), coord
                    ));
                    result = ShotResult.MINESWEEPER_HIT;
                }
            }

            case SUBMARINE -> {
                // Попадание в подлодку — она тонет сразу и стреляет в ответ
                Ship ship = victim.fleet().getShipAt(coord);
                if (ship != null) {
                    ship.hit(coord);  // Подлодка тонет (1 клетка = 1 HP)
                }
                victimBoard.updateCell(coord, CellState.SUNK);
                shooter.knowledge().update(coord, CellState.SUNK);

                events.add(new ShipSunkEvent(
                        shooter.name(), victim.name(),
                        List.of(coord), ShipType.SUBMARINE
                ));

                // Ответный выстрел подлодки по ТОЙ ЖЕ координате поля стрелявшего
                events.addAll(submarineCounterShot(victim, shooter, coord));
                result = ShotResult.SUBMARINE_HIT;
            }

            case MINE -> {
                if (isMineClearedByMinesweeper(victim, coord)) {
                    result = ShotResult.CLEARED;
                    victimBoard.updateCell(coord, CellState.CLEARED);
                    shooter.knowledge().update(coord, CellState.CLEARED);
                } else {
                    Mine mine = victim.fleet().getMineAt(coord);
                    result = ShotResult.MINE_HIT;
                    victimBoard.updateCell(coord, CellState.MINE_EXPLODED);
                    shooter.knowledge().update(coord, CellState.MINE_EXPLODED);

                    List<Coordinate> affected = explodeMine(victim, mine, shooter);
                    events.add(new MineExplodedEvent(
                            shooter.name(), mine.getCoord(), mine.getRadius(), affected
                    ));
                }
            }

            default -> {
                result = ShotResult.MISS;
            }
        }

        events.add(new ShotEvent(shooter.name(), coord, result));
        return events;
    }

    /**
     * Подлодка совершает ответный выстрел по ТОЙ ЖЕ координате поля стрелявшего.
     * Если игрок А стрелял в B5 и попал в подлодку, подлодка стреляет в B5 поля игрока А.
     */
    private List<GameEvent> submarineCounterShot(Player submarineOwner, Player target, Coordinate coord) {
        List<GameEvent> events = new ArrayList<>();

        // Проверяем, не стреляли ли уже в эту клетку на поле цели
        if (submarineOwner.knowledge().isShot(coord)) {
            return events;  // Клетка уже обстреляна
        }

        Board targetBoard = target.board();
        CellState state = targetBoard.getState(coord);

        ShotResult result;
        switch (state) {
            case SHIP, MINESWEEPER -> {
                Ship ship = target.fleet().getShipAt(coord);
                if (ship != null) {
                    boolean sunk = ship.hit(coord);
                    if (sunk) {
                        result = ShotResult.SUNK;
                        targetBoard.updateCell(coord, CellState.SUNK);
                        submarineOwner.knowledge().update(coord, CellState.SUNK);
                        for (Coordinate cell : ship.getCells()) {
                            targetBoard.updateCell(cell, CellState.SUNK);
                            submarineOwner.knowledge().update(cell, CellState.SUNK);
                        }
                        events.add(new ShipSunkEvent(
                                submarineOwner.name() + " (подлодка)",
                                target.name(), ship.getCells(), ship.getType()
                        ));
                    } else {
                        result = ShotResult.HIT;
                        targetBoard.updateCell(coord, CellState.HIT);
                        submarineOwner.knowledge().update(coord, CellState.HIT);
                    }
                } else {
                    result = ShotResult.MISS;
                    targetBoard.updateCell(coord, CellState.MISS);
                    submarineOwner.knowledge().update(coord, CellState.MISS);
                }
            }
            case SUBMARINE -> {
                // Подлодка по подлодке — тонет
                Ship ship = target.fleet().getShipAt(coord);
                if (ship != null) ship.hit(coord);
                result = ShotResult.SUNK;
                targetBoard.updateCell(coord, CellState.SUNK);
                submarineOwner.knowledge().update(coord, CellState.SUNK);
            }
            case MINE -> {
                result = ShotResult.MINE_HIT;
                targetBoard.updateCell(coord, CellState.MINE_EXPLODED);
                submarineOwner.knowledge().update(coord, CellState.MINE_EXPLODED);
            }
            default -> {
                result = ShotResult.MISS;
                targetBoard.updateCell(coord, CellState.MISS);
                submarineOwner.knowledge().update(coord, CellState.MISS);
            }
        }

        events.add(new SubmarineCounterShotEvent(
                submarineOwner.name(), target.name(), coord, result
        ));
        return events;
    }

    private List<Coordinate> explodeMine(Player victim, Mine mine, Player shooter) {
        List<Coordinate> affected = new ArrayList<>();
        List<Coordinate> area = mine.getCoord().inRadius(mine.getRadius());

        for (Coordinate cell : area) {
            Ship ship = victim.fleet().getShipAt(cell);
            if (ship != null && !ship.isSunk()) {
                boolean sunk = ship.hit(cell);
                victim.board().updateCell(cell, sunk ? CellState.SUNK : CellState.HIT);
                shooter.knowledge().update(cell, sunk ? CellState.SUNK : CellState.HIT);
                affected.add(cell);

                if (sunk) {
                    for (Coordinate c : ship.getCells()) {
                        victim.board().updateCell(c, CellState.SUNK);
                        shooter.knowledge().update(c, CellState.SUNK);
                    }
                }
            }
        }

        return affected;
    }

    public boolean isMineClearedByMinesweeper(Player victim, Coordinate mineCoord) {
        for (Ship ship : victim.fleet().getShips()) {
            if (ship.isMinesweeper() && !ship.isSunk()) {
                for (Coordinate cell : ship.getCells()) {
                    if (cell.inRadius(1).contains(mineCoord)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String checkWinner(Player player1, Player player2) {
        if (player1.isDefeated()) return player2.name();
        if (player2.isDefeated()) return player1.name();
        return null;
    }
}