package com.xxtreasurekingxx.plinko.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;
import com.xxtreasurekingxx.plinko.Map.Levels;

import static com.xxtreasurekingxx.plinko.Core.*;

public class GameScreen implements Screen, InputProcessor {
    private final Core core;
    private ECSEngine engine;
    private SpriteBatch batch;
    private final FitViewport viewport;

    public static Levels currentLevel;

    private float accummulator = 0f;

    public GameScreen(final Core core) {
        this.core = core;
        core.getInputManager().addListener(this);
        batch = core.getBatch();
        viewport = new FitViewport(Core.GAMEW, Core.GAMEH, new OrthographicCamera());
        viewport.getCamera().position.set(GAMEW/2f, GAMEH/2f, 0);
        viewport.getCamera().update();
    }

    @Override
    public void show() {
        System.out.println("Switched To Game Screen");
        Gdx.input.setInputProcessor(core.getInputManager());
        engine = new ECSEngine(core, viewport);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0.33f, 0.66f, 0);
        viewport.apply();

        accummulator += delta * gameSpeed;
        while (accummulator > tickRate) {
            engine.update(tickRate);
            accummulator -= tickRate;
        }
        engine.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        if(engine != null) {
            engine.resize(width, height);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        engine.hide();
    }

    @Override
    public void dispose() {
        System.out.println("Game Screen Disposed");
        //engine.getSystem(GameUISystem.class).getStage().dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
