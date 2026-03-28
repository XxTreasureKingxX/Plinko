package com.xxtreasurekingxx.plinko.Map;

public enum Resources implements Item {
    METAL_HUNK("metalHunkIcon.png", "metalHunkBagIcon.png"),
    SLIME_BLOB("slimeBlobIcon.png", "slimeBlobBagIcon.png");

    final String iconPath;
    final String bagIconPath;

    Resources(final String atlasPath, final String bagIconPath) {
        this.iconPath = atlasPath;
        this.bagIconPath = bagIconPath;
    }

    @Override
    public String getIcon() {
        return iconPath;
    }

    @Override
    public String getBagIcon() {
        return bagIconPath;
    }
}
