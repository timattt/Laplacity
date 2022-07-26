package steelUnicorn.laplacity.field.structures;

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

public class FlimsyStructure extends FieldStructure {

	private static final int[] codes = new int[] {-1778384641};
	
	// Body
	private Body body;
	
	private float x;
	private float y;
	
	private float width;
	private float height;
	
	// durability
	private int durability;
	private boolean justBroken;
	
	public FlimsyStructure(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, codes);
		float sz = LaplacityField.tileSize;
		x = (bounds.left + bounds.right + 1) * sz / 2;
		y = (bounds.bottom + bounds.top + 1) * sz / 2;
		
		width = (bounds.right - bounds.left + 1) * sz;
		height = (bounds.top - bounds.bottom + 1) * sz;
		
		durability = GameProcess.FLIMSY_STRUCTURE_START_DURABILITY;
		
		justBroken = false;
		
		Gdx.app.log("new flimsy structure", "bounds: " + bounds);
	}

	@Override
	public void register() {
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.StaticBody;
		
		bodydef.position.set(x, y);
		
		body = GameProcess.registerPhysicalObject(bodydef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 1;
		fxt.restitution = 1f;
		body.createFixture(fxt);
		body.setUserData(this);
		shape.dispose();
	}

	@Override
	public void render(float timeFromStart) {
		if (justBroken) {
			body.setTransform(100000, 100000, 0);
			justBroken = false;
		}
		if (durability > 0) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.SALMON);
			shapeRenderer.rect(x - width / 2, y - height / 2, width, height);
			shapeRenderer.end();
		}
	}

	@Override
	public void reset() {
		durability = GameProcess.FLIMSY_STRUCTURE_START_DURABILITY;
		body.setTransform(x, y, 0);
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
