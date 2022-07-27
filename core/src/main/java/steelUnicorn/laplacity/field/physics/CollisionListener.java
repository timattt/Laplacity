package steelUnicorn.laplacity.field.physics;

public interface CollisionListener {

	public boolean isDeadly();
	public void collidedWithDeadly();
	
	public boolean isFinish();
	public void collidedWithFinish();
	
	public boolean isMainParticle();
	public void collidedWithMainParticle();
	
	public boolean isStructure();
	public void collidedWithStructure();
	
	public boolean isTile();
	public void collidedWithTile();
	
}
