package com.university.battleship.event;

/**
 * Событие окончания игры.
 *
 * <p>Фиксирует победителя и общее количество выстрелов.
 *
 * @param winnerName  имя победителя
 * @param loserName   имя проигравшего
 * @param countShots  общее количество выстрелов в партии
 */
public record GameOverEvent(String winnerName, String loserName, int countShots) implements GameEvent{
    @Override
    public String getPlayerName(){
        return winnerName;
    }

    @Override
    public String describe(){
        return winnerName + " победил!!!! Игроку потребовалось " + countShots + " выстрелов.";
    }
}
