package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.IntRect;

/**
 * 
 * @author timat
 *
 */
public class MovingWallStructure extends FieldStructure {

	private static final int[] allCodes = new int[] {16777215, -16711681};
	
	// block
	private IntRect blockRect;
	private boolean isHorizontal = false;
	private float startCoord;
	private float endCoord;
	private float currentCoord;
	private float phaseDelta;
	private float width;
	private float height;
	private float staticCoord;
	
	// Body
	private Body body;
	
	public MovingWallStructure(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, allCodes);
		blockRect = new IntRect();
		
		findSubRect(pm, bounds, -16711681, blockRect);
	
		Gdx.app.log("new moving wall structure", "bounds: " + bounds + " wall: " + blockRect);
		
		if (blockRect.top == bounds.top && blockRect.bottom == bounds.bottom) {
			isHorizontal = true;
		}
		
		float sz = LaplacityField.tileSize;
		
		if (isHorizontal) {
			startCoord = bounds.left * sz;
			endCoord = (bounds.right + 1) * sz;
		} else {
			startCoord = bounds.bottom * sz;;
			endCoord = (bounds.top + 1) * sz;
		}
		
		width = (blockRect.right - blockRect.left + 1) * sz;
		height = (blockRect.top - blockRect.bottom + 1) * sz;
		
		currentCoord = startCoord;
		
		if (isHorizontal) {
			currentCoord += width / 2;
			staticCoord = LaplacityField.tileSize * 0.5f * (blockRect.top + blockRect.bottom + 1);
			phaseDelta = (float) (Math.PI * (0.5f * sz * (blockRect.right + blockRect.left + 1) - (startCoord + endCoord) / 2f ) / (0.5f * (endCoord - startCoord - width)));
		} else {
			currentCoord += height / 2;
			staticCoord = LaplacityField.tileSize * 0.5f * (blockRect.right + blockRect.left + 1);
			phaseDelta = (float) (Math.PI * (0.5f * sz * (blockRect.top + blockRect.bottom + 1) - (startCoord + endCoord) / 2f) / (0.5f * (endCoord - startCoord - height)));
		}
	}

	@Override
	public void register() {
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.StaticBody;
		
		if (isHorizontal) {
			bodydef.position.set(currentCoord, staticCoord);
		} else {
			bodydef.position.set(staticCoord, currentCoord);
		}
		
		body = GameProcess.registerPhysicalObject(bodydef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 10000;
		fxt.restitution = 1f;
		body.createFixture(fxt);
		body.setUserData((Integer)10);
		shape.dispose();
	}

	@Override
	public void update(float timeFromStart) {
		float phi = (float) Math.sin(phaseDelta + Math.PI * (double) (timeFromStart) / (double) (MOVING_WALL_CYCLE_TIME));
		
		if (isHorizontal) {
			currentCoord = (startCoord + endCoord) / 2f + phi * (endCoord - startCoord - width) / 2f;
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.GOLD);
			shapeRenderer.rect(currentCoord - width / 2, staticCoord - height / 2, width, height);
			shapeRenderer.end();

			body.setTransform(currentCoord, staticCoord, 0);
		} else {
			currentCoord = (startCoord + endCoord) / 2f + phi * (endCoord - startCoord - height) / 2f;
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.GOLD);
			shapeRenderer.rect(staticCoord - width / 2, currentCoord - height / 2, width, height);
			shapeRenderer.end();
			
			body.setTransform(staticCoord, currentCoord, 0);
		}
	}

	@Override
	public void cleanup() {
		deleteObject(null, body);
	}

}
