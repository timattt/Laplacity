package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.game;
import static steelUnicorn.laplacity.core.Globals.nameStoryScreen;
import static steelUnicorn.laplacity.core.Globals.storyTransitionName;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.SnapshotArray;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.utils.LevelsParser;

public class LevelWidget extends Table {
    private static final float widgetScale = 0.6f;
    private static final float starPadRat = 0.6f;
    private static final float sideStarPadRat = 0.13f;

    private static final float starsPadRat = 0.2f;

    private LevelButton btn;
    private Table starsTable;
    private int stars;

    private static final String[] imgNames = new String[]{"star_level00", "star_level01", "star_level02",
            "star_level10", "star_level11", "star_level12"};

    public LevelWidget(Skin skin, int level, int section, int stars) {
        super();
        setName("level" + level);
        this.stars = stars;

        //creation of stars Table
        createStars(skin);
        //creation of btn
        btn = new LevelButton(String.valueOf(level), skin, "Level",
                sectionLevels.get(section - 1).get(level - 1), level, section);
        btn.addListener(LevelButton.listener);
        //add to table
        add(starsTable).padBottom(starsTable.getPrefHeight() * starsPadRat);
        row();
        add(btn).size(btn.getPrefWidth() * widgetScale, btn.getPrefHeight() * widgetScale);

        setDisabled(stars == -1);
    }

    private String getImgName(int pos) {
        return imgNames[(stars > pos ? 3 : 0) + pos];
    }

    private void createStars(Skin skin) {
        starsTable = new Table();
        Image starImg = new Image(skin, getImgName(0));
        starsTable.add(starImg)
                .size(starImg.getPrefWidth() * widgetScale,
                        starImg.getPrefHeight() * widgetScale)
                .padBottom(- starImg.getPrefHeight() * widgetScale * starPadRat);

        starImg = new Image(skin, getImgName(1));
        starsTable.add(starImg)
                .size(starImg.getPrefWidth() * widgetScale,
                        starImg.getPrefHeight() * widgetScale)
                .space(0, sideStarPadRat * starImg.getPrefWidth() * widgetScale,
                        0, sideStarPadRat * starImg.getPrefWidth() * widgetScale);

        starImg = new Image(skin, getImgName(2));
        starsTable.add(starImg)
                .size(starImg.getPrefWidth() * widgetScale,
                        starImg.getPrefHeight() * widgetScale)
                .padBottom(- starImg.getPrefHeight() * widgetScale * starPadRat);
    }

    public void updateStars(Skin skin, int stars) {
        this.stars = stars;
        SnapshotArray<Actor> starImgs = starsTable.getChildren();
        for (int i = 0; i < starImgs.size; i++) {
            ((Image) starImgs.get(i)).setDrawable(skin, getImgName(i));
        }
    }


    public void setDisabled(boolean isDisabled) {
        btn.setDisabled(isDisabled);
        starsTable.setVisible(!isDisabled);
    }

    public static class LevelButton extends TextButton {
        private final Texture lvlImg;
        private final int levelNumber;
        private final int sectionNumber;

        /**
         * Обработчик событий. Т.к. все кнопки одинаковые, разница лишь в подгружаемом уровне
         * создается один обработчик на все уровни!
         */
        public static ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                LevelButton btn = (LevelButton) actor;
                GameProcess.levelNumber = btn.levelNumber;
                GameProcess.sectionNumber = btn.sectionNumber;
                GameProcess.levelParams = LevelsParser.getParams(btn.sectionNumber, btn.levelNumber);
                
                if (btn.levelNumber == 1 && btn.sectionNumber == 1) {
                	game.getScreenManager().pushScreen(nameStoryScreen + "0", storyTransitionName);
                } else {
                	GameProcess.initLevel(btn.lvlImg);
                }
                
                LaplacityAssets.setLevelTrack();;
            }
        };

        public LevelButton(String text, Skin skin, String styleName,
                           Texture lvlImg, int levelNumber, int sectionNumber) {
            super(text, skin, styleName);
            this.lvlImg = lvlImg;
            this.levelNumber = levelNumber;
            this.sectionNumber = sectionNumber;
        }
    }
}
