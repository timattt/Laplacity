package steelUnicorn.laplacity.chargedParticles;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;

public class Proton extends ChargedParticle {

	private long lastAnimationUpdate;
	private int currentTextureIndex = 0;
	
	public Proton(float x, float y) {
		super(x, y, PARTICLE_SIZE, -PARTICLE_CHARGE, true, Color.CYAN);
		pointLight.setDistance(PARTICLE_LIGHT_DISTANCE);
	}

	@Override
	public void draw() {
		if (TimeUtils.millis() - lastAnimationUpdate > PARTICLE_TEXTURES_UPDATE_DELTA) {
			lastAnimationUpdate = TimeUtils.millis();
			currentTextureIndex = (currentTextureIndex + 1) % LaplacityAssets.PROTON_REGIONS.length;
		}
		
		float x = getX();
		float y = getY();
		
		if (useVirtualCoords) {
			x = virtualX;
			y = virtualY;
		}
		
		GameProcess.gameBatch.enableBlending();
		GameProcess.gameBatch.draw(LaplacityAssets.PROTON_REGIONS[currentTextureIndex], x - PARTICLE_SIZE, y - PARTICLE_SIZE, 2 * PARTICLE_SIZE, 2 * PARTICLE_SIZE);
		GameProcess.gameBatch.disableBlending();
	}

}
