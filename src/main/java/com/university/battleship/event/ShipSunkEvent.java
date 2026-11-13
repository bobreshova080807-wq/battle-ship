package com.university.battleship.event;

import com.university.battleship.model.ShipType;
import com.university.battleship.model.Coordinate;

import java.util.List;

/**
 * Событие потопления корабля.
 *
 * <p>Фиксирует факт потопления корабля определённого типа.
 *
 * @param shooterName имя игрока, который потопил корабль
 * @param victimName  имя игрока, чей корабль потоплен
 * @param shipType    тип потопленного корабля
 * @param cells       клетки, которые занимал корабль
 */
public record ShipSunkEvent(String shooterName, String victimName, List<Coordinate> cells, ShipType shipType)
        implements GameEvent{
    @Override
    public String getPlayerName(){
        return shooterName;
    }

    @Override
    public String describe(){
        return shooterName + " потопил(а) " + shipType.getName() + " игрока " + victimName;
    }
}
