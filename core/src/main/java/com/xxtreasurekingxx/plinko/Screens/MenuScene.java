package com.xxtreasurekingxx.plinko.Screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.Map.Levels;

import static com.xxtreasurekingxx.plinko.Core.*;
import static com.xxtreasurekingxx.plinko.Screens.GameScreen.currentLevel;

public class MenuScene {
    private final Core core;
    private SpriteBatch batch;
    private Stage stage;
    private FitViewport viewport;
    private AssetManager assetManager;
    private BitmapFont font;
    private TextureAtlas atlas;
    private Stack UIStack;

    private Label.LabelStyle labelStyle;
    private TextButton.TextButtonStyle buttonStyle;

    //Main Menu Screen
    private Table mainMenuBackground;
    private TextButton playButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    //Level Select Screen
    private Table levelSelectBackground;

    //Upgrade Tree Screen
    private Table upgradeTreeBackground;

    public MenuScene(final Core core) {
        this.core = core;
        batch = core.getBatch();
        assetManager = core.getAssetManager();
        viewport = new FitViewport(GAMEW, GAMEH, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        initFiles();
        createStyles();
        createTables();
        createLabels();
        createButtons();

        initMainMenu();
        initLevelSelect();

        stage.addActor(UIStack);
    }

    public void update(float delta) {
        viewport.apply();
        stage.act(delta);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private void createStyles() {
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = new TextureRegionDrawable(atlas.findRegion("blank.png"));
        buttonStyle.over = new TextureRegionDrawable(atlas.findRegion("blankOver.png"));

        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.background = new TextureRegionDrawable(atlas.findRegion("blank.png"));
    }

    private void createTables() {
        UIStack = new Stack();
        UIStack.setFillParent(true);

        mainMenuBackground = new Table();
        //background.background(new TextureRegionDrawable(assetManager.get("ui/menuBackground.png", Texture.class)));

        levelSelectBackground = new Table();
        //

        upgradeTreeBackground = new Table();
        //

        showMainMenu();
        UIStack.add(upgradeTreeBackground);
        UIStack.add(levelSelectBackground);
        UIStack.add(mainMenuBackground);
    }

    private void createLabels() {
    }

    private void createButtons() {
        playButton = new TextButton("PLAY", buttonStyle);
        playButton.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    showLevelSelect();
                }
            }
        );

        settingsButton = new TextButton("SETTINGS", buttonStyle);
        settingsButton.addListener(
            new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {

                }
            }
        );

        exitButton = new TextButton("EXIT", buttonStyle);
        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
    }

    private void initMainMenu() {
        mainMenuBackground.add(playButton).row();
        mainMenuBackground.add(settingsButton).row();
        mainMenuBackground.add(exitButton);
    }

    private void initLevelSelect() {
        TextButton level1 = new TextButton("level 1", buttonStyle);
        level1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                currentLevel = Levels.ONE;
                core.setScreen(core.gameScreen);
            }
        });

        levelSelectBackground.add(level1);
    }

    private void showMainMenu() {
        mainMenuBackground.setVisible(true);
        levelSelectBackground.setVisible(false);
        upgradeTreeBackground.setVisible(false);
    }

    private void showLevelSelect() {
        levelSelectBackground.setVisible(true);
        mainMenuBackground.setVisible(false);
        upgradeTreeBackground.setVisible(false);
    }

    private void showUpgradeTree() {
        upgradeTreeBackground.setVisible(true);
        mainMenuBackground.setVisible(false);
        levelSelectBackground.setVisible(false);
    }

    private void initFiles() {
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        atlas = assetManager.get("ui/main.atlas", TextureAtlas.class);
    }

    public Stage getStage() {
        return stage;
    }
}
