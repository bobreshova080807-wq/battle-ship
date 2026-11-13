package com.university.battleship.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Физическое поле игрока 10×10.
 *
 * <p>Хранит реальное расположение кораблей и мин.
 * Используется для проверки выстрелов и определения результата.
 *
 * <p>Поле инициализируется из {@link Fleet}: размещает корабли и мины
 * на соответствующих клетках, остальные клетки заполняются водой.
 */
public final class Board {
    private final Map<Coordinate, CellState> cells;
    private final Fleet fleet;

    /**
     * Создаёт поле на основе флота.
     *
     * @param fleet флот игрока (корабли и мины)
     */
    public Board(Fleet fleet){
        this.fleet = fleet;
        this.cells = new HashMap<>();

        // Инициализируем поле водой
        for (int r = 0; r < Coordinate.BOARD_SIZE; r++){
            for (int c = 0; c < Coordinate.BOARD_SIZE; c++){
                cells.put(new Coordinate(r, c), CellState.WATER);
            }
        }

        // Размещаем корабли
        for (Ship ship : fleet.getShips()){
            CellState state = switch (ship.getType()){
                case SUBMARINE -> CellState.SUBMARINE;
                case MINESWEEPER -> CellState.MINESWEEPER;
                default -> CellState.SHIP;
            };
            for (Coordinate coord : ship.getCells()){
                cells.put(coord, state);
            }
        }

        // Размещаем мины
        for (Mine mine : fleet.getMines()){
            cells.put(mine.getCoord(), CellState.MINE);
        }

    }

    /**
     * Возвращает состояние клетки на поле.
     *
     * @param coord координата клетки
     * @return состояние клетки (WATER, SHIP, MINE и т.д.)
     */
    public CellState getState(Coordinate coord){
        return cells.getOrDefault(coord, CellState.WATER);
    }

    /**
     * Возвращает флот, размещённый на этом поле.
     *
     * @return флот игрока
     */
    public Fleet getFleet(){
        return fleet;
    }

    /**
     * Обновляет состояние клетки после выстрела.
     * Используется внутри пакета для изменения состояния поля.
     *
     * @param coord    координата клетки
     * @param newState новое состояние клетки
     */
    public void updateCell(Coordinate coord, CellState newState){
        cells.put(coord, newState);
    }
}
