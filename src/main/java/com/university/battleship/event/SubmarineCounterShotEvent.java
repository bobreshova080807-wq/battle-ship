package com.university.battleship.event;

import com.university.battleship.model.Coordinate;
import com.university.battleship.model.ShotResult;

/**
 * Событие ответного выстрела подлодки.
 * Подлодка стреляет по той же координате поля стрелявшего.
 */
public record SubmarineCounterShotEvent(
        String submarineOwnerName,
        String targetName,
        Coordinate coord,
        ShotResult result
) implements GameEvent {

    @Override
    public String getPlayerName() {
        return submarineOwnerName;
    }

    @Override
    public String describe() {
        return "Подлодка игрока " + submarineOwnerName + " стреляет в " + coord
                + " поля " + targetName + " — " + switch (result) {
            case HIT -> "попадание!";
            case SUNK -> "корабль потоплен!";
            case MISS -> "промах";
            case MINE_HIT -> "МИНА!";
            default -> result.toString();
        };
    }
}