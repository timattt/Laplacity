package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Electron extends ChargedParticle {

	public Electron(float x, float y, boolean isStatic) {
		super(x, y, ELECTRON_SIZE, -PARTICLE_CHARGE, isStatic);
		setName("Electron");
	}
	
	public Electron(float x, float y) {
		this(x, y, true);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0f, 1f, 0, 1f);
		shapeRenderer.circle(getX(), getY(), ELECTRON_SIZE);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.line(getX() - ELECTRON_SIZE / 3 * 2, getY(), getX() + ELECTRON_SIZE / 3 * 2, getY());
		shapeRenderer.end();
		super.draw(batch, parentAlpha);
	}

}
