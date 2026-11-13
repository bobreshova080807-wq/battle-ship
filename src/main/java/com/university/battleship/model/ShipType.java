package com.university.battleship.model;

/**
 * Тип корабля с его характеристиками.
 *
 * <p>Каждый тип имеет:
 * <ul>
 *   <li>Размер (количество клеток)</li>
 *   <li>Количество в стандартном флоте</li>
 *   <li>Название на русском языке</li>
 * </ul>
 *
 * <p>Стандартный флот состоит из:
 * <ul>
 *   <li>4 катера (1 клетка)</li>
 *   <li>3 эсминца (2 клетки)</li>
 *   <li>2 крейсера (3 клетки)</li>
 *   <li>1 линкор (4 клетки)</li>
 *   <li>2 подводные лодки (1 клетка, скрыты)</li>
 *   <li>1 тральщик (2 клетки, обезвреживает мины)</li>
 * </ul>
 */
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

    /**
     * Конструктор типа корабля.
     *
     * @param size  размер корабля в клетках
     * @param count количество кораблей этого типа в стандартном флоте
     * @param name  название корабля на русском языке
     */
    ShipType(int size, int count, String name){
        this.size = size;
        this.count = count;
        this.name = name;
    }

    /**
     * Возвращает размер корабля в клетках.
     *
     * @return количество клеток, которые занимает корабль
     */
    public int getSize() {
        return size;
    }

    /**
     * Возвращает количество кораблей этого типа в стандартном флоте.
     *
     * @return количество кораблей
     */
    public int getCount() {
        return count;
    }

    /**
     * Возвращает название корабля на русском языке.
     *
     * @return название корабля
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает true, если корабль этого типа является подводной лодкой.
     * Подводные лодки скрыты от обычных выстрелов.
     *
     * @return true, если это подводная лодка
     */
    public boolean isSubmarine(){
        return this == SUBMARINE;
    }

    /**
     * Возвращает true, если корабль этого типа является тральщиком.
     * Тральщики обезвреживают мины в радиусе 1 клетки.
     *
     * @return true, если это тральщик
     */
    public boolean isMinesweeper(){
        return this == MINESWEEPER;
    }

}
