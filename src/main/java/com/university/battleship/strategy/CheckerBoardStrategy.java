package com.university.battleship.strategy;

import com.university.battleship.model.Coordinate;
import com.university.battleship.model.Knowledge;

import java.util.Random;

/**
 * Стратегия стрельбы в шахматном порядке.
 *
 * <p>Сначала обстреливает клетки, где (row+col) чётное, потом нечётное.
 * Эффективнее случайной: минимальное число выстрелов для поиска кораблей
 * (теоретически достаточно 50 выстрелов для поиска всех кораблей).
 */
public final class CheckerBoardStrategy implements ShotStrategy {
    private final Random random;
    private boolean evenPhase = true;

    /**
     * Создаёт стратегию с заданным генератором случайных чисел.
     *
     * @param random генератор случайных чисел
     */
    public CheckerBoardStrategy(Random random){
        this.random = random;
    }

    @Override
    public Coordinate chooseShot(Knowledge knowledge){
        // Сначала чётные клетки
        for (int i = 0; i < 1000; i++){
            int row = random.nextInt(Coordinate.BOARD_SIZE);
            int col = random.nextInt(Coordinate.BOARD_SIZE);

            if ((row + col) % 2 == 0){
                Coordinate coord = new Coordinate(row, col);
                if (!knowledge.isShot(coord)){
                    return coord;
                }
            }
        }
        // Если все чётные обстреляны — переходим к нечётным
        while (true){
            int row = random.nextInt(Coordinate.BOARD_SIZE);
            int col = random.nextInt(Coordinate.BOARD_SIZE);

            Coordinate coord = new Coordinate(row, col);
            if (!knowledge.isShot(coord)){
                return coord;
            }
        }
    }

    @Override
    public String getName(){
        return "Шахматная";
    }
}
