package steelUnicorn.laplacity.field.tiles;

import steelUnicorn.laplacity.field.physics.CollisionListener;

public class TileCollisionListener implements CollisionListener {

	private boolean isDeadly;
	private boolean isTrampoline;
	
	public TileCollisionListener(int id) {
		isDeadly = id == 4;
		isTrampoline = id == 15;
	}

	@Override
	public boolean isDeadly() {
		return isDeadly;
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
		return !isDeadly && !isTrampoline;
	}

	@Override
	public void collidedWithTile() {
	}

	@Override
	public boolean isTrampoline() {
		return isTrampoline;
	}

	@Override
	public void collidedWithTrampoline() {
	}

	@Override
	public boolean isParticle() {
		return false;
	}

	@Override
	public void collidedWithParticle() {
	}

}
