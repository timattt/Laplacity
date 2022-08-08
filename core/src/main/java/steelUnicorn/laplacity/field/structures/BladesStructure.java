package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;

public class BladesStructure extends FieldStructure {

	private static final int[] allCodes = new int[] {1684301055};

	// Body
	private Body body;
	
	public BladesStructure(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, allCodes);
		
		if (bounds.width() != bounds.height()) {
			throw new RuntimeException("Blades structure is not quad!");
		}
		
		Gdx.app.log("new blades structure", "bounds: " + bounds);
	}

	@Override
	public void register() {
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.StaticBody;
		
		body = GameProcess.registerPhysicalObject(bodydef);

		// 1
		PolygonShape shape = new PolygonShape();

		shape.setAsBox(worldWidth / 2, worldWidth * BLADES_THICKNESS_FACTOR / 2);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 1;
		fxt.restitution = 1f;
		body.createFixture(fxt);
		body.setUserData(this);
		shape.dispose();
		
		// 2
		shape = new PolygonShape();

		shape.setAsBox(worldWidth * BLADES_THICKNESS_FACTOR / 2, worldWidth / 2);
		
		fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 1;
		fxt.restitution = 1f;
		body.createFixture(fxt);
		body.setUserData(this);
		body.setTransform(centerX, centerY, 0);
		shape.dispose();
	
	}

	@Override
	public void updatePhysics(float timeFromStart) {
		float curAngle = (float) (360 * timeFromStart / (float)BLADES_ROTATION_TIME);
		body.setTransform(centerX, centerY, (float) (2 * Math.PI * curAngle / 360f));
	}

	@Override
	public void renderBatched(float timeFromStart) {
		float curAngle = (float) (360 * timeFromStart / (float)BLADES_ROTATION_TIME);
		float sz = worldWidth * BLADES_THICKNESS_FACTOR;
		
		gameBatch.enableBlending();
		
		// center
		gameBatch.draw(LaplacityAssets.BLADES_REGIONS[0],
				centerX - sz / 2,
				centerY - sz / 2,
				sz / 2,
				sz / 2,
				sz,
				sz,
				1f,
				1f,
				curAngle,
				false
				);
		
		// borders
		for (int i = 0; i < 4; i++) {
			float r = (worldWidth  - sz) / 4f + sz / 2- 2f*sz;
			float x1 = MathUtils.cosDeg(curAngle) * r + centerX;
			float y1 = MathUtils.sinDeg(curAngle) * r + centerY;
			gameBatch.draw(LaplacityAssets.BLADES_REGIONS[1],
					x1 - sz/2,
					y1 - sz/2,
					sz/2,
					sz/2,
					sz,
					worldWidth / 2f - 0.5f * sz + 0.5f * sz, // last is tmp
					1f,
					1f,
					curAngle-90,
					false
					);
			curAngle += 90;
		}
		
		gameBatch.disableBlending();
	}

	@Override
	public void cleanup() {
		deletePhysicalObject(body);
	}

}
