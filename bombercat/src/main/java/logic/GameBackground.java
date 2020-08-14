package logic;

import game.BomberCat;

import java.util.Random;

public class GameBackground {

    public static Integer chooseRandomGamePosition(){
        return new Random().nextInt(BomberCat.HEIGHT + 1);
    }
}
