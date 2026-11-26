package com.university.battleship.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Координаты клетки на поле 10×10.
 *
 * <p>Используется как immutable-ключ для клеток в {@link java.util.Map}.
 * Record автоматически генерирует {@link #equals(Object)}, {@link #hashCode()}
 * и {@link #toString()}.
 *
 * @param row номер строки (0-9, сверху вниз)
 * @param col номер столбца (0-9, слева направо)
 */
public record Coordinate(int row, int col) {
    /** Размер поля (10×10). */
    public static final int BOARD_SIZE = 10;

    /**
     * Компактный конструктор с валидацией координат.
     *
     * @throws IllegalArgumentException если row или col вне диапазона [0, 9]
     */
    public Coordinate{
        if (row < 0 || row >= BOARD_SIZE){
            throw new IllegalArgumentException("Строка вне поля: " + row);
        }
        if (col < 0 || col >= BOARD_SIZE){
            throw new IllegalArgumentException("Столбец вне поля: " + col);
        }
    }

    /**
     * Возвращает координату со смещением относительно текущей.
     *
     * @param dRow смещение по строке (может быть отрицательным)
     * @param dCol смещение по столбцу (может быть отрицательным)
     * @return новая координата или null, если результат вне поля
     */
    public Coordinate offset(int dRow, int dCol){
        int newRow = row + dRow;
        int newCol = col + dCol;
        if (newRow < 0 || newRow >= BOARD_SIZE || newCol < 0 || newCol >= BOARD_SIZE){
            return null;
        }
        return new Coordinate(newRow, newCol);
    }

    /**
     * Возвращает соседние клетки по вертикали и горизонтали (4 направления).
     * Не включает диагонали.
     *
     * @return список соседних координат (от 2 до 4 элементов)
     */
    public List<Coordinate> neighbors(){
        List<Coordinate> result = new ArrayList<>();
        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for(int[] d: dirs){
            Coordinate n = offset(d[0], d[1]);
            if (n != null){
                result.add(n);
            }
        }
        return result;
    }

    /**
     * Возвращает все клетки в заданном радиусе (включая диагонали).
     * Используется для взрыва мины.
     *
     * @param radius радиус (1 = область 3×3, 2 = область 5×5)
     * @return список координат в радиусе (включая саму клетку)
     */
    public List<Coordinate> inRadius(int radius){
        List<Coordinate> result = new ArrayList<>();
        for (int dr = -radius; dr <= radius; dr++){
            for (int dc = -radius; dc <= radius; dc++){
                Coordinate c = offset(dr,dc);
                if (c != null){
                    result.add(c);
                }
            }
        }
        return result;
    }

    /**
     * Возвращает строковое представление координат в формате "A1", "B5" и т.д.
     * Столбец обозначается буквой (A-J), строка — числом (1-10).
     *
     * @return строковое представление координат
     */
    @Override
    public String toString(){
        return (char) ('А' + col) + " " + (row + 1);
    }

}
