package com.tridoo.kulki;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import aurelienribon.tweenengine.TweenManager;


public class Poziom extends InputAdapter implements Screen {

    private static int stanStop=0;
    private static int stanReady=1;
    private static int stanPlay=2;
    private static float TOLERANCJA=1f;
    private int ileZyc=5;
    private int ileZycMax=10;
    private int ileZycNaMinute=5;
    private int czasNaNoweZycie=0;

    public int poziom;
    private int stanGry;
    private boolean ustawionePrzyciski=false;
    private final float TIME_STEP = 1 / 60f;
    private final int VELOCITY_INTER = 8;
    private final int POSITION_INTER = 3;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private Body box;
    private MouseJointDef mouseJoint;
    private MouseJoint joint;
    private RevoluteJointDef revoluteJointDef;

    private SpriteBatch batch;
    private com.badlogic.gdx.utils.Array<Body> tmpBodies = new com.badlogic.gdx.utils.Array<Body>();
    private Sprite background;

    private FixtureDef fixtureMetal, fixtureDrewno, fixtureGuma,fixtureCegla, fixtureKubek;
    private FixtureDef fixtureDetektor;
    private PolygonShape shapePodloga, shapeScianaLewa, shapeScianaPrawa, shapeSufit, shapeMagazyn, shapeStart;
    //private ChainShape shapeKubek;
    private Body bodyKulka1, bodyKulka2, bodyKulka3;
    private Body bodyKubek1, bodyKubek2, bodyKubek3;
    private Body bodyPodloga, bodyScianaLewa, bodyScianaPrawa, bodySufit, bodyMagazyn, bodyStart;
    private Body bodyDetektor1;
    private BodyDef bodyDefPodloga, bodyDefScianaLewa, bodyDefScianaPrawa, bodyDefSufit, bodyDefMagazyn, bodyDefStart;
    private BodyDef bodyDefKulka1, bodyDefKulka2, bodyDefKulka3;
    //private BodyDef bodyDefKubek1,  bodyDefKubek2, bodyDefKubek3;
    private BodyDef bodyDefDetektor1;
    private Body[] bodyKulki;
    private Body bodyOs, bodyBelka;
    private BodyDef bodyDefBelka, bodyDefOs;
    private WeldJoint wJoint;


    private Stage stage;
    private Table table;
    private Skin skin, skinGUI;
    private TextureAtlas atlas, atlasGUI;
    private OrthographicCamera guicam;
    private Rectangle recBack, recPlay,recNext;
    private ImageButton imgPlay, imgBack, imgNext;
    private Label napisWygrana, napisLevel, napisZycia;
    private TweenManager tweenManager;
    CollisionListener collisionListener;

    private Vector3 tmp= new Vector3();
    private Vector2 tmp2= new Vector2();

    Timer timer;
    Timer.Task timerTask;
    InterstitialAd mInterstitialAd;

    Pozycje pozycje=new Pozycje();

    public Poziom(int aPoziom) {
        poziom=aPoziom;
        ileZyc=odczytajZycia();
        float pInterval=60/ileZycNaMinute;
        float pDelay=60/ileZycNaMinute-czasNaNoweZycie;

        timer = new Timer();
        timerTask = timer.scheduleTask(new Timer.Task() {

            @Override
            public void run () {
                if (ileZyc < ileZycMax ) ileZyc++;
            }
        }, pDelay , pInterval);
    }

