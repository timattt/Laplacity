package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;

/**
 * Интерфейс отображающий количество еды, таймер восстановления и кнопки для восполнения еды
 * через просмотр рекламы. Реклама восстанавливает +2 и +10 еды.
 * <p>
 * Количество запусков управляется классом {@link steelUnicorn.laplacity.utils.CatFood}
 *
 * Таймер в классе меняет вспомогательный класс {@link steelUnicorn.laplacity.utils.CatFoodTimer},
 * который отвечает за подсчет времени, и изменение надписи в интерфейсе
 *
 * @see steelUnicorn.laplacity.utils.CatFood
 * @see steelUnicorn.laplacity.utils.CatFoodTimer
 */
public class CatFoodInterface extends Table {
    private static final float padSize = 5;

    private final Label text;
    private final Label timerLabel;

    public static final float showHideDur = 10f;
    private static final float fadeOutDur = 0.1f;
    private static final float fadeInDur = 0.1f;

    private boolean isShown;

    private final Timer.Task hideTask = new Timer.Task() {
        @Override
        public void run() {
            if (isShown) {
                CatFoodInterface.this.addAction(
                        Actions.sequence(
                                Actions.fadeOut(fadeOutDur),
                                Actions.visible(false),
                                Actions.touchable(Touchable.disabled)
                        ));
                isShown = false;
            }
        }
    };

    private final Timer.Task showTask = new Timer.Task() {
        @Override
        public void run() {
            if (!isShown) {
                CatFoodInterface.this.addAction(
                        Actions.sequence(
                                Actions.visible(true),
                                Actions.touchable(Touchable.enabled),
                                Actions.fadeIn(fadeInDur)
                        ));
                isShown = true;
            }
        }
    };

    /**
     * Создает надписи с количеством еды и временем таймера и кнопки для пополнения еды.
     * @param skin скин с текстурами кнопок и лейблов.
     */
    public CatFoodInterface(Skin skin) {
        isShown = true;
        defaults().pad(padSize);

        text = new Label("Food: 00", skin);
        text.setAlignment(Align.center);
        add(text).size(text.getPrefWidth(), text.getPrefHeight());

        Button btn = new Button(skin.get("interstitial_bug", Button.ButtonStyle.class));
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.lightClickSound);
                Globals.game.showInterstitial();
            }
        });
        add(btn);

        btn = new Button(skin.get("rewarded_bug", Button.ButtonStyle.class));
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.lightClickSound);
                Globals.game.showRewarded();
            }
        });
        add(btn);

        timerLabel = new Label("00:00", skin);
        timerLabel.setVisible(false);
        timerLabel.setAlignment(Align.center);
        add(timerLabel).size(timerLabel.getPrefWidth(), timerLabel.getPrefHeight());

        update(Globals.catFood.getLaunches());
        showHide();
    }

    /**
     * Обновляет надпись с количеством запусков.
     * @param launches количество запусков = еды.
     */
    public void update(int launches) {
        text.setText("Food: " + launches);
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    /**
     * Показывает еду сразу и после {@link #showHideDur} скрывает блок.
     *
     * @see #showHideDur
     */
    public void showHide() {
        clearActions();
        showTask.cancel();
        hideTask.cancel();

        Timer.schedule(showTask, 0);
        Timer.schedule(hideTask, showHideDur);
    }
}
