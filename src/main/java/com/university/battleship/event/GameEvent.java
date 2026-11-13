package com.university.battleship.event;

import com.university.battleship.model.Coordinate;
import com.university.battleship.model.ShotResult;
import com.university.battleship.model.ShipType;

/**
 * Событие игры.
 *
 * <p>Механика ({@link com.university.battleship.service.GameService}) возвращает
 * список событий, а слой вывода ({@link com.university.battleship.demo.Demo}) их отображает.
 *
 * <p>Запечатанный интерфейс: разрешены только 4 реализации:
 * {@link ShotEvent}, {@link ShipSunkEvent}, {@link MineExplodedEvent}, {@link GameOverEvent}.
 */
public sealed interface GameEvent permits ShotEvent, ShipSunkEvent, MineExplodedEvent, GameOverEvent{
    /**
     * Возвращает имя игрока, которому адресовано событие.
     *
     * @return имя игрока
     */
    String getPlayerName();

    /**
     * Возвращает текстовое описание события для вывода в консоль.
     *
     * @return описание события
     */
    String describe();
}
