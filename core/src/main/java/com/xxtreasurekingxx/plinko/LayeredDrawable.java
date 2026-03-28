package com.xxtreasurekingxx.plinko;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class LayeredDrawable extends BaseDrawable {
    final Drawable base;
    final Drawable overlay;

    public LayeredDrawable(final Drawable base, final Drawable overlay) {
        this.base = base;
        this.overlay = overlay;

        setMinWidth(base.getMinWidth());
        setMinHeight(base.getMinHeight());
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        base.draw(batch, x, y, width, height);
        overlay.draw(batch, x, y, width, height);
    }
}
