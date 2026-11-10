package com.university.battleship.model;

public enum ShipType {

    BOAT(1, 4, "Катер"),
    DESTROYER(2, 3, "Эсминец"),
    CRUISER(3, 2, "Крейсер"),
    BATTLESHIP(4,1, "Линкор"),
    SUBMARINE(1,2,"Субмарина"),
    MINESWEEPER(2,1,"Тральщик");

    private final int size;
    private final int count;
    private final String name;

    ShipType(int size, int count, String name){
        this.size = size;
        this.count = count;
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public boolean isSubmarine(){
        return this == SUBMARINE;
    }

    public boolean isMinesweeper(){
        return this == MINESWEEPER;
    }

}
