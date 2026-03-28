package com.xxtreasurekingxx.plinko;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class GameData {
    private int health;
    private Array<Entity> balls;

    public GameData() {
        health = 10;
        balls = new Array<>();
    }

    public void pushData(final int health, final Array<Entity> balls) {
        this.health = health;
        this.balls = balls;
    }

    public void pullData() {

    }

    public int getHealth() {
        return health;
    }
}
