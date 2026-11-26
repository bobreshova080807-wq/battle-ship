package com.university.battleship.io;

import com.university.battleship.model.Player;

public record GameState(
        String player1Name,
        String player2Name,
        long randomSeed,
        int countShots
) {
    public static GameState fromGame(Player player1, Player player2,
                                     long randomSeed, int countShots){
        return new GameState(player1.name(), player2.name(),
                randomSeed, countShots);
    }
}
