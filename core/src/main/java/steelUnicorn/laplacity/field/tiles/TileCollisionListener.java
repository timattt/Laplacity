package steelUnicorn.laplacity.field.tiles;

import steelUnicorn.laplacity.field.physics.CollisionListener;

public class TileCollisionListener implements CollisionListener {

	private boolean isFinish;
	private boolean isDeadly;
	
	public TileCollisionListener(int id) {
		isFinish = id == 5;
		isDeadly = id == 4;
	}

	@Override
	public boolean isDeadly() {
		return isDeadly;
	}

	@Override
	public void collidedWithDeadly() {
	}

	@Override
	public boolean isFinish() {
		return isFinish;
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

	@Override
	public boolean isTile() {
		return !isFinish;
	}

	@Override
	public void collidedWithTile() {
	}

}
