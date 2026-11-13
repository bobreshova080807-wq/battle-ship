package com.university.battleship.model;

import java.util.*;

/**
 * Корабль на поле.
 *
 * <p>Состоит из набора клеток, имеет тип и состояние (потоплен/нет).
 * Клетки должны образовывать прямую линию (горизонтальную или вертикальную).
 *
 * <p>Корабль считается потопленным, когда все его клетки поражены.
 */
public final class Ship {
    private final ShipType type;
    private final List<Coordinate> cells;
    private final Set<Coordinate> hitCells;

    /**
     * Создаёт корабль заданного типа на указанных клетках.
     *
     * @param type  тип корабля (определяет размер)
     * @param cells клетки, которые занимает корабль (должны быть смежными по прямой)
     * @throws IllegalArgumentException если количество клеток не соответствует типу
     *                                  или клетки не образуют прямую линию
     */
    public Ship(ShipType type, List<Coordinate> cells){
        Objects.requireNonNull(type, "Тип корабля не может быть пустым.");
        Objects.requireNonNull(cells, "Клетки корабля не могут быть пустыми.");

        if (cells.size() != type.getSize()){
            throw new IllegalArgumentException(
                    "Корабль типа " + type.getName() + " должен занимать " +
                     type.getSize() + " клеток, а не " + cells.size());
        }

        validateLine(cells);

        this.type = type;
        this.cells = new ArrayList<>(cells);
        this.hitCells = new HashSet<>();
    }

    /**
     * Проверяет, что клетки образуют прямую линию (горизонтальную или вертикальную).
     *
     * @param cells список клеток для проверки
     * @throws IllegalArgumentException если клетки не на одной линии
     */
    private void validateLine(List<Coordinate> cells){
        if (cells.size() == 1) return;

        Coordinate first = cells.get(0);
        Coordinate last = cells.get(cells.size() - 1);

        boolean horizontal = first.row() == last.row();
        boolean vertical = first.col() == last.col();

        if (!horizontal && !vertical){
            throw new IllegalArgumentException("Клетки должны быть на одной линии.");
        }
    }

    /**
     * Возвращает тип корабля.
     *
     * @return тип корабля
     */
    public ShipType getType(){
        return type;
    }

    /**
     * Возвращает защитную копию списка клеток корабля.
     *
     * @return неизменяемый список координат
     */
    public List<Coordinate> getCells() {
        return Collections.unmodifiableList(cells);
    }

    /**
     * Возвращает true, если корабль находится на указанной клетке.
     *
     * @param coord координата для проверки
     * @return true, если корабль занимает эту клетку
     */
    public boolean contains(Coordinate coord){
        return cells.contains(coord);
    }

    /**
     * Наносит повреждение кораблю в указанной клетке.
     *
     * @param coord координата попадания
     * @return true, если корабль потоплен после этого попадания
     * @throws IllegalArgumentException если клетка не принадлежит кораблю
     */
    public boolean hit(Coordinate coord){
        if (!contains(coord)){
            return false;
        }
        hitCells.add(coord);
        return isSunk();
    }

    /**
     * Возвращает true, если корабль потоплен (все клетки поражены).
     *
     * @return true, если корабль потоплен
     */
    public boolean isSunk(){
        return hitCells.size() == cells.size();
    }

    /**
     * Возвращает количество неповреждённых клеток корабля.
     *
     * @return количество здоровых клеток
     */
    public int getHealth(){
        return cells.size() - hitCells.size();
    }

    /**
     * Возвращает true, если корабль — подводная лодка.
     *
     * @return true, если это подводная лодка
     */
    public boolean isSubmarine(){
        return type.isSubmarine();
    }

    /**
     * Возвращает true, если корабль — тральщик.
     *
     * @return true, если это тральщик
     */
    public boolean isMinesweeper(){
        return type.isMinesweeper();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Ship ship)) return false;
        return type == ship.type && Objects.equals(cells, ship.cells);
    }

    @Override
    public int hashCode(){
        return Objects.hash(type, cells);
    }

    @Override
    public String toString(){
        return type.getName() + " " + cells + (isSunk() ? "[ПОТОПЛЕН]" : " ");
    }
}
