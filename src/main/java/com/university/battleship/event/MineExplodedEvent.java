package com.university.battleship.event;

import com.university.battleship.model.Coordinate;

import java.util.List;

/**
 * Событие взрыва мины.
 *
 * <p>Фиксирует факт подрыва на мине и список повреждённых кораблей.
 *
 * @param playerName имя игрока, который подорвался на мине
 * @param mineCoord  координаты мины
 * @param radius     радиус взрыва
 * @param hurtShips  координаты клеток кораблей, повреждённых взрывом
 */
public record MineExplodedEvent(String playerName, Coordinate mineCoord, int radius, List<Coordinate> hurtShips)
        implements GameEvent{
    @Override
    public String getPlayerName(){
        return playerName;
    }

    @Override
    public String describe(){
        return playerName + " подорвался на мине в " + mineCoord + "! Взрыв с радиусом " + radius + " задел " + hurtShips.size() + " кораблей.";
    }
}
