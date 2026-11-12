package strategy;

import com.university.battleship.model.Coordinate;
import com.university.battleship.model.Knowledge;

import java.util.Random;

public final class CheckerBoardStrategy implements ShotStrategy {
    private final Random random;
    private boolean evenPhase = true;

    public CheckerBoardStrategy(Random random){
        this.random = random;
    }

    @Override
    public Coordinate chooseShot(Knowledge knowledge){
        for (int i = 0; i < 1000; i++){
            int row = random.nextInt(Coordinate.BOARD_SIZE);
            int col = random.nextInt(Coordinate.BOARD_SIZE);

            if ((row + col) % 2 == 0){
                Coordinate coord = new Coordinate(row, col);
                if (!knowledge.isShot(coord)){
                    return coord;
                }
            }
        }
        while (true){
            int row = random.nextInt(Coordinate.BOARD_SIZE);
            int col = random.nextInt(Coordinate.BOARD_SIZE);

            Coordinate coord = new Coordinate(row, col);
            if (!knowledge.isShot(coord)){
                return coord;
            }
        }
    }

    @Override
    public String getName(){
        return "Шахматная";
    }
}
