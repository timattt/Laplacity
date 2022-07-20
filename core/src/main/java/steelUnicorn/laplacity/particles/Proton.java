package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Proton extends ChargedParticle {

	public Proton(float x, float y) {
		super(x, y, PROTON_SIZE, PARTICLE_CHARGE);
		setName("Electron");
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0f, 0f, 1f, 1f);
		shapeRenderer.circle(getX(), getY(), PROTON_SIZE);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.line(getX() - ELECTRON_SIZE / 3 * 2, getY(), getX() + ELECTRON_SIZE / 3 * 2, getY());
		shapeRenderer.line(getX(), getY() - ELECTRON_SIZE / 3 * 2, getX(), getY() + ELECTRON_SIZE / 3 * 2);
		shapeRenderer.end();
		super.draw(batch, parentAlpha);
	}

}
