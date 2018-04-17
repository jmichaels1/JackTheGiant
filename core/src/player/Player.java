package player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

/**
 * Crreated by J Michael
 */
public class Player extends Sprite {

    private World world;
    private Body body;

    private TextureAtlas playerAtlas;
    private Animation animation;
    private float elapsedTime;

    private boolean isWalking;


    /**
     * Método Constructor
     * @param world
     */
    public Player(World world, float x, float y) {
        super(new Texture("player/Player 1.png"));
        this.world = world;
        setPosition(x,y);
        createBody();
        playerAtlas = new TextureAtlas("Player Animation/Player Animation.atlas");
    }

    /**
     * Create body
     */
    private void createBody() {

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / GameInfo.PPH, getY() / GameInfo.PPH);

        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2f - 20) / GameInfo.PPH,
                (getHeight() / 2f) / GameInfo.PPH);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 4f;
        fixtureDef.friction = 2f;
        fixtureDef.shape = shape;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    /**
     *
     * @param x
     */
    public void movePlayer(float x){
        body.setLinearVelocity(x, body.getLinearVelocity().y);
    }

    /**
     *
     * @param batch
     */
    public void drawPlayerIdle(SpriteBatch batch){

        if (!isWalking){

            batch.draw(this, getX() + getWidth() / 2f - 20,
                    getY() - getHeight() / 2f);

        }

    }

    public void drawPlayerAnimation(SpriteBatch batch){
        if (isWalking){

            elapsedTime += Gdx.graphics.getDeltaTime();

            animation = new Animation(1f/10f, playerAtlas.getRegions());

            batch.draw((Texture) animation.getKeyFrame(elapsedTime, true),
                    getX() + getWidth() / 2f - 20 ,
                    getY() - getHeight() / 2f);
        }
    }



    /**
     *
     */
    public void updatePlayer(){
        setPosition(body.getPosition().x * GameInfo.PPH,
                body.getPosition().y * GameInfo.PPH);
    }

    public void setWaling(boolean isWalking){
        this.isWalking = isWalking;
    }
}
