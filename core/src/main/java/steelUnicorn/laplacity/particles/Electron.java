package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Electron extends ChargedParticle {

	public Electron(float x, float y) {
		super(x, y, ELECTRON_SIZE, ELECTRON_CHARGE);
		body.setLinearVelocity(40f, 90f);
		setName("Electron");
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0f, 1f, 0, 1f);
		shapeRenderer.circle(getX(), getY(), ELECTRON_SIZE);
		shapeRenderer.end();
		super.draw(batch, parentAlpha);
	}

	@Override
	public void act(float delta) {
		setPosition(body.getPosition().x, body.getPosition().y);
		super.act(delta);
	}

}
