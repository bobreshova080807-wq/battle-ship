package com.university.battleship.event;

import com.university.battleship.model.Coordinate;

/**
 * Событие попадания в тральщик, когда у стреляющего нет активных мин.
 */
public record MinesweeperNoMinesEvent(
        String shooterName,
        String victimName,
        Coordinate trawlerCoord
) implements GameEvent {

    @Override
    public String getPlayerName() {
        return shooterName;
    }

    @Override
    public String describe() {
        return shooterName + " попал на тральщик в " + trawlerCoord
                + ", но у него не осталось активных мин — выдавать нечего";
    }
}