package rules;

import com.university.battleship.model.Coordinate;
import com.university.battleship.model.Ship;
import com.university.battleship.model.Fleet;

import java.util.List;

public final class PlacementRules {

    private PlacementRules() {}

    public static boolean canPlace(Ship ship, List<Ship> existingShips){
        for (Coordinate cell: ship.getCells()){
            if (cell.row() < 0 || cell.col() >= Coordinate.BOARD_SIZE || cell.col() < 0 || cell.col() >= Coordinate.BOARD_SIZE){
                return false;
            }

            for (Ship existing: existingShips){
                for (Coordinate existingCells: existing.getCells()){
                    if (isTouching(cell, existingCells)){
                        return false;
                    }
                }
            }
        }
        return true;

    }

    public static boolean isTouching(Coordinate a, Coordinate b){
        return Math.abs(a.row() - b.row()) <= 1 && Math.abs(a.col() - b.col()) <= 1;
    }


    public static boolean rightFleet(Fleet fleet){
        List<Ship> ships = fleet.getShips();
        for (int i = 0; i < ships.size(); i++){
            List<Ship> others = ships.subList(0, i);
            if (!canPlace(ships.get(i), others)){
                return false;
            }
        }
        return true;
    }

}