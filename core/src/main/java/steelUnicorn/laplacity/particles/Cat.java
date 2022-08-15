package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.TilesRenderer;
import steelUnicorn.laplacity.field.physics.CollisionListener;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.field.tiles.DeadlyTile;
import steelUnicorn.laplacity.gameModes.GameMode;


/**
 * Класс управляемой частицы.
 * Тут есть поле начальной скорости. При обновлении к телу добавляется сила от поля.
 * @author timat
 *
 */
public class Cat implements CollisionListener {

	// Start velocity
	private float slingshotX;
	private float slingshotY;

	// Prev pos
	private float prevX = 0f;
	private float prevY = 0f;
	
	// emoji
	private int currentEmoji = 0;
	
	// finish animation
	private float finishX;
	private float finishY;
	private boolean playingFinishAnim = false;
	private float animationCoef = 0;
	
	// dead animation
	private boolean playingDeadAnim = false;
	private long deadAnimationStartTime;
	
	// body
	private Body body;
	
	public Cat() {
		prevX = LaplacityField.electronStartPos.x;
		prevY = LaplacityField.electronStartPos.y;
		
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.DynamicBody;
		bodydef.position.set(LaplacityField.electronStartPos.x, LaplacityField.electronStartPos.y);
		
		body = registerPhysicalObject(bodydef);
		
		CircleShape cir = new CircleShape();
		cir.setRadius(CAT_SIZE);
		
		FixtureDef fxt = new FixtureDef();
		fxt.shape = cir;
		fxt.density = 0.25f;
		fxt.restitution = 1f;
		fxt.friction = 0f;
		fxt.restitution = 0;
		
		body.createFixture(fxt);
		body.setUserData(this);
		
		cir.dispose();
		
		body.setBullet(true);
	}

	public float getX() {
		return body.getTransform().getPosition().x;
	}
	
	public float getY() {
		return body.getTransform().getPosition().y;
	}
	
	private void setDeadEmoji() {
		currentEmoji = 5;
	}
	
	public void draw() {
		float x = interpX();
		float y = interpY();
		float scale = 1;
		
		if (playingFinishAnim) {
			x = (finishX - x) * animationCoef + x;
			y = (finishY - y) * animationCoef + y;
			scale = 0.8f + 0.2f * (1f - animationCoef);
		}
		if (playingDeadAnim) {
			setDeadEmoji();
			if (TimeUtils.millis() - deadAnimationStartTime > DEAD_ANIMATION_TIME) {
				playingDeadAnim = false;
				GameProcess.justHitted = true;
				DeadlyTile.changeTexture();
				TilesRenderer.requestRepaint();
				setNormalRandomEmoji();
			}
			float t = TimeUtils.millis() - deadAnimationStartTime;
			float r = 1f;
			float omega = 0.1f;
			x += r * Math.cos(omega * t);
			y += r * Math.sin(omega * t);
		}
		
		GameProcess.gameBatch.enableBlending();
		GameProcess.gameBatch.draw(LaplacityAssets.CAT_REGIONS[currentEmoji % LaplacityAssets.CAT_REGIONS.length][currentEmoji / LaplacityAssets.CAT_REGIONS.length],
				x - CAT_SIZE,
				y - CAT_SIZE,
				CAT_SIZE,
				CAT_SIZE,
				2 * CAT_SIZE,
				2 * CAT_SIZE,
				scale,
				scale,
				body.getAngle() * MathUtils.radDeg
				);
		GameProcess.gameBatch.disableBlending();
	}
	
	private void setNormalRandomEmoji() {
		do {
			currentEmoji = (int) (Math.random() * LaplacityAssets.CAT_REGIONS.length * LaplacityAssets.CAT_REGIONS[0].length);
		} while (currentEmoji == 5);
	}

	@Override
	public boolean isMainParticle() {
		return true;
	}

	public void updatePhysics(float delta) {
		if (currentGameMode == GameMode.FLIGHT) {
			FieldCalculator.calculateFieldIntensity(getX(), getY(), LaplacityField.tiles, TMP1);
			body.applyForceToCenter(TMP1.scl(-PARTICLE_CHARGE / body.getMass()), false);
		}
	}

