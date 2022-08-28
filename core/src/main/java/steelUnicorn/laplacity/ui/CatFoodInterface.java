package steelUnicorn.laplacity.ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import steelUnicorn.laplacity.core.Globals;

/**
 * Класс таблица хранящий верхнюю строку с количеством еды и кнопкой +5 +10 таймером
 *
 * Так же в нем хранится статическая надпись CatHungry которая выводится при нажатии на запуск,
 * когда нет запусков
 *
 * Таймер в классе меняет вспомогательный класс CatFoodTimer который отвечает за подсчет времени, и
 * изменение label'a
 */
public class CatFoodInterface extends Table {
    private Label text;
    private Label timerLabel;

    private float padSize = 5;

    private CatDialog hungryDialog;

    private static boolean isShown = false;

    public CatFoodInterface(Skin skin) {
        //Interface creation
        defaults().pad(padSize);

        text = new Label("Food: 00", skin);
        text.setColor(Color.WHITE);
        text.setAlignment(Align.center);
        add(text).size(text.getPrefWidth(), text.getPrefHeight());

        Button btn = new Button(skin.get("interstitial_bug", Button.ButtonStyle.class));
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Globals.game.showInterstitial();
            }
        });
        add(btn);

        btn = new Button(skin.get("rewarded_bug", Button.ButtonStyle.class));
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Globals.game.showRewarded();
            }
        });
        add(btn);

        timerLabel = new Label("00:00", skin);
        timerLabel.setColor(Color.WHITE);
        timerLabel.setVisible(false);
        timerLabel.setAlignment(Align.center);
        add(timerLabel).size(timerLabel.getPrefWidth(), timerLabel.getPrefHeight());

        //hungryMsg init
        if (hungryDialog == null) {
            hungryDialog = new CatDialog(this, skin);
        }

        update(Globals.catFood.getLaunches());
    }

    public void update(int launches) {
        text.setText("Food: " + launches);
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    public void showHungry(Stage stg) {
        hungryDialog.show(stg);
    }
}
