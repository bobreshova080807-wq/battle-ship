package com.university.battleship.io;

import com.university.battleship.model.Player;

public record GameState(
        String player1Name,
        String player2Name,
        FleetState fleetState1,
        FleetState fleetState2,
        KnowledgeState knowledgeState1,
        KnowledgeState knowledgeState2,
        long randomSeed,
        int countShots
) {
    public static GameState fromGame(Player player1, Player player2,
                                     long randomSeed, int countShots){
        return new GameState(player1.name(), player2.name(),
                FleetState.fromFleet(player1.fleet()), FleetState.fromFleet(player2.fleet()),
                KnowledgeState.fromKnowledge(player1.knowledge()), KnowledgeState.fromKnowledge(player2.knowledge()),
                randomSeed, countShots);
    }
}
