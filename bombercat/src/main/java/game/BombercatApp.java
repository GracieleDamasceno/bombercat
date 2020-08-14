package game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.GameBackground;


public class BombercatApp extends GameApplication{

    private Entity player;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("Bombercat");
        settings.setVersion("0.1");
        settings.setIntroEnabled(false); // turn off intro
        settings.setMenuEnabled(true);  // turn off menus
    }

    @Override
    protected void initGame() {
        player = Entities.builder()
                .at(300, 300)
                .viewFromNode(new Rectangle(25, 25, Color.HOTPINK))
                .buildAndAttach(getGameWorld());
        getGameScene().addUINode(generateWallTexture());
        getGameScene().addUINode(generateWallTexture());
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                player.translateX(5); // move right 5 pixels
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                player.translateX(-5); // move left 5 pixels
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                player.translateY(-5); // move up 5 pixels
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                player.translateY(5); // move down 5 pixels
            }
        }, KeyCode.S);
    }

    private Texture generateWallTexture(){
        Texture brickTexture = getAssetLoader().loadTexture("brick.png");
        brickTexture.setTranslateX(GameBackground.chooseRandomGamePosition());
        brickTexture.setTranslateY(GameBackground.chooseRandomGamePosition());
        return brickTexture;
    }
}
