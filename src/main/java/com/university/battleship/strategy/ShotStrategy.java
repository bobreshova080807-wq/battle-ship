package com.university.battleship.strategy;

import com.university.battleship.model.Knowledge;
import com.university.battleship.model.Coordinate;

public interface ShotStrategy {
    Coordinate chooseShot(Knowledge knowledge);

    String getName();
}
