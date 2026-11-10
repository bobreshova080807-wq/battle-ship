package com.university.battleship.model;

import java.util.ArrayList;
import java.util.List;

public record Coordinate(int row, int col) {
    public static final int BOARD_SIZE = 10;

    public Coordinate{
        if (row < 0 || row >= BOARD_SIZE){
            throw new IllegalArgumentException("Строка вне поля: " + row);
        }
        if (col < 0 || col >= BOARD_SIZE){
            throw new IllegalArgumentException("Столбец вне поля: " + col);
        }
    }

    public Coordinate offset(int dRow, int dCol){
        int newRow = row + dRow;
        int newCol = col + dCol;
        if (newRow < 0 || newRow >= BOARD_SIZE || newCol < 0 || newCol >= BOARD_SIZE){
            return null;
        }
        return new Coordinate(newRow, newCol);
    }

    public List<Coordinate> neighbors(){
        List<Coordinate> result = new ArrayList<>();
        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for(int[] d: dirs){
            Coordinate n = offset(d[0], d[1]);
            if (n != null){
                result.add(n);
            }
        }
        return result;
    }

    public List<Coordinate> inRadius(int radius){
        List<Coordinate> result = new ArrayList<>();
        for (int dr = -radius; dr <= radius; dr++){
            for (int dc = -radius; dc <= radius; dc++){
                Coordinate c = offset(dr,dc);
                if (c != null){
                    result.add(c);
                }
            }
        }
        return result;
    }

    @Override
    public String toString(){
        return (char) ('А' + col) + " " + (row + 1);
    }

}
