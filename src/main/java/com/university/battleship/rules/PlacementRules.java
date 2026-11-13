package com.university.battleship.rules;

import com.university.battleship.model.Coordinate;
import com.university.battleship.model.Ship;
import com.university.battleship.model.Fleet;

import java.util.List;

/**
 * Правила расстановки флота.
 *
 * <p>Корабли не могут:
 * <ul>
 *   <li>Выходить за пределы поля 10×10</li>
 *   <li>Пересекаться друг с другом</li>
 *   <li>Касаться друг друга (включая диагонали)</li>
 * </ul>
 *
 * <p>Мины могут размещаться на любых пустых клетках (не на кораблях).
 */
public final class PlacementRules {

    private PlacementRules() {}

    /**
     * Проверяет, можно ли разместить корабль на поле.
     *
     * @param ship          корабль для размещения
     * @param existingShips уже размещённые корабли
     * @return true, если размещение допустимо
     */
    public static boolean canPlace(Ship ship, List<Ship> existingShips){
        for (Coordinate cell: ship.getCells()){
            // Проверка выхода за пределы
            if (cell.row() < 0 || cell.col() >= Coordinate.BOARD_SIZE ||
                cell.col() < 0 || cell.col() >= Coordinate.BOARD_SIZE){
                return false;
            }

            // Проверка пересечения и касания
            for (Ship existing: existingShips){
                for (Coordinate existingCells: existing.getCells()){
                    if (isTouching(cell, existingCells)){
                        return false;
                    }
                }
            }
        }
        return true;

    }

    /**
     * Проверяет, касаются ли две клетки (включая диагонали).
     *
     * @param a первая клетка
     * @param b вторая клетка
     * @return true, если клетки касаются
     */
    public static boolean isTouching(Coordinate a, Coordinate b){
        return Math.abs(a.row() - b.row()) <= 1 && Math.abs(a.col() - b.col()) <= 1;
    }


    /**
     * Проверяет весь флот на корректность расстановки.
     *
     * @param fleet флот для проверки
     * @return true, если расстановка корректна
     */
    public static boolean rightFleet(Fleet fleet){
        List<Ship> ships = fleet.getShips();
        for (int i = 0; i < ships.size(); i++){
            List<Ship> others = ships.subList(0, i);
            if (!canPlace(ships.get(i), others)){
                return false;
            }
        }
        return true;
    }

}