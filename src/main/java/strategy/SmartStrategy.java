package strategy;

import com.university.battleship.model.Coordinate;
import com.university.battleship.model.Knowledge;
import com.university.battleship.model.CellState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class SmartStrategy implements ShotStrategy {
    private final Random random;

    public SmartStrategy(Random random){
        this.random = random;
    }

    @Override
    public Coordinate chooseShot(Knowledge knowledge){
        List<Coordinate> targets = new ArrayList<>();

        for (int r = 0; r < Coordinate.BOARD_SIZE; r++){
            for (int c = 0; c < Coordinate.BOARD_SIZE; c++){
                Coordinate coord = new Coordinate(r, c);
                if (knowledge.getState(coord) == CellState.HIT){
                    targets.addAll(coord.neighbors().stream().filter(n -> !knowledge.isShot(n)).toList());
                }
            }
        }
        if (!targets.isEmpty()){
            return targets.get(random.nextInt(targets.size()));
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
        return "Умная (есть добивание)";
    }
}
