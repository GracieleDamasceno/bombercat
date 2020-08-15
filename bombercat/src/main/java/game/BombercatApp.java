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

    private static final int WIDGTH = 800;
    private static final int HEIGHT = 800;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Bombercat");
        settings.setVersion("0.1");
        settings.setWidth(WIDGTH);
        settings.setHeight(HEIGHT);
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                getGameWorld().getSingleton(EntityType.CAT).translateX(5); // move right 5 pixels
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                getGameWorld().getSingleton(EntityType.CAT).translateX(-5); // move left 5 pixels
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                getGameWorld().getSingleton(EntityType.CAT).translateY(-5); // move up 5 pixels
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                getGameWorld().getSingleton(EntityType.CAT).translateY(5); // move down 5 pixels
            }
        }, KeyCode.S);
    }

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.DARKGRAY);
        getGameWorld().addEntityFactory(new BombercatFactory());
        spawn("cat", 0, WIDGTH - (WIDGTH - 20));
        spawn("mouse", 0, WIDGTH - (WIDGTH - 80));
    }

    @Override
    protected void initPhysics() {
        FXGL.onCollision(EntityType.CAT, EntityType.MOUSE, (proj, enemy) -> {
            proj.removeFromWorld();
            enemy.removeFromWorld();
        });
    }
}
