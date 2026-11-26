package com.university.battleship.io;

import com.university.battleship.model.Coordinate;
import com.university.battleship.model.Fleet;
import com.university.battleship.model.Mine;

import java.util.List;

public record FleetState(
        List<ShipState> ships,
        List<MineState> mines,
        List<Coordinate> revealedMines
) {
    public static FleetState fromFleet(Fleet fleet){
        List<ShipState> ships = fleet.getShips().stream().map(ShipState::fromShip).toList();
        List<MineState> mines = fleet.getMines().stream().map(MineState::fromMine).toList();
        List<Coordinate> revealedMines = fleet.getMines().stream().filter(mine -> fleet.isMineRevealed(mine.coord())).map(Mine::getCoord).toList();

        return new FleetState(ships, mines, revealedMines);
    }
}
