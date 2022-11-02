package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
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
	
	// hinge
	private float hingeX;
	private float hingeY;
	
	// box2d stuff
	private Body box;
	private Body hinge;
	private Joint joint;

	// for interpolation
	private float prevAngle = 0f;
	private Vector2 prevPos = new Vector2(0f, 0f);
	
	private int cacheId;
	private static final Matrix4 transMat = new Matrix4();
	
	public HingeStructure(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, codes);
		
		float sz = LaplacityField.tileSize;
		
		IntRect hinge = new IntRect();
		findSubRect(pm, bounds, -926365441, hinge);
		
		hingeX = (hinge.left + 0.5f) * sz;
		hingeY = (hinge.top + 0.5f) * sz;
		
		if (bounds.width() != 1 && bounds.height() != 1) {
			throw new RuntimeException("bad bounds size, may be 1");
		}
		if (hinge.width() != 1 || hinge.height() != 1) {
			throw new RuntimeException("bad hinge size, may be 1");
		}
		
		gameCache.beginCache();
		
		if (bounds.width() == 1) {
			for (int y = bounds.bottom; y <= bounds.top; y++) {
				if (y == hinge.bottom) {
					gameCache.add(LaplacityAssets.HINGE_REGIONS[3], 0, (y - bounds.bottom) * sz, sz, sz);
				} else
				if (y - 1 == hinge.bottom || y + 1 == hinge.bottom) {
					if (hinge.bottom == bounds.bottom) {
						gameCache.add(LaplacityAssets.HINGE_REGIONS[2], 0, (y - bounds.bottom) * sz, sz/2, sz/2, sz, sz,1,1,-90);
					} else {
						gameCache.add(LaplacityAssets.HINGE_REGIONS[2], 0, (y - bounds.bottom) * sz, sz/2, sz/2, sz, sz,1,1,90);
					}
				} else
				if (y == bounds.top) {
					gameCache.add(LaplacityAssets.HINGE_REGIONS[0], 0, (y - bounds.bottom) * sz, sz/2, sz/2, sz, sz, 1, 1, -90);
				} else 
				if (y == bounds.bottom) {
					gameCache.add(LaplacityAssets.HINGE_REGIONS[0], 0, (y - bounds.bottom) * sz, sz/2, sz/2, sz, sz, 1, 1, 90);
				} else
				{
					gameCache.add(LaplacityAssets.HINGE_REGIONS[1], 0, (y - bounds.bottom) * sz, sz/2, sz/2, sz, sz, 1, 1, 90);
				}
			}
		} else {
			for (int x = bounds.left; x <= bounds.right; x++) {
				if (x == hinge.left) {
					gameCache.add(LaplacityAssets.HINGE_REGIONS[3], (x - bounds.left)*sz, 0, sz, sz);
				} else
				if (x - 1 == hinge.left || x + 1 == hinge.left) {
					if (hinge.left == bounds.left) {
						gameCache.add(LaplacityAssets.HINGE_REGIONS[2], (x - bounds.left)*sz, 0, sz/2, sz/2, sz, sz,1,1,180);
					} else {
						gameCache.add(LaplacityAssets.HINGE_REGIONS[2], (x - bounds.left)*sz, 0, sz/2, sz/2, sz, sz,1,1,0);
					}
				} else
				if (x == bounds.right) {
					gameCache.add(LaplacityAssets.HINGE_REGIONS[0], (x - bounds.left)*sz, 0, sz/2, sz/2, sz, sz, 1, 1, 180);
				} else 
				if (x == bounds.left) {
					gameCache.add(LaplacityAssets.HINGE_REGIONS[0], (x - bounds.left)*sz, 0, sz/2, sz/2, sz, sz, 1, 1, 0);
				} else
				{
					gameCache.add(LaplacityAssets.HINGE_REGIONS[1], (x - bounds.left)*sz, 0, sz/2, sz/2, sz, sz, 1, 1, 0);
				}
			}
		}
		
		cacheId = gameCache.endCache();
	}

	@Override
	public void register() {
		// box
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.DynamicBody;
		bodydef.position.set(centerX, centerY);
		box = GameProcess.registerPhysicalObject(bodydef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(worldWidth / 2, worldHeight / 2);

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
		rjd.localAnchorA.set(hingeX - centerX, hingeY - centerY);
		rjd.localAnchorB.setZero();
		
		joint = GameProcess.registerJoint(rjd);
	}

	@Override
	public void reset() {
		box.setAngularVelocity(0);
		box.setLinearVelocity(0, 0);
		box.setTransform(centerX, centerY, 0);
		prevAngle = 0;
		prevPos.set(centerX, centerY);
	}

	@Override
	public void renderCached(float timeFromStart) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		
		gameCache.setProjectionMatrix(CameraManager.camMat());
		gameCache.setTransformMatrix(
				transMat.idt().
				translate(interpX(), interpY(), 0).
				rotate(0, 0, 1, MathUtils.radDeg * interpAngle()).
				translate( - worldWidth / 2, - worldHeight/2, 0)
				);
		gameCache.begin();
		gameCache.draw(cacheId);
		gameCache.end();
		gameCache.setTransformMatrix(transMat.idt());
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	@Override
	public void cleanup() {
		GameProcess.deleteJoint(joint);
		GameProcess.deletePhysicalObject(box);
		GameProcess.deletePhysicalObject(hinge);
	}

	/**
	 * Сохраняет текущие координаты центра и угол поворота.
	 * Этот метод должен быть вызван перед обновлением физического
	 * состояния при использовании интерполяции при отображении структуры на экране
	 */
	@Override
	public void savePosition() {
		prevAngle = box.getAngle();
		prevPos = box.getPosition();
	}

	private float interpAngle() {
		return (1 - interpCoeff) * prevAngle + interpCoeff * box.getAngle();
	}

	private float interpX() {
		return (1 - interpCoeff) * prevPos.x + interpCoeff * box.getPosition().x;
	}

	private float interpY() {
		return (1 - interpCoeff) * prevPos.y + interpCoeff * box.getPosition().y;
	}
}
