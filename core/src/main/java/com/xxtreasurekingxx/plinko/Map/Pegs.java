package com.xxtreasurekingxx.plinko.Map;

public enum Pegs {
    BASE("base", 6);

    private String name;
    private int size;

    Pegs(final String name, final int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
