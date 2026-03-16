package com.xxtreasurekingxx.plinko.Map;

public enum Monsters {
    SLIME("slime", 12, 12);

    private String name;
    private int width;
    private int height;

    Monsters(final String name, final int width, final int height) {
        this.name = name;
        this.width = width;
        this.height = height;
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
}
