package com.xxtreasurekingxx.plinko.Map;

import static com.xxtreasurekingxx.plinko.Map.Resources.SLIME_BLOB;

public enum Monsters {
    SLIME("slime", 12, 12, 1, SLIME_BLOB);

    private final String name;
    private final int width;
    private final int height;
    private final int health;
    private final Item drop;

    Monsters(final String name, final int width, final int height, final int health, final Item drop) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.health = health;
        this.drop = drop;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHealth() {
        return health;
    }

    public Item getDrop() {
        return drop;
    }
}
