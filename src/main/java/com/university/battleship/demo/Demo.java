package com.university.battleship.demo;

import com.university.battleship.event.GameEvent;
import com.university.battleship.event.ShotEvent;
import com.university.battleship.model.*;
import com.university.battleship.service.FleetGenerator;
import com.university.battleship.service.GameService;
import com.university.battleship.strategy.CheckerBoardStrategy;
import com.university.battleship.strategy.ShotStrategy;
import com.university.battleship.strategy.SmartStrategy;

import java.util.List;
import java.util.Random;

/**
 * Демонстрационное приложение "Морской бой".
 *
 * <p>Показывает:
 * <ul>
 *   <li>Генерацию флота с минами</li>
 *   <li>Игру между двумя ботами с разными стратегиями</li>
 *   <li>Журнал событий</li>
 * </ul>
 *
 * <p>Использует фиксированное зерно генератора случайных чисел для воспроизводимости.
 */
public class Demo {
    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        System.out.println("**** МОРСКОЙ БОЙ (ДЕМО) ****");

        Random random = new Random(42);

        GameService gameService = new GameService(random);
        FleetGenerator fleetGenerator = new FleetGenerator(random);

        Fleet fleet1 = fleetGenerator.generate(5);
        Fleet fleet2 = fleetGenerator.generate(5);

        Player player1 = new Player("Адмирал А", fleet1, new Board(fleet1), new Knowledge());
        Player player2 = new Player("Боцман Б", fleet2, new Board(fleet2), new Knowledge());

        System.out.println("ИГРОКИ:");
        System.out.println("1) " + player1.name() + " - кораблей: " + player1.fleet().getShips().size() + ", мин: " + player1.fleet().getMines().size());
        System.out.println("2) " + player2.name() + " - кораблей: " + player2.fleet().getShips().size() + ", мин: " + player2.fleet().getMines().size());
        System.out.println();

        ShotStrategy strategy1 = new SmartStrategy(new Random(42));
        ShotStrategy strategy2 = new CheckerBoardStrategy(new Random(42));

        System.out.println("СТРАТЕГИИ:");
        System.out.println("1) " + player1.name() + ": " + strategy1.getName());
        System.out.println("2) " + player2.name() + ": " + strategy2.getName());
        System.out.println();

        Player current = player1;
        Player opponent = player2;

        ShotStrategy currentStrategy = strategy1;
        ShotStrategy opponentStrategy = strategy2;

        int turn = 0;
        int allShots = 0;

        System.out.println("НАЧАЛО ИГРЫ:");

        while (true) {
            turn++;
            System.out.println("Ход " + turn + ": " + current.name());

            Coordinate shot = currentStrategy.chooseShot(current.knowledge());

            List<GameEvent> events = gameService.applyShot(current, opponent, shot);

            for (GameEvent event : events) {
                System.out.println(" " + event.describe());
            }
            System.out.println();

            allShots++;

            // Проверяем попадание и меняем игрока (если промах)
            boolean hit = events.stream()
                    .filter(e -> e instanceof ShotEvent)
                    .map(e -> (ShotEvent) e)
                    .anyMatch(se -> se.shotResult() != ShotResult.MISS
                            && se.shotResult() != ShotResult.SUBMARINE_MISSED);

            if (!hit) {
                Player temp = current;
                current = opponent;
                opponent = temp;

                ShotStrategy tempStrategy = currentStrategy;
                currentStrategy = opponentStrategy;
                opponentStrategy = tempStrategy;
            }

            // Проверяем победителя
            String winner = gameService.checkWinner(current, opponent);
            System.out.println("ПОБЕДИТЕЛЬ: " + winner);
            System.out.println("Всего было ходов: " + turn);
            System.out.println("Всего было выстрелов: " + allShots);
            break;
        }

        System.out.println("\n=== ДЕМО ПОДВОДНОЙ ЛОДКИ ===\n");

        Ship submarine = new Ship(ShipType.SUBMARINE, List.of(new Coordinate(5, 5)));
        Ship boat = new Ship(ShipType.BOAT, List.of(new Coordinate(5, 6)));  // Рядом с подлодкой!
        Fleet demoFleet = new Fleet(List.of(submarine, boat), List.of());
        Player demoPlayer = new Player("Подводник", demoFleet, new Board(demoFleet), new Knowledge());

// У охотника корабль в клетке (5, 5) — туда подлодка и выстрелит
        Ship hunterShip = new Ship(ShipType.BOAT, List.of(new Coordinate(5, 5)));
        Fleet hunterFleet = new Fleet(List.of(hunterShip), List.of());
        Player hunter = new Player("Охотник", hunterFleet, new Board(hunterFleet), new Knowledge());

        System.out.println("Подлодка может касаться кораблей (катер стоит в (5,6))");
        System.out.println("Охотник стреляет в (5,5) — попадает в подлодку\n");

        List<GameEvent> eventsSubmarine = gameService.applyShot(hunter, demoPlayer, new Coordinate(5, 5));
        for (GameEvent event : eventsSubmarine) {
            System.out.println("  " + event.describe());
        }
        System.out.println("Корабль охотника потоплен: " + hunterShip.isSunk());

        System.out.println("\n=== ДЕМО МИННОГО ТРАЛЬЩИКА ===\n");

        Ship trawler = new Ship(ShipType.MINESWEEPER,
                List.of(new Coordinate(3, 3), new Coordinate(3, 4)));
        Mine shooterMine = new Mine(new Coordinate(7, 7));
        Fleet trawlerFleet = new Fleet(List.of(trawler), List.of());
        Fleet shooterFleet2 = new Fleet(
                List.of(new Ship(ShipType.BOAT, List.of(new Coordinate(0, 0)))),
                List.of(shooterMine)
        );

        Player trawlerOwner = new Player("Тральщик", trawlerFleet, new Board(trawlerFleet), new Knowledge());
        Player mineOwner = new Player("Минёр", shooterFleet2, new Board(shooterFleet2), new Knowledge());

        System.out.println("Минёр стреляет в тральщик — вынужден выдать координату своей мины\n");

        List<GameEvent> events2 = gameService.applyShot(mineOwner, trawlerOwner, new Coordinate(3, 3));
        for (GameEvent event : events2) {
            System.out.println("  " + event.describe());
        }
        System.out.println("Тральщик знает о мине: "
                + (trawlerOwner.knowledge().getState(new Coordinate(7, 7)) == CellState.KNOWN_MINE));

    }
}

