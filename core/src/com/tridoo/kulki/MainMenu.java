package com.tridoo.kulki;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import javafx.animation.Timeline;

public class MainMenu implements Screen {

    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    private TextButton buttonPlay, buttonExit;
    private BitmapFont  white, black;
    private Label heading;
    private TweenManager tweenManager;

    @Override
    public void show() {
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        atlas=new TextureAtlas("button.pack");
        skin = new Skin(atlas);

        table = new Table(skin);
        table.setFillParent(true);


        white= new BitmapFont(Gdx.files.internal("font/white.fnt"),false);
        black= new BitmapFont(Gdx.files.internal("font/black.fnt"),false);

        TextButton.TextButtonStyle textButtonStyle=new TextButton.TextButtonStyle();
        textButtonStyle.up=skin.getDrawable("button.up");
        textButtonStyle.down=skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX=1;
        textButtonStyle.pressedOffsetY=-1;
        textButtonStyle.font=black;



        buttonExit=new TextButton("EXIT",textButtonStyle);
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        buttonExit.pad(20);

        buttonPlay=new TextButton("PLAY",textButtonStyle);
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((MyGame)Gdx.app.getApplicationListener()).setScreen(new Levels());
            }
        });
        buttonPlay.pad(20);

        Label.LabelStyle headingStyle=new Label.LabelStyle(white, Color.WHITE);

        heading=new Label("KULKI",headingStyle);
        heading.setFontScale(1.5f);

        table.add(heading);
        table.getCell(heading).spaceBottom(50);
        table.row();
        table.add(buttonPlay);
        table.getCell(buttonPlay).spaceBottom(20);
        table.row();
        table.add(buttonExit);
        //table.debug();
        stage.addActor(table);

        tweenManager=new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());


        aurelienribon.tweenengine.Timeline.createSequence().beginSequence()
                .push(Tween.set(buttonPlay,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(buttonExit,ActorAccessor.ALPHA).target(0))
                .push(Tween.from(heading,ActorAccessor.ALPHA,.2f).target(0))
                .push(Tween.to(buttonPlay,ActorAccessor.ALPHA,.2f).target(1))
                .push(Tween.to(buttonExit,ActorAccessor.ALPHA,.2f).target(1))
                .end().start(tweenManager);

        tweenManager.update(Float.MIN_VALUE);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
        tweenManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        table.invalidate();
        table.setSize(width,height);
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
        stage.dispose();
        atlas.dispose();
        skin.dispose();
        white.dispose();
        black.dispose();

    }
}
