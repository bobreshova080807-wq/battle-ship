package com.university.battleship.strategy;

import com.university.battleship.model.Knowledge;
import com.university.battleship.model.Coordinate;

/**
 * Стратегия выбора клетки для выстрела.
 *
 * <p>Паттерн Strategy: разные алгоритмы стрельбы реализуют этот интерфейс.
 * Позволяет легко добавлять новые стратегии без изменения механики игры.
 *
 * <p>Реализации:
 * <ul>
 *   <li>{@link RandomStrategy} — случайный выбор</li>
 *   <li>{@link CheckerBoardStrategy} — шахматный порядок</li>
 *   <li>{@link SmartStrategy} — добивание раненых кораблей</li>
 * </ul>
 */
public interface ShotStrategy {
    /**
     * Выбирает клетку для следующего выстрела.
     *
     * @param knowledge знания игрока о поле противника
     * @return координаты для выстрела
     */
    Coordinate chooseShot(Knowledge knowledge);

    /**
     * Возвращает название стратегии.
     *
     * @return название стратегии
     */
    String getName();
}
