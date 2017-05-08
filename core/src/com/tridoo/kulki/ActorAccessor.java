package com.tridoo.kulki;

import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;

public class ActorAccessor implements TweenAccessor<Actor> {

    public static final int RGB=1;
    public static final int ALPHA = 0;


    @Override
    public int getValues(Actor actor, int i, float[] floats) {
        switch (i){
            case ALPHA:
                floats[0]=actor.getColor().a;
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(Actor actor, int i, float[] floats) {
        switch (i){
            case ALPHA:
                actor.setColor(actor.getColor().r,actor.getColor().g,actor.getColor().b,floats[0]);
        }
    }
}
