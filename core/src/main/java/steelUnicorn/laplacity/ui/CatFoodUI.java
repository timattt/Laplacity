package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;

import steelUnicorn.laplacity.core.Globals;

public class CatFoodUI {
    public Table launchesInfo;
    private Label hungryMsg;
    private Label text;
    private TextButton btn;

    private float scale = 3;

    private float padSize = 10;
    private float tableWidth = Globals.UI_WORLD_WIDTH * 0.1f;
    private float tableHeight = Globals.UI_WORLD_HEIGHT * 0.05f;


    public CatFoodUI(int launches, Skin skin) {
        //launchesInfo creation
        launchesInfo = new Table();
        launchesInfo.defaults().pad(padSize).size(tableWidth / 2, tableHeight);

        text = new Label("Food: " + launches, skin);
        text.setColor(Color.BLACK);
        launchesInfo.add(text);

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
        launchesInfo.add(btn);

        //catHungry Message
        hungryMsg = new Label("Cat Hungry", skin);
        hungryMsg.setColor(Color.RED);
        hungryMsg.setName("hungryMessage");
        hungryMsg.setVisible(false);
        hungryMsg.setSize(hungryMsg.getWidth() * scale, hungryMsg.getHeight() * scale);
        hungryMsg.setFontScale(scale);
    }

    public void update(int launches) {
        text.setText("Food: " + launches);
    }


    public void showHungry(Stage stg) {
        if (!stg.getActors().contains(hungryMsg, true)) {
            hungryMsg.setPosition(stg.getWidth() / 2 - hungryMsg.getWidth() / 2,
                    stg.getHeight() / 2 - hungryMsg.getHeight() / 2);
            stg.addActor(hungryMsg);
        }
        hungryMsg.setVisible(true);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                hungryMsg.setVisible(false);
            }
        }, 2.5f);
    }
}
