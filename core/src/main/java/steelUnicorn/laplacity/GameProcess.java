package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.BackgroundRenderer;
import steelUnicorn.laplacity.field.graphics.DensityRenderer;
import steelUnicorn.laplacity.field.graphics.ParticlesRenderer;
import steelUnicorn.laplacity.field.graphics.TilesRenderer;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.gameModes.GameMode;
import steelUnicorn.laplacity.particles.ChargedParticle;
import steelUnicorn.laplacity.particles.ControllableElectron;
import steelUnicorn.laplacity.particles.HitController;
import steelUnicorn.laplacity.ui.GameInterface;
import steelUnicorn.laplacity.utils.Settings;

/**
 * Основной класс функциями и переменными игрового процесса.
 * А именно: основные функции цикла игры, регистрация и удаление новых объектов,
 * изменение игрового режима и окончание уровня.
 * @author timat
 *
 */
public class GameProcess {
	
	// LEVEL PROPERTIES
	//========================================================================================
	public static int levelNumber;
	public static int sectionNumber;
	public static GameMode currentGameMode;
	//========================================================================================
	
	
	// OBJECTS
	//========================================================================================	
	// Main electron
	public static ControllableElectron mainParticle;
	
	// Other particles
	public static final Array<ChargedParticle> particles = new Array<ChargedParticle>();
	
	// lights
	private static final Array<PointLight> lights = new Array<PointLight>();
	//========================================================================================
	
	
	// INGAME STUFF
	//========================================================================================
	// Stage, world, ui
	private static World levelWorld;
	private static GameInterface gameUI;
	public static SpriteBatch gameBatch;
	public static SpriteCache gameCache;
	private static RayHandler rayHandler;
	
	// Debug
	private static Box2DDebugRenderer debugRend;
	
	// Hits
	private static final HitController hitController = new HitController();
	
	// Time
	public static long startTime = 0;
	
	// After hit
	public static boolean justHitted;
	public static boolean justFinished;
	//========================================================================================
	
	
	// CONSTANTS
	//========================================================================================
	// PARTICLES
	public static final float PARTICLE_CHARGE = 300f;
	public static final float PARTICLE_SIZE = 2f;
	public static final float CAT_SIZE = 4f;
	public static final float PARTICLE_MASS = 1f;
	public static final float DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER = 1f;
	public static final long PARTICLE_TEXTURES_UPDATE_DELTA = 80;
	
	// BRUSHES
	public static final float BRUSH_RADIUS = 5f;
	public static final float MAX_DENSITY = 5f;
	public static final double DIRICHLET_SPRAY_TILE_PROBABILITY = 0.2;
	
	// TRAJECTORY
	public static final int TRAJECTORY_POINTS = 50;
	public static final int STEPS_PER_POINT = 2;
	public static final float TRAJECTORY_VELOCITY_MULTIPLIER = 2f;
	
	// SCORE
	public static final float SCORE_PER_PARTICLE = 1f;
	public static final float SCORE_PER_DENSITY_UNIT = 0.01f;
	public static final float MAX_SCORE = 100f;
	
	// PHYSICS
	public static final float PHYSICS_TIME_STEP = 1f/60f;
	public static final int VELOCITY_STEPS = 1;
	public static final int POSITION_STEPS = 3;
	public static final int RAYS_COUNT = 500;
	
	// OBJECTS
	public static final long MOVING_WALL_CYCLE_TIME = 3000;
	public static final long BLADES_ROTATION_TIME = 3000;
	public static final float BLADES_THICKNESS_FACTOR = 0.1f;
	public static final float DISSIPATIVE_ACCELERATION_FACTOR = 100f;
	public static final float DISSIPATIVE_MODERATION_FACTOR = -30f;
	public static final int FLIMSY_STRUCTURE_START_DURABILITY = 3;
	
