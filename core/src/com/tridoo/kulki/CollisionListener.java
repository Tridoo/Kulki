package com.tridoo.kulki;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener {
    Poziom poziom;
    int ileKulek;
    int pkt=0;

    public CollisionListener(Poziom aPoziom){
        poziom=aPoziom;
        ileKulek=poziom.pozycje.kulki.get(poziom.poziom).size();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Object pUserDataA = fixtureA.getUserData();
        Object pUserDataB = fixtureB.getUserData();

        if (pUserDataA != null && pUserDataB != null) {
            if (pUserDataA instanceof Integer && pUserDataB instanceof Integer) {
                if (((Integer) pUserDataB).intValue() == ((Integer) pUserDataA).intValue()) {
                    pkt++;
                    if (pkt == ileKulek) poziom.wygrana();
                }
            }

        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Object pUserDataA = fixtureA.getUserData();
        Object pUserDataB = fixtureB.getUserData();

        if (pUserDataA != null && pUserDataB != null) {
            if (pUserDataA instanceof Integer && pUserDataB instanceof Integer) {
                if (((Integer) pUserDataB).intValue() == ((Integer) pUserDataA).intValue()) {
                    pkt--;
                }
            }
            if (pUserDataA instanceof String){
                fixtureA.getBody().setLinearVelocity(fixtureA.getBody().getLinearVelocity().x*5,fixtureA.getBody().getLinearVelocity().y);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Object pUserDataA = fixtureA.getUserData();

        if (pUserDataA instanceof String && pUserDataA.equals("T")){
            fixtureB.getBody().setLinearVelocity(fixtureB.getBody().getLinearVelocity().x,fixtureB.getBody().getLinearVelocity().y*1.8f);
        }
    }

}
