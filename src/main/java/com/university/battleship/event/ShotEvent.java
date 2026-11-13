package com.university.battleship.event;

import com.university.battleship.model.ShotResult;
import com.university.battleship.model.Coordinate;

/**
 * Событие выстрела.
 *
 * <p>Записывает результат выстрела: промах, попадание, потопление, мина и т.д.
 *
 * @param playerName имя игрока, который стрелял
 * @param coord      координаты выстрела
 * @param shotResult результат выстрела
 */
public record ShotEvent(String playerName, Coordinate coord, ShotResult shotResult)
        implements GameEvent{
    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String describe(){
        return switch (shotResult){
            case MISS -> playerName + " стреляет в " + coord + " - Промах.";
            case HIT -> playerName + " стреляет в " + coord + " - Попадание!";
            case SUNK -> playerName + " стреляет в " + coord + " - Корабль потоплен!!!";
            case MINE_HIT -> playerName + " стреляет в " + coord + " - Попадание в мину! БАБАХ!!!";
            case ALREADY_SHOT -> playerName + " стреляет в " + coord + " - Сюда уже стреляли.";
            case SUBMARINE_MISSED -> playerName + " стреляет в " + coord + " - Промах";
            case CLEARED ->  playerName + " стреляет в " + coord + " - Мина успешна обезврежена!";
        };
    }
}
