package com.university.battleship.model;

import java.util.Objects;

/**
 * Морская мина.
 *
 * <p>При попадании взрывается по площади, повреждая корабли в радиусе.
 * Радиус взрыва по умолчанию равен 1 (область 3×3).
 *
 * @param coord  координаты мины на поле
 * @param radius радиус взрыва (должен быть >= 1)
 */
public record Mine(Coordinate coord, int radius){

    /**
     * Компактный конструктор с валидацией.
     *
     * @throws NullPointerException     если coord null
     * @throws IllegalArgumentException если radius < 1
     */
    public Mine{
        Objects.requireNonNull(coord, "Координаты мины не могут быть пустыми.");
        if (radius < 1){
            throw new IllegalArgumentException("Радиус взрыва должен быть больше или равен 1.");
        }
    }

    /**
     * Создаёт мину с радиусом взрыва 1 (область 3×3).
     *
     * @param coord координаты мины
     */
    public Mine(Coordinate coord){
        this(coord, 1);
    }

    /**
     * Возвращает координаты мины.
     *
     * @return координаты мины
     */
    public Coordinate getCoord(){
        return coord;
    }

    /**
     * Возвращает радиус мины.
     *
     * @return радиус взрыва мины
     */
    public int getRadius(){
        return radius;
    }

}
