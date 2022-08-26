package steelUnicorn.laplacity.field.structures.rigid;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.structures.FieldStructure;
import steelUnicorn.laplacity.utils.RigidTextureLoader;

/**
 * Стурктура, которая поддреживает подгрузку сложного физического тела.
 * @author timat
 *
 */
public class RigidStructure extends FieldStructure {
	
	// Texture
	private String texturePath;
	
	// Physics
	private Body body;
	private Vector2 origin;
	private Vector2 pos;
	private float scale;

	// interpolation
	private Vector2 prevPos = new Vector2(0f, 0f);
	private float prevAngle = 0f;
	
	// sprite
	private Sprite sprite;
	
	// name
	private String name;
	
	public RigidStructure(int left, int bottom, Pixmap pm, int[] codes, float scale, String path, Texture texture, String name) {
		super(left, bottom, pm, codes);
		origin = new Vector2();

		pos = new Vector2(centerX, centerY);
		
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
		body.setTransform(centerX, centerY, 0);
	}

	@Override
	public void renderBatched(float timeFromStart) {
		pos.set(prevPos);
		pos.lerp(Globals.TMP1.set(body.getPosition()).sub(origin), GameProcess.interpCoeff);
        sprite.setPosition(pos.x, pos.y);
        sprite.setOrigin(origin.x, origin.y);
        sprite.setRotation(interpAngle() * MathUtils.radiansToDegrees);
        GameProcess.gameBatch.enableBlending();
		sprite.draw(GameProcess.gameBatch);
		GameProcess.gameBatch.disableBlending();
	}

	@Override
	public void cleanup() {
		GameProcess.deletePhysicalObject(body);
	}

	@Override
	public void reset() {
		body.setTransform(centerX, centerY, 0);
		body.setLinearVelocity(0, 0);
		body.setAngularVelocity(0);
		savePosition();
	}

	/**
	 * Сохраняет текущие координаты центра и угол поворота.
	 * Этот метод должен быть вызван перед обновлением физического
	 * состояния при использовании интерполяции при отображении структуры на экране
	 */
	@Override
	public void savePosition() {
		prevPos.set(body.getPosition()).sub(origin);
		prevAngle = body.getAngle();
	}

	private float interpAngle() {
		return (1 - GameProcess.interpCoeff) * prevAngle + GameProcess.interpCoeff * body.getAngle();
	}

}
