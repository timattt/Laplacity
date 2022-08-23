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
import steelUnicorn.laplacity.cat.Cat;
import steelUnicorn.laplacity.chargedParticles.ChargedParticle;
import steelUnicorn.laplacity.chargedParticles.HitController;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.graphics.BackgroundRenderer;
import steelUnicorn.laplacity.field.graphics.DensityRenderer;
import steelUnicorn.laplacity.field.graphics.ParticlesRenderer;
import steelUnicorn.laplacity.field.graphics.TilesRenderer;
import steelUnicorn.laplacity.field.graphics.TrajectoryRenderer;
import steelUnicorn.laplacity.field.physics.FieldCalculator;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.gameModes.GameMode;
import steelUnicorn.laplacity.particles.ParticlesManager;
import steelUnicorn.laplacity.ui.GameInterface;
import steelUnicorn.laplacity.utils.LevelParams;
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
	private static int totalStarts = 0;

	public static LevelParams levelParams;
	private static int currentlyStarsCollected;
	public static final int MAX_STAR = 3;
	//========================================================================================
	
	
	// OBJECTS
	//========================================================================================	
	// Main electron
	public static Cat cat;
	
	// Other particles
	public static final Array<ChargedParticle> particles = new Array<ChargedParticle>();
	
	// lights
	private static final Array<PointLight> lights = new Array<PointLight>();
	//========================================================================================
	
	
	// INGAME STUFF
	//========================================================================================
	// Stage, world, ui
	private static World levelWorld;
	public static GameInterface gameUI;
	public static SpriteBatch gameBatch;
	public static SpriteCache gameCache;
	private static RayHandler rayHandler;
	
	// Debug
	private static Box2DDebugRenderer debugRend;
	
	// Hits
	private static final HitController hitController = new HitController();
	
	// Time
	public static long startTime = 0;
	private static float frameAccumulator = 0f;
	/*
	 * Время, используемое для физических расчётов
	 * При запуске полёта, который происходит в 
	 * changeGameMode currentTime и startTime равны
	 * Далее currentTime обновляется каждый фрейм внутри физической
	 * части цикла рендеринга (под ифом)
	 * newTime используется как промежуточная переменная для
	 * вычисления frameTime (см метод update)
	 * frameTime - переменная, аналогичная delta,
	 * но для физических расчётов, а не для графики.
	 * Нужна для того, чтобы отсеять все возможные фризы перед запуском
	 * физики.
	 */
	private static long currentTime = 0;
	/*
	 * Интерполяционный коэффициент показывает, насколько близко
	 * состояние на экране к текущему физическому состоянию.
	 * Коэффициент, равный 1 означает, что состояние на экране и
	 * в мире (Box2D.World) совпадают
 	 */
	public static float interpCoeff = 1f;
	
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
	public static final float DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER = 1f;
	public static final long PARTICLE_TEXTURES_UPDATE_DELTA = 80;
	public static final long DEAD_ANIMATION_TIME = 1000;
	public static final long FINISH_ANIMATION_TIME = 1200;
	
	// BRUSHES
	public static final float BRUSH_RADIUS = 5f;
	public static final float MAX_DENSITY = 5f;
	public static final float BRUSH_DENSITY_POWER = 0.05f * MAX_DENSITY;
	
	// TRAJECTORY
	public static final int TRAJECTORY_POINTS = 30;
	public static final int STEPS_PER_POINT = 2;
	public static final float TRAJECTORY_VELOCITY_MULTIPLIER = 3f;
	public static final float SLINGSHOT_MAX_LENGTH = 22f;
	
	// PHYSICS
	public static final float TRAJECTORY_TIME_STEP = 1f/60f; // используется при расчёте траектории для подсказки
	public static final int VELOCITY_STEPS = 4;
	public static final int POSITION_STEPS = 2;
	public static final float SIMULATION_TIME_STEP = 1f/60f; // используется непосредственно в игре
	public static final int RAYS_COUNT = 500;
	
	// OBJECTS
	public static final long MOVING_WALL_CYCLE_TIME = 3000;
	public static final long BLADES_ROTATION_TIME = 3000;
	public static final float BLADES_THICKNESS_FACTOR = 0.1f;
	public static final float CELERATION_FACTOR = 5000f;
	public static final int FLIMSY_STRUCTURE_START_DURABILITY = 3;
	public static final float STAR_SIZE = 2f;
	public static final long STAR_ROTATION_TIME = 100;
	public static final long TILE_ANIMATION_DELAY = 100;
	
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
		
		Gdx.app.log("gameProcess", "level init started: " + "section "+ sectionNumber + "; level " + levelNumber);
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
		
		cat = new Cat();
		totalStarts = 0;
		
		/*
		 * При инициализации уровня установить коэффициент в 1f
		 * во избежание интерференции начального положения тел в Box2D.World
		 * с начальными значениями сохранённых координат тел,
		 * записанных в экземплярах классов этих тел
		 */
		interpCoeff = 1f;
		FieldCalculator.resetPotential();
		CameraManager.setToMainParticle();
	}
	
	public static void updateLevel(float delta) {
		if (levelWorld == null) {
			return;
		}
		
		// update
		//---------------------------------------------
		ParticlesManager.updateParticleSystem(delta);
		currentGameMode.update();
		gameUI.act(delta);
		if (currentGameMode == GameMode.FLIGHT) {
			long newTime = TimeUtils.millis();
			float frameTime = ((float) (newTime - currentTime)) / 1000f;
			currentTime = newTime;
			frameAccumulator += frameTime;
			while (frameAccumulator >= 0) {
				saveCurrentState();
				cat.updatePhysics(frameTime);
				LaplacityField.updateStructuresPhysics(currentTime - startTime);
				levelWorld.step(SIMULATION_TIME_STEP, VELOCITY_STEPS, POSITION_STEPS);
				frameAccumulator -= SIMULATION_TIME_STEP;
			}
			interpCoeff = 1 + (frameAccumulator / SIMULATION_TIME_STEP);
			CameraManager.update(frameTime);
		} else {
			CameraManager.update(delta);
			if (FieldCalculator.isCalculating()) {
				FieldCalculator.iterate();
				TrajectoryRenderer.updateTrajectory();
			}
		}
		//---------------------------------------------
		
		// ADS
		if (Globals.game.interstitialJustShown) {
			Globals.game.interstitialJustShown = false;
			gameUI.catFI.update(catFood.interstitialShown());
		}
		if (Globals.game.rewardedJustShown) {
			Globals.game.rewardedJustShown = false;
			gameUI.catFI.update(catFood.rewardedShown());
		}
		
		gameBatch.setProjectionMatrix(CameraManager.camMat());
		rayHandler.setCombinedMatrix(CameraManager.getCamera());
		
		// render
		//---------------------------------------------
		BackgroundRenderer.render();
		TilesRenderer.render();
		DensityRenderer.render();
		LaplacityField.renderStructuresCached(currentGameMode == GameMode.FLIGHT ? currentTime - startTime : 0);
		
		gameBatch.begin();
		LaplacityField.renderStructuresBatched(currentGameMode == GameMode.FLIGHT ? currentTime - startTime : 0);
		ParticlesRenderer.render(delta);
		gameBatch.end();
		
		TrajectoryRenderer.render();
		
		gameBatch.begin();
		ParticlesRenderer.render(delta);
		ParticlesManager.renderAllParticles();
		gameBatch.end();
		
		// light
		if (Settings.isLightingEnabled()) {
			rayHandler.updateAndRender();
		}
		
		gameBatch.begin();
		LaplacityField.renderStructuresBatchedForeground(currentGameMode == GameMode.FLIGHT ? currentTime - startTime : 0);
		gameBatch.end();
		
		gameUI.draw();
		//debugRend.render(levelWorld, CameraManager.camMat());
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

	/**
	 * Записать текущие физические координаты тел в тела,
	 * обладающие памятью. Необходимо вызвать при каждом обновлении мира (Box2D.World).
	 * Если положение Вашего тела подлежит расчёту средствами Box2D, убедитесь,
	 * что в данном методе Вы вызываете его метод сохранения состояния.
	 */
	private static void saveCurrentState() {
		cat.savePosition();
		LaplacityField.saveStructuresState();
	}
	
	public static void disposeLevel() {
		Gdx.app.log("gameProcess", "level cleanup started");
		totalStarts = 0;
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
		cat = null;
		if (gameCache != null) {
			gameCache.dispose();
			gameCache = null;
		}
		lights.clear();
	}

	public static void clearLevel() {
		Gdx.app.log("gameProcess", "clearing level...");
		changeGameMode(GameMode.NONE);
		cat.setSlingshot(0.0f, 0.0f);
		cat.resetToStartPosAndStartVelocity();
		
		Array<ChargedParticle> tmp = new Array<ChargedParticle>();
		tmp.addAll(particles);
		for (ChargedParticle cp : tmp) {
			deleteStaticParticle(cp);
		}
		particles.clear();
		
		LaplacityField.clearElectricField();
		FieldCalculator.resetPotential();
		DensityRenderer.updateDensity();
		TrajectoryRenderer.updateTrajectory();
		Gdx.app.log("gameProcess", "level cleared!");
	}
	//========================================================================================
	
	public static void changeGameMode(GameMode mode) {
		boolean nowFlight = mode == GameMode.FLIGHT;
		boolean wasFlight = currentGameMode == GameMode.FLIGHT;
		
		if ((nowFlight) && (wasFlight)) {
			LaplacityField.resetStructures();
			cat.resetToStartPosAndStartVelocity();
			cat.makeParticleMoveWithStartVelocity();
			startTime = TimeUtils.millis();
		} else {
			if (nowFlight) {
				totalStarts++;
				currentlyStarsCollected = 0;
				FieldCalculator.finishCalculation();;
				startTime = TimeUtils.millis();
				currentTime = startTime;
				cat.makeParticleMoveWithStartVelocity();
			}
		
			if (wasFlight) {
				cat.resetToStartPosAndStartVelocity();
				LaplacityField.resetStructures();
				// После полёта нужно вернуть интер
				interpCoeff = 1f;
			}
		}

		currentGameMode.replaced();
		currentGameMode = mode;
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
	
	private static boolean isParticleTooClose(ChargedParticle part) {
		for (ChargedParticle p : particles) {
			TMP1.set(p.getX(), p.getY()).sub(part.getX(), part.getY());
			if (TMP1.len2() < (p.getRadius() + part.getRadius()) * (p.getRadius() + part.getRadius())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean tryToAddStaticParticle(ChargedParticle part) {
		EmptyTile tl = LaplacityField.getTileFromWorldCoords(part.getX(), part.getY());
		if (tl != null && tl.isAllowDensityChange() && !isParticleTooClose(part)) {
			LaplacityAssets.playSound(LaplacityAssets.placeSound);
			tl.addInvisibleDensity(part.getCharge()*DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER);
			particles.add(part);
			return true;
		} else {
			deletePhysicalObject(part.getBody());
			deletePointLight(part.getPointLight());
			return false;
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
	
	public static boolean tryToMoveStaticParticle(ChargedParticle part, float x, float y) {
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
	
	public static void collectStar() {
		currentlyStarsCollected++;
		LaplacityAssets.playSound(LaplacityAssets.starSound);
	}
	
	public static void levelFinished() {
		Globals.game.sendLevelStats(levelNumber, sectionNumber, currentlyStarsCollected, particles.size, totalStarts);
		changeGameMode(GameMode.NONE);
		Gdx.app.log("game process", "Level finished");

		progress.levelFinished(sectionNumber, levelNumber, currentlyStarsCollected);
		levelsScreen.levelsTab.levelFinished(sectionNumber, levelNumber);
		Globals.winScreen.loadWinScreen(currentlyStarsCollected);
		Globals.game.getScreenManager().pushScreen(nameWinScreen, null);
	}
	
}
