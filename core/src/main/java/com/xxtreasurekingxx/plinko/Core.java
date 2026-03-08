package com.xxtreasurekingxx.plinko;

import com.badlogic.gdx.Game;
import com.xxtreasurekingxx.plinko.Screens.GameScreen;
import com.xxtreasurekingxx.plinko.Screens.LoadingScreen;
import com.xxtreasurekingxx.plinko.Screens.MenuScreen;
import com.xxtreasurekingxx.plinko.Screens.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {
    public static final int GAMEH = 1080;
    public static final int GAMEW = 1920;
    public static final float FPS = 1 / 60f;

    SplashScreen splashScreen;
    LoadingScreen loadingScreen;
    MenuScreen menuScreen;
    GameScreen gameScreen;

    @Override
    public void create() {
        splashScreen = new SplashScreen();
        loadingScreen = new LoadingScreen();
        menuScreen = new MenuScreen();
        gameScreen = new GameScreen();

        setScreen(splashScreen);
    }
}
