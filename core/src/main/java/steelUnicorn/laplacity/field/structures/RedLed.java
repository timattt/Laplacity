package steelUnicorn.laplacity.field.structures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import box2dLight.PointLight;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;

public class RedLed extends FieldStructure {

	private static int[] codes = new int[] {-771751681};
	
	// location
	private float x;
	private float y;
	
	private PointLight light;
	
	public RedLed(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, codes);
		
		x = (bounds.right + bounds.left + 1) * LaplacityField.tileSize / 2;
		y = (bounds.top + bounds.bottom + 1) * LaplacityField.tileSize / 2;
	}

	@Override
	public void register() {
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.StaticBody;
		bodydef.position.set(x, y);
		
		//body = registerPhysicalObject(bodydef);
		
		CircleShape cir = new CircleShape();
		//cir.setRadius(rad);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = cir;
		fxt.density = 0.25f;
		fxt.restitution = 1f;
		
		//body.createFixture(fxt);
		//body.setUserData(this);
		
		cir.dispose();
		
		light = GameProcess.registerPointLight(x, y, Color.RED, GameProcess.ELECTRON_SIZE * 2);
	}

	@Override
	public void renderBatched(float timeFromStart) {
		float rad = GameProcess.ELECTRON_SIZE * 2;
		
		GameProcess.gameBatch.enableBlending();
		GameProcess.gameBatch.draw(LaplacityAssets.RED_RED_REGIONS[0], x - rad, y - rad, 2*rad, 2*rad);
		GameProcess.gameBatch.disableBlending();
	}

	@Override
	public void cleanup() {
		GameProcess.deletePointLight(light);
		//GameProcess.deletePhysicalObject(body);
	}

}
