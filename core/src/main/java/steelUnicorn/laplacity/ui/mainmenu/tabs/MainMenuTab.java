package steelUnicorn.laplacity.ui.mainmenu.tabs;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.LaplacityAssets.TEXSKIN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Вкладка главного меню
 * Во вкладке сверху добавляется кнопка вызода в главное меню с помощью addReturnButton
 */
public class MainMenuTab  extends Table {
    protected static float tabSpace = UI_WORLD_HEIGHT * 0.03f;
    protected static float descriptionScale = 1.2f;

    public MainMenuTab() {
        setName("menuTab");
    }

    /**
     * Метод добавляющий кнопку выхода в главное меню
     * @param listener обработчик событий
     * @return клетку с кнопкой
     */
    protected Cell<ImageButton> addReturnButton(ChangeListener listener) {
        ImageButton btn = new ImageButton(TEXSKIN, "Home");
        btn.addListener(listener);
        return add(btn);
    }
}
