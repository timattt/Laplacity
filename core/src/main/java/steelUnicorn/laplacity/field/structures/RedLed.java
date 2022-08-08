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

public class RedLed extends FieldStructure {

	private static int[] codes = new int[] {-771751681};
	
	private PointLight light;
	
	public RedLed(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, codes);
	}

	@Override
	public void register() {
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.StaticBody;
		bodydef.position.set(centerX, centerY);
		
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
		
		light = GameProcess.registerPointLight(centerX, centerY, Color.RED, GameProcess.RED_LED_LIGHT_DISTANCE, true);
	}

	@Override
	public void renderBatched(float timeFromStart) {
		float rad = GameProcess.PARTICLE_SIZE;
		
		GameProcess.gameBatch.enableBlending();
		GameProcess.gameBatch.draw(LaplacityAssets.RED_RED_REGIONS[0], centerX - rad, centerY - rad, 2*rad, 2*rad);
		GameProcess.gameBatch.disableBlending();
	}

	@Override
	public void cleanup() {
		GameProcess.deletePointLight(light);
		//GameProcess.deletePhysicalObject(body);
	}

}
