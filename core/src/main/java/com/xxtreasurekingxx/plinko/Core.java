package com.xxtreasurekingxx.plinko;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.xxtreasurekingxx.plinko.Input.InputManager;
import com.xxtreasurekingxx.plinko.Screens.GameScreen;
import com.xxtreasurekingxx.plinko.Screens.LoadingScreen;
import com.xxtreasurekingxx.plinko.Screens.MenuScreen;
import com.xxtreasurekingxx.plinko.Screens.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {
    public static final int GAMEH = 360;
    public static final int GAMEW = 640;
    public static final float FPS = 1 / 60f;
    public static final float tickRate = 1 / 20f;
    public static final int gameSpeed = 10; //default 1x speed (1)
    public static final int gridSize = 16;
    public static final int PPM = 32;

    public static final short BIT_OBJECT = 1 << 0;
    public static final short BIT_WALL = 1 << 1;

    private SplashScreen splashScreen;
    private LoadingScreen loadingScreen;
    public MenuScreen menuScreen;
    public GameScreen gameScreen;

    private SpriteBatch batch;
    private AssetManager assetManager;
    private InputManager inputManager;

    private InputMultiplexer inputMultiplexer;

    @Override
    public void create() {
        inputManager = new InputManager();
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputManager);
        Gdx.input.setInputProcessor(inputMultiplexer);
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        splashScreen = new SplashScreen();
        loadingScreen = new LoadingScreen(this);
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);

        setScreen(loadingScreen);
    }

    //TODO can be moved to a utils class if needed
    public static float toMeters(final float pixels) {
        return pixels / PPM;
    }

    public static float toPixels(final float meters) {
        return meters * PPM;
    }

    public static Vector2 toPixels(final Vector2 meterPos) {
        return new Vector2(meterPos.x * PPM, meterPos.y * PPM);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        assetManager.dispose();
    }
}
