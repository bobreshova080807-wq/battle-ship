package com.university.battleship.io;

import com.university.battleship.model.*;

import java.util.List;

public record ShipState(
        ShipType shipType,
        List<Coordinate> cells,
        List<Coordinate> hitCells,
        boolean sunk
) {
    public static ShipState fromShip(Ship ship){
        return new ShipState(ship.getType(), ship.getCells(),
                List.of(), ship.isSunk());
    }
}
