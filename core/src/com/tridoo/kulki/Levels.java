package com.tridoo.kulki;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;


public class Levels implements Screen {

    private Stage stage;
    private Table table;
    private TextureAtlas atlas;
    private Skin skin;
    private Label heading;
    private BitmapFont font;
    private TextButton button01,button02,button03,button04, button05,button06,button07,button08, buttonBack;
    private TweenManager tweenManager;


    @Override
    public void show() {
        stage=new Stage();

        Gdx.input.setInputProcessor(stage);

        int dostepnyPoziom= Gdx.app.getPreferences("kulki").getInteger("level");

        atlas=new TextureAtlas("levels.pack");
        skin = new Skin(atlas);

        table = new Table(skin);
        table.setBounds(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font=new BitmapFont(Gdx.files.internal("font/white.fnt"),false);

        TextButton.TextButtonStyle textButtonStyle=new TextButton.TextButtonStyle();
        textButtonStyle.up=skin.getDrawable("lv.up");
        textButtonStyle.down=skin.getDrawable("lv.down");
        textButtonStyle.disabled=skin.getDrawable("lv.off");
        textButtonStyle.pressedOffsetX=1;
        textButtonStyle.pressedOffsetY=-1;
        textButtonStyle.font=font;

        button01=new TextButton("1",textButtonStyle);
        button01.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((MyGame)Gdx.app.getApplicationListener()).setScreen(new Poziom(1));
            }
        });

        button02=new TextButton("2",textButtonStyle);
        button02.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((MyGame)Gdx.app.getApplicationListener()).setScreen(new Poziom(2));
            }
        });

        button03=new TextButton("3",textButtonStyle);
        button03.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((MyGame)Gdx.app.getApplicationListener()).setScreen(new Poziom(3));
            }
        });

        button04=new TextButton("4",textButtonStyle);
        button04.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((MyGame)Gdx.app.getApplicationListener()).setScreen(new Poziom(4));
            }
        });

        button05=new TextButton("5",textButtonStyle);
        button05.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((MyGame)Gdx.app.getApplicationListener()).setScreen(new Poziom(5));
            }
        });

        button06=new TextButton("6",textButtonStyle);
        button06.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((MyGame)Gdx.app.getApplicationListener()).setScreen(new Poziom(6));
            }
        });

        button07=new TextButton("7",textButtonStyle);
        button07.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((MyGame)Gdx.app.getApplicationListener()).setScreen(new Poziom(7));
            }
        });

        button08=new TextButton("8",textButtonStyle);
        button08.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((MyGame)Gdx.app.getApplicationListener()).setScreen(new Poziom(8));
            }
        });

        buttonBack=new TextButton("<<",textButtonStyle);
        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((MyGame)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            }
        });

        ustawDostepnePoziomy(dostepnyPoziom);

        Label.LabelStyle headingStyle=new Label.LabelStyle(font, Color.WHITE);

        heading=new Label("LEVELS",headingStyle);
        heading.setFontScale(1.5f);

        table.add(heading).colspan(3).center();
        table.getCell(heading).space(10);
        table.row();
        table.add(button01).space(10);
        table.add(button02).space(10);
        table.add(button03).space(10);
        table.row();
        table.add(button04).space(10);
        table.add(button05).space(10);
        table.add(button06).space(10);
        table.row();
        table.add(button07).space(10);
        table.add(button08).space(10);
        table.add(buttonBack).space(10);
        //table.debug();
        stage.addActor(table);

        tweenManager=new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        aurelienribon.tweenengine.Timeline.createSequence().beginSequence()
                .push(Tween.set(button01,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(button02,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(button03,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(button04,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(button05,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(button06,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(button07,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(button08,ActorAccessor.ALPHA).target(0))
                .push(Tween.set(buttonBack,ActorAccessor.ALPHA).target(0))

                .push(Tween.from(heading,ActorAccessor.ALPHA,.06f).target(0))
                .push(Tween.to(button01,ActorAccessor.ALPHA,.06f).target(1))
                .push(Tween.to(button02,ActorAccessor.ALPHA,.06f).target(1))
                .push(Tween.to(button03,ActorAccessor.ALPHA,.06f).target(1))
                .push(Tween.to(button04,ActorAccessor.ALPHA,.06f).target(1))
                .push(Tween.to(button05,ActorAccessor.ALPHA,.06f).target(1))
                .push(Tween.to(button06,ActorAccessor.ALPHA,.06f).target(1))
                .push(Tween.to(button07,ActorAccessor.ALPHA,.06f).target(1))
                .push(Tween.to(button08,ActorAccessor.ALPHA,.06f).target(1))
                .push(Tween.to(buttonBack,ActorAccessor.ALPHA,.06f).target(1))
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
        font.dispose();
    }

    private void ustawDostepnePoziomy(int aPoziom){
        switch (aPoziom) {
            case 0:
            case 1:
                button02.setDisabled(true);
            case 2:
                button03.setDisabled(true);
            case 3:
                button04.setDisabled(true);
            case 4:
                button05.setDisabled(true);
            case 5:
                button06.setDisabled(true);
            case 6:
                button07.setDisabled(true);
            case 7:
                button08.setDisabled(true);
            break;
        }
    }
}
