package logic;

import game.BombercatApp;

import java.util.Random;

public class GameBackground {

    public static Integer chooseRandomGamePosition(){
        return new Random().nextInt(BombercatApp.HEIGHT + 1);
    }
}
