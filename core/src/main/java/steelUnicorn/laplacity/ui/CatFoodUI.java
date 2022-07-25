package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import steelUnicorn.laplacity.core.Globals;

public class CatFoodUI extends Table {
    private Label text;
    private TextButton btn;
    private float padSize = 10;
    private float tableWidth = Globals.UI_WORLD_WIDTH * 0.1f;
    private float tableHeight = Globals.UI_WORLD_HEIGHT * 0.05f;


    public CatFoodUI(int launches, Skin skin) {
        super(skin);

        defaults().pad(padSize).size(tableWidth / 2, tableHeight);

        text = new Label("Food: " + launches, skin);
        text.setColor(Color.BLACK);
        add(text);

        btn = new TextButton(" + 5", skin);
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Globals.game.showInterstitial();
                Globals.catFood.totalLaunchesAvailable += 5;
                Globals.catFood.update();

                Gdx.app.log("Food btn", "pressed"
                        + Globals.catFood.totalLaunchesAvailable);
            }
        });
        add(btn);
    }

    public void update(int launches) {
        text.setText("Food: " + launches);
    }
}
