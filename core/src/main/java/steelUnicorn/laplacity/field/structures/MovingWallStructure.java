package steelUnicorn.laplacity.field.structures;

import static steelUnicorn.laplacity.GameProcess.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	private float blockWidth;
	private float blockHeight;
	private float staticCoord;
	
	// Body
	private Body body;
	
	// graph
	private int cacheId;
	private int cacheStableId;
	private static final Matrix4 cacheMat = new Matrix4();
	private int gcd;
	
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
		
		blockWidth = (blockRect.right - blockRect.left + 1) * sz;
		blockHeight = (blockRect.top - blockRect.bottom + 1) * sz;
		
		currentCoord = startCoord;
		
		float x = 0;
		float r = 0;
		
		if (isHorizontal) {
			currentCoord += blockWidth / 2;
			staticCoord = LaplacityField.tileSize * 0.5f * (blockRect.top + blockRect.bottom + 1);
			
			x = (blockRect.left + blockRect.right + 1) * sz / 2f - (startCoord + endCoord) / 2f;
			r = (endCoord - startCoord - blockWidth) / 2f;
		} else {
			currentCoord += blockHeight / 2;
			staticCoord = LaplacityField.tileSize * 0.5f * (blockRect.right + blockRect.left + 1);
			
			x = (blockRect.bottom + blockRect.top + 1) * sz / 2f - (startCoord + endCoord) / 2f;
			r = (endCoord - startCoord - blockHeight) / 2f;
		}
		
		phaseDelta = (float) Math.asin(Math.max(-1.0,Math.min(1.0, x / r)));
		gcd = gcd(blockRect.width(), blockRect.height());
	}
	
	private int gcd(int a, int b) {
		if (b == 0)
			return a;
		return gcd(b, a % b);
	}
	
	private void createCache() {
		GameProcess.gameCache.beginCache();
		
		float sz = LaplacityField.tileSize;
		
		if (isHorizontal) {
			for (int x = 0; x < blockRect.width(); x += gcd) {
				for (int y = 0; y < blockRect.height(); y += gcd) {
					TextureRegion reg = LaplacityAssets.ELEVATOR_REGIONS[1][0];
					if (y == 0) {
						reg = LaplacityAssets.ELEVATOR_REGIONS[0][0];
					}
					if (y == blockRect.height() - gcd) {
						reg = LaplacityAssets.ELEVATOR_REGIONS[2][0];
					}
					GameProcess.gameCache.add(reg, x * sz, y * sz, gcd * sz/2, gcd * sz/2, gcd * sz, gcd * sz, 1, 1, 90);
				}
			}
		} else {
			for (int x = 0; x < blockRect.width(); x+=gcd) {
				for (int y = 0; y < blockRect.height(); y+=gcd) {
					TextureRegion reg = LaplacityAssets.ELEVATOR_REGIONS[1][0];
					if (x == 0) {
						reg = LaplacityAssets.ELEVATOR_REGIONS[0][0];
					}
					if (x == blockRect.width() - gcd) {
						reg = LaplacityAssets.ELEVATOR_REGIONS[2][0];
					}
					GameProcess.gameCache.add(reg, x * sz, y * sz, gcd*sz, gcd*sz);
				}
			}
		}
		
		cacheId = GameProcess.gameCache.endCache();
		
		GameProcess.gameCache.beginCache();
		
		if (isHorizontal) {
			for (int x = 0; x < bounds.width(); x++) {
				if (x == 0) {
					TextureRegion reg = LaplacityAssets.ELEVATOR_REGIONS[0][2];
					GameProcess.gameCache.add(reg, x * sz, 0 * sz, sz/2, sz/2, sz, sz, 1, 1, 180);
					GameProcess.gameCache.add(reg, x * sz, (bounds.height() - 1) * sz, sz/2, sz/2, sz, sz, 1, 1, 180);
				} else if (x == bounds.width() - 1) {
					TextureRegion reg = LaplacityAssets.ELEVATOR_REGIONS[0][2];
					GameProcess.gameCache.add(reg, x * sz, 0 * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
					GameProcess.gameCache.add(reg, x * sz, (bounds.height() - 1) * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
				} else {
					TextureRegion reg = LaplacityAssets.ELEVATOR_REGIONS[2][2];
					GameProcess.gameCache.add(reg, x * sz, 0 * sz, sz/2, sz/2, sz, sz, 1, 1, 90);
					GameProcess.gameCache.add(reg, x * sz, (bounds.height() - 1) * sz, sz/2, sz/2, sz, sz, 1, 1, 90);
				}
			}
		} else {
			for (int y = 0; y < bounds.height(); y++) {
				if (y == 0) {
					TextureRegion reg = LaplacityAssets.ELEVATOR_REGIONS[0][2];
					GameProcess.gameCache.add(reg, 0 * sz, y * sz, sz/2, sz/2, sz, sz, 1, 1, -90);
					GameProcess.gameCache.add(reg, (bounds.width() - 1) * sz, y * sz, sz/2, sz/2, sz, sz, 1, 1, -90);
				} else if (y == bounds.height() - 1) {
					TextureRegion reg = LaplacityAssets.ELEVATOR_REGIONS[0][2];
					GameProcess.gameCache.add(reg, 0 * sz, y * sz, sz/2, sz/2, sz, sz, 1, 1, 90);
					GameProcess.gameCache.add(reg, (bounds.width() - 1) * sz, y * sz, sz/2, sz/2, sz, sz, 1, 1, 90);
				} else {
					TextureRegion reg = LaplacityAssets.ELEVATOR_REGIONS[2][2];
					GameProcess.gameCache.add(reg, 0 * sz, y * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
					GameProcess.gameCache.add(reg, (bounds.width() - 1) * sz, y * sz, sz/2, sz/2, sz, sz, 1, 1, 0);
				}
			}
		}
		
		cacheStableId = GameProcess.gameCache.endCache();
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
		shape.setAsBox(blockWidth / 2, blockHeight / 2);
		
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
	public void updatePhysics(float timeFromStart) {
		float phi = (float) Math.sin(phaseDelta + Math.PI * (double) (timeFromStart) / (double) (MOVING_WALL_CYCLE_TIME));
		
		if (isHorizontal) {
			currentCoord = (startCoord + endCoord) / 2f + phi * (endCoord - startCoord - blockWidth) / 2f;
			body.setTransform(currentCoord, staticCoord, 0);
		} else {
			currentCoord = (startCoord + endCoord) / 2f + phi * (endCoord - startCoord - blockHeight) / 2f;
			body.setTransform(staticCoord, currentCoord, 0);
		}
	}

	@Override
	public void renderCached(float timeFromStart) {
		float phi = (float) Math.sin(phaseDelta + Math.PI * (double) (timeFromStart) / (double) (MOVING_WALL_CYCLE_TIME));

		Gdx.gl.glEnable(GL20.GL_BLEND);

		GameProcess.gameCache.setProjectionMatrix(CameraManager.camMat());

		float sz = LaplacityField.tileSize;

		GameProcess.gameCache.setTransformMatrix(cacheMat.idt().translate(bounds.left * sz, bounds.bottom * sz, 0));

		GameProcess.gameCache.begin();
		GameProcess.gameCache.draw(cacheStableId);
		GameProcess.gameCache.end();

		if (isHorizontal) {
			currentCoord = (startCoord + endCoord) / 2f + phi * (endCoord - startCoord - blockWidth) / 2f;
			GameProcess.gameCache.setTransformMatrix(cacheMat.idt().translate(currentCoord - blockWidth / 2, staticCoord - blockHeight / 2, 0));
			body.setTransform(currentCoord, staticCoord, 0);
		} else {
			currentCoord = (startCoord + endCoord) / 2f + phi * (endCoord - startCoord - blockHeight) / 2f;
			GameProcess.gameCache.setTransformMatrix(cacheMat.idt().translate(staticCoord - blockWidth / 2, currentCoord - blockHeight / 2, 0));
			body.setTransform(staticCoord, currentCoord, 0);
		}

		GameProcess.gameCache.begin();
		GameProcess.gameCache.draw(cacheId);
		GameProcess.gameCache.end();

		GameProcess.gameCache.setTransformMatrix(cacheMat.idt());

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	@Override
	public void cleanup() {
		deletePhysicalObject(body);
	}

}
