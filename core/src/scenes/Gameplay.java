package scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.escoladeltreball.dam2.GameMain;

import clouds.Cloud;
import clouds.CloudsController;
import helpers.GameInfo;
import player.Player;


/**
 * Created by iam4208017 on 3/12/18.
 */
public class Gameplay implements Screen {

    private GameMain game;
    private Sprite[] bgs;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera box2Dcamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;

    private float lastYPosition;

    private CloudsController cloudsController;

    private Player player;


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

        world = new World(new Vector2(0, -9.8f), true);

        cloudsController = new CloudsController(world);

        player = cloudsController.positionThePlayer(player);

        createBackGrounds();
    }

    void habldeInput(float dt){

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            player.movePlayer(-2);
        }else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.movePlayer(2);
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

        player.drawPlayer(game.getBatch());

        game.getBatch().end();

        debugRenderer.render(world, box2Dcamera.combined);

        game.getBatch().setProjectionMatrix(mainCamera.combined);
        mainCamera.update();

        player.updatePlayer();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

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

    }

    @Override
    public void dispose() {

    }
}
