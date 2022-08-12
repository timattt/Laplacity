package steelUnicorn.laplacity.ui.mainmenu.tabs;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Класс родитель вкладок в главном меню.
 * Во вкладке сверху добавляется название с помощью метода addDescription
 */
public class MainMenuTab  extends Table {
    protected static float tabSpace = UI_WORLD_HEIGHT * 0.03f;
    private static float returnBtnScale = 0.7f;

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

    protected Cell<Button> addReturnButton(ChangeListener listener) {
        Button btn = new Button(TEXSKIN.get("BackMenuBtn", Button.ButtonStyle.class));
        btn.addListener(listener);
        Gdx.app.log("Return", "Created");
        return add(btn).size(btn.getPrefWidth() * returnBtnScale,
                btn.getPrefHeight() * returnBtnScale);
    }
}
