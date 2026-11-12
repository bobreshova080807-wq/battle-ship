package com.university.battleship.strategy;

import com.university.battleship.model.Knowledge;
import com.university.battleship.model.Coordinate;

import java.util.Random;

public final class RandomStrategy implements ShotStrategy {
    private final Random random;

    public RandomStrategy(Random random){
        this.random = random;
    }

    @Override
    public Coordinate chooseShot(Knowledge knowledge){
        while (true){
            int row = random.nextInt(Coordinate.BOARD_SIZE);
            int col = random.nextInt(Coordinate.BOARD_SIZE);
            Coordinate coord = new Coordinate(row, col);
            if (!knowledge.isShot(coord)){
                return coord;
            }
        }
    }

    @Override
    public String getName(){
        return "Случайная";
    }

}
