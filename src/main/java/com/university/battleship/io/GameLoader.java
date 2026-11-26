package com.university.battleship.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.university.battleship.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class GameLoader {

    private final ObjectMapper mapper;

    public GameLoader(){
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public GameState load(File file) throws IOException{
        return mapper.readValue(file, GameState.class);
    }

    public GameState loadFromString(String json) throws IOException{
        return mapper.readValue(json, GameState.class);
    }

    public Player[] restorePlayers(GameState gameState){
        Player player1 = restorePlayer(gameState.player1Name(),
                gameState.fleetState1(), gameState.knowledgeState1());
        Player player2 = restorePlayer(gameState.player2Name(),
                gameState.fleetState2(), gameState.knowledgeState2());

        return new Player[]{player1, player2};
    }

    private Player restorePlayer(String name, FleetState fleetState, KnowledgeState knowledgeState){
        Fleet fleet = resoreFleet(fleetState);
        Knowledge knowledge = restoreKnowledge(knowledgeState);
        Board board = new Board(fleet);

        return new Player(name, fleet, board, knowledge);
    }

    private Fleet resoreFleet(FleetState state){
        List<Ship> ships = state.ships().stream().map(this::restoreShip).toList();
        List<Mine> mines = state.mines().stream().map(ms -> new Mine(ms.coord(), ms.radius())).toList();

        Fleet fleet = new Fleet(ships, mines);

        for (Coordinate coord: state.revealedMines()){
            fleet.isMineRevealed(coord);
        }
        return fleet;
    }

    private Ship restoreShip(ShipState state){
        Ship ship = new Ship(state.shipType(), state.cells());
        if (ship.isSunk()){
            for (Coordinate cell: state.cells()){
                ship.hit(cell);
            }
        }
        return ship;
    }

    private Knowledge restoreKnowledge(KnowledgeState state){
        Knowledge knowledge = new Knowledge();
        for (var entry: state.cells().entrySet()){
            knowledge.update(entry.getKey(), entry.getValue());
        }
        return knowledge;
    }



}
