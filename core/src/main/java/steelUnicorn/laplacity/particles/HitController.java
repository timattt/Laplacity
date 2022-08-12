package steelUnicorn.laplacity.particles;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.physics.CollisionListener;
import steelUnicorn.laplacity.gameModes.GameMode;

/**
 * Класс, который обрабатывает столкновения.
 * Проверить столкновения можно через два метода.
 * Причем, при второй проверке после удара флаг удара уже будет сброшен.
 * @author timat
 *
 */
public class HitController implements ContactListener {
	
	@Override
	public void beginContact(Contact contact) {
		Body body1 = contact.getFixtureA().getBody();
		Body body2 = contact.getFixtureB().getBody();
		
		CollisionListener dat1 = (CollisionListener) body1.getUserData();
		CollisionListener dat2 = (CollisionListener) body2.getUserData();

		if (GameProcess.currentGameMode != GameMode.FLIGHT) {
			return;
		}
		
		if (dat1.isDeadly()) {
			dat2.collidedWithDeadly();
		}
		if (dat1.isStructure()) {
			dat2.collidedWithStructure();
		}
		if (dat1.isMainParticle()) {
			dat2.collidedWithMainParticle();
		}
		if (dat1.isTile()) {
			dat2.collidedWithTile();
		}
		if (dat1.isTrampoline()) {
			dat2.collidedWithTrampoline();
		}
		
		if (dat2.isDeadly()) {
			dat1.collidedWithDeadly();
		}
		if (dat2.isStructure()) {
			dat1.collidedWithStructure();
		}
		if (dat2.isMainParticle()) {
			dat1.collidedWithMainParticle();
		}
		if (dat2.isTile()) {
			dat1.collidedWithTile();
		}
		if (dat2.isTrampoline()) {
			dat1.collidedWithTrampoline();
		}
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
