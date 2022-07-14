package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.Globals;

public class GameInterface extends Stage {
	public GameInterface(Viewport viewport) {
		super(viewport);
		
		createInterface();
	}
	
	private void createInterface() {
		Table root = new Table();
		root.setFillParent(true);
		root.align(Align.right);
		addActor(root);
		
		TextureAtlas icons = Globals.assetManager.get("ui/gameicons/icons.atlas", TextureAtlas.class);
		
		Drawable img = new TextureRegionDrawable(icons.findRegion("return"));
		ImageButton btn = new ImageButton(img);
		btn.setName("return");
		root.add(btn);
		root.row();
		
		
		img = new TextureRegionDrawable(icons.findRegion("reload"));
		btn = new ImageButton(img);
		btn.setName("reload");
		root.add(btn);
		root.row();
		
		img = new TextureRegionDrawable(icons.findRegion("start"));
		btn = new ImageButton(img);
		btn.setName("start");
		root.add(btn);
		root.row();
		
		img = new TextureRegionDrawable(icons.findRegion("eraser"));
		btn = new ImageButton(img);
		btn.setName("eraser");
		root.add(btn);
		root.row();
		
		img = new TextureRegionDrawable(icons.findRegion("electron"));
		btn = new ImageButton(img);
		btn.setName("electron");
		root.add(btn);
		root.row();
		
		img = new TextureRegionDrawable(icons.findRegion("proton"));
		btn = new ImageButton(img);
		btn.setName("proton");
		root.add(btn);
		root.row();
		
		img = new TextureRegionDrawable(icons.findRegion("dirich"));
		btn = new ImageButton(img);
		btn.setName("dirich");
		root.add(btn);
		root.row();
	}
}
