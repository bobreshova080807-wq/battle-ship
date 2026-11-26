package com.university.battleship.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.university.battleship.model.Player;

import java.io.File;
import java.io.IOException;

public final class GameSaver {

    private final ObjectMapper mapper;

    public GameSaver(){
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void save(Player player1, Player player2,
                     long randomSeed, int countShots, File file) throws IOException{
        GameState gameState = GameState.fromGame(player1, player2, randomSeed, countShots);
        mapper.writeValue(file, gameState);
    }

    public String saveToString(Player player1, Player player2,
                               long randomSeed, int countShots) throws IOException{
        GameState gameState = GameState.fromGame(player1, player2, randomSeed, countShots);
        return mapper.writeValueAsString(gameState);
    }
}
