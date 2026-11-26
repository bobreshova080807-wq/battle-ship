package com.university.battleship.io;

import com.university.battleship.model.CellState;
import com.university.battleship.model.Coordinate;
import com.university.battleship.model.Knowledge;

import java.util.HashMap;
import java.util.Map;

public record KnowledgeState(
        Map<Coordinate, CellState> cells
) {
    public static KnowledgeState fromKnowledge(Knowledge knowledge){
        Map<Coordinate, CellState> cells = new HashMap<>();
        for (int i = 0; i < Coordinate.BOARD_SIZE; i++){
            for (int j = 0; j < Coordinate.BOARD_SIZE; j++){
                Coordinate coord = new Coordinate(i, j);
                CellState cellState = knowledge.getState(coord);
                if (cellState != CellState.WATER){
                    cells.put(coord, cellState);
                }
            }
        }
        return new KnowledgeState(cells);
    }
}
