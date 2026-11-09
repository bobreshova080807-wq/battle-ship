package model;

import java.util.*;

public final class Ship {
    private final ShipType type;
    private final List<Coordinate> cells;
    private final Set<Coordinate> hitCells;

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

    public ShipType getType(){
        return type;
    }

    public List<Coordinate> getCells() {
        return Collections.unmodifiableList(cells);
    }

    public boolean contains(Coordinate coord){
        return cells.contains(coord);
    }

    public boolean hit(Coordinate coord){
        if (!contains(coord)){
            return false;
        }
        hitCells.add(coord);
        return isSunk();
    }

    public boolean isSunk(){
        return hitCells.size() == cells.size();
    }

    public int getHealth(){
        return cells.size() - hitCells.size();
    }

    public boolean isSubmarine(){
        return type.isSubmarine();
    }

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
