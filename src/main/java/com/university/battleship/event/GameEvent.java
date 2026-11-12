package com.university.battleship.event;

import com.university.battleship.model.Coordinate;
import com.university.battleship.model.ShotResult;
import com.university.battleship.model.ShipType;

public sealed interface GameEvent permits ShotEvent, ShipSunkEvent, MineExplodedEvent, GameOverEvent{
    String getPlayerName();

    String describe();
}
