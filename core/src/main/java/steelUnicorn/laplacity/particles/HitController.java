package steelUnicorn.laplacity.particles;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import steelUnicorn.laplacity.field.tiles.DeadlyTile;
import steelUnicorn.laplacity.field.tiles.FieldTile;
import steelUnicorn.laplacity.field.tiles.FinishTile;

public class HitController implements ContactListener {

	private boolean hitted;
	private boolean finished;
	
	@Override
	public void beginContact(Contact contact) {
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
			if (part instanceof ControllableElectron) {
				if (tile instanceof DeadlyTile)
					hitted = true;
				if (tile instanceof FinishTile)
					finished = true;
			}
		}
	}
	
	public boolean isHitted() {
		if (hitted) {
			hitted = false;
			return true;
		}
		return false;
	}

	public boolean isFinished() {
		if (finished) {
			finished = false;
			return true;
		}
		return false;
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
