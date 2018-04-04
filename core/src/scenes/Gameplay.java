package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.escoladeltreball.dam2.GameMain;


/**
 * Created by iam4208017 on 3/12/18.
 */
public class Gameplay implements Screen {

    private GameMain game;
    private Sprite bg1, bg2, bg3;
    private Sprite[] bgs;

    /**
     * Method Constructor
     * @param game
     */
    public Gameplay(GameMain game){
        this.game = game;
        createBackGrounds();
    }

    /**
     * createBackGrounds()
     */
    void createBackGrounds(){

        bgs = new Sprite[3];

        for (int i = 0; i < bgs.length; i++) {
            bgs[i] = new Sprite(new Texture("Backgrounds/Game BG.png"));
            bgs[i].setPosition(0, -(i * bgs[i].getHeight()));
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


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();
//        game.getBatch().draw(bg1, bg1.getX(), bg1.getY());
//        game.getBatch().draw(bg2, bg2.getX(), bg2.getY());

        drawBackGround();

        game.getBatch().end();
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
