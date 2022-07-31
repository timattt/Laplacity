package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;

import steelUnicorn.laplacity.core.Globals;

/**
 * Класс таблица хранящий верхнюю строку с количеством еды и кнопкой +5 +10 таймером
 *
 * Так же в нем хранится статическая надпись CatHungry которая выводится при нажатии на запуск,
 * когда нет запусков
 */
public class CatFoodInterface extends Table {
    private Label text;
    private Label timerLabel;

    private float padSize = 10;
    private float tableWidth = Globals.UI_WORLD_WIDTH * 0.1f;
    private float tableHeight = Globals.UI_WORLD_HEIGHT * 0.05f;

    private static float scale = 3;
    private static Label hungryMsg;
    private static boolean isShown = false;

    public CatFoodInterface(int launches, Skin skin) {
        //Interface creation
        defaults().pad(padSize).size(tableWidth / 2, tableHeight);

        text = new Label("Food: " + launches, skin);
        text.setColor(Color.WHITE);
        add(text);

        TextButton btn = new TextButton(" + 5", skin);
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                update(Globals.catFood.callInterstitialAd());   //showInterstetial inside callAd
            }
        });
        add(btn);

        btn = new TextButton(" + 10", skin);
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                update(Globals.catFood.callRewardedAd());   //showInterstetial inside callAd
            }
        });
        add(btn);

        timerLabel = new Label("5:00", skin);
        timerLabel.setColor(Color.WHITE);
        timerLabel.setVisible(false);
        add(timerLabel);
        //hungryMsg init
        if (hungryMsg == null) {
            createHungry(skin);
        }
    }

    public void update(int launches) {
        text.setText("Food: " + launches);
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    //hungry message logic
    private static void createHungry(Skin skin) {
        hungryMsg = new Label("Cat Hungry", skin);
        hungryMsg.setColor(Color.RED);
        hungryMsg.setName("hungryMessage");
        hungryMsg.setVisible(false);
        hungryMsg.setSize(hungryMsg.getWidth() * scale, hungryMsg.getHeight() * scale);
        hungryMsg.setFontScale(scale);
    }

    public static void showHungry(Stage stg) {
        if (!stg.getActors().contains(hungryMsg, true)) {
            hungryMsg.setPosition(stg.getWidth() / 2 - hungryMsg.getWidth() / 2,
                    stg.getHeight() / 2 - hungryMsg.getHeight() / 2);
            stg.addActor(hungryMsg);
        }
        if (!isShown) { //Проверка на то что надпись уже показывается
            isShown = true;
            hungryMsg.setVisible(true);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    hungryMsg.setVisible(false);
                    isShown = false;
                }
            }, 2.5f);
        }
    }
}
