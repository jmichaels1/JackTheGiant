package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.escoladeltreball.dam2.GameMain;

import clouds.CloudsController;
import helpers.GameInfo;
import helpers.GameManager;
import huds.UIHud;
import player.Player;


/**
 * Created by iam4208017 on 3/12/18.
 */
public class Gameplay implements Screen, ContactListener{

    private GameMain game;
    private Sprite[] bgs;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera box2Dcamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;

    private float lastYPosition;

    private UIHud hud;

    private CloudsController cloudsController;

    private Player player;

    private Sound coinSound, lifeSound;

    private float cameraSpeed = 10;
    private float maxSpeed = 10;
    private float acceleration = 10;

    /**
     *
     * Method Constructor
     * @param game
     */
    public Gameplay(GameMain game){
        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT,
                mainCamera);

        box2Dcamera = new OrthographicCamera();
        box2Dcamera.setToOrtho(false, GameInfo.WIDTH/GameInfo.PPH,
                GameInfo.HEIGHT/GameInfo.PPH);

        box2Dcamera.position.set(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f, 0);

        debugRenderer = new Box2DDebugRenderer();

        hud = new UIHud(game);

        world = new World(new Vector2(0, -9.8f), true);

        cloudsController = new CloudsController(world);

        player = cloudsController.positionThePlayer(player);

        createBackGrounds();

        setCameraSpeed();


        coinSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Coin Sound.wav"));
        lifeSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Life Sound.wav"));

    }

    /**
     *
     */
    private void setCameraSpeed() {
        if(GameManager.getInstance().gameData.isEasyDifficulty()){
            cameraSpeed = 80;
            maxSpeed = 100;
        }

        if(GameManager.getInstance().gameData.isMediumDifficulty()){
            cameraSpeed = 100;
            maxSpeed = 120;
        }

        if(GameManager.getInstance().gameData.isHardDifficulty()){
            cameraSpeed = 120;
            maxSpeed = 140;
        }
    }

    void habldeInput(float dt){

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
//            player.setWalking(true);
            player.movePlayer(-2);
        }else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
//            player.setWalking(true);
            player.movePlayer(2);
        } else {
            player.setWalking(false);
        }

    }

    /**
     *
     * @param dt
     */
    void update(float dt){

        habldeInput(dt);
//        moveCamera();
        checkBackgroundsOutofBounds();
        cloudsController.setCameraY(mainCamera.position.y);
        cloudsController.createAndArrangeNewClouds();

    }

    /**
     *
     */
    private void moveCamera() {
        mainCamera.position.y -= 1.5;
    }

    /**
     * createBackGrounds()
     */
    void createBackGrounds(){

        bgs = new Sprite[3];

        for (int i = 0; i < bgs.length; i++) {
            bgs[i] = new Sprite(new Texture("Backgrounds/Game BG.png"));
            bgs[i].setPosition(0, -(i * bgs[i].getHeight()));
            lastYPosition = Math.abs(bgs[i].getY());
        }

    }

    /**
     * drawBackGround()
     */
    void drawBackGround(){
        for (int i = 0; i < bgs.length; i++) {
            game.getBatch().draw(bgs[i], bgs[i].getX(), bgs[i].getY());

        }
    }

    void checkBackgroundsOutofBounds(){
        for (int i=0; i < bgs.length; i++){
            if ((bgs[i].getY() - bgs[i].getHeight() / 2f - 5) > mainCamera.position.y){
                float newPosition = bgs[i].getHeight() + lastYPosition;
                bgs[i].setPosition(0, -newPosition);
                lastYPosition = Math.abs(newPosition);
            }
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();

        drawBackGround();

        cloudsController.drawClouds(game.getBatch());
        cloudsController.drawCollectables(game.getBatch());

        player.drawPlayerIdle(game.getBatch());
        player.drawPlayerAnimation(game.getBatch());

        game.getBatch().end();

        debugRenderer.render(world, box2Dcamera.combined);

        game.getBatch().setProjectionMatrix(mainCamera.combined);
        mainCamera.update();

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();

        player.updatePlayer();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void beginContact(Contact contact) {
        Fixture body1, body2;

        if(contact.getFixtureA().getUserData() == "Player"){
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        }else {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }

        if (body1.getUserData() == "Player" && body2.getUserData() == "Coin"){
            hud.incrementCoins();
            coinSound.play();
            body2.setUserData("Remove");
            cloudsController.removeCollectables();
            //collided with coin
        }

        if (body1.getUserData() == "Player" && body2.getUserData() == "Life"){
            hud.incrementLifes();
            lifeSound.play();
            body2.setUserData("Remove");
            cloudsController.removeCollectables();
            //collided with life
        }

        if (body1.getUserData() == "Player" && body2.getUserData() == "Dark Cloud"){
            if(!player.isDead()){
                playerDied();
            }
        }


    }

    private void playerDied() {

        GameManager.getInstance().isPaused = true;

        //decrement life
        hud.decrementLife();
        player.setDead(true);
        player.setPosition(1000,1000);

        if(GameManager.getInstance().lifeScore < 0){
            //player has no more lifes left

            //check if we have a new higschore
            GameManager.getInstance().checkForNewHighscores();
            //show the end score to the user
            hud.createGameOverPanel();
            //loadMainMenu

            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new MainMenu(game));
                }
            });

            SequenceAction sa = new SequenceAction();
            sa.addAction(Actions.delay(3f));
            sa.addAction(Actions.fadeOut(1f));
            sa.addAction(run);

            hud.getStage().addAction(sa);


        }else {
            //reload the game

            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new Gameplay(game));
                }
            });

            SequenceAction sa = new SequenceAction();
            sa.addAction(Actions.delay(3f));
            sa.addAction(Actions.fadeOut(1f));
            sa.addAction(run);

            hud.getStage().addAction(sa);
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
