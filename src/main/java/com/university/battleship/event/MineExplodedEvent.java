package com.university.battleship.event;

import com.university.battleship.model.Coordinate;

import java.util.List;

public record MineExplodedEvent(String playerName, Coordinate mineCoord, int radius, List<Coordinate> hurtShips)
        implements GameEvent{
    @Override
    public String getPlayerName(){
        return playerName;
    }

    @Override
    public String describe(){
        return playerName + " подорвался на мине в " + mineCoord + "! Взрыв с радиусом " + radius + " задел " + hurtShips.size() + " кораблей.";
    }
}
