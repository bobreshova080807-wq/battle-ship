package com.university.battleship.io;

import com.university.battleship.model.*;

public record MineState(
        Coordinate coord,
        int radius,
        boolean exploded
) {
    public static MineState fromMine(Mine mine){
        return new MineState(mine.coord(), mine.getRadius(), false);
    }
}
