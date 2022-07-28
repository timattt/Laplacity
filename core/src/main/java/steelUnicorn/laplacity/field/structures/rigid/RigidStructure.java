package steelUnicorn.laplacity.field.structures.rigid;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.structures.FieldStructure;
import steelUnicorn.laplacity.utils.RigidTextureLoader;

public class RigidStructure extends FieldStructure {
	
	// Texture
	private String texturePath;
	
	// Physics
	private Body body;
	private Vector2 origin;
	private float scale;
	
	// center
	private float x;
	private float y;
	
	// sprite
	private Sprite sprite;
	
	// name
	private String name;
	
	public RigidStructure(int left, int bottom, Pixmap pm, int code, float scale, String path, Texture texture, String name) {
		super(left, bottom, pm, new int[] {code});
		origin = new Vector2();
		x = (bounds.left + bounds.right + 1) * LaplacityField.tileSize / 2;
		y = (bounds.bottom + bounds.top + 1) * LaplacityField.tileSize / 2;
		
		this.scale = scale;
		this.texturePath = path;
		this.name = name;
		
		sprite = new Sprite(texture);
		sprite.setSize(scale, scale * sprite.getHeight() / sprite.getWidth());
	}

	@Override
	public void register() {
		body = RigidTextureLoader.createSolidTexture(texturePath, scale, origin, name);
		body.setUserData(this);
		body.setTransform(x, y, 0);
	}

	@Override
	public void render(float timeFromStart) {
		Vector2 pos = Globals.TMP1.set(body.getPosition()).sub(origin);
        sprite.setPosition(pos.x, pos.y);
        sprite.setOrigin(origin.x, origin.y);
        sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
	
		sprite.draw(GameProcess.gameBatch);
	}

	@Override
	public void cleanup() {
		GameProcess.deletePhysicalObject(body);
	}

	@Override
	public void reset() {
		body.setTransform(x, y, 0);
		body.setLinearVelocity(0, 0);
		body.setAngularVelocity(0);
	}

}
