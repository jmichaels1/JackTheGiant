package clouds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import Collectable.Collectable;
import helpers.GameInfo;
import player.Player;

/**
 * Created by J Michael
 */
public class CloudsController {

    private World world;
    private Array<Cloud> aCloud = new Array<Cloud>();
    private final float DISTANCE_BETWEEN_CLOUDS = 20000f;

    private float cameraY;

    private float minX, maxX;
    private Random random = new Random();

    private float lastCloudPositionY;

    private Array<Collectable> collectables = new Array<Collectable>();


    /**
     * Método constructor
     * @param world
     */
    public CloudsController(World world) {
        this.world = world;
        minX = GameInfo.WIDTH /2f - 120;
        maxX = GameInfo.WIDTH/2f + 120;
        createdClouds();
        positionsClouds(true);
    }

    /**
     * created clouds
     */
    private void createdClouds() {

        for (int i = 0; i < 2; i++) {
            aCloud.add(new Cloud(world, "Dark Cloud"));
        }

        int index = 1;

        for (int i = 0; i < 6; i++) {
            aCloud.add(new Cloud(world, "Cloud " + index));
            index++;

            if (index == 4) index = 1;
        }

        aCloud.shuffle();
    }

    /**
     * positionsClouds
     */
    public void positionsClouds(boolean firstTimeArranging){

        while (aCloud.get(0).getCloudName() == "Dark Cloud") {
            aCloud.shuffle();
        }

        float positionY = GameInfo.HEIGHT/2f;

        if (firstTimeArranging) positionY = GameInfo.HEIGHT / 2f;
        else positionY = lastCloudPositionY;


        int controlX = 0;

        for (Cloud c : aCloud) {

            if (c.getX() == 0  && c.getY() == 0){

                float tempX = 0;

                if (controlX == 0) {

                    tempX = randomBetweenNumber(maxX - 40, maxX);
                    controlX = 1;
                    c.setDrawLeft(false);


                } else if (controlX == 1){

                    tempX = randomBetweenNumber(minX + 40, minX);
                    controlX = 0;
                    c.setDrawLeft(true);

                }

                c.setSpritePosition(tempX, positionY);
                positionY -= DISTANCE_BETWEEN_CLOUDS;

                lastCloudPositionY = positionY;

            }

        }

        Collectable c1 = new Collectable(world, "Coin");
        c1.setCollectablePosition(aCloud.get(1).getX(),
                aCloud.get(1).getY() + 40);

        collectables.add(c1);

    }

    /**
     *
     * @param batch
     */
    public void drawClouds(SpriteBatch batch){
        for (Cloud c : aCloud) {

            if (c.getDrawLeft()){

                batch.draw(c, c.getX() - c.getWidth()/2f - 20,
                        c.getY()/c.getHeight()/2f);


            } else {

                batch.draw(c, c.getX() - c.getWidth()/2f + 10,
                        c.getY()/c.getHeight()/2f);

            }
        }

    }

    /**
     * drawCollectables
     */
    public void drawCollectables(SpriteBatch batch){

        for (Collectable c :
                collectables) {
            c.updateCollectable();
            batch.draw(c, c.getX(), c.getY());
        }

    }


    /**
     * createAndArrangeNewClouds
     */
    public void createAndArrangeNewClouds() {
        for (int i = 0; i < aCloud.size; i++) {
//            cameraY -= 10000f;
//            System.out.println("evaluando : " + (aCloud.get(i).getY() - GameInfo.HEIGHT / 2) + " vs " + cameraY );
            if ((aCloud.get(i).getY() - GameInfo.HEIGHT / 2) > cameraY - 10) {
//                System.out.println("entra aqui");
                aCloud.get(i).getTexture().dispose();
                aCloud.removeIndex(i);
            }
        }

        if (aCloud.size == 4){
            createdClouds();


            positionsClouds(false);
        }
    }

    /**
     *
     * @param player
     * @return
     */
    public Player positionThePlayer(Player player){
        player = new Player(world, aCloud.get(0).getX(),
                aCloud.get(0).getY() + 100);
        return player;
    }

    /**
     *
     * @param cameraY
     */
    public void setCameraY(float cameraY){
        this.cameraY = cameraY;

    }

    /**
     *
     * @param max
     * @param min
     * @return
     */
    private float randomBetweenNumber(float min, float max){

        return random.nextFloat() * (max - min) + min;
    }

    public void removeCollectables() {
        for (int i = 0; i < collectables.size; i++) {
            if (collectables.get(i).getFixture().getUserData() == "Remove") {
                collectables.get(i).changeFilter();
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);
            }
        }

    }
}
