package steelUnicorn.laplacity.ui.mainmenu.tabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.mainmenu.MainMenu;

/**
 * Класс с описанием компании в главном меню.
 */
public class CreditsTab extends MainMenuTab {
    public CreditsTab(Skin skin) {
        super();

        addReturnButton(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                MainMenu stage = (MainMenu) CreditsTab.this.getStage();
                stage.returnMainMenu();
            }
        });
        row();
        addDescription("Credits:", skin);
        row();
        addCredits(skin);
    }

    private void addCredits(Skin skin) {
        Label label = new Label("Made by Steel Unicorn\n"
                + "Steel-uni.com", skin);
        label.setAlignment(Align.center);
        label.setColor(Color.WHITE);
        label.setName("credits_label");
        add(label);
    }
}
