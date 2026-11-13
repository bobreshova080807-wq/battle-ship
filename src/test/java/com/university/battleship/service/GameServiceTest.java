package com.university.battleship.service;

import com.university.battleship.event.*;
import com.university.battleship.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для GameService.
 */
class GameServiceTest {

    private GameService gameService;
    private Player player1;
    private Player player2;

    /**
     * Подготовка тестового окружения перед каждым тестом.
     *
     * <p>Создаёт два игрока с случайными флотами (3 мины у каждого)
     * и сервис игры. Использует фиксированное зерно Random(42)
     * для воспроизводимости результатов.
     *
     * @see BeforeEach
     */
    @BeforeEach
    void setUp() {
        Random random = new Random(42);
        gameService = new GameService(random);

        FleetGenerator generator = new FleetGenerator(random);
        Fleet fleet1 = generator.generate(3);
        Fleet fleet2 = generator.generate(3);

        player1 = new Player("Игрок1", fleet1, new Board(fleet1), new Knowledge());
        player2 = new Player("Игрок2", fleet2, new Board(fleet2), new Knowledge());
    }

    @Test
    void applyShot_miss_returnsMissEvent() {
        // Создаём корабль в стороне от места выстрела
        Ship ship = new Ship(ShipType.BOAT, List.of(new Coordinate(9, 9)));
        Fleet fleet = new Fleet(List.of(ship), List.of());

        Player shooter = new Player("S",
                new Fleet(List.of(new Ship(ShipType.BOAT, List.of(new Coordinate(0, 9)))), List.of()),
                new Board(new Fleet(List.of(new Ship(ShipType.BOAT, List.of(new Coordinate(0, 9)))), List.of())),
                new Knowledge());
        Player victim = new Player("V", fleet, new Board(fleet), new Knowledge());

        Coordinate coord = new Coordinate(0, 0);
        List<GameEvent> events = gameService.applyShot(shooter, victim, coord);

        assertTrue(events.stream()
                .filter(e -> e instanceof ShotEvent se)
                .anyMatch(e -> ((ShotEvent) e).shotResult() == ShotResult.MISS));
    }

    @Test
    void applyShot_hit_returnsHitEvent() {
        // Создаём корабль из 2 клеток (DESTROYER)
        Ship ship = new Ship(ShipType.DESTROYER,
                List.of(new Coordinate(5, 5), new Coordinate(5, 6)));
        Fleet fleet = new Fleet(List.of(ship), List.of());

        Player shooter = new Player("S",
                new Fleet(List.of(new Ship(ShipType.BOAT, List.of(new Coordinate(0, 9)))), List.of()),
                new Board(new Fleet(List.of(new Ship(ShipType.BOAT, List.of(new Coordinate(0, 9)))), List.of())),
                new Knowledge());
        Player victim = new Player("V", fleet, new Board(fleet), new Knowledge());

        // Стреляем в первую клетку корабля (не топит, только HIT)
        List<GameEvent> events = gameService.applyShot(shooter, victim, new Coordinate(5, 5));

        assertTrue(events.stream()
                .filter(e -> e instanceof ShotEvent se)
                .anyMatch(e -> ((ShotEvent) e).shotResult() == ShotResult.HIT));
    }

    @Test
    void applyShot_sunk_returnsSunkEvent() {
        Ship ship = new Ship(ShipType.BOAT, List.of(new Coordinate(5, 5)));
        Fleet fleet = new Fleet(List.of(ship), List.of());

        Player shooter = new Player("S", new Fleet(List.of(), List.of()),
                new Board(new Fleet(List.of(), List.of())), new Knowledge());
        Player victim = new Player("V", fleet, new Board(fleet), new Knowledge());

        List<GameEvent> events = gameService.applyShot(shooter, victim, new Coordinate(5, 5));

        assertTrue(events.stream()
                .anyMatch(e -> e instanceof ShipSunkEvent));
    }

    @Test
    void applyShot_mine_returnsMineHitEvent() {
        Mine mine = new Mine(new Coordinate(3, 3));
        Fleet fleet = new Fleet(List.of(), List.of(mine));

        Player shooter = new Player("S", new Fleet(List.of(), List.of()),
                new Board(new Fleet(List.of(), List.of())), new Knowledge());
        Player victim = new Player("V", fleet, new Board(fleet), new Knowledge());

        List<GameEvent> events = gameService.applyShot(shooter, victim, new Coordinate(3, 3));

        assertTrue(events.stream()
                .anyMatch(e -> e instanceof MineExplodedEvent));
    }

    @Test
    void applyShot_submarine_returnsSubmarineMissed() {
        Ship sub = new Ship(ShipType.SUBMARINE, List.of(new Coordinate(4, 4)));
        Fleet fleet = new Fleet(List.of(sub), List.of());

        Player shooter = new Player("S", new Fleet(List.of(), List.of()),
                new Board(new Fleet(List.of(), List.of())), new Knowledge());
        Player victim = new Player("V", fleet, new Board(fleet), new Knowledge());

        List<GameEvent> events = gameService.applyShot(shooter, victim, new Coordinate(4, 4));

        assertTrue(events.stream()
                .filter(e -> e instanceof ShotEvent se)
                .anyMatch(e -> ((ShotEvent) e).shotResult() == ShotResult.SUBMARINE_MISSED));
    }

    @Test
    void applyShot_alreadyShot_returnsAlreadyShotEvent() {
        Fleet fleet = new Fleet(List.of(), List.of());
        Player shooter = new Player("S", new Fleet(List.of(), List.of()),
                new Board(new Fleet(List.of(), List.of())), new Knowledge());
        Player victim = new Player("V", fleet, new Board(fleet), new Knowledge());

        Coordinate coord = new Coordinate(0, 0);
        gameService.applyShot(shooter, victim, coord);
        List<GameEvent> events = gameService.applyShot(shooter, victim, coord);

        assertTrue(events.stream()
                .filter(e -> e instanceof ShotEvent se)
                .anyMatch(e -> ((ShotEvent) e).shotResult() == ShotResult.ALREADY_SHOT));
    }

    @Test
    void checkWinner_returnsNullWhenNoOneDefeated() {
        assertNull(gameService.checkWinner(player1, player2));
    }

    @Test
    void checkWinner_returnsWinnerWhenOneDefeated() {
        // Потопим все корабли player1
        for (Ship ship : player1.fleet().getShips()) {
            for (Coordinate cell : ship.getCells()) {
                ship.hit(cell);
            }
        }

        String winner = gameService.checkWinner(player1, player2);
        assertEquals("Игрок2", winner);
    }

    @Test
    void mineExplosion_affectsNearbyShips() {
        // Корабль рядом с миной
        Ship ship = new Ship(ShipType.BOAT, List.of(new Coordinate(3, 4)));
        Mine mine = new Mine(new Coordinate(3, 3), 1);
        Fleet fleet = new Fleet(List.of(ship), List.of(mine));

        Player shooter = new Player("S", new Fleet(List.of(), List.of()),
                new Board(new Fleet(List.of(), List.of())), new Knowledge());
        Player victim = new Player("V", fleet, new Board(fleet), new Knowledge());

        List<GameEvent> events = gameService.applyShot(shooter, victim, new Coordinate(3, 3));

        MineExplodedEvent mineEvent = events.stream()
                .filter(e -> e instanceof MineExplodedEvent)
                .map(e -> (MineExplodedEvent) e)
                .findFirst()
                .orElseThrow();

        assertTrue(mineEvent.hurtShips().size() >= 1);
    }

}