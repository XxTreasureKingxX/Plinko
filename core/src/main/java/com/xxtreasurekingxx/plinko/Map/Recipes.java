package com.xxtreasurekingxx.plinko.Map;

import com.badlogic.gdx.utils.Array;

import static com.xxtreasurekingxx.plinko.Map.Resources.METAL_HUNK;
import static com.xxtreasurekingxx.plinko.Map.Resources.SLIME_BLOB;

public enum Recipes {
    BASE_BALL(METAL_HUNK, null, Balls.BASE, "baseBallIcon.png"),
    BOUNCY_BALL(Balls.BASE, SLIME_BLOB, Balls.BOUNCY, "bouncyBallIcon.png");

    final Item item1;
    final Item item2;
    final Item output;
    final String resultOutputImage;

    Recipes(final Item item1, final Item item2, final Item output, final String resultOutputImage) {
        this.item1 = item1;
        this.item2 = item2;
        this.output = output;
        this.resultOutputImage = resultOutputImage;
    }

    public Array<Item> getRecipe() {
        Array<Item> recipe = new Array<>();

        recipe.add(item1);
        if(item2 == null) {
            return recipe;
        }
        recipe.add(item2);
        return recipe;
    }

    public Item getOutput() {
        return output;
    }

    public String getResultOutputImage() {
        return resultOutputImage;
    }
}
