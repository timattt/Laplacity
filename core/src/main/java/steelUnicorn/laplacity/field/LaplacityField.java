package steelUnicorn.laplacity.field;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;

public class LaplacityField extends Group {

	// Tiles
	private FieldTile[][] tiles;
	
	// Sizes
	private int fieldWidth;
	private int fieldHeight;
	
	public LaplacityField(Texture tileMap) {
		fieldWidth = tileMap.getWidth();
		fieldHeight = tileMap.getHeight();
		loadTilemap(tileMap);
	}
	
	private void loadTilemap(Texture tileMap) {
		// TODO load from texture
	}
	
	public void updateModeFlight() {
		
	}
	
	public void updateModeNone() {
		
	}
	
	public void updateModeDirichlet() {
		
	}
	
	public void updateModeEraser() {
		
	}

	public void updateModeProtons() {
		
	}
	
	public void updateModeElectrons() {
		
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
}
