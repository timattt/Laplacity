package steelUnicorn.laplacity.ui.mainmenu.tabs;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Класс родитель вкладок в главном меню.
 * Во вкладке сверху добавляется название с помощью метода addDescription
 */
public class MainMenuTab  extends Table {
    protected static float tabSpace = UI_WORLD_HEIGHT * 0.03f;

    public MainMenuTab() {
        setName("tab");
    }

    protected void addDescription(String name, Skin skin) {
        addDescription(name, skin, Color.BLACK);
    }

    protected void addDescription(String name, Skin skin, Color color) {
        Label description = new Label(name, skin);
        description.setName("tabDescription");
        description.setColor(color);
        add(description).space(tabSpace);
    }
}