	public void setSlingshot(float x, float y) {
		slingshotX = x;
		slingshotY = y;
	}
	
	public void makeParticleMoveWithStartVelocity() {
		body.setLinearVelocity(-slingshotX * TRAJECTORY_VELOCITY_MULTIPLIER, -slingshotY * TRAJECTORY_VELOCITY_MULTIPLIER);
	}
	
	public void resetToStartPosAndStartVelocity() {
		body.setTransform(LaplacityField.electronStartPos.x, LaplacityField.electronStartPos.y, 0);
		savePosition();
		body.setLinearVelocity(0, 0);
		body.setAngularVelocity(0);
		playingFinishAnim = false;
	}
	
	public void drawStartVelocityArrow() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.line(getX(), getY(), getX() + slingshotX, getY() + slingshotY);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(getX() + slingshotX, getY() + slingshotY, PARTICLE_SIZE / 3);
		shapeRenderer.end();
	}

	public void calculateTrajectory(Vector2[] dest) {
		makeParticleMoveWithStartVelocity();
		LaplacityField.resetStructures();
		for (int i = 0; i < TRAJECTORY_POINTS; i++) {
			dest[i].set(body.getTransform().getPosition());
			for (int j = 0; j < STEPS_PER_POINT; j++) {
				FieldCalculator.calculateFieldIntensity(body.getTransform().getPosition().x, body.getTransform().getPosition().y, LaplacityField.tiles, TMP1);
				body.applyForceToCenter(TMP1.scl(-PARTICLE_CHARGE / body.getMass()), false);
				body.getWorld().step(TRAJECTORY_TIME_STEP, VELOCITY_STEPS, POSITION_STEPS);
			}
		}
		LaplacityField.resetStructures();
		resetToStartPosAndStartVelocity();
	}

	@Override
	public void collidedWithDeadly() {
		LaplacityAssets.playSound(LaplacityAssets.hurtSound);
		playingDeadAnim = true;
		deadAnimationStartTime = TimeUtils.millis();
		body.setLinearVelocity(0, 0);
		DeadlyTile.changeTexture();
		TilesRenderer.requestRepaint();
	}

	@Override
	public void collidedWithTile() {
		LaplacityAssets.playSound(LaplacityAssets.bumpSound);
		setNormalRandomEmoji();
	}

	public float interpX() {
		return (1 - interpCoeff) * prevX + interpCoeff * getX();
	}

	public float interpY() {
		return (1 - interpCoeff) * prevY + interpCoeff * getY();
	}

	@Override
	public void collidedWithTrampoline() {
		LaplacityAssets.playSound(LaplacityAssets.trampolineSound);
		currentEmoji = (int) (Math.random() * LaplacityAssets.CAT_REGIONS.length * LaplacityAssets.CAT_REGIONS[0].length);
	}

	/**
	 * Сохраняет текущее положение частицы во внутреннюю память частицы
	 * При прорисовке будет использована интерполяция между записанным
	 * и текущим физическим состоянием (текущее состояние опережает экранное время)
	 * с использованием глобального коэффициента {@linkplain GameProcess#interpCoeff}.
	 */
	public void savePosition() {
		prevX = getX();
		prevY = getY();
	}

	public Vector2 getVelocity() {
		return body.getLinearVelocity();
	}

	public void playFinishAnimation(float finX, float finY) {
		finishX = finX;
		finishY = finY;
		playingFinishAnim = true;
	}

	public void setAnimationCoef(float animationCoef) {
		this.animationCoef = animationCoef;
	}

	@Override
	public boolean isDeadly() {
		return false;
	}

	@Override
	public void collidedWithMainParticle() {
	}

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public void collidedWithStructure() {
	}

	@Override
	public boolean isTile() {
		return false;
	}

	@Override
	public boolean isTrampoline() {
		return false;
	}

	public Body getBody() {
		return body;
	}
	
	
}
