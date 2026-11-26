package com.university.battleship.model;

import com.university.battleship.rules.PlacementRules;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

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
        assertThrows(IllegalArgumentException.class, () -> new Coordinate(0, -1));

        assertThrows(IllegalArgumentException.class,
                () -> new Coordinate(Coordinate.BOARD_SIZE, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Coordinate(0, Coordinate.BOARD_SIZE));
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

    @Test
    void submarine_sinksInOneHit() {
        Ship sub = new Ship(ShipType.SUBMARINE, List.of(new Coordinate(5, 5)));
        assertTrue(sub.hit(new Coordinate(5, 5)));  // Тонет с первого попадания
    }

    @Test
    void submarine_canTouchOtherShips() {
        Ship boat = new Ship(ShipType.BOAT, List.of(new Coordinate(5, 5)));
        Ship sub = new Ship(ShipType.SUBMARINE, List.of(new Coordinate(5, 6)));
        assertTrue(PlacementRules.canPlace(sub, List.of(boat)));
    }

    @Test
    void minesweeper_cannotTouchShips() {
        Ship boat = new Ship(ShipType.BOAT, List.of(new Coordinate(5, 5)));
        Ship trawler = new Ship(ShipType.MINESWEEPER,
                List.of(new Coordinate(5, 6), new Coordinate(5, 7)));
        assertFalse(PlacementRules.canPlace(trawler, List.of(boat)));
    }

    @Test
    void fleet_revealOneMine_returnsActiveMine() {
        Mine mine1 = new Mine(new Coordinate(1, 1));
        Mine mine2 = new Mine(new Coordinate(2, 2));
        Fleet fleet = new Fleet(List.of(), List.of(mine1, mine2));

        Optional<Coordinate> revealed = fleet.revealOneMine();
        assertTrue(revealed.isPresent());
        assertTrue(revealed.get().equals(new Coordinate(1, 1))
                || revealed.get().equals(new Coordinate(2, 2)));

        // Вторая мина ещё активна
        assertEquals(1, fleet.getActiveMinesCount());
    }

    @Test
    void fleet_revealOneMine_returnsEmptyWhenNoMines() {
        Fleet fleet = new Fleet(List.of(), List.of());
        assertTrue(fleet.revealOneMine().isEmpty());
    }

    @Test
    void knowledge_knownMine_isShotReturnsTrue() {
        Knowledge k = new Knowledge();
        Coordinate coord = new Coordinate(5, 5);
        k.update(coord, CellState.KNOWN_MINE);
        assertTrue(k.isShot(coord));  // Стратегия не должна стрелять сюда
    }
}