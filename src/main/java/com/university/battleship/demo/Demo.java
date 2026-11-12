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

public class Demo {
    public static void main(String[] args) {
        System.out.println("**** МОРСКОЙ БОЙ (ДЕМО) ****");

        Random random = new Random(42);

        GameService gameService = new GameService();
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

        while (true){
            turn++;
            System.out.println("Ход " + turn + ": " + current.name());

            Coordinate shot = currentStrategy.chooseShot(current.knowledge());

            List<GameEvent> events = gameService.appleShot(current, opponent, shot);

            for (GameEvent event: events){
                System.out.println(" " + event.describe());
            }
            System.out.println();

            allShots++;

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

            String winner = gameService.checkWinner(current, opponent);
            System.out.println("ПОБЕДИТЕЛЬ: " + winner);
            System.out.println("Всего было ходов: " + turn);
            System.out.println("Всего было выстрелов: " + allShots);
            break;

        }

    }
}
