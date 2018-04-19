package player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private boolean isWalking, dead;

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isDead() {
        return dead;
    }

    public Player(World world, float x, float y) {
        super(new Texture("player/Player 1.png"));
        this.world = world;
        setPosition(x, y);
        createBody();
        playerAtlas = new TextureAtlas("Player Animation/Player Animation.atlas");

        dead = false;

    }

    void createBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / GameInfo.PPH, getY() / GameInfo.PPH);
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2f - 20) / GameInfo.PPH, (getHeight() / 2f) / GameInfo.PPH);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0f;
        fixtureDef.friction = 2;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.PLAYER;
        fixtureDef.filter.maskBits = GameInfo.DEFAULT | GameInfo.COLLECTABLE;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Player");

        shape.dispose();
    }

    public void movePlayer(float x) {
        if (x < 0 && !this.isFlipX()) {
            this.flip(true, false);
        } else if (x > 0 && this.isFlipX()) {
            this.flip(true, false);
        }
        setWalking(true);
        body.setLinearVelocity(x, body.getLinearVelocity().y);
    }

    public void updatePlayer() {

        if(body.getLinearVelocity().x > 0){
            setPosition(body.getPosition().x * GameInfo.PPH, body.getPosition().y * GameInfo.PPH);

        }else if(body.getLinearVelocity().x < 0 ){
            setPosition((body.getPosition().x - 0.3f) * GameInfo.PPH, body.getPosition().y * GameInfo.PPH);

        }

    }


    public void drawPlayerIdle(SpriteBatch batch) {

        if (!isWalking) {
            batch.draw(this, getX() + getWidth() / 2f - 20, getY() - getHeight() / 2f);

        }
    }

    public void drawPlayerAnimation(SpriteBatch batch) {
        if (isWalking) {
            elapsedTime += Gdx.graphics.getDeltaTime();

            com.badlogic.gdx.utils.Array<TextureAtlas.AtlasRegion> frames = playerAtlas.getRegions();
            for (TextureRegion frame : frames) {
                if (body.getLinearVelocity().x < 0 && !frame.isFlipX()) {
                    frame.flip(true, false);
                } else if (body.getLinearVelocity().x > 0 && frame.isFlipX()) {
                    frame.flip(true, false);

                }
            }

            animation = new Animation(1f / 10f, playerAtlas.getRegions());

            batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime, true), getX() + getWidth() / 2f - 20, getY() - getHeight() / 2f);

        }
    }
}
