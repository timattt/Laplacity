package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
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
	
	private int cacheId;
	private static final Matrix4 transMat = new Matrix4();
	
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
		
		gameCache.beginCache();
		for (int x = bounds.left; x <= bounds.right; x++) {
			for (int y = bounds.bottom; y <= bounds.top; y++) {
				int i = (int) (Math.random() * 3);
				int j = (int) (Math.random() * 2);
				gameCache.add(LaplacityAssets.HINGE_REGIONS[i][j], (x - bounds.left) * sz, (y - bounds.bottom) * sz, sz, sz);
			}
		}
		cacheId = gameCache.endCache();
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
		fxt.density = 0.2f;
		fxt.restitution = 1f;
		box.createFixture(fxt);
		box.setUserData(this);
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
		hinge.setUserData(this);
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
	public void renderCached(float timeFromStart) {
		gameCache.setProjectionMatrix(CameraManager.camMat());
		gameCache.setTransformMatrix(
				transMat.idt().
				translate(box.getPosition().x, box.getPosition().y, 0).
				rotate(0, 0, 1, MathUtils.radDeg * box.getAngle()).
				translate( - boxWidth / 2, - boxHeight/2, 0)
				);
		gameCache.begin();
		gameCache.draw(cacheId);
		gameCache.end();
		gameCache.setTransformMatrix(transMat.idt());
	}

	@Override
	public void cleanup() {
		GameProcess.deleteJoint(joint);
		GameProcess.deletePhysicalObject(box);
		GameProcess.deletePhysicalObject(hinge);
	}

}
