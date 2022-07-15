package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import steelUnicorn.laplacity.field.FieldPotentialCalculator;
import steelUnicorn.laplacity.field.GameMode;

public class ControllableElectron extends Electron {

	// Start velocity
	private float dirX;
	private float dirY;
	
	// Start pos
	private float startX;
	private float startY;
	
	public ControllableElectron(float x, float y) {
		super(x, y, false);
		startX = x;
		startY = y;
	}

	@Override
	public void act(float delta) {
		if (currentGameMode == GameMode.flight) {
			FieldPotentialCalculator.calculateForce(getX() + field.getFieldWidth() * field.getTileSize() / 2, getY() + field.getFieldHeight() * field.getTileSize() / 2, field.getTiles(), TMP1);
			body.applyForceToCenter(TMP1.scl(charge), false);
		}
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (currentGameMode != GameMode.flight) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.YELLOW);
			shapeRenderer.line(getX(), getY(), getX() + dirX, getY() + dirY);
			shapeRenderer.end();
		}
		super.draw(batch, parentAlpha);
	}

	public void setDir(float x, float y) {
		dirX = x;
		dirY = y;
	}
	
	public void startFlight() {
		body.setLinearVelocity(dirX, dirY);
	}
	
	public void reset() {
		setPosition(startX, startY);
		body.setLinearVelocity(0, 0);
		body.setAngularVelocity(0);
	}

}
