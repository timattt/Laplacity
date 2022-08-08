package steelUnicorn.laplacity.field.structures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;

public class FlimsyStructure extends FieldStructure {

	private static final int[] codes = new int[] {-1778384641};
	
	// Body
	private Body body;
	
	// durability
	private int durability;
	private boolean justBroken;
	
	public FlimsyStructure(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, codes);
		
		durability = GameProcess.FLIMSY_STRUCTURE_START_DURABILITY;
		justBroken = false;
		
		Gdx.app.log("new flimsy structure", "bounds: " + bounds);
	}

	@Override
	public void register() {
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.StaticBody;
		
		bodydef.position.set(centerX, centerY);
		
		body = GameProcess.registerPhysicalObject(bodydef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(worldWidth / 2, worldHeight / 2);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 1;
		fxt.restitution = 1f;
		body.createFixture(fxt);
		body.setUserData(this);
		shape.dispose();
	}

	@Override
	public void updatePhysics(float timeFromStart) {
		if (justBroken) {
			body.setTransform(100000, 100000, 0);
			justBroken = false;
		}
	}

	@Override
	public void renderBatched(float timeFromStart) {
		GameProcess.gameBatch.enableBlending();
		if (durability > 0) {
			int index = 2* durability / GameProcess.FLIMSY_STRUCTURE_START_DURABILITY;
			index = index % LaplacityAssets.GLASS_REGIONS.length;
			if (worldWidth < worldHeight) {
				GameProcess.gameBatch.draw(LaplacityAssets.GLASS_REGIONS[index], centerX - worldWidth/2, centerY - worldHeight/2, worldWidth, worldHeight);
			} else {
				GameProcess.gameBatch.draw(LaplacityAssets.GLASS_REGIONS[index], centerX - worldHeight/2, centerY - worldWidth/2, worldHeight / 2, worldWidth / 2, worldHeight, worldWidth, 1, 1, 90);
			}
		}
		GameProcess.gameBatch.disableBlending();
	}

	@Override
	public void reset() {
		durability = GameProcess.FLIMSY_STRUCTURE_START_DURABILITY;
		body.setTransform(centerX, centerY, 0);
	}

	@Override
	public void collidedWithMainParticle() {
		durability = Math.max(0, durability-1);
		
		if (durability == 0) {
			justBroken = true;
		}
	}

	@Override
	public void cleanup() {
		GameProcess.deletePhysicalObject(body);
	}

}
