package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.Globals;
import steelUnicorn.laplacity.field.GameMode;

/**
 * @brief Class that creates game ui and add listeners.
 *
 * When any icon clicked, GameProcess.currentGameMode change its state
 *
 */
public class GameInterface extends Stage {
	/**
	 * Constructor create Stage and interface
	 * @param viewport
	 */
	public GameInterface(Viewport viewport) {
		super(viewport);

		createInterface();
	}
	
	/**
	 * @brief Function creates interface buttons
	 *
	 * The order is
	 * <ol>
	 * 	<li>return</li>
	 * 	<li>reload</li>
	 * 	<li>flight</li>
	 * 	<li>eraser</li>
	 * 	<li>electrons</li>
	 * 	<li>protons</li>
	 * 	<li>dirichlet</li>
	 * </ol>
	 */
	private void createInterface() {
		Table root = new Table();
		root.setFillParent(true);
		root.align(Align.right);
		addActor(root);

		TextureAtlas icons = Globals.assetManager.get("ui/gameicons/icons.atlas", TextureAtlas.class);

		//reload and return buttons
		createIcon(icons, "return", root, new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("game ui", "return pressed");
				Globals.game.setScreen(Globals.mainMenuScreen);
			}
		});

		createIcon(icons, "reload", root, new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("game ui", "reload pressed");
				//TODO level reload
			}
		});

		//modes
		createModeIcon(icons, "flight", root, GameMode.flight);
		createModeIcon(icons, "eraser", root, GameMode.eraser);
		createModeIcon(icons, "electrons", root, GameMode.electrons);
		createModeIcon(icons, "protons", root, GameMode.protons);
		createModeIcon(icons, "dirichlet", root, GameMode.dirichlet);
	}

	/**
	 * Function that creates 1 icon button of game interface
	 *
	 * @param icons - TextureAtlas with icons in it
	 * @param name - name of TextureRegion in Atlas
	 * @param root - table where button is placed
	 * @param listener - listener that define buttons behaviour
	 */
	private void createIcon(TextureAtlas icons, String name, Table root, ChangeListener listener) {
		ImageButton btn = new ImageButton(new TextureRegionDrawable(icons.findRegion(name)));
		btn.setName(name);
		btn.addListener(listener);
		root.add(btn);
		root.row();
	}

	/**
	 * Function that creates mode button
	 *
	 * @param icons - Texture Atlas with icons of modes
	 * @param name - name of mode
	 * @param root - table where button is placed
	 * @param mode - mode that button enables
	 */
	private void createModeIcon(TextureAtlas icons, String name, Table root, GameMode mode) {
		createIcon(icons, name, root, new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameProcess.currentGameMode = mode;
				Gdx.app.log("game ui", GameProcess.currentGameMode.toString());
			}
		});
	}
}
