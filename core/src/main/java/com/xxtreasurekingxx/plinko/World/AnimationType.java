package com.xxtreasurekingxx.plinko.World;

public enum AnimationType {
    //pegs
    PEG_BASE("objects.atlas", "pegs", 1f, 0),
    PEG_METAL("objects.atlas", "pegs", 1f, 1),

    //balls
    BASE_BALL_ANI("objects.atlas", "pegs", 1f, 2),
    SOUL_BALL_ANI("objects.atlas", "pegs", 1f, 3),

    //monsters
    MONSTER1("objects.atlas", "pegs", 1f, 4);

    final String atlas;
    final String region;
    final float frameRate;
    final int row;

    AnimationType(final String atlas, final String region, final float frameRate, final int row) {
        this.atlas = atlas;
        this.region = region;
        this.frameRate = frameRate;
        this.row = row;
    }

    public String getAtlas() {
        return atlas;
    }

    public String getRegion() {
        return region;
    }

    public float getFrameRate() {
        return frameRate;
    }

    public int getRow() {
        return row;
    }
}
