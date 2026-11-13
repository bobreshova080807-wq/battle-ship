package com.university.battleship.model;

/**
 * Результат выстрела.
 *
 * <p>Возвращается методом {@link com.university.battleship.service.GameService#applyShot}
 * и используется в событии {@link com.university.battleship.event.ShotEvent}.
 */
public enum ShotResult {

    /** Промах — клетка пуста (вода) */
    MISS,

    /** Попадание в корабль (корабль ещё не потоплен) */
    HIT,

    /** Корабль потоплен (все клетки поражены) */
    SUNK,

    /** Попал в мину — произошёл взрыв по площади */
    MINE_HIT,

    /** Клетка уже была обстреляна ранее */
    ALREADY_SHOT,

    /** Выстрел в подводную лодку — не обнаружена (считается промахом) */
    SUBMARINE_MISSED,

    /** Тральщик обезвредил мину */
    CLEARED

}
