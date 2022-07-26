package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.core.LaplacityAssets;

public class Proton extends ChargedParticle {

	private long lastAnimationUpdate;
	private int currentTextureIndex = 0;
	
	public Proton(float x, float y) {
		super(x, y, PROTON_SIZE, PARTICLE_CHARGE);
		setName("Electron");
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (TimeUtils.millis() - lastAnimationUpdate > PARTICLE_TEXTURES_UPDATE_DELTA) {
			lastAnimationUpdate = TimeUtils.millis();
			currentTextureIndex = (currentTextureIndex + 1) % LaplacityAssets.PARTICLES_REGIONS.length;
		}
		batch.draw(LaplacityAssets.PARTICLES_REGIONS[currentTextureIndex][1], getX() - ELECTRON_SIZE, getY() - ELECTRON_SIZE, 2 * ELECTRON_SIZE, 2 * ELECTRON_SIZE);
	}

}
