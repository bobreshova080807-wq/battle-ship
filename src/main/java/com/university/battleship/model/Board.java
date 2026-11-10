package com.university.battleship.model;

import java.util.EnumMap;
import java.util.Map;

public final class Board {
    private final Map<Coordinate, CellState> cells;
    private final Fleet fleet;

    public Board(Fleet fleet){
        this.fleet = fleet;
        this.cells = new EnumMap<>(Map.class);

        for (int r = 0; r < Coordinate.BOARD_SIZE; r++){
            for (int c = 0; c < Coordinate.BOARD_SIZE; c++){
                cells.put(new Coordinate(r, c), CellState.WATER);
            }
        }

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

        for (Mine mine : fleet.getMines()){
            cells.put(mine.getCoord(), CellState.MINE);
        }

    }

    public CellState getState(CellState coord){
        return cells.getOrDefault(coord, CellState.WATER);
    }

    public Fleet getFleet(){
        return fleet;
    }

    void updateCell(Coordinate coord, CellState newState){
        cells.put(coord, newState);
    }
}
