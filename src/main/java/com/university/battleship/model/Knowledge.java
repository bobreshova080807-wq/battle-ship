package com.university.battleship.model;

import java.util.EnumMap;
import java.util.Map;

public final class Knowledge {
    private final Map<Coordinate, CellState> cells;

    public Knowledge(){
        this.cells = new EnumMap<>(Map.class);

        for (int r = 0; r < Coordinate.BOARD_SIZE; r++){
            for (int c = 0; c < Coordinate.BOARD_SIZE; c++){
                cells.put(new Coordinate(c, r), CellState.WATER);
            }
        }
    }

    public CellState getState(Coordinate coord){
        return cells.getOrDefault(coord, CellState.WATER);
    }

    public void update(Coordinate coord, CellState state){
        cells.put(coord, state);
    }

    public int getShotCells(){
        return (int) cells.values().stream().filter(
                cellState -> cellState == CellState.MISS || cellState == CellState.HIT ||
                cellState == CellState.SUNK || cellState == CellState.MINE_EXPLODED).count();
    }

    public boolean isShot(Coordinate coord){
        CellState state = getState(coord);
        return state == CellState.MISS || state == CellState.HIT ||
                state == CellState.SUNK || state == CellState.MINE_EXPLODED;
    }
}
