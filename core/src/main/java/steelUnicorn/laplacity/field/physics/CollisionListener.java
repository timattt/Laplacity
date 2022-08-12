package steelUnicorn.laplacity.field.physics;

/**
 * Интерфейс, который передается в userData любого тела на карте.
 * @author timat
 *
 */
public interface CollisionListener {

	public boolean isDeadly();
	public void collidedWithDeadly();
	
	public boolean isMainParticle();
	public void collidedWithMainParticle();
	
	public boolean isStructure();
	public void collidedWithStructure();
	
	public boolean isTile();
	public void collidedWithTile();
	
	public boolean isTrampoline();
	public void collidedWithTrampoline();
	
}
