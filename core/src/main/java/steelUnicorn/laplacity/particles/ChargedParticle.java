package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.physics.CollisionListener;

/**
 * Класс частицы. Тут есть ее физ. тело. И заряд.
 * При изменении положения актера сразу меняется положение физ. тела.
 * @author timat
 *
 */
public class ChargedParticle implements CollisionListener {

	// Body
	protected Body body;
	
	// Charge
	protected float charge;
	
	public ChargedParticle(float x, float y, float rad, float charge, boolean isStatic) {
		this.charge = charge;
		
		BodyDef bodydef = new BodyDef();
		bodydef.type = isStatic ? BodyType.StaticBody : BodyType.DynamicBody;
		bodydef.position.set(x, y);
		
		body = registerPhysicalObject(bodydef);
		
		CircleShape cir = new CircleShape();
		cir.setRadius(rad);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = cir;
		fxt.density = 1;
		fxt.restitution = 1f;
		
		body.createFixture(fxt);
		body.setUserData(this);
		
		cir.dispose();
		
		setPosition(x, y);
	}
	
	public void draw() {
	}
	
	public void update(float delta) {
	}
	
	public float getX() {
		return body.getTransform().getPosition().x;
	}
	
	public float getY() {
		return body.getTransform().getPosition().y;
	}
	
	public ChargedParticle(float x, float y, float rad, float charge) {
		this(x, y, rad, charge, true);
	}
	
	public float getMass() {
		return body.getMass();
	}

	public void setPosition(float x, float y) {
		body.setTransform(x, y, 0);
	}
	
	public float getCharge() {
		return charge;
	}

	public Body getBody() {
		return body;
	}
	
	public void addDissipative(float factor) {
		body.applyForceToCenter(Globals.TMP1.set(body.getLinearVelocity()).scl(factor), false);
	}

	@Override
	public boolean isDeadly() {
		return false;
	}

	@Override
	public void collidedWithDeadly() {
	}

	@Override
	public boolean isFinish() {
		return false;
	}

	@Override
	public void collidedWithFinish() {
	}

	@Override
	public boolean isMainParticle() {
		return false;
	}

	@Override
	public void collidedWithMainParticle() {
	}

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public void collidedWithStructure() {
	}

}
