package com.university.battleship.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Флот игрока — набор кораблей и мин.
 *
 * <p>Хранит все корабли и мины игрока, предоставляет методы
 * для поиска кораблей и мин по координатам, проверки состояния флота.
 */
public final class Fleet{
    private final List<Ship> ships;
    private final List<Mine> mines;

    /**
     * Создаёт флот из списка кораблей и мин.
     *
     * @param ships список кораблей
     * @param mines список мин
     */
    public Fleet(List<Ship> ships, List<Mine> mines){
        this.ships = new ArrayList<>(ships);
        this.mines = new ArrayList<>(mines);
    }

    /**
     * Возвращает защитную копию списка кораблей.
     *
     * @return неизменяемый список кораблей
     */
    public List<Ship> getShips(){
        return Collections.unmodifiableList(ships);
    }

    /**
     * Возвращает защитную копию списка мин.
     *
     * @return неизменяемый список мин
     */
    public List<Mine> getMines(){
        return Collections.unmodifiableList(mines);
    }

    /**
     * Возвращает корабль, находящийся на указанной клетке, или null.
     *
     * @param coord координата для поиска
     * @return корабль на этой клетке или null
     */
    public Ship getShipAt(Coordinate coord){
        return ships.stream().filter(ships -> ships.contains(coord)).findFirst().orElse(null);
    }

    /**
     * Возвращает мину на указанной клетке, или null.
     *
     * @param coord координата для поиска
     * @return мина на этой клетке или null
     */
    public Mine getMineAt(Coordinate coord){
        return mines.stream().filter(mine -> mine.getCoord().equals(coord)).findFirst().orElse(null);
    }

    /**
     * Возвращает true, если все корабли флота потоплены.
     *
     * @return true, если все корабли потоплены
     */
    public boolean allShipsSunk(){
        return ships.stream().allMatch(Ship::isSunk);
    }

    /**
     * Возвращает количество неповреждённых (не потопленных) кораблей.
     *
     * @return количество живых кораблей
     */
    public int getAliveShipsCount(){
        return (int) ships.stream().filter(ship -> !ship.isSunk()).count();
    }

    /**
     * Возвращает общее количество клеток всех кораблей флота.
     *
     * @return суммарный размер флота в клетках
     */
    public int getAllShipCells(){
        return ships.stream().mapToInt(ships -> ships.getType().getSize()).sum();
    }

    /**
     * Возвращает количество потопленных клеток (сумма размеров потопленных кораблей).
     *
     * @return количество потопленных клеток
     */
    public int getSunkCellsCount(){
        return ships.stream().filter(Ship::isSunk).mapToInt(ship -> ship.getType().getSize()).sum();
    }
}
