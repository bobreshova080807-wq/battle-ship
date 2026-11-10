package com.university.battleship.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Fleet{
    private final List<Ship> ships;
    private final List<Mine> mines;

    private Fleet(List<Ship> ships, List<Mine> mines){
        this.ships = new ArrayList<>(ships);
        this.mines = new ArrayList<>(mines);
    }

    public List<Ship> getShips(){
        return Collections.unmodifiableList(ships);
    }

    public List<Mine> getMines(){
        return Collections.unmodifiableList(mines);
    }

    public Ship getShipAt(Coordinate coord){
        return ships.stream().filter(ships -> ships.contains(coord)).findFirst().orElse(null);
    }

    public Mine getMineAt(Coordinate coord){
        return mines.stream().filter(mine -> mine.getCoord().equals(coord)).findFirst().orElse(null);
    }

    public boolean allShipsSunk(){
        return ships.stream().allMatch(Ship::isSunk);
    }

    public int getAliveShipsCount(){
        return (int) ships.stream().filter(ship -> !ship.isSunk()).count();
    }

    public int getAllShipCells(){
        return ships.stream().mapToInt(ships -> ships.getType().getSize()).sum();
    }

    public int getSunkCellsCount(){
        return ships.stream().filter(Ship::isSunk).mapToInt(ship -> ship.getType().getSize()).sum();
    }
}
