package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;

public class Electron extends ChargedParticle {

	private long lastAnimationUpdate;
	private int currentTextureIndex = 0;
	
	public Electron(float x, float y, boolean isStatic) {
		super(x, y, ELECTRON_SIZE, -PARTICLE_CHARGE, isStatic);
	}
	
	public Electron(float x, float y) {
		this(x, y, true);
	}

	@Override
	public void draw() {
		if (TimeUtils.millis() - lastAnimationUpdate > PARTICLE_TEXTURES_UPDATE_DELTA) {
			lastAnimationUpdate = TimeUtils.millis();
			currentTextureIndex = (currentTextureIndex + 1) % LaplacityAssets.PARTICLES_REGIONS.length;
		}
		GameProcess.gameBatch.draw(LaplacityAssets.PARTICLES_REGIONS[currentTextureIndex][0], getX() - ELECTRON_SIZE, getY() - ELECTRON_SIZE, 2 * ELECTRON_SIZE, 2 * ELECTRON_SIZE);
	}

}
