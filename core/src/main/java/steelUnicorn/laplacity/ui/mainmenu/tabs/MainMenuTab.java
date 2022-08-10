package steelUnicorn.laplacity.ui.mainmenu.tabs;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import steelUnicorn.laplacity.ui.mainmenu.MainMenu;

/**
 * Класс родитель вкладок в главном меню.
 * Во вкладке сверху добавляется название с помощью метода addDescription
 */
public class MainMenuTab  extends Table {
    protected static float tabSpace = UI_WORLD_HEIGHT * 0.03f;

    public MainMenuTab() {
        setName("menuTab");
    }

    protected void addDescription(String name, Skin skin) {
        addDescription(name, skin, Color.WHITE);
    }

    protected void addDescription(String name, Skin skin, Color color) {
        Label description = new Label(name, skin);
        description.setName("tabDescription");
        description.setColor(color);
        add(description).space(tabSpace);
    }

    protected Cell<TextButton> addReturnButton(Skin skin, ChangeListener listener) {
        TextButton btn = new TextButton("Menu", skin);
        btn.addListener(listener);
        return add(btn).size(MainMenu.menuWidth, MainMenu.menuHeight);
    }
}
