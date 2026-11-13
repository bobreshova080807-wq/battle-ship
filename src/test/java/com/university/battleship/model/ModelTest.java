package com.university.battleship.model;

import com.university.battleship.model.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для моделей.
 */
class ModelTest {

    @Test
    void coordinate_equalsAndHashCode_sameCoords() {
        Coordinate c1 = new Coordinate(5, 5);
        Coordinate c2 = new Coordinate(5, 5);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void coordinate_differentCoords_notEqual() {
        Coordinate c1 = new Coordinate(5, 5);
        Coordinate c2 = new Coordinate(5, 6);
        assertNotEquals(c1, c2);
    }

    @Test
    void coordinate_outOfBounds_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Coordinate(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Coordinate(10, 0));
    }

    @Test
    void coordinate_neighbors_returns4() {
        Coordinate c = new Coordinate(5, 5);
        assertEquals(4, c.neighbors().size());
    }

    @Test
    void coordinate_neighbors_corner_returns2() {
        Coordinate c = new Coordinate(0, 0);
        assertEquals(2, c.neighbors().size());
    }

    @Test
    void coordinate_inRadius_returnsCorrectCount() {
        Coordinate c = new Coordinate(5, 5);
        assertEquals(9, c.inRadius(1).size()); // 3×3
    }

    @Test
    void ship_hit_reducesHealth() {
        Ship ship = new Ship(ShipType.DESTROYER,
                List.of(new Coordinate(0, 0), new Coordinate(0, 1)));
        assertEquals(2, ship.getHealth());
        ship.hit(new Coordinate(0, 0));
        assertEquals(1, ship.getHealth());
    }

    @Test
    void ship_sunkWhenAllCellsHit() {
        Ship ship = new Ship(ShipType.BOAT, List.of(new Coordinate(0, 0)));
        assertFalse(ship.isSunk());
        ship.hit(new Coordinate(0, 0));
        assertTrue(ship.isSunk());
    }

    @Test
    void ship_hitOutsideReturnsFalse() {
        Ship ship = new Ship(ShipType.BOAT, List.of(new Coordinate(0, 0)));
        assertFalse(ship.hit(new Coordinate(5, 5)));
    }

    @Test
    void fleet_allShipsSunk_whenAllHit() {
        Ship ship = new Ship(ShipType.BOAT, List.of(new Coordinate(0, 0)));
        Fleet fleet = new Fleet(List.of(ship), List.of());
        assertFalse(fleet.allShipsSunk());
        ship.hit(new Coordinate(0, 0));
        assertTrue(fleet.allShipsSunk());
    }

    @Test
    void fleet_getShipAt_returnsCorrectShip() {
        Ship ship = new Ship(ShipType.BOAT, List.of(new Coordinate(3, 3)));
        Fleet fleet = new Fleet(List.of(ship), List.of());
        assertEquals(ship, fleet.getShipAt(new Coordinate(3, 3)));
        assertNull(fleet.getShipAt(new Coordinate(0, 0)));
    }

    @Test
    void knowledge_initiallyAllWater() {
        Knowledge k = new Knowledge();
        assertEquals(CellState.WATER, k.getState(new Coordinate(0, 0)));
        assertFalse(k.isShot(new Coordinate(0, 0)));
    }

    @Test
    void knowledge_updateChangesState() {
        Knowledge k = new Knowledge();
        k.update(new Coordinate(0, 0), CellState.HIT);
        assertEquals(CellState.HIT, k.getState(new Coordinate(0, 0)));
        assertTrue(k.isShot(new Coordinate(0, 0)));
    }

    @Test
    void mine_defaultRadius_isOne() {
        Mine mine = new Mine(new Coordinate(5, 5));
        assertEquals(1, mine.getRadius());
    }

    @Test
    void mine_invalidRadius_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Mine(new Coordinate(0, 0), 0));
    }

    @Test
    void shipType_properties() {
        assertEquals(4, ShipType.BATTLESHIP.getSize());
        assertEquals(1, ShipType.BATTLESHIP.getCount());
        assertTrue(ShipType.SUBMARINE.isSubmarine());
        assertTrue(ShipType.MINESWEEPER.isMinesweeper());
        assertFalse(ShipType.BOAT.isSubmarine());
    }
}