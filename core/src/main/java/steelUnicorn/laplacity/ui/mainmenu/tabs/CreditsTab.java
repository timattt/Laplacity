package steelUnicorn.laplacity.ui.mainmenu.tabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/**
 * Класс с описанием компании в главном меню.
 */
public class CreditsTab extends MainMenuTab {
    public CreditsTab(Skin skin) {
        super();

        addDescription("Credits:", skin);
        row();
        addCredits(skin);
    }

    private void addCredits(Skin skin) {
        Label label = new Label("Made by Steel Unicorn\n"
                + "steel-unicorn.org", skin);
        label.setAlignment(Align.center);
        label.setColor(Color.BLACK);
        label.setName("credits_label");
        add(label);
    }
}
