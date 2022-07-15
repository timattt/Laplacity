package steelUnicorn.laplacity.particles;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class HitController implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		/*
		Body body1 = contact.getFixtureA().getBody();
		Body body2 = contact.getFixtureB().getBody();
		
		FieldTile tile = null;
		ChargedParticle part = null;
		
		if (body1.getUserData() != null && body1.getUserData() instanceof FieldTile) {
			tile = (FieldTile) body1.getUserData();
		}
		if (body2.getUserData() != null && body2.getUserData() instanceof FieldTile) {
			tile = (FieldTile) body2.getUserData();
		}
		if (body1.getUserData() != null && body1.getUserData() instanceof ChargedParticle) {
			part = (ChargedParticle) body1.getUserData();
		}
		if (body2.getUserData() != null && body2.getUserData() instanceof ChargedParticle) {
			part = (ChargedParticle) body2.getUserData();
		}
		
		if (tile != null && part != null) {
			if (tile instanceof DeadlyTile) {
				part.delete();
			}
		}
		*/
	}

	@Override
	public void endContact(Contact contact) {


	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