	// LIGHT
	public static final float AMBIENT_INTENSITY = 0.9f;
	public static final float CAT_LIGHT_DISTANCE = 2*CAT_SIZE;
	public static final float PARTICLE_LIGHT_DISTANCE = 2*PARTICLE_SIZE;
	public static final float RED_LED_LIGHT_DISTANCE = 15f * PARTICLE_SIZE;
	public static final float SOFTNESS_FACTOR = 0.5f;
	//========================================================================================
	
	
	// GAME LOOP
	//========================================================================================	
	public static void initLevel(Texture level) {
		disposeLevel();

		game.getScreenManager().pushScreen(nameLoadingScreen, nameSlideIn);
		
		Gdx.app.log("gameProcess", "level init started: " +
				"section "+ sectionNumber + "; level " + levelNumber);
		levelWorld = new World(Vector2.Zero, false);
		currentGameMode = GameMode.NONE;
		debugRend = new Box2DDebugRenderer();
		gameUI = new GameInterface(guiViewport);
		gameBatch = new SpriteBatch();
		inputMultiplexer.setProcessors(gameUI, new GestureDetector(gameUI));
		gameCache = new SpriteCache(8000, true);
		RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(levelWorld);
		rayHandler.setAmbientLight(AMBIENT_INTENSITY);
		rayHandler.setAmbientLight(AMBIENT_INTENSITY, AMBIENT_INTENSITY, AMBIENT_INTENSITY, 1f);
		levelWorld.setContactListener(hitController);
		
		LaplacityField.initField(level);
		DensityRenderer.init();
		TrajectoryRenderer.init();
		TilesRenderer.init();
		BackgroundRenderer.init();
		
		mainParticle = new ControllableElectron(LaplacityField.electronStartPos.x, LaplacityField.electronStartPos.y);
		
		CameraManager.setToMainParticle();
	}
	
	public static void updateLevel(float delta) {
		if (levelWorld == null) {
			return;
		}
		
		gameBatch.setProjectionMatrix(CameraManager.camMat());
		rayHandler.setCombinedMatrix(CameraManager.getCamera());
		
		// render
		//---------------------------------------------
		BackgroundRenderer.render();
		TrajectoryRenderer.render();
		TilesRenderer.render();
		DensityRenderer.render();
		LaplacityField.renderStructuresCached(currentGameMode == GameMode.FLIGHT ? TimeUtils.millis() - startTime : 0);
		
		gameBatch.begin();
		LaplacityField.renderStructuresBatched(currentGameMode == GameMode.FLIGHT ? TimeUtils.millis() - startTime : 0);
		ParticlesRenderer.render(delta);
		gameBatch.end();
		
		if (Settings.isLightingEnabled()) {
			rayHandler.updateAndRender();
		}
		
		gameUI.draw();
		//debugRend.render(levelWorld, CameraManager.camMat());
		//---------------------------------------------
		
		// update
		//---------------------------------------------
		CameraManager.update(delta);
		currentGameMode.update();
		gameUI.act(delta);
		if (currentGameMode == GameMode.FLIGHT) {
			levelWorld.step(PHYSICS_TIME_STEP, VELOCITY_STEPS, POSITION_STEPS);
		}
		
		for (PointLight pl : lights) {
			pl.update();
		}
		//---------------------------------------------
		
		// hits
		//---------------------------------------------
		if (justHitted) {
			changeGameMode(GameMode.NONE);
			justHitted = false;
		}
		if (justFinished) {
			levelFinished();
			justFinished = false;
		}
		//---------------------------------------------
	}
	
	public static void disposeLevel() {
		Gdx.app.log("gameProcess", "level cleanup started");
		BackgroundRenderer.cleanup();
		LaplacityField.cleanupStructures();
		TrajectoryRenderer.cleanup();
		DensityRenderer.cleanup();
		TilesRenderer.cleanup();
		if (rayHandler != null) {
			rayHandler.dispose();
			rayHandler = null;
		}
		if (levelWorld != null) {
			levelWorld.dispose();
			levelWorld = null;
		}
		if (debugRend != null) {
			debugRend.dispose();
			debugRend = null;
		}
		if (gameUI != null) {
			gameUI.dispose();
			gameUI = null;
		}
		particles.clear();
		currentGameMode = GameMode.NONE;
		mainParticle = null;
		if (gameCache != null) {
			gameCache.dispose();
			gameCache = null;
		}
		lights.clear();
	}

	public static void clearLevel() {
		Gdx.app.log("gameProcess", "clearing level...");
		changeGameMode(GameMode.NONE);
		mainParticle.setSlingshot(0.0f, 0.0f);
		mainParticle.resetToStartPosAndStartVelocity();
		
		Array<ChargedParticle> tmp = new Array<ChargedParticle>();
		tmp.addAll(particles);
		for (ChargedParticle cp : tmp) {
			deleteStaticParticle(cp);
		}
		particles.clear();
		
		LaplacityField.clearElectricField();
		DensityRenderer.updateDensity();
		TrajectoryRenderer.updateTrajectory();
		Gdx.app.log("gameProcess", "level cleared!");
	}
	//========================================================================================
	
