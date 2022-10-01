package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.handler.ButtonNames;

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

    public Button interstitialBtn;
    public Button rewardedBtn;

    /**
     * Создает надписи с количеством еды и временем таймера и кнопки для пополнения еды.
     * @param skin скин с текстурами кнопок и лейблов.
     */
    public CatFoodInterface(Skin skin) {
        defaults().pad(padSize);

        text = new Label("Food: 00", skin);
        text.setAlignment(Align.center);
        add(text).size(text.getPrefWidth(), text.getPrefHeight());

        interstitialBtn = new Button(skin.get("interstitial_bug", Button.ButtonStyle.class));
        interstitialBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.lightClickSound);
                if (GameProcess.gameUI != null) {
                    GameProcess.gameUI.guiHandler.pressButton(ButtonNames.INTER);
                }
                Globals.game.showInterstitial();
            }
        });
        interstitialBtn.setName(ButtonNames.INTER);
        add(interstitialBtn);

        rewardedBtn = new Button(skin.get("rewarded_bug", Button.ButtonStyle.class));
        rewardedBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.lightClickSound);
                if (GameProcess.gameUI != null) {
                    GameProcess.gameUI.guiHandler.pressButton(ButtonNames.REWARD);
                }
                Globals.game.showRewarded();
            }
        });
        rewardedBtn.setName(ButtonNames.REWARD);
        add(rewardedBtn);

        timerLabel = new Label("00:00", skin);
        timerLabel.setVisible(false);
        timerLabel.setAlignment(Align.center);
        add(timerLabel).size(timerLabel.getPrefWidth(), timerLabel.getPrefHeight());

        update(Globals.catFood.getLaunches());
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
}