    @Override
    public void show() {
        stanGry=stanStop;
        world=new World(new Vector2(0,-9.81f),true);
        debugRenderer=new Box2DDebugRenderer();
        camera=new OrthographicCamera();

        batch=new SpriteBatch();

        stage=new Stage();
        atlas=new TextureAtlas("levels.pack");
        atlasGUI=new TextureAtlas("gui.pack");
        skin = new Skin(atlas);
        skinGUI = new Skin(atlasGUI);

        table = new Table(skin);
        table.setFillParent(true);
        //table.setBounds(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(this);
        collisionListener= new CollisionListener(this);
        world.setContactListener(collisionListener);

        utrzorzMaterialy();
        utworzScene();
        utworzKubki(poziom);
        utworzKulki(poziom);
        utworzPrzyrzady(poziom);
        utworzDetektory(poziom);

        mouseJoint=new MouseJointDef();
        mouseJoint.bodyA=bodyPodloga;
        mouseJoint.collideConnected=true;
        mouseJoint.maxForce=2000;

        generujPrzyciski();

        table.add(napisLevel).colspan(2).center();
        table.add(napisZycia).left().padRight(80f);
        table.row();
        table.add();
        table.add().expandX();
        table.add(imgPlay).padTop(60f);
        table.row();
        table.add();
        table.add().expandX();
        table.add(imgBack).padTop(100f);
        table.row();
        table.add(napisWygrana).space(20).colspan(3);
        table.row();
        table.add(imgNext).expandY().top().colspan(3).padTop(30);
        //table.debug();
        stage.addActor(table);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                //beginPlayingGame();
                //TODO
            }
        });

        requestNewInterstitial();

    }

    @Override
    public void render(float delta) {
        if (stanGry == stanPlay) {
            for (Body aBodyKulki : bodyKulki) {
                aBodyKulki.setType(BodyDef.BodyType.DynamicBody);
            }
        } else {
            for (Body aBodyKulki : bodyKulki) {
                aBodyKulki.setType(BodyDef.BodyType.StaticBody);
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        world.getBodies(tmpBodies);
        for (Body body : tmpBodies) {
            if (body.getUserData()!=null && body.getUserData() instanceof Sprite) {
                Sprite sprite = (Sprite) body.getUserData();
                sprite.setPosition(body.getPosition().x-sprite.getWidth()/2, body.getPosition().y-sprite.getHeight()/2);
                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                sprite.draw(batch);
            }
        }
        batch.end();

        debugRenderer.render(world, camera.combined);

        world.step(TIME_STEP, VELOCITY_INTER, POSITION_INTER);

        stage.act(delta);
        //long pAktualnyCzas=System.currentTimeMillis();
        //System.out.println(timerTask.getExecuteTimeMillis());//todo WTF?
        napisZycia.setText(" LIVES " + String.valueOf(ileZyc));
        stage.draw();

        if (!ustawionePrzyciski) {
            //odwrocony Y
            recPlay = new Rectangle(imgPlay.getX(), Gdx.graphics.getHeight()-imgPlay.getY()-imgPlay.getHeight(), imgPlay.getWidth(), imgPlay.getHeight());
            recBack = new Rectangle(imgBack.getX(), Gdx.graphics.getHeight()-imgBack.getY()- imgBack.getHeight(), imgBack.getWidth(), imgBack.getHeight());
            recNext= new Rectangle(imgNext.getX(), Gdx.graphics.getHeight()-imgNext.getY()- imgNext.getHeight(), imgNext.getWidth(), imgNext.getHeight());

            ustawionePrzyciski = true;
            stanGry=stanReady;
        }
    }

    @Override
    public void resize(int width, int height) {
        float skala=45; //todo
        camera.viewportWidth=width/skala;
        camera.viewportHeight=height/skala;
        camera.update();
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
        world.dispose();
        debugRenderer.dispose();
        stage.dispose();
        atlas.dispose();
        skin.dispose();
    }


    private QueryCallback queryCallBack = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            if (!fixture.testPoint(tmp.x, tmp.y) && (!fixture.testPoint(tmp.x, tmp.y + TOLERANCJA)) && (!fixture.testPoint(tmp.x, tmp.y - TOLERANCJA))) //todo
                return true;

            mouseJoint.bodyB = fixture.getBody();
            mouseJoint.target.set(fixture.getBody().getPosition());
            joint = (MouseJoint) world.createJoint(mouseJoint);
            if (fixture.getUserData() != null ) {
                if (fixture.getUserData() instanceof String) {
                    fixture.getBody().setType(BodyDef.BodyType.DynamicBody);
                    Filter filter = new Filter();
                    filter.categoryBits = 0;
                    fixture.setFilterData(filter);
                    String pOpis=(String) fixture.getUserData();
                    if (pOpis.equals("R1")|| pOpis.equals("R2")){
                        WeldJointDef wJointDef = new WeldJointDef();
                        wJointDef.bodyA = bodyOs;
                        wJointDef.bodyB = bodyBelka;
                        wJoint= (WeldJoint)world.createJoint(wJointDef);
                        bodyOs.setType(BodyDef.BodyType.DynamicBody);
                        bodyOs.getFixtureList().get(0).setFilterData(filter);
                        bodyBelka.getFixtureList().get(0).setFilterData(filter);
                    }
                }
            }
            return false;
        }
    };

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (stanGry == stanReady) {
            camera.unproject(tmp.set(screenX, screenY, 0));
            world.QueryAABB(queryCallBack, tmp.x, tmp.y-TOLERANCJA, tmp.x, tmp.y+TOLERANCJA);
        }
        //GUI
        if (recBack.contains(screenX, screenY)) {
             zapiszZycia();
            ((MyGame) Gdx.app.getApplicationListener()).setScreen(new Levels());
        } else if (recPlay.contains(screenX, screenY)) {
            if (stanGry == stanReady) {
                if (ileZyc>0) {
                    stanGry = stanPlay;
                    imgPlay.setChecked(true);
                }else {}//todo reklamy

            } else {
                imgPlay.setChecked(false);
                restart();
            }
        } else if (imgNext.isVisible() && recNext.contains(screenX, screenY)) {
            zapiszZycia();
            ((MyGame)Gdx.app.getApplicationListener()).setScreen(new Poziom(poziom+1));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (joint == null) return false;
        if (joint.getBodyB().getUserData() != null) {
            joint.getBodyB().setType(BodyDef.BodyType.StaticBody);
            Filter filter = new Filter();
            filter.categoryBits = 1;
            joint.getBodyB().getFixtureList().get(0).setFilterData(filter);

            String pOpis = (String) joint.getBodyB().getFixtureList().get(0).getUserData();
            if (pOpis != null) {
                if (pOpis.equals("R1") || pOpis.equals("R2")) {

                    bodyOs.setType(BodyDef.BodyType.StaticBody);
                    bodyBelka.setType(BodyDef.BodyType.DynamicBody);
                    bodyOs.getFixtureList().get(0).setFilterData(filter);
                    bodyBelka.getFixtureList().get(0).setFilterData(filter);
                    camera.unproject(tmp.set(screenX, screenY, 0));
                    bodyOs.setTransform(tmp.x, tmp.y, 0);
                    bodyOs.setAwake(true);
                    bodyBelka.setAwake(true);
                    render(Float.MIN_VALUE);
                    world.destroyJoint(wJoint);
                    wJoint=null;
                }
            }
        }
        world.destroyJoint(joint);
        joint = null;
//        restart(); //????
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (joint==null) return false;
        camera.unproject(tmp.set(screenX,screenY,0));
        joint.setTarget(tmp2.set(tmp.x,tmp.y));
        return true;
    }

    private void utrzorzMaterialy(){
        fixtureMetal = new FixtureDef();
        fixtureMetal.friction=.1f;
        fixtureMetal.restitution=.5f;
        fixtureMetal.density=4f;

        fixtureDrewno= new FixtureDef();
        fixtureDrewno.friction=.2f;
        fixtureDrewno.restitution=0.2f;
        fixtureDrewno.density=0f;

        fixtureGuma= new FixtureDef();
        fixtureGuma.density=2.5f;
        fixtureGuma.friction=.7f;
        fixtureGuma.restitution=.7f;

        fixtureCegla= new FixtureDef();
        fixtureCegla.friction=.5f;
        fixtureCegla.restitution=0.05f;
        fixtureCegla.density=3;

        fixtureKubek=new FixtureDef();
        fixtureKubek.friction=.05f;
        fixtureKubek.restitution=.05f;
        fixtureKubek.density=3;


        fixtureDetektor=new FixtureDef();
        fixtureDetektor.isSensor=true;
    }

    private void utworzScene(){
        //podloga
        Sprite pPodlogaSprite = new Sprite(new Texture("brickH.jpg"));
        bodyDefPodloga=new BodyDef();
        bodyDefPodloga.type= BodyDef.BodyType.StaticBody;
        bodyDefPodloga.position.set(0, -11.5f);

        shapePodloga=new PolygonShape();
        shapePodloga.setAsBox(20,0.5f);
        pPodlogaSprite.setSize(20*2 ,0.5f*2);

        fixtureCegla.shape=shapePodloga;

        bodyPodloga=world.createBody(bodyDefPodloga);
        bodyPodloga.createFixture(fixtureCegla);
        bodyPodloga.setUserData(pPodlogaSprite);
        pPodlogaSprite.setPosition(bodyPodloga.getPosition().x-pPodlogaSprite.getWidth()/2, bodyPodloga.getPosition().y-pPodlogaSprite.getHeight()/2);
        shapePodloga.dispose();

        //sciana prawa
        Sprite pScianaPSprite = new Sprite(new Texture("brickV.jpg"));
        bodyDefScianaPrawa=new BodyDef();
        bodyDefScianaPrawa.type= BodyDef.BodyType.StaticBody;
        bodyDefScianaPrawa.position.set(20.5f,0);

        shapeScianaPrawa=new PolygonShape();
        shapeScianaPrawa.setAsBox(0.5f,13);
        pScianaPSprite.setSize(0.5f*2 ,13*2);

        fixtureCegla.shape=shapeScianaPrawa;

        bodyScianaPrawa=world.createBody((bodyDefScianaPrawa));
        bodyScianaPrawa.createFixture(fixtureCegla);
        bodyScianaPrawa.setUserData(pScianaPSprite);
        pScianaPSprite.setPosition(bodyScianaPrawa.getPosition().x-pScianaPSprite.getWidth()/2, bodyScianaPrawa.getPosition().y-pScianaPSprite.getHeight()/2);
        shapeScianaPrawa.dispose();

        //sciana lewa
        Sprite pScianaLSprite = new Sprite(new Texture("brickV.jpg"));
        bodyDefScianaLewa=new BodyDef();
        bodyDefScianaLewa.type= BodyDef.BodyType.StaticBody;
        bodyDefScianaLewa.position.set(-19.5f,0);

        shapeScianaLewa=new PolygonShape();
        shapeScianaLewa.setAsBox(0.5f,13);
        pScianaLSprite.setSize(0.5f*2 ,13*2);

        fixtureCegla.shape=shapeScianaLewa;

        bodyScianaLewa=world.createBody((bodyDefScianaLewa));
        bodyScianaLewa.createFixture(fixtureCegla);
        bodyScianaLewa.setUserData(pScianaLSprite);
        pScianaLSprite.setPosition(bodyScianaLewa.getPosition().x-pScianaLSprite.getWidth()/2, bodyScianaLewa.getPosition().y-pScianaLSprite.getHeight()/2);
        shapeScianaLewa.dispose();

        //sufit
        Sprite pSufitSprite = new Sprite(new Texture("brickH.jpg"));
        bodyDefSufit=new BodyDef();
        bodyDefSufit.type= BodyDef.BodyType.StaticBody;
        bodyDefSufit.position.set(0, 11.5f);

        shapeSufit=new PolygonShape();
        shapeSufit.setAsBox(20,0.5f);
        pSufitSprite.setSize(20*2,0.5f*2);

        fixtureCegla.shape=shapeSufit;

        bodySufit=world.createBody(bodyDefSufit);
        bodySufit.createFixture(fixtureCegla);
        bodySufit.setUserData(pSufitSprite);
        pSufitSprite.setPosition(bodySufit.getPosition().x-pSufitSprite.getWidth()/2, bodySufit.getPosition().y-pSufitSprite.getHeight()/2);
        shapeSufit.dispose();

        //magazyn
        Sprite pMagazynSprite = new Sprite(new Texture("magazyn.png"));
        pMagazynSprite.setSize(2.5f*2,11.5f*2);

        bodyDefMagazyn=new BodyDef();
        bodyDefMagazyn.type= BodyDef.BodyType.StaticBody;
        bodyDefMagazyn.position.set(-16,0);

        PolygonShape shapeML=new PolygonShape();
        shapeML.setAsBox(0.1f,11.5f,new Vector2(2.2f,0),0);

        fixtureDrewno.shape=shapeML;

        bodyMagazyn=world.createBody(bodyDefMagazyn);
        bodyMagazyn.createFixture(fixtureDrewno);

        PolygonShape shapeMP=new PolygonShape();
        shapeMP.setAsBox(0.1f,11.5f,new Vector2(-2.2f,0),0);

        fixtureDrewno.shape=shapeMP;
        bodyMagazyn.createFixture(fixtureDrewno);

        PolygonShape shapeP1=new PolygonShape();
        shapeP1.setAsBox(2.2f,0.1f,new Vector2(0,0),0);

        fixtureDrewno.shape=shapeP1;
        bodyMagazyn.createFixture(fixtureDrewno);

        PolygonShape shapeP2=new PolygonShape();
        shapeP2.setAsBox(2.2f,0.1f,new Vector2(0,4),0);

        fixtureDrewno.shape=shapeP2;
        bodyMagazyn.createFixture(fixtureDrewno);

        PolygonShape shapeP3=new PolygonShape();
        shapeP3.setAsBox(2.2f,0.1f,new Vector2(0,-4),0);

        fixtureDrewno.shape=shapeP3;
        bodyMagazyn.createFixture(fixtureDrewno);

        PolygonShape shapeP4=new PolygonShape();
        shapeP4.setAsBox(2.2f,0.1f,new Vector2(0,8),0);

        fixtureDrewno.shape=shapeP4;
        bodyMagazyn.createFixture(fixtureDrewno);

        PolygonShape shapeP5=new PolygonShape();
        shapeP5.setAsBox(2.2f,0.1f,new Vector2(0,-8),0);

        fixtureDrewno.shape=shapeP5;
        bodyMagazyn.createFixture(fixtureDrewno);

        bodyMagazyn.setUserData(pMagazynSprite);
        pMagazynSprite.setPosition(bodyMagazyn.getPosition().x-pSufitSprite.getWidth()/2, bodyMagazyn.getPosition().y-pSufitSprite.getHeight()/2);

        shapeML.dispose();
        shapeMP.dispose();
        shapeP1.dispose();
        shapeP2.dispose();
        shapeP3.dispose();
        shapeP4.dispose();
        shapeP5.dispose();

        //start
        ArrayList pListaPozycji;
        pListaPozycji= pozycje.kulki.get(poziom);
        Sprite pStartSprite=new Sprite(new Texture("arrow.png"));

        bodyDefStart=new BodyDef();
        bodyDefStart.type= BodyDef.BodyType.StaticBody;
        bodyDefStart.position.set(-10.5f, 8.5f);

        shapeStart=new PolygonShape();
        shapeStart.setAsBox(3.2f,0.25f,new Vector2(0,0),-0.2f);
        pStartSprite.setSize(3.2f * 2, 1.1f * 2);

        fixtureDrewno.shape=shapeStart;

        bodyStart=world.createBody(bodyDefStart);
        bodyStart.createFixture(fixtureDrewno);
        bodyStart.setUserData(pStartSprite);
        pStartSprite.setPosition(bodyStart.getPosition().x-pStartSprite.getWidth()/2,bodyStart.getPosition().y-pStartSprite.getHeight()/2);

        if (pListaPozycji.size()>1){
            Sprite pStartSprite2=new Sprite(new Texture("arrow.png"));
            pStartSprite2.setSize(3.2f * 2, 1.1f * 2);
            bodyDefStart.position.set(-10.5f, 6.5f);
            Body bodyStart2=world.createBody(bodyDefStart);
            bodyStart2.createFixture(fixtureDrewno);
            bodyStart2.setUserData(pStartSprite2);
            pStartSprite2.setPosition(bodyStart2.getPosition().x-pStartSprite2.getWidth()/2,bodyStart2.getPosition().y-pStartSprite2.getHeight()/2);
        }

        shapeStart.dispose();
    }

    private void utworzKulki(int aPoziom){
        ArrayList pListaPozycji;
        pListaPozycji= pozycje.kulki.get(poziom);
        CircleShape ballShape=new CircleShape();
        ballShape.setRadius(0.5f);

        FixtureDef[] fixtureKulki = new FixtureDef[3];
        fixtureKulki[0]=fixtureGuma;
        fixtureKulki[1]=fixtureGuma;
        //fixtureKulki[1]=fixtureMetal;
        fixtureKulki[2]=fixtureDrewno;

        Sprite[] pKulkaSprite= new Sprite[3];
        pKulkaSprite[0]=new Sprite(new Texture("ballGreen.png"));
        pKulkaSprite[1]=new Sprite(new Texture("ballBlue.png"));
        pKulkaSprite[2]=new Sprite(new Texture("ballRed.png"));

        BodyDef[] bodyDefKulki = new BodyDef[pListaPozycji.size()];
        bodyKulki=new Body [pListaPozycji.size()];


        int i=0;
        for (Object pozycja : pListaPozycji) {

            bodyDefKulki[i]=new BodyDef();
            bodyDefKulki[i].type=BodyDef.BodyType.DynamicBody;
            fixtureKulki[i].shape=ballShape;
            bodyDefKulki[i].position.set((Vector2)pozycja);
            bodyKulki[i]=world.createBody(bodyDefKulki[i]);
            //Sprite pKulkaSprite0 = new Sprite(new Texture("ballGreen.png"));
            pKulkaSprite[i].setSize(0.5f * 2, 0.5f * 2);
            pKulkaSprite[i].setOrigin(pKulkaSprite[i].getWidth()/2,pKulkaSprite[i].getHeight()/2);
            bodyKulki[i].setUserData(pKulkaSprite[i]);
            Fixture fix= bodyKulki[i].createFixture(fixtureKulki[i]);
            fix.setUserData(i);
            i++;
        }
        ballShape.dispose();
    }

    private void utworzPrzyrzady(int aPoziom){
        Fixture fix;
        String pOpis="przyrzad";

        for (int i=0; i<PrzyrzadyIlosc.przyrzad1[poziom-1];i++){
            Sprite pDeskaSprite = new Sprite(new Texture("ladderH.png"));
            BodyDef boxDeskaPozioma= new BodyDef();

            boxDeskaPozioma.type= BodyDef.BodyType.StaticBody;

            boxDeskaPozioma.position.set(Pozycje.PRZYRZAD1);

            PolygonShape boxShape=new PolygonShape();
            boxShape.setAsBox(1.75f,0.5f);
            pDeskaSprite.setSize(1.75f*2,0.5f*2);

            fixtureDrewno.shape=boxShape;

            box=world.createBody(boxDeskaPozioma);
            box.setGravityScale(0);
            box.setUserData(pDeskaSprite);
            fix= box.createFixture(fixtureDrewno);
            fix.setUserData(pOpis);
            boxShape.dispose();
        }

        for (int i=0; i<PrzyrzadyIlosc.przyrzad2[poziom-1];i++) {
            Sprite pDeskaSprite = new Sprite(new Texture("ladderV.png"));
            BodyDef boxDeskaPionowa = new BodyDef();
            boxDeskaPionowa.type = BodyDef.BodyType.StaticBody;

            boxDeskaPionowa.position.set(Pozycje.PRZYRZAD2);

            PolygonShape boxShape2 = new PolygonShape();
            boxShape2.setAsBox(.5f, 1.75f);
            pDeskaSprite.setSize(0.5f*2,1.75f*2);

            fixtureDrewno.shape = boxShape2;

            box = world.createBody(boxDeskaPionowa);
            box.setGravityScale(0);
            box.setUserData(pOpis);
            box.setUserData(pDeskaSprite);
            fix=box.createFixture(fixtureDrewno);
            fix.setUserData(pOpis);
            boxShape2.dispose();
        }

        for (int i=0; i<PrzyrzadyIlosc.przyrzad3[poziom-1];i++) {
            Sprite pDeskaSprite = new Sprite(new Texture("ladderP.png"));
            BodyDef deskaSkosP = new BodyDef();
            deskaSkosP.type = BodyDef.BodyType.StaticBody;

            deskaSkosP.position.set(Pozycje.PRZYRZAD3);

            Vector2[] vertices = new Vector2[4];
            vertices[0] = new Vector2(-1.8f, 1.5f);
            vertices[1] = new Vector2(-1.8f, 0.5f);
            vertices[2] = new Vector2(1.8f, -1.5f);
            vertices[3] = new Vector2(1.8f, -0.5f);
            PolygonShape boxShape3 = new PolygonShape();
            boxShape3.set(vertices);
            pDeskaSprite.setSize(1.75f*2,1.5f*2);

            fixtureDrewno.shape = boxShape3;

            box = world.createBody(deskaSkosP);
            box.setGravityScale(0);
            box.setUserData(pDeskaSprite);
            fix=box.createFixture(fixtureDrewno);
            fix.setUserData(pOpis);
            boxShape3.dispose();
        }

        for (int i=0; i<PrzyrzadyIlosc.przyrzad4[poziom-1];i++) {
            Sprite pDeskaSprite = new Sprite(new Texture("ladderL.png"));
            BodyDef deskaSkosL = new BodyDef();
            deskaSkosL.type = BodyDef.BodyType.StaticBody;
            deskaSkosL.position.set(Pozycje.PRZYRZAD4);

            Vector2[] vertices2 = new Vector2[4];
            vertices2[0] = new Vector2(-1.8f, -1.5f);
            vertices2[1] = new Vector2(-1.8f, -0.5f);
            vertices2[2] = new Vector2(1.8f, 1.5f);
            vertices2[3] = new Vector2(1.8f, 0.5f);
            PolygonShape boxShape4 = new PolygonShape();
            boxShape4.set(vertices2);
            pDeskaSprite.setSize(1.75f*2,1.5f*2);

            fixtureDrewno.shape = boxShape4;

            box = world.createBody(deskaSkosL);
            box.setGravityScale(0);
            box.setUserData(pDeskaSprite);
            fix=box.createFixture(fixtureDrewno);
            fix.setUserData(pOpis);
            boxShape4.dispose();
        }

        // rownowaznia
        for (int i=0; i<PrzyrzadyIlosc.przyrzad5[poziom-1];i++) {
            //os obrotu
            Sprite pOsSprite = new Sprite(new Texture("triangle.png"));
            Vector2[] vertices2 = new Vector2[3];
            vertices2[0] = new Vector2(0, 0.6f);
            vertices2[1] = new Vector2(-1.3f, -0.6f);
            vertices2[2] = new Vector2(1.3f, -0.6f);
            PolygonShape shapeOs = new PolygonShape();
            shapeOs.set(vertices2);
            pOsSprite.setSize(2.6f,1.2f);
            pOsSprite.setOriginCenter();

            bodyDefOs = new BodyDef();
            bodyDefOs.type = BodyDef.BodyType.StaticBody;
            bodyDefOs.position.set(Pozycje.PRZYRZAD5);

            fixtureDrewno.shape = shapeOs;

            bodyOs = world.createBody(bodyDefOs);
            bodyOs.setUserData(pOsSprite);
            fix=bodyOs.createFixture(fixtureDrewno);
            fix.setUserData("R1");
            shapeOs.dispose();

            //belka
            Sprite pBelkaSprite = new Sprite(new Texture("wood.jpg"));
            bodyDefBelka = new BodyDef();
            bodyDefBelka.position.set(Pozycje.PRZYRZAD5);
            bodyDefBelka.type = BodyDef.BodyType.DynamicBody;

            PolygonShape shapeBelka = new PolygonShape();
            shapeBelka.setAsBox(2.1f, 0.25f);
            pBelkaSprite.setSize(2*2.1f, 2*0.25f);
            pBelkaSprite.setOriginCenter();
            fixtureCegla.shape = shapeBelka; //todo chwilowo ze wzgl na ciezar
            bodyBelka = world.createBody(bodyDefBelka);
            bodyBelka.setGravityScale(0);
            bodyBelka.setUserData(pBelkaSprite);
            fix=bodyBelka.createFixture(fixtureCegla);
            fix.setUserData("R2");

            revoluteJointDef = new RevoluteJointDef();
            revoluteJointDef.bodyA = bodyOs;
            revoluteJointDef.bodyB = bodyBelka;
            revoluteJointDef.localAnchorA.set(0, 0.5f);
            revoluteJointDef.localAnchorB.set(0, 0);
            revoluteJointDef.enableLimit = true;
            revoluteJointDef.lowerAngle = -30 * MathUtils.degreesToRadians;
            revoluteJointDef.upperAngle = 30 * MathUtils.degreesToRadians;
            revoluteJointDef.collideConnected = false;
            world.createJoint(revoluteJointDef);
        }

        //trampolina
        for (int i=0; i<PrzyrzadyIlosc.przyrzad6[poziom-1];i++) {
            Sprite pTrampolinaSprite = new Sprite(new Texture("trampolina.png"));
            BodyDef bodyDefTrampolina = new BodyDef();
            bodyDefTrampolina.position.set(Pozycje.PRZYRZAD6);
            bodyDefTrampolina.type = BodyDef.BodyType.StaticBody;

            PolygonShape shapeTrampolina = new PolygonShape();
            shapeTrampolina.setAsBox(2, 1f);
            pTrampolinaSprite.setSize(2 * 2, 2 * 1f);

            fixtureDrewno.shape = shapeTrampolina;
            Body bodyTrampolina = world.createBody(bodyDefTrampolina);
            bodyTrampolina.setGravityScale(0);
            bodyTrampolina.setUserData(pTrampolinaSprite);
            fix = bodyTrampolina.createFixture(fixtureDrewno);
            fix.setUserData("T");
            shapeTrampolina.dispose();
        }
    }

    private void utworzKubki(int aPoziom){
        BodyDef bodyDefKubek=new BodyDef();
        bodyDefKubek.type= BodyDef.BodyType.StaticBody;
        Body pKubek;
        Sprite[] pKubekSprite= new Sprite[3];
        pKubekSprite[0]=new Sprite(new Texture("kubekGreen.png"));
        pKubekSprite[1]=new Sprite(new Texture("kubekBlue.png"));
        pKubekSprite[2]=new Sprite(new Texture("kubekRed.png"));

        Vector2[] vertices;
        vertices = new Vector2[11];
        vertices[0] = new Vector2(-0.5f, 0.3f);
        vertices[1] = new Vector2(-1.3f, 0.6f);
        vertices[2] = new Vector2(-1.5f, 3.3f);
        vertices[3] = new Vector2(-1.7f, 3.3f);
        vertices[4] = new Vector2(-1.7f, 0f);
        vertices[5] = new Vector2( 1.7f, 0f);
        vertices[6] = new Vector2( 1.7f, 3.3f);
        vertices[7] = new Vector2( 1.5f, 3.3f);
        vertices[8] = new Vector2( 1.3f, 0.6f);
        vertices[9] = new Vector2( 0.5f, 0.3f);
        vertices[10] = new Vector2(-0.5f, 0.3f);

        for (Vector2 vec: vertices){
            vec.add(0,-1.65f);
        }

        ChainShape shapeKubek= new ChainShape();
        shapeKubek.createChain(vertices);

        fixtureKubek.shape=shapeKubek;

        ArrayList pListaPozycji;
        pListaPozycji= pozycje.kubki.get(poziom);

        int i=0;
        for (Object pozycja : pListaPozycji) {
            pKubekSprite[i].setSize(3.4f,3.3f);
            bodyDefKubek.position.set((Vector2)pozycja);
            pKubek=world.createBody(bodyDefKubek);
            pKubek.createFixture(fixtureKubek);
            pKubek.setUserData(pKubekSprite[i]);
            i++;
        }

        shapeKubek.dispose();
    }

    private void utworzDetektory(int aPoziom){
        ArrayList pListaPozycji;
        pListaPozycji= pozycje.kubki.get(poziom);
        CircleShape ballShape=new CircleShape();
        ballShape.setRadius(1.25f);

        fixtureDetektor.shape=ballShape;

        BodyDef[] bodyDefDetektor=new BodyDef[pListaPozycji.size()];
        Body[] bodyDetektor=new Body[pListaPozycji.size()];

        int i=0;
        for (Object pozycja : pListaPozycji) {
            bodyDefDetektor[i]=new BodyDef();
            bodyDefDetektor[i].type=BodyDef.BodyType.StaticBody;
            bodyDefDetektor[i].position.set(((Vector2)pozycja).add(0,-0.4f));

            bodyDetektor[i]=world.createBody(bodyDefDetektor[i]);
            Fixture fix=bodyDetektor[i].createFixture(fixtureDetektor);
            fix.setUserData(i);
            i++;
        }

        ballShape.dispose();
    }

    private void restart() {
        for (Body aBodyKulki : bodyKulki) {
            world.destroyBody(aBodyKulki);
        }
        utworzKulki(poziom);
        if (revoluteJointDef != null) { //rownowaznia
            revoluteJointDef.bodyB.setTransform(revoluteJointDef.bodyB.getPosition(), 0);
            revoluteJointDef.bodyB.setAwake(true);
        }
        napisWygrana.setVisible(false);
        imgNext.setVisible(false);
        collisionListener.pkt = 0;
        ileZyc--;
        stanGry = stanReady;
    }

    public void wygrana(){
        stanGry=stanStop;
        napisWygrana.setVisible(true);
        imgNext.setVisible(true);
        savePoziom(poziom);
    }

    private void generujPrzyciski(){
        imgPlay=new ImageButton(skinGUI.getDrawable("play"),skinGUI.getDrawable("play"),skinGUI.getDrawable("restart"));
        imgBack=new ImageButton(skinGUI.getDrawable("back"),skinGUI.getDrawable("back"));

        BitmapFont font=new BitmapFont(Gdx.files.internal("font/white.fnt"),false);
        Label.LabelStyle headingStyle=new Label.LabelStyle(font, Color.WHITE);
        napisWygrana=new Label("LEVEL COMPLETE",headingStyle);
        napisWygrana.setFontScale(1.5f);
        napisWygrana.setVisible(false);
        napisLevel=new Label("LEVEL " + String.valueOf(poziom),headingStyle);
        napisZycia=new Label("LIVES " + String.valueOf(ileZyc),headingStyle);

        imgNext=new ImageButton(skinGUI.getDrawable("next"),skinGUI.getDrawable("next"));
        imgNext.setVisible(false);
    }

    public void savePoziom(int aPoziom){
        int dostepnyPoziom= Gdx.app.getPreferences("kulki").getInteger("level");
        if (dostepnyPoziom<aPoziom+1) {
            Preferences prefs = Gdx.app.getPreferences("kulki");
            prefs.putInteger("level", aPoziom+1);
            prefs.flush();
        }
    }

    private void zapiszZycia(){
        Preferences prefs = Gdx.app.getPreferences("kulki");
        prefs.putInteger("ileZyc", ileZyc);
        prefs.putLong("czasWyjscia", new Date().getTime());
        prefs.flush();
    }

    private int odczytajZycia() {
        int pIleZyc = Gdx.app.getPreferences("kulki").getInteger("ileZyc");
        long pCzasWyjscia = Gdx.app.getPreferences("kulki").getLong("czasWyjscia");
        long pCzasAktualny = new Date().getTime();
        int pNoweZycia = (int) ((pCzasAktualny - pCzasWyjscia) * 0.001f *ileZycNaMinute / 60);

        if (pCzasWyjscia > 0) {
            pIleZyc = pIleZyc + pNoweZycia;
            if (pIleZyc > ileZycMax) pIleZyc = ileZycMax;
            else
                czasNaNoweZycie = (int) ((pCzasAktualny - pCzasWyjscia) * 0.001f * ileZycNaMinute) % 60;
            return pIleZyc;
        } else return ileZyc;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

}
