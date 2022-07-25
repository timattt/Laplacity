package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import steelUnicorn.laplacity.GameProcess;
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
		shape.dispose();
		
		// 2
		shape = new PolygonShape();

		shape.setAsBox(size * BLADES_THICKNESS_FACTOR / 2, size / 2);
		
		fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 1;
		fxt.restitution = 1f;
		body.createFixture(fxt);
		shape.dispose();
	
	}

	@Override
	public void update(float timeFromStart) {
		float curAngle = (float) (360 * timeFromStart / (float)BLADES_ROTATION_TIME);
		body.setTransform(x, y, (float) (2 * Math.PI * curAngle / 360f));
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.CORAL);
		shapeRenderer.rect(x - size/2, y - size * BLADES_THICKNESS_FACTOR / 2, size / 2, size * BLADES_THICKNESS_FACTOR / 2, size, size * BLADES_THICKNESS_FACTOR, 1, 1, curAngle);
		shapeRenderer.rect(x - size * BLADES_THICKNESS_FACTOR / 2, y - size/2, size * BLADES_THICKNESS_FACTOR / 2, size / 2, size * BLADES_THICKNESS_FACTOR, size, 1, 1, curAngle);
		shapeRenderer.end();
	}

	@Override
	public void cleanup() {
		deleteObject(null, body);
	}

}
