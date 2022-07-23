package steelUnicorn.laplacity.particles;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.tiles.SolidTile;
import steelUnicorn.laplacity.gameModes.GameMode;

/**
 * Класс, который обрабатывает столкновения.
 * Проверить столкновения можно через два метода.
 * Причем, при второй проверке после удара флаг удара уже будет сброшен.
 * @author timat
 *
 */
public class HitController implements ContactListener {

	private boolean hitted;
	private boolean finished;
	
	@Override
	public void beginContact(Contact contact) {
		Body body1 = contact.getFixtureA().getBody();
		Body body2 = contact.getFixtureB().getBody();
		
		Integer tileId = null;
		ChargedParticle part = null;
		
		if (body1.getUserData() != null && body1.getUserData() instanceof Integer) {
			tileId = (Integer) body1.getUserData();
		}
		if (body2.getUserData() != null && body2.getUserData() instanceof SolidTile) {
			tileId = (Integer) body2.getUserData();
		}
		if (body1.getUserData() != null && body1.getUserData() instanceof ChargedParticle) {
			part = (ChargedParticle) body1.getUserData();
		}
		if (body2.getUserData() != null && body2.getUserData() instanceof ChargedParticle) {
			part = (ChargedParticle) body2.getUserData();
		}
		
		if (tileId != null && part != null) {
			if (part instanceof ControllableElectron && GameProcess.currentGameMode == GameMode.FLIGHT) {
				if (tileId == 4) { // deadly tile id
					LaplacityAssets.playSound(LaplacityAssets.hurtSound);;
					hitted = true;
				} else if (tileId == 5) {// finish tile id
					finished = true;
				} else { // it was just a collision
					LaplacityAssets.playSound(LaplacityAssets.bumpSound);;
				}
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
