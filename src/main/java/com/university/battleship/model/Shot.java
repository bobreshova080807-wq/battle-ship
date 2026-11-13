package com.university.battleship.model;

/**
 * Выстрел игрока.
 *
 * <p>Записывает координаты выстрела и его результат.
 * Используется в журнале событий для анализа партии.
 *
 * @param coord  координаты выстрела
 * @param result результат выстрела (MISS, HIT, SUNK и т.д.)
 */
public record Shot(Coordinate coord, ShotResult result) {}