package com.university.battleship.event;

import com.university.battleship.model.ShipType;
import com.university.battleship.model.Coordinate;

import java.util.List;

public record ShipSunkEvent(String shooterName, String victimName, List<Coordinate> cells, ShipType shipType)
        implements GameEvent{
    @Override
    public String getPlayerName(){
        return shooterName;
    }

    @Override
    public String describe(){
        return shooterName + " потопил(а) " + shipType.getName() + " игрока " + victimName;
    }
}