	public static void changeGameMode(GameMode mode) {
		boolean nowFlight = mode == GameMode.FLIGHT;
		boolean wasFlight = currentGameMode == GameMode.FLIGHT;
		
		currentGameMode.replaced();
		currentGameMode = mode;

		if ((nowFlight) && (wasFlight)) {
			LaplacityField.resetStructures();
			mainParticle.resetToStartPosAndStartVelocity();
			mainParticle.makeParticleMoveWithStartVelocity();
			startTime = TimeUtils.millis();
		} else {
			if (nowFlight) {
				startTime = TimeUtils.millis();
				FieldCalculator.calculateFieldPotential(LaplacityField.tiles);
				mainParticle.makeParticleMoveWithStartVelocity();
			}
		
			if (wasFlight) {
				mainParticle.resetToStartPosAndStartVelocity();
				LaplacityField.resetStructures();
			}
		}

		gameUI.updateCurMode();
	}
	
	public static Body registerPhysicalObject(BodyDef bodydef) {
		return levelWorld.createBody(bodydef);
	}
	
	public static Joint registerJoint(JointDef def) {
		return levelWorld.createJoint(def);
	}
	
	public static void deleteJoint(Joint j) {
		levelWorld.destroyJoint(j);
	}
	
	public static void deletePhysicalObject(Body body) {
		if (body != null)
			levelWorld.destroyBody(body);
	}
	
	public static void addStaticParticle(ChargedParticle part) {
		EmptyTile tl = LaplacityField.getTileFromWorldCoords(part.getX(), part.getY());
		if (tl != null && tl.isAllowDensityChange()) {
			tl.addInvisibleDensity(part.getCharge()*DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER);
			particles.add(part);
		} else {
			deletePhysicalObject(part.getBody());
			deletePointLight(part.getPointLight());
		}
	}
	
	public static void deleteStaticParticle(ChargedParticle part) {
		deletePhysicalObject(part.getBody());
		EmptyTile tl = LaplacityField.getTileFromWorldCoords(part.getX(), part.getY());
		if (tl != null) {
			tl.addInvisibleDensity(-part.getCharge()*DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER);
		}
		deletePointLight(part.getPointLight());
		if (!particles.removeValue(part, false)) {
			throw new RuntimeException("Not deleted!");
		}
	}
	
	public static boolean moveStaticParticle(ChargedParticle part, float x, float y) {
		EmptyTile from = LaplacityField.getTileFromWorldCoords(part.getX(), part.getY());
		EmptyTile to = LaplacityField.getTileFromWorldCoords(x, y);
		
		if (to == null || from == null || !to.isAllowDensityChange()) {
			return false;
		}
		
		from.addInvisibleDensity(-part.getCharge()*DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER);
		part.setPosition(x, y);
		to.addInvisibleDensity(part.getCharge()*DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER);
			
		return true;
	}
	
	public static PointLight registerPointLight(float x, float y, Color col, float rad) {
		PointLight pl = new PointLight(rayHandler, RAYS_COUNT, col, rad, x, y);
		pl.setSoftnessLength(rad*SOFTNESS_FACTOR);
		pl.setSoft(true);
		lights.add(pl);
		return pl;
	}
	
	public static PointLight registerPointLight(float x, float y, Color col, float rad, boolean isStatic) {
		PointLight pl = new PointLight(rayHandler, RAYS_COUNT, col, rad, x, y);
		pl.setStaticLight(isStatic);
		pl.setSoftnessLength(rad * SOFTNESS_FACTOR);
		lights.add(pl);
		return pl;
	}
	
	public static void deletePointLight(PointLight pl) {
		pl.remove();
		if (!lights.removeValue(pl, false)) {
			throw new RuntimeException("Light is not removed!");
		}
	}
	
	public static void levelFinished() {
		changeGameMode(GameMode.NONE);
		Gdx.app.log("game process", "Level finished");
		
		Globals.winScreen.loadWinScreen(calculateScore());
		Globals.game.getScreenManager().pushScreen(nameWinScreen, null);
	}
	
	public static int calculateScore() {
		float dens = 0;
		
		for (int x = 0; x < LaplacityField.fieldWidth; x++) {
			for (int y = 0; y < LaplacityField.fieldHeight; y++) {
				dens += Math.abs(LaplacityField.tiles[x][y].getTotalChargeDensity()) * SCORE_PER_DENSITY_UNIT;
			}
		}
		
		float part = particles.size * SCORE_PER_PARTICLE;
		
		return (int) (0.5f * MAX_SCORE / (1f + dens) + 0.5f * MAX_SCORE / (part + 1f));
	}
	
}
