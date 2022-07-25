package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.IntRect;

public class HingeStructure extends FieldStructure {

	private static final int[] codes = new int[] {13107455, -926365441};
	
	// box
	private float boxX;
	private float boxY;
	
	private float boxWidth;
	private float boxHeight;
	
	// hinge
	private float hingeX;
	private float hingeY;
	
	// box2d stuff
	private Body box;
	private Body hinge;
	private Joint joint;
	
	public HingeStructure(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, codes);
		
		float sz = LaplacityField.tileSize;
		
		IntRect hinge = new IntRect();
		findSubRect(pm, bounds, -926365441, hinge);
		
		hingeX = (hinge.left + 0.5f) * sz;
		hingeY = (hinge.top + 0.5f) * sz;
		
		boxX = (bounds.left + bounds.right + 1) * sz / 2;
		boxY = (bounds.bottom + bounds.top + 1) * sz / 2;
		
		boxWidth = (bounds.right - bounds.left + 1) * sz;
		boxHeight = (bounds.top - bounds.bottom + 1) * sz;
	}

	@Override
	public void register() {
		// box
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.DynamicBody;
		bodydef.position.set(boxX, boxY);
		box = GameProcess.registerPhysicalObject(bodydef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(boxWidth / 2, boxHeight / 2);

		FixtureDef fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 0.1f;
		fxt.restitution = 1f;
		box.createFixture(fxt);
		box.setUserData((Integer) 10);
		shape.dispose();
		
		// hinge
		bodydef = new BodyDef();
		bodydef.type = BodyType.StaticBody;
		bodydef.position.set(hingeX, hingeY);
		hinge = GameProcess.registerPhysicalObject(bodydef);

		CircleShape cir = new CircleShape();
		cir.setRadius(LaplacityField.tileSize / 2);

		fxt = new FixtureDef();
		fxt.shape = cir;
		fxt.density = 1;
		fxt.restitution = 1f;
		hinge.createFixture(fxt);
		hinge.setUserData((Integer) 10);
		cir.dispose();
		
		RevoluteJointDef rjd = new RevoluteJointDef();
		rjd.bodyA = box;
		rjd.bodyB = hinge;
		rjd.localAnchorA.set(hingeX - boxX, hingeY - boxY);
		rjd.localAnchorB.setZero();
		
		joint = GameProcess.registerJoint(rjd);
	}

	@Override
	public void reset() {
		box.setAngularVelocity(0);
		box.setLinearVelocity(0, 0);
		box.setTransform(boxX, boxY, 0);
	}

	@Override
	public void update(float timeFromStart) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.FOREST);
		shapeRenderer.rect(boxX - boxWidth / 2, boxY - boxHeight / 2, hingeX - boxX + boxWidth / 2, hingeY - boxY + boxHeight / 2, boxWidth, boxHeight, 1, 1, 180f * box.getAngle() / 3.1415f);
		shapeRenderer.end();
	}

	@Override
	public void cleanup() {
		GameProcess.deleteJoint(joint);
		GameProcess.deleteObject(null, box);
		GameProcess.deleteObject(null, hinge);
	}

}
