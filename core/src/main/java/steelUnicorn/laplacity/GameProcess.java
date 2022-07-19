package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import steelUnicorn.laplacity.field.DensityRenderer;
import steelUnicorn.laplacity.field.FieldPotentialCalculator;
import steelUnicorn.laplacity.field.GameMode;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.TrajectoryRenderer;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.particles.ChargedParticle;
import steelUnicorn.laplacity.particles.ControllableElectron;
import steelUnicorn.laplacity.particles.HitController;
import steelUnicorn.laplacity.ui.GameInterface;

public class GameProcess {
	public static int levelNumber;
	public static int MAX_LEVEL;
	// OBJECTS
	//========================================================================================
	// Tilemap
	public static LaplacityField field;
	
	// Main electron
	public static ControllableElectron mainParticle;
	
	// Other particles
	public static final Array<ChargedParticle> particles = new Array<ChargedParticle>();
	//========================================================================================
	
	// Stage and world and ui
	private static Stage levelStage;
	private static World levelWorld;
	public static GameInterface gameUI;
	public static InputMultiplexer inputMultiplexer;

	// Mode
	public static GameMode currentGameMode;
	
	// Debug
	public static Box2DDebugRenderer debugRend;
	
	// Hits
	private static final HitController hitController = new HitController();
	
	// Constants
	public static final float ELECTRON_CHARGE = -10f;
	public static final float PROTON_CHARGE = 10f;
	
	public static final float ELECTRON_SIZE = 2f;
	public static final float PROTON_SIZE = 2f;
	
	public static final float PARTICLE_MASS = 1f;
	
	// BRUSHES
	public static final float DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER = 1f;
	public static final float BRUSH_RADIUS = 5f;
	public static final float MAX_DENSITY = 5f;
	public static final double DIRICHLET_SPRAY_TILE_PROBABILITY = 0.2;
	
	// TRAJECTORY
	public static final int TRAJECTORY_POINTS = 10;
	public static final float TRAJECTORY_STEP = 0.2f;
	
	// SCORE
	public static final float SCORE_PER_PARTICLE = 1f;
	public static final float SCORE_PER_DENSITY_UNIT = 0.01f;
	public static final float MAX_SCORE = 100f;
	
	// Methods
	// GAME LOOP
	//========================================================================================	
	public static void initLevel(Texture level) {
		levelStage = new Stage(gameViewport);
		levelWorld = new World(Vector2.Zero, false);
		currentGameMode = GameMode.none;
		field = new LaplacityField();
		debugRend = new Box2DDebugRenderer();
		levelWorld.setContactListener(hitController);
		
		loadObjects(level);
		
		camera.position.x = SCREEN_WORLD_WIDTH / 2;
		camera.position.y = SCREEN_WORLD_HEIGHT / 2;
		camera.update();
		
		DensityRenderer.init();
		TrajectoryRenderer.updateTrajectory();
	}
	
	public static void updateLevel(float delta) {
		TrajectoryRenderer.render();
		levelStage.draw();
		//debugRend.render(levelWorld, Globals.camera.combined);

		if (currentGameMode == GameMode.flight) {
			levelWorld.step(1f/60f, 6, 2);
		}
		
		levelStage.act();
		gameUI.draw();
		gameUI.act();
		
		DensityRenderer.render(levelStage.getBatch());
		

		if (hitController.isHitted()) {
			changeGameMode(GameMode.none);
		}
		if (hitController.isFinished()) {
			levelFinished();
		}
	}
	
	public static void disposeLevel() {
		DensityRenderer.cleanup();
		levelStage.dispose();
		levelWorld.dispose();
		debugRend.dispose();
		field = null;
		//gameUI.dispose();
	}
	//========================================================================================
	
	public static boolean isPlaying() {
		return field != null;
	}
	
	private static void loadObjects(Texture level) {
		levelStage.addActor(field);
		field.init(level);
		
		findMainParticleStartPos(level, TMP1);
		mainParticle = new ControllableElectron(TMP1.x, TMP1.y);
	}
	
	private static void findMainParticleStartPos(Texture tileMap, Vector2 res) {
		int fieldWidth = tileMap.getWidth();
		int fieldHeight = tileMap.getHeight();
		tileMap.getTextureData().prepare();
		Pixmap pxmap = tileMap.getTextureData().consumePixmap();
		
		for (int i = 0; i < fieldWidth; i++) {
			for (int k = 0; k < fieldHeight; k++) {
				int j = fieldHeight - k - 1;
				int c = pxmap.getPixel(i, k);
				
				// yellow
				if (c == -65281) {
					field.fromGridToWorldCoords(i, j, res);
					tileMap.getTextureData().disposePixmap();
					return;
				}
			}
		}

		tileMap.getTextureData().disposePixmap();
		
		throw new RuntimeException("No start position!");
	}
	
	public static void changeGameMode(GameMode mode) {
		boolean nowFlight = mode == GameMode.flight;
		boolean wasFlight = currentGameMode == GameMode.flight;
		
		currentGameMode = mode;
		
		if (nowFlight) {
			FieldPotentialCalculator.calculateFieldPotential(GameProcess.field.getTiles());
			mainParticle.startFlight();
		}
	
		if (wasFlight) {
			mainParticle.reset();
		}	
	}

	public static void registerObject(Actor act) {
		levelStage.addActor(act);
	}
	
	public static Body registerPhysicalObject(BodyDef bodydef) {
		return levelWorld.createBody(bodydef);
	}
	
	public static void deleteObject(Actor act, Body body) {
		if (body != null)
			levelWorld.destroyBody(body);
		if (act != null)
			act.remove();
	}
	
	public static void addStaticParticle(ChargedParticle part) {
		EmptyTile tl = field.getTileFromWorldCoords(part.getX(), part.getY());
		if (tl != null && tl.isAllowDensityChange()) {
			tl.setChargeDensity(part.getCharge()*DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER);
			particles.add(part);
		}
	}
	
	public static void deleteStaticParticle(ChargedParticle part) {
		part.delete();
		if (!particles.removeValue(part, false)) {
			throw new RuntimeException("Not deleted!");
		}
	}
	
	public static void levelFinished() {
		changeGameMode(GameMode.none);
		Gdx.app.log("game process", "Level finished");
		
		Globals.winScreen.clearStage();
		Globals.winScreen.buildStage(calculateScore());
		Globals.game.getScreenManager().pushScreen(nameWinScreen, nameSlideIn);
	}
	
	public static float calculateScore() {
		float dens = 0;
		
		for (int x = 0; x < field.getFieldWidth(); x++) {
			for (int y = 0; y < field.getFieldHeight(); y++) {
				dens += Math.abs(field.getTiles()[x][y].getChargeDensity()) * SCORE_PER_DENSITY_UNIT;
			}
		}
		
		float part = particles.size * SCORE_PER_PARTICLE;
		
		return 0.5f * MAX_SCORE / (1f + dens) + 0.5f * MAX_SCORE / (part + 1f);
	}
	
}
