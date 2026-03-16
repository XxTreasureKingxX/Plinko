package com.xxtreasurekingxx.plinko.Map;

public enum Levels {
    ONE("maps/level1.tmx");

    private String mapPath;

    Levels(final String mapPath) {
        this.mapPath = mapPath;
    }

    public String getMapPath() {
        return mapPath;
    }
}
