package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import steelUnicorn.laplacity.core.Globals;

public class CatDialog extends Dialog {
    private static final String wsName = "cat_hungry";

    private static final float exitSize = 0.6f;
    private static final float buttonsPad = Globals.UI_WORLD_WIDTH * 0.03f;

    private enum HungryResult {
        INTER,
        REWARD
    }

    private CatFoodInterface catFI;

    public CatDialog(CatFoodInterface catFI, Skin skin) {
        super("", skin, wsName);
        this.catFI = catFI;
        initializeCatDialog();

        Color color = getColor();
        color.a = 0;
        setColor(color);
    }

    public void initializeCatDialog() {
        getCells().clear();
        clearChildren();
        Table content = getContentTable();
        Table buttons = getButtonTable();

        add(content).expand().uniform();
        add().expand().uniform();//Для того чтобы правую часть занимал котик - пустая клетка

        //content
        content.row();
        Label label = new Label("Cat is hungry!\n\n\n" +
                "Feed cat right now, or\n" +
                "wait until food will restore.\n\n", getSkin());
        label.setAlignment(Align.center);
        text(label);



        //buttons
        content.row();
        buttons.defaults().space(buttonsPad);

        Button btn = new Button(getSkin().get("ExitBtn", Button.ButtonStyle.class));
        buttons.add(btn).size(btn.getPrefWidth() * exitSize, btn.getPrefHeight() * exitSize);
        setObject(btn, null);

        Button inter = new Button(getSkin().get("interstitial_bug", Button.ButtonStyle.class));
        button(inter, HungryResult.INTER);

        Button reward = new Button(getSkin().get("rewarded_bug", Button.ButtonStyle.class));
        button(reward, HungryResult.REWARD);

        content.add(buttons).padTop(buttonsPad);
    }

    @Override
    protected void result(Object object) {
        if (object instanceof HungryResult) {
            switch ((HungryResult) object) {
                case INTER:
                    Globals.game.showInterstitial();
                    break;
                case REWARD:
                    Globals.game.showRewarded();
                    break;
            }
        }
    }
}
