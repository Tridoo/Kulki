package com.tridoo.kulki;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class Splash implements Screen {

    private SpriteBatch batch;
    private Sprite splash;
    private TweenManager tweenManager;


    @Override
    public void show() {
        batch = new SpriteBatch();
        tweenManager=new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Texture splashTexture = new Texture("img/splash.png");
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Tween.set(splash,SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(splash,SpriteAccessor.ALPHA,2).target(1).start(tweenManager);
        Tween.to(splash,SpriteAccessor.ALPHA,1).target(0).delay(1).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                ((MyGame)(Gdx.app.getApplicationListener())).setScreen(new MainMenu());
            }
        }) .start(tweenManager);


        tweenManager.update(Float.MIN_VALUE);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        splash.draw(batch);
        batch.end();
        tweenManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        splash.setSize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        splash.getTexture().dispose();

    }
}
