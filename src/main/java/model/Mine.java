package model;

import java.util.Objects;

public record Mine(Coordinate coord, int radius){

    public Mine{
        Objects.requireNonNull(coord, "Координаты мины не могут быть пустыми.");
        if (radius < 1){
            throw new IllegalArgumentException("Радиус взрыва должен быть больше или равен 1.");
        }
    }

    public Mine(Coordinate coord){
        this(coord, 1);
    }

    public Coordinate getCoord(){
        return coord;
    }

    public int getRadius(){
        return radius;
    }

}
