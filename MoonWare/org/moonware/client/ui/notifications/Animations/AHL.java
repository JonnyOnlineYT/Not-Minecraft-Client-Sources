package org.moonware.client.ui.notifications.Animations;


import org.moonware.client.helpers.Utils.Animation;
import org.moonware.client.helpers.math.animations.Direction;
import org.moonware.client.helpers.math.animations.impl.EaseInOutQuad;

public class AHL {

    public int x;

    public int y;
    public int s;

    public int xAnim;

    public int yAnim;
    public int sAnim;

    public float xS;
    public float yS;
    public float sS;

    public EaseInOutQuad animX;
    public EaseInOutQuad animY;
    public EaseInOutQuad animSc;

    public Animation openingAnimation;
    public AHL(int x,int y, float xSpeed, float ySpeed) {
        this.x = x;
        this.y  = y;
        this.s = 0;
        this.xAnim = x;
        this.yAnim  = y;
        this.sAnim  = 0;

        this.xS = xSpeed;
        this.yS = ySpeed;
        this.animX = new EaseInOutQuad((int) xS,x, Direction.FORWARDS);
        this.animY = new EaseInOutQuad((int) yS,y, Direction.FORWARDS);
        this.animSc = new EaseInOutQuad((int) xS,0, Direction.FORWARDS);

    }

    public void setX(int x) {
        this.x = x;
        this.animX.setEndPoint(x);
    }

    public void setY(int y) {
        this.y = y;
        this.animY.setEndPoint(y);
    }

    public void setS(int s) {
        this.s = s;
        this.animSc.setEndPoint(s);
    }

    public int getX() {
        System.out.println(animX.getOutput());
        return (int) animX.getOutput();
    }

    public int getY() {

        return (int) animY.getOutput();
    }

    public float getS() {
        return (float) animSc.getOutput();
    }
}
