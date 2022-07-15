package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ChargedParticle extends Actor {

	// Body
	protected Body body;
	
	// Charge
	protected float charge;
	
	public ChargedParticle(float x, float y, float rad, float charge) {
		this.charge = charge;
		
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.DynamicBody;
		bodydef.position.set(x, y);
		
		body = registerPhysicalObject(this, bodydef);
		
		CircleShape cir = new CircleShape();
		cir.setRadius(rad);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = cir;
		fxt.density = 10000;
		fxt.restitution = 1f;
		
		body.createFixture(fxt);
		body.setUserData(this);
		
		cir.dispose();
		
		setPosition(x, y);
	}

	@Override
	public void act(float delta) {
		setPosition(body.getPosition().x, body.getPosition().y);
		super.act(delta);
	}

	public void setPosition(float x, float y) {
		body.setTransform(x, y, 0);
		super.setPosition(x, y);
	}
	
	public float getCharge() {
		return charge;
	}

	public void delete() {
		deleteObject(this, body);
	}

}
