package com.university.battleship.event;

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
