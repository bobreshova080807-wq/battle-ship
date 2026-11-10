package com.university.battleship.model;

import java.util.Objects;

public record Player(String name, Fleet fleet, Board board, Knowledge knowledge) {

    public Player{
        Objects.requireNonNull(name, "Имя игрока не может быть пустым.");
        Objects.requireNonNull(fleet, "Флот игрока не может быть пустым.");
        Objects.requireNonNull(board, "Поле игрока не может быть пустым.");
        Objects.requireNonNull(knowledge, "Знания игрока о противнике не могут быть пустыми.");
    }

    public boolean isDefeated(){
        return fleet.allShipsSunk();
    }
}
