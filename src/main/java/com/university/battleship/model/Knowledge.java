package com.university.battleship.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Знания игрока о поле противника.
 *
 * <p>Это субъективная картина мира: что игрок знает о каждой клетке поля противника.
 * Отделяется от реального поля ({@link Board}) для инкапсуляции информации.
 *
 * <p>Изначально все клетки считаются необстрелянными (WATER).
 * После выстрела клетка обновляется: MISS, HIT, SUNK, MINE_EXPLODED.
 *
 * <p>Используется стратегиями стрельбы ({@link com.university.battleship.strategy.ShotStrategy})
 * для выбора следующей клетки.
 */
public final class Knowledge {
    private final Map<Coordinate, CellState> cells;

    /**
     * Создаёт пустые знания (все клетки необстрелянные).
     */
    public Knowledge(){
        this.cells = new HashMap<>();

        // Изначально все клетки неизвестны (WATER = не обстреляна)
        for (int r = 0; r < Coordinate.BOARD_SIZE; r++){
            for (int c = 0; c < Coordinate.BOARD_SIZE; c++){
                cells.put(new Coordinate(c, r), CellState.WATER);
            }
        }
    }

    /**
     * Возвращает состояние клетки в знаниях игрока.
     *
     * @param coord координата клетки
     * @return состояние клетки (WATER, MISS, HIT, SUNK и т.д.)
     */
    public CellState getState(Coordinate coord){
        return cells.getOrDefault(coord, CellState.WATER);
    }

    /**
     * Обновляет знания игрока после выстрела.
     *
     * @param coord координата клетки
     * @param state новое состояние клетки
     */
    public void update(Coordinate coord, CellState state){
        cells.put(coord, state);
    }

    /**
     * Возвращает количество обстрелянных клеток.
     *
     * @return количество клеток со статусом MISS, HIT, SUNK или MINE_EXPLODED
     */
    public int getShotCells(){
        return (int) cells.values().stream().filter(
                cellState -> cellState == CellState.MISS || cellState == CellState.HIT ||
                cellState == CellState.SUNK || cellState == CellState.MINE_EXPLODED).count();
    }

    /**
     * Возвращает true, если клетка уже была обстреляна.
     *
     * @param coord координата клетки
     * @return true, если клетка уже обстреляна
     */
    public boolean isShot(Coordinate coord){
        CellState state = getState(coord);
        return state == CellState.MISS || state == CellState.HIT ||
                state == CellState.SUNK || state == CellState.MINE_EXPLODED;
    }
}
