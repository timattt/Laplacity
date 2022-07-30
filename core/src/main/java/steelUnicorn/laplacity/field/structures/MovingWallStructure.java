package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
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
	
	// graph
	private int cacheId;
	private static final Matrix4 cacheMat = new Matrix4();
	
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
	
	private void createCache() {
		GameProcess.gameCache.beginCache();
		
		float sz = LaplacityField.tileSize;
		
		if (isHorizontal) {
			for (int y = 0; y < blockRect.height(); y++) {
				GameProcess.gameCache.add(LaplacityAssets.MOVING_WALL_STRUCTURE_REGIONS[0][y % 4], 0, y * sz, sz, sz);
				GameProcess.gameCache.add(LaplacityAssets.MOVING_WALL_STRUCTURE_REGIONS[0][y % 4], (blockRect.width() - 1) * sz, y * sz, sz, sz);
			}
			for (int x = 1; x < blockRect.width() - 1; x++) {
				for (int y = 0; y < blockRect.height(); y++) {
					GameProcess.gameCache.add(LaplacityAssets.MOVING_WALL_STRUCTURE_REGIONS[8][3], x * sz, y * sz, sz, sz);
					GameProcess.gameCache.add(LaplacityAssets.MOVING_WALL_STRUCTURE_REGIONS[9][3], x * sz, y * sz, sz, sz);
				}
			}
		} else {
			for (int x = 0; x < blockRect.width(); x++) {
				GameProcess.gameCache.add(LaplacityAssets.MOVING_WALL_STRUCTURE_REGIONS[x % 4][0], x * sz, 0, sz, sz);
				GameProcess.gameCache.add(LaplacityAssets.MOVING_WALL_STRUCTURE_REGIONS[x % 4][0], x * sz, (blockRect.height() - 1) * sz, sz, sz);
			}
			for (int x = 0; x < blockRect.width(); x++) {
				for (int y = 1; y < blockRect.height() - 1; y++) {
					GameProcess.gameCache.add(LaplacityAssets.MOVING_WALL_STRUCTURE_REGIONS[8][3], x * sz, y * sz, sz, sz);
					GameProcess.gameCache.add(LaplacityAssets.MOVING_WALL_STRUCTURE_REGIONS[9][3], x * sz, y * sz, sz, sz);
				}
			}
		}
		
		cacheId = GameProcess.gameCache.endCache();
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
		body.setUserData(this);
		shape.dispose();
		
		createCache();
	}

	@Override
	public void renderCached(float timeFromStart) {
		float phi = (float) Math.sin(phaseDelta + Math.PI * (double) (timeFromStart) / (double) (MOVING_WALL_CYCLE_TIME));
		
		if (isHorizontal) {
			currentCoord = (startCoord + endCoord) / 2f + phi * (endCoord - startCoord - width) / 2f;
			
			GameProcess.gameCache.setProjectionMatrix(CameraManager.camMat());
			GameProcess.gameCache.setTransformMatrix(cacheMat.idt().translate(currentCoord - width / 2, staticCoord - height / 2, 0));
			GameProcess.gameCache.begin();
			GameProcess.gameCache.draw(cacheId);
			GameProcess.gameCache.end();

			body.setTransform(currentCoord, staticCoord, 0);
		} else {
			currentCoord = (startCoord + endCoord) / 2f + phi * (endCoord - startCoord - height) / 2f;
			
			GameProcess.gameCache.setProjectionMatrix(CameraManager.camMat());
			GameProcess.gameCache.setTransformMatrix(cacheMat.idt().translate(staticCoord - width / 2, currentCoord - height / 2, 0));
			GameProcess.gameCache.begin();
			GameProcess.gameCache.draw(cacheId);
			GameProcess.gameCache.end();

			body.setTransform(staticCoord, currentCoord, 0);
		}
		
		GameProcess.gameCache.setTransformMatrix(cacheMat.idt());
	}

	@Override
	public void cleanup() {
		deletePhysicalObject(body);
	}

}
