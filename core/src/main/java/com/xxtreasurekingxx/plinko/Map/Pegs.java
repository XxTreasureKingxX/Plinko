package com.xxtreasurekingxx.plinko.Map;

import com.xxtreasurekingxx.plinko.World.AnimationType;

import static com.xxtreasurekingxx.plinko.World.AnimationType.PEG_BASE;
import static com.xxtreasurekingxx.plinko.World.AnimationType.PEG_METAL;

public enum Pegs {
    BASE("base", PEG_BASE, 6),
    METAL("metal", PEG_METAL, 6);

    private String name;
    private AnimationType aniType;
    private int size;

    Pegs(final String name, final AnimationType aniType, final int size) {
        this.name = name;
        this.aniType = aniType;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public AnimationType getAniType() {
        return aniType;
    }

    public int getSize() {
        return size;
    }
}
