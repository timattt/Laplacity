package steelUnicorn.laplacity.ui.mainmenu.tabs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.mainmenu.MainMenu;

/**
 * Класс с описанием компании в главном меню.
 */
public class CreditsTab extends MainMenuTab {
    private static final float infoPad = Globals.UI_WORLD_HEIGHT * 0.05f;

    public CreditsTab(Skin skin) {
        super();

        addReturnButton(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                MainMenu stage = (MainMenu) CreditsTab.this.getStage();
                stage.returnMainMenu();
            }
        }).expand().uniform().top().left().padLeft(MainMenu.menuLeftSpace);

        addCredits(skin).top().padTop(MainMenu.menuTopPad);

        add().expand().uniform();
    }

    private Cell<Table> addCredits(Skin skin) {
        Table creditsTable = new Table();
        creditsTable.setBackground(skin.getDrawable("label_back"));
        Label description = new Label("Credits", skin, "noback");
        description.setFontScale(descriptionScale);
        creditsTable.add(description).pad(tabSpace);

        creditsTable.row();
        Label info = new Label("Made by Steel Unicorn\n"
                + "Steel-uni.com", skin, "noback");
        info.setAlignment(Align.center);
        creditsTable.add(info).pad(infoPad, 0, infoPad, 0);

        creditsTable.setName("credits_label");
        return add(creditsTable);
    }
}
