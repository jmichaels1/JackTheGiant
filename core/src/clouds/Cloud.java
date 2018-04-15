package clouds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

/**
 * Created by Michael
 */
public class Cloud extends Sprite {

    private World world;
    private Body body;
    private String cloudName;

    /**
     * Método Constructor
     * @param world
     * @param cloudName
     */
    public Cloud(World world, String cloudName) {
        super(new Texture("Clouds/" + cloudName + ".png"));
        this.world = world;
        this.cloudName = cloudName;
    }

    /**
     * created body
     */
    void createdBody(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set((getX() - 40) / GameInfo.PPH,
                getY() / GameInfo.PPH);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2 - 20) / GameInfo.PPH,
                (getHeight() / 2) / GameInfo.PPH);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }


    public void setSpritePosition(float x, float y){
        setPosition(x, y);
        createdBody();
    }

    public String getCloudName(){
        return this.cloudName;
    }

}
