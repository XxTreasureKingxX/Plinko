package com.xxtreasurekingxx.plinko.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.Map.Levels;

public class LoadingScreen implements Screen {
    private final Core core;
    private AssetManager assetManager;

    public LoadingScreen(final Core core) {
        this.core = core;
    }

    @Override
    public void show() {
        System.out.println("Switched To Loading Screen");
        assetManager = core.getAssetManager();
        queueAssets();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 1, 0);

        if(assetManager.update()) {
            core.setScreen(core.menuScreen);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void queueAssets() {
        //assetManager.load("ui/menuBackground.png", Texture.class);
        assetManager.load("ui/main.atlas", TextureAtlas.class);
        assetManager.load("objects.atlas", TextureAtlas.class);
        //load level tile maps
        for(Levels level: Levels.values()) {
            assetManager.load(level.getMapPath(), TiledMap.class);
        }
    }
}
