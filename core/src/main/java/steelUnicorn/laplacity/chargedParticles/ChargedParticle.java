package steelUnicorn.laplacity.chargedParticles;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import box2dLight.PointLight;
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
	
	// light
	protected PointLight pointLight;
	
	// size
	protected float radius;
	
	// drag
	protected boolean useVirtualCoords = false;
	protected float virtualX;
	protected float virtualY;
	
	public ChargedParticle(float x, float y, float rad, float charge, boolean isStatic, Color lightColor) {
		this.charge = charge;
		this.radius = rad;
		
		BodyDef bodydef = new BodyDef();
		bodydef.type = isStatic ? BodyType.StaticBody : BodyType.DynamicBody;
		bodydef.position.set(x, y);
		
		body = registerPhysicalObject(bodydef);
		
		CircleShape cir = new CircleShape();
		cir.setRadius(rad);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = cir;
		fxt.density = 0.25f;
		fxt.restitution = 1f;
		fxt.friction = 0f;
		
		body.createFixture(fxt);
		body.setUserData(this);
		
		cir.dispose();
	
		pointLight = registerPointLight(x, y, lightColor, rad, true);
		pointLight.attachToBody(body);
		
		setPosition(x, y);
	}
	
	public void useVirtual(float x, float y) {
		useVirtualCoords = true;
		virtualX = x;
		virtualY = y;
	}
	
	public void disableVirtual() {
		useVirtualCoords = false;
	}
	
	public void draw() {
	}
	
	public PointLight getPointLight() {
		return pointLight;
	}

	public float getX() {
		return body.getTransform().getPosition().x;
	}
	
	public float getY() {
		return body.getTransform().getPosition().y;
	}
	
	public ChargedParticle(float x, float y, float rad, float charge) {
		this(x, y, rad, charge, true, Color.GREEN);
	}
	
	public float getMass() {
		return body.getMass();
	}

	public void setPosition(float x, float y) {
		body.setTransform(x, y, body.getAngle());
		pointLight.setPosition(x, y);
	}
	
	public void setPosition(float x, float y, float angle) {
		body.setTransform(x, y, angle);
		pointLight.setPosition(x, y);
	}
	
	public float getCharge() {
		return charge;
	}

	public Body getBody() {
		return body;
	}

	@Override
	public boolean isDeadly() {
		return false;
	}

	@Override
	public void collidedWithDeadly() {
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

	@Override
	public boolean isTile() {
		return false;
	}

	@Override
	public void collidedWithTile() {
	}

	public float getRadius() {
		return radius;
	}

	@Override
	public boolean isTrampoline() {
		return false;
	}

	@Override
	public void collidedWithTrampoline() {
	}

	@Override
	public boolean isParticle() {
		return true;
	}

	@Override
	public void collidedWithParticle() {
	}

}