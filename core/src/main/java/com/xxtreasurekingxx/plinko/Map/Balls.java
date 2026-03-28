package com.xxtreasurekingxx.plinko.Map;

import com.xxtreasurekingxx.plinko.World.AnimationType;

public enum Balls implements Item {
    BASE("Normal Ball" , 1, AnimationType.BASE_BALL_ANI, true, "Normal Ball with no unique features.", "baseBallIcon.png", "baseBallBagIcon.png"),
    BOUNCY("Slime Ball" ,1, AnimationType.BASE_BALL_ANI, false, "The slime coating on this ball gives it the unique property of better bouncing.", "bouncyBallIcon.png", "bouncyBallBagIcon.png"),
    SILKY("Silky Ball" ,1, AnimationType.BASE_BALL_ANI, false, "", "bouncyBallIcon.png", "baseBallIcon.png"),
    HEAVY("Heavy Ball" ,1, AnimationType.BASE_BALL_ANI, false, "", "baseBallIcon.png", "baseBallIcon.png"),
    BIG("Big Ball" ,1, AnimationType.BASE_BALL_ANI, false, "", "baseBallIcon.png", "baseBallIcon.png"),
    SOUL("Soul Ball" ,1, AnimationType.SOUL_BALL_ANI, false, "This ball is always used as a last resort, every use of this ball drains the users life by 1 amount.", "soulBallIcon.png", "soulBallBagIcon.png");

    final String name;
    final int damage;
    final AnimationType ani;
    final boolean unlocked;
    final String description;
    final String iconPath;
    final String bagIconPath;

    Balls(final String name, final int damage, final AnimationType ani, final boolean unlocked, final String description, final String iconPath, final String bagIconPath) {
        this.name = name;
        this.damage = damage;
        this.ani = ani;
        this.unlocked = unlocked;
        this.description = description;
        this.iconPath = iconPath;
        this.bagIconPath = bagIconPath;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public AnimationType getAnimation() {
        return ani;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return iconPath;
    }

    @Override
    public String getBagIcon() {
        return bagIconPath;
    }
}
