package com.university.battleship.model;

import java.util.Objects;

/**
 * Игрок в морском бое.
 *
 * <p>Имеет имя, флот, физическое поле и знания о поле противника.
 * Знания отделены от поля для инкапсуляции информации.
 *
 * @param name      имя игрока
 * @param fleet     флот игрока (корабли и мины)
 * @param board     физическое поле 10×10
 * @param knowledge знания о поле противника
 */
public record Player(String name, Fleet fleet, Board board, Knowledge knowledge) {

    /**
     * Компактный конструктор с валидацией.
     *
     * @throws NullPointerException если любое поле null
     */
    public Player{
        Objects.requireNonNull(name, "Имя игрока не может быть пустым.");
        Objects.requireNonNull(fleet, "Флот игрока не может быть пустым.");
        Objects.requireNonNull(board, "Поле игрока не может быть пустым.");
        Objects.requireNonNull(knowledge, "Знания игрока о противнике не могут быть пустыми.");
    }

    /**
     * Возвращает true, если все корабли игрока потоплены.
     *
     * @return true, если игрок проиграл
     */
    public boolean isDefeated(){
        return fleet.allShipsSunk();
    }
}
