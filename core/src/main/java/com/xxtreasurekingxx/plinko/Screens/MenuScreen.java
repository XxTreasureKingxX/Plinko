package com.xxtreasurekingxx.plinko.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.xxtreasurekingxx.plinko.Core;

public class MenuScreen implements Screen {
    private final Core core;
    private SpriteBatch batch;
    private MenuScene scene;

    public MenuScreen(final Core core) {
        this.core = core;
    }

    @Override
    public void show() {
        System.out.println("Switched To Menu Screen");
        batch = core.getBatch();
        scene = new MenuScene(core);
        core.getInputMultiplexer().addProcessor(scene.getStage());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 1, 0, 0);
        scene.update(delta);

        batch.setProjectionMatrix(scene.getStage().getCamera().combined);
        scene.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        core.getInputMultiplexer().removeProcessor(scene.getStage());
    }

    @Override
    public void dispose() {
        System.out.println("Menu Screen Disposed");
        scene.getStage().dispose();
    }
}
