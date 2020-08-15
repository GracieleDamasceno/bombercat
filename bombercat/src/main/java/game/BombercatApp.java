package game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import entity.EntityType;
import factory.BombercatFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class BombercatApp extends GameApplication{

    private Entity entity;

    private static final int WIDTH = 760; //600
    private static final int HEIGHT = 600; //440
    private static final int BRICK_SIZE = 40;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Bombercat");
        settings.setVersion("0.1");
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
    }

    @Override
    protected void initInput() {
        int PIXEL = 2;
        Input input = FXGL.getInput();
        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                getGameWorld().getSingleton(EntityType.CAT).translateX(PIXEL); // move right 5 pixels
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                getGameWorld().getSingleton(EntityType.CAT).translateX(-PIXEL); // move left 5 pixels
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                getGameWorld().getSingleton(EntityType.CAT).translateY(-PIXEL); // move up 5 pixels
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                getGameWorld().getSingleton(EntityType.CAT).translateY(PIXEL); // move down 5 pixels
            }
        }, KeyCode.S);
    }

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.GREEN);
        getGameWorld().addEntityFactory(new BombercatFactory());
        spawn("cat", 200, WIDTH - (WIDTH - 40));
        spawn("mouse", 120, WIDTH - (WIDTH - 80));
        spawn("fire", 120, WIDTH - (WIDTH - 160));
    }

    @Override
    protected void initPhysics() {
        FXGL.onCollision(EntityType.CAT, EntityType.MOUSE, (cat, mouse) -> {
            cat.removeFromWorld();
            mouse.removeFromWorld();
            FXGL.showMessage("YOU DIED!", () -> {
                getGameController().startNewGame();
            });
        });
        FXGL.onCollision(EntityType.CAT, EntityType.FIRE, (cat, bomb) -> {
            cat.removeFromWorld();
            bomb.removeFromWorld();
            FXGL.showMessage("YOU DIED!", () -> {
                getGameController().startNewGame();
            });
        });
    }
    @Override
    protected void initUI() {
        for (int i = 0; i < (WIDTH / BRICK_SIZE); i++) {
            for (int j = 0; j < (HEIGHT / BRICK_SIZE); j++) {
                if (i == 0 || j == 0 || i == (WIDTH / BRICK_SIZE) - 1 || j == (HEIGHT / BRICK_SIZE) - 1 ||
                ((i + 1) % 2 != 0 && (j + 1) % 2 != 0)) {
                    spawn("brick", i * BRICK_SIZE , j * BRICK_SIZE);
                }

            }
        }

    }
}
