package steelUnicorn.laplacity.particles;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.physics.BodyData;
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
		
		BodyData dat1 = (BodyData) body1.getUserData();
		BodyData dat2 = (BodyData) body2.getUserData();

		if (GameProcess.currentGameMode != GameMode.FLIGHT) {
			return;
		}
		
		if ((dat1.isMainParticle() && dat2.isDeadly()) || (dat2.isMainParticle() && dat1.isDeadly())) {
			hitted = true;
			//LaplacityAssets.playSound(LaplacityAssets.hurtSound);
			return;
		}
		if ((dat1.isMainParticle() && dat2.isFinish()) || (dat2.isMainParticle() && dat1.isFinish())) {
			finished = true;
			return;
		}
		if ((dat1.isMainParticle() && dat2.isStructure()) || (dat2.isMainParticle() && dat1.isStructure())) {
			//LaplacityAssets.playSound(LaplacityAssets.bumpStructureSound);
			return;
		}
		
		//LaplacityAssets.playSound(LaplacityAssets.bumpSound);
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
