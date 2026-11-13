package com.university.battleship.rules;

import com.university.battleship.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для правил расстановки.
 */
class PlacementRuleTest {

    @Test
    void canPlace_singleShipOnEmptyBoard_returnsTrue() {
        Ship ship = new Ship(ShipType.BOAT, List.of(new Coordinate(5, 5)));
        assertTrue(PlacementRules.canPlace(ship, List.of()));
    }

    @Test
    void canPlace_touchingShips_returnsFalse() {
        Ship ship1 = new Ship(ShipType.BOAT, List.of(new Coordinate(5, 5)));
        Ship ship2 = new Ship(ShipType.BOAT, List.of(new Coordinate(5, 6)));
        assertFalse(PlacementRules.canPlace(ship2, List.of(ship1)));
    }

    @Test
    void canPlace_diagonalTouching_returnsFalse() {
        Ship ship1 = new Ship(ShipType.BOAT, List.of(new Coordinate(5, 5)));
        Ship ship2 = new Ship(ShipType.BOAT, List.of(new Coordinate(6, 6)));
        assertFalse(PlacementRules.canPlace(ship2, List.of(ship1)));
    }

    @Test
    void canPlace_farShips_returnsTrue() {
        Ship ship1 = new Ship(ShipType.BOAT, List.of(new Coordinate(0, 0)));
        Ship ship2 = new Ship(ShipType.BOAT, List.of(new Coordinate(5, 5)));
        assertTrue(PlacementRules.canPlace(ship2, List.of(ship1)));
    }

    @Test
    void canPlace_outOfBounds_returnsFalse() {
        assertThrows(IllegalArgumentException.class, () ->
                new Coordinate(-1, 0));
    }

    @Test
    void ship_invalidSize_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Ship(ShipType.BOAT, List.of(new Coordinate(0, 0), new Coordinate(0, 1))));
    }

    @Test
    void ship_nonLinear_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Ship(ShipType.DESTROYER,
                        List.of(new Coordinate(0, 0), new Coordinate(1, 1))));
    }
}