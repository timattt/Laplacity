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
import steelUnicorn.laplacity.field.LaplacityField;

public class BladesStructure extends FieldStructure {

	private static final int[] allCodes = new int[] {1684301055};

	// Body
	private Body body;
	
	// Size
	private float size;
	
	// location
	private float x;
	private float y;
	
	public BladesStructure(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, allCodes);
		
		float width = (bounds.right - bounds.left + 1) * LaplacityField.tileSize;
		float height = (bounds.top - bounds.bottom + 1) * LaplacityField.tileSize;
		
		if (width != height) {
			throw new RuntimeException("Blades structure is not quad!");
		}
		
		size = width;
		
		x = (bounds.right + bounds.left + 1) * LaplacityField.tileSize / 2;
		y = (bounds.top + bounds.bottom + 1) * LaplacityField.tileSize / 2;
		
		Gdx.app.log("new blades structure", "bounds: " + bounds);
	}

	@Override
	public void register() {
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.StaticBody;
		
		body = GameProcess.registerPhysicalObject(bodydef);

		// 1
		PolygonShape shape = new PolygonShape();

		shape.setAsBox(size / 2, size * BLADES_THICKNESS_FACTOR / 2);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 1;
		fxt.restitution = 1f;
		body.createFixture(fxt);
		body.setUserData(this);
		shape.dispose();
		
		// 2
		shape = new PolygonShape();

		shape.setAsBox(size * BLADES_THICKNESS_FACTOR / 2, size / 2);
		
		fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 1;
		fxt.restitution = 1f;
		body.createFixture(fxt);
		body.setUserData(this);
		shape.dispose();
	
	}

	@Override
	public void render(float timeFromStart) {
		float curAngle = (float) (360 * timeFromStart / (float)BLADES_ROTATION_TIME);
		body.setTransform(x, y, (float) (2 * Math.PI * curAngle / 360f));
		
		float sz = size * BLADES_THICKNESS_FACTOR;
		
		gameBatch.enableBlending();
		
		// center
		gameBatch.draw(LaplacityAssets.BLADES_REGIONS[2],
				x - sz / 2,
				y - sz / 2,
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
			float x1 = MathUtils.cosDeg(curAngle) * size * (1 - BLADES_THICKNESS_FACTOR) / 2 + x;
			float y1 = MathUtils.sinDeg(curAngle) * size * (1 - BLADES_THICKNESS_FACTOR) / 2 + y;
			gameBatch.draw(LaplacityAssets.BLADES_REGIONS[0],
					x1 - sz/2,
					y1 - sz/2,
					sz/2,
					sz/2,
					sz,
					sz,
					1f,
					1f,
					curAngle+90,
					false
					);
			
			float r = size / 4 + 1f * sz;
			x1 = MathUtils.cosDeg(curAngle) * r + x;
			y1 = MathUtils.sinDeg(curAngle) * r + y;
			gameBatch.draw(LaplacityAssets.BLADES_REGIONS[1],
					x1 - sz/2,
					y1 - sz/2,
					sz/2,
					sz/2,
					sz,
					size / 2f - 1.5f * sz,
					1f,
					1f,
					curAngle+90,
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
