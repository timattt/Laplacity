package steelUnicorn.laplacity;

import static steelUnicorn.laplacity.Globals.*;

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

import steelUnicorn.laplacity.field.FieldPotentialCalculator;
import steelUnicorn.laplacity.field.GameMode;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.tiles.FieldTile;
import steelUnicorn.laplacity.particles.ChargedParticle;
import steelUnicorn.laplacity.particles.Electron;
import steelUnicorn.laplacity.particles.HitController;

public class GameProcess {

	// OBJECTS
	//========================================================================================
	// Tilemap
	public static LaplacityField field;
	
	// Main electron
	public static Electron mainParticle;
	
	// Other particles
	public static final Array<ChargedParticle> particles = new Array<ChargedParticle>();
	//========================================================================================
	
	// Stage and world
	private static Stage levelStage;
	private static World levelWorld;

	// Mode
	public static GameMode currentGameMode;
	
	// Debug
	public static Box2DDebugRenderer debugRend;
	
	// Hits
	private static final HitController hitController = new HitController();
	
	// Controlls
	public static final LaplacityIngameController controller = new LaplacityIngameController();
	
	// Constants
	public static final float ELECTRON_CHARGE = -10f;
	public static final float PROTON_CHARGE = 10f;
	
	public static final float ELECTRON_SIZE = 2f;
	public static final float PROTON_SIZE = 2f;
	
	// BRUSHES
	public static final float DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER = 1f;
	public static final float BRUSH_RADIUS = 5f;
	public static final float MAX_DENSITY = 5f;
	public static final double DIRICHLET_SPRAY_TILE_PROBABILITY = 0.2;
	
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
		
		camera.position.setZero();
		camera.update();
	}
	
	public static void updateLevel(float delta) {
		levelStage.draw();
		//debugRend.render(levelWorld, Globals.camera.combined);
		
		if (currentGameMode == GameMode.flight) {
			levelWorld.step(delta, 6, 2);
		}
		levelStage.act();
	}
	
	public static void disposeLevel() {
		levelStage.dispose();
		field = null;
	}
	//========================================================================================
	
	public static boolean isPlaying() {
		return field != null;
	}
	
	private static void loadObjects(Texture level) {
		field.init(level);
		levelStage.addActor(field);
		
		findMainParticleStartPos(level, TMP1);
		mainParticle = new Electron(TMP1.x, TMP1.y);
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
		if (mode == GameMode.flight) {
			FieldPotentialCalculator.calculateFieldPotential(GameProcess.field.getTiles());
		}
		
		currentGameMode = mode;
	}
	
	public static Body registerPhysicalObject(Actor act, BodyDef bodydef) {
		levelStage.addActor(act);
		return levelWorld.createBody(bodydef);
	}
	
	public static void deleteObject(Actor act, Body body) {
		levelWorld.destroyBody(body);
		act.remove();
	}
	
	public static void addStaticParticle(ChargedParticle part) {
		particles.add(part);
		FieldTile tl = field.getTileFromWorldCoords(part.getX(), part.getY());
		if (tl != null) {
			tl.setChargeDensity(part.getCharge()*DELTA_FUNCTION_POINT_CHARGE_MULTIPLIER);
		}
	}
	
}
