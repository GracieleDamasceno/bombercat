package bombergirl;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GameBackground {

    public static Integer chooseRandomGamePosition(){
        return new Random().nextInt(BomberGirlApp.HEIGHT + 1);
    }
}
