package steelUnicorn.laplacity.ui.mainmenu.tabs;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Класс родитель вкладок в главном меню.
 * Во вкладке сверху добавляется название с помощью метода addDescription
 */
public class MainMenuTab  extends Table {
    protected static float tabSpace = UI_WORLD_HEIGHT * 0.03f;
    protected static float descriptionScale = 1.2f;

    public MainMenuTab() {
        setName("menuTab");
    }

    protected Cell<ImageButton> addReturnButton(ChangeListener listener) {
        ImageButton btn = new ImageButton(TEXSKIN, "Home");
        btn.addListener(listener);
        Gdx.app.log("Return", "Created");
        return add(btn);
    }
}
