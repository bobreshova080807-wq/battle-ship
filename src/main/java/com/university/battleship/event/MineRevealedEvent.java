package com.university.battleship.event;

import com.university.battleship.model.Coordinate;

/**
 * Событие выдачи координаты мины через тральщик.
 * Стреляющий игрок вынужден раскрыть одну из своих мин противнику.
 */
public record MineRevealedEvent(
        String shooterName,
        String victimName,
        Coordinate mineCoord
) implements GameEvent {

    @Override
    public String getPlayerName() {
        return shooterName;
    }

    @Override
    public String describe() {
        return shooterName + " попал на тральщик и выдал координату мины: "
                + mineCoord + " (теперь " + victimName + " знает об этой мине)";
    }
}