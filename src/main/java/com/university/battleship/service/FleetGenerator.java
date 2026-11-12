package com.university.battleship.service;

import com.university.battleship.model.*;
import com.university.battleship.rules.PlacementRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class FleetGenerator {

    private Random random;

    public FleetGenerator(Random random){
        this.random = random;
    }

    public Fleet generate(int mineCount){
        List<Ship> ships = new ArrayList<>();

        for (ShipType type : ShipType.values()){
            for (int i = 0; i < type.getCount(); i++){
                Ship ship = placeRandomShip(type, ships);
                ships.add(ship);
            }
        }

        List<Mine> mines = placeRandomMines(ships, mineCount);
        return new Fleet(ships, mines);

    }

    private Ship placeRandomShip(ShipType type, List<Ship> existing){
        int maxAttempts = 1000;
        for (int att = 0; att < maxAttempts; att++){
            boolean hor = random.nextBoolean();
            int row = random.nextInt(Coordinate.BOARD_SIZE);
            int col = random.nextInt(Coordinate.BOARD_SIZE);

            List<Coordinate> cells = new ArrayList<>();
            boolean valid = true;

            for (int i = 0; i < type.getSize(); i++){
                int r = hor ? row : row + i;
                int c = hor ? col + i : col;

                if (r >= Coordinate.BOARD_SIZE || c >= Coordinate.BOARD_SIZE){
                    valid = false;
                    break;
                }
                cells.add(new Coordinate(r, c));
            }

            if (valid){
                Ship ship = new Ship(type, cells);
                if (PlacementRules.canPlace(ship, existing)){
                    return ship;
                }
            }

        }

        throw new IllegalArgumentException("Не удалось разместить корабль: " + type.getName());
    }

    private List<Mine> placeRandomMines(List<Ship> ships, int count){
        List<Mine> mines = new ArrayList<>();
        List<Coordinate> occupied = ships.stream().flatMap(ship -> ship.getCells().stream()).toList();

        int attempts = 0;
        while (mines.size() < count && attempts < 10000){
            int row = random.nextInt(Coordinate.BOARD_SIZE);
            int col = random.nextInt(Coordinate.BOARD_SIZE);
            Coordinate coord = new Coordinate(row, col);

            if (!occupied.contains(coord) && mines.stream().noneMatch(mine -> mine.getCoord().equals(coord))){
                mines.add(new Mine(coord));
            }
            attempts++;
        }
        return mines;
    }

}
