package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.core.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.BackgroundRenderer;
import steelUnicorn.laplacity.field.graphics.DensityRenderer;
import steelUnicorn.laplacity.field.graphics.TilesRenderer;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.gameModes.GameMode;
import steelUnicorn.laplacity.particles.ChargedParticle;
import steelUnicorn.laplacity.particles.ControllableElectron;
import steelUnicorn.laplacity.particles.HitController;
import steelUnicorn.laplacity.ui.GameInterface;

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
	public static GameMode currentGameMode;
	//========================================================================================
	
	
	// OBJECTS
	//========================================================================================	
	// Main electron
	public static ControllableElectron mainParticle;
	
	// Other particles
	public static final Array<ChargedParticle> particles = new Array<ChargedParticle>();
	//========================================================================================
	
	
	// INGAME STUFF
	//========================================================================================
	// Stage, world, ui
	private static Stage levelStage;
	private static World levelWorld;
	private static GameInterface gameUI;
	
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
	public static final float ELECTRON_SIZE = 2f;
	public static final float PROTON_SIZE = 2f;
	public static final float PARTICLE_MASS = 1f;
	public static final float DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER = 1f;
	
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
	
	// OBJECTS
	public static final long MOVING_WALL_CYCLE_TIME = 3000;
	public static final long BLADES_ROTATION_TIME = 4000;
	public static final float BLADES_THICKNESS_FACTOR = 0.1f;
	public static final float DISSIPATIVE_ACCELERATION_FACTOR = 100f;
	public static final float DISSIPATIVE_MODERATION_FACTOR = -30f;
	public static final int FLIMSY_STRUCTURE_START_DURABILITY = 1;
	//========================================================================================
	
	
	// GAME LOOP
	//========================================================================================	
	public static void initLevel(Texture level) {
		disposeLevel();
		
		Gdx.app.log("gameProcess", "level init started");
		levelStage = new Stage(gameViewport);
		levelWorld = new World(Vector2.Zero, false);
		currentGameMode = GameMode.NONE;
		debugRend = new Box2DDebugRenderer();
		gameUI = new GameInterface(guiViewport);
		inputMultiplexer.setProcessors(gameUI, new GestureDetector(gameUI));
		
		levelWorld.setContactListener(hitController);
		
		LaplacityField.initField(level);
		DensityRenderer.init();
		TrajectoryRenderer.init();
		TilesRenderer.init();
		BackgroundRenderer.init();
		
		mainParticle = new ControllableElectron(LaplacityField.electronStartPos.x, LaplacityField.electronStartPos.y);
		
		camera.position.x = SCREEN_WORLD_WIDTH / 2;
		camera.position.y = SCREEN_WORLD_HEIGHT / 2;
		camera.update();
	}
	
	public static void updateLevel(float delta) {
		if (levelStage == null) {
			return;
		}
		
		BackgroundRenderer.render(levelStage.getBatch());
		
		if (currentGameMode == GameMode.FLIGHT) {
			LaplacityField.updateStructures(TimeUtils.millis() - startTime);
		} else {
			LaplacityField.updateStructures(0);
		}
		
		currentGameMode.update();
		TrajectoryRenderer.render();
		levelStage.draw();
		TilesRenderer.render();

		levelStage.act();
		DensityRenderer.render(levelStage.getBatch());		
		gameUI.draw();
		gameUI.act(delta);
		//debugRend.render(levelWorld, Globals.camera.combined);

		if (currentGameMode == GameMode.FLIGHT) {
			levelWorld.step(PHYSICS_TIME_STEP, VELOCITY_STEPS, POSITION_STEPS);
		}		
		if (justHitted) {
			changeGameMode(GameMode.NONE);
			justHitted = false;
		}
		if (justFinished) {
			levelFinished();
			justFinished = false;
		}
	}
	
	public static void disposeLevel() {
		Gdx.app.log("gameProcess", "level cleanup started");
		BackgroundRenderer.cleanup();
		LaplacityField.cleanupStructures();
		TrajectoryRenderer.cleanup();
		DensityRenderer.cleanup();
		TilesRenderer.cleanup();
		
		if (levelStage != null) {
			levelStage.dispose();
			levelStage = null;
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
	}

	public static void clearLevel() {
		Gdx.app.log("gameProcess", "clearing level...");
		changeGameMode(GameMode.NONE);
		mainParticle.setSlingshot(0.0f, 0.0f);
		mainParticle.resetToStartPosAndStartVelocity();
		for (ChargedParticle particle : particles)
			deleteObject(particle, particle.getBody());
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

		gameUI.updateCurModeImg();
	}

	public static void registerObject(Actor act) {
		levelStage.addActor(act);
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
	
	public static void deleteObject(Actor act, Body body) {
		if (body != null)
			levelWorld.destroyBody(body);
		if (act != null)
			act.remove();
	}
	
	public static void addStaticParticle(ChargedParticle part) {
		EmptyTile tl = LaplacityField.getTileFromWorldCoords(part.getX(), part.getY());
		if (tl != null && tl.isAllowDensityChange()) {
			tl.addInvisibleDensity(part.getCharge()*DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER);
			particles.add(part);
		} else {
			deleteObject(part, part.getBody());
		}
	}
	
	public static void deleteStaticParticle(ChargedParticle part) {
		deleteObject(part, part.getBody());
		EmptyTile tl = LaplacityField.getTileFromWorldCoords(part.getX(), part.getY());
		if (tl != null) {
			tl.addInvisibleDensity(-part.getCharge()*DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER);
		}
		if (!particles.removeValue(part, false)) {
			throw new RuntimeException("Not deleted!");
		}
	}
	
	public static void levelFinished() {
		changeGameMode(GameMode.NONE);
		Gdx.app.log("game process", "Level finished");
		
		Globals.winScreen.loadWinScreen(calculateScore());
		Globals.game.getScreenManager().pushScreen(nameWinScreen, nameSlideIn);
	}
	
	public static float calculateScore() {
		float dens = 0;
		
		for (int x = 0; x < LaplacityField.fieldWidth; x++) {
			for (int y = 0; y < LaplacityField.fieldHeight; y++) {
				dens += Math.abs(LaplacityField.tiles[x][y].getTotalChargeDensity()) * SCORE_PER_DENSITY_UNIT;
			}
		}
		
		float part = particles.size * SCORE_PER_PARTICLE;
		
		return 0.5f * MAX_SCORE / (1f + dens) + 0.5f * MAX_SCORE / (part + 1f);
	}
	
}
