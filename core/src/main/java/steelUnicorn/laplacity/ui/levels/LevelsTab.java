package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.UI_WORLD_WIDTH;
import static steelUnicorn.laplacity.core.Globals.nameMainMenuScreen;
import static steelUnicorn.laplacity.core.Globals.nameSlideOut;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;


import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.mainmenu.MainMenu;
import steelUnicorn.laplacity.ui.mainmenu.tabs.MainMenuTab;
import steelUnicorn.laplacity.utils.LevelsParser;

/**
 * Класс LevelTab при создании создает вкладку с уровнями в главном меню.
 * Вкладка начинается с названия, и под названием таблица с уровнями.
 * Уровни подгружаются из папки /assets/levels/
 */
public class LevelsTab extends MainMenuTab {
    private static final float menuBtnWidth = UI_WORLD_WIDTH * 0.08f;
    private LevelsNav nav;
    private int currentSection;

    private Array<LevelSection> sections;

    private Cell<LevelSection> sectionCell;

    public LevelsTab(Skin skin) {
        super();
        currentSection = 1;

        addReturnButton(skin);
        //Description
        row();
        addDescription("Levels:", skin);
        //Sections
        row();
        sectionCell = addSections(skin);

        row();
        nav = new LevelsNav(skin);
        add(nav).pad(nav.navPad);
    }

    private Cell<TextButton> addReturnButton(Skin skin) {
        TextButton btn = new TextButton("Menu", skin);
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LaplacityAssets.playSound(LaplacityAssets.clickSound);
                Globals.game.getScreenManager().pushScreen(nameMainMenuScreen, nameSlideOut);
            }
        });
        return add(btn).size(menuBtnWidth, MainMenu.menuHeight);
    }

    private Cell<LevelSection> addSections(Skin skin) {
        sections = new Array<>(LevelsParser.sectionLevelsPaths.size);

        for (int i = 0; i < LevelsParser.sectionLevelsPaths.size; i++) {
            sections.add(new LevelSection(i + 1, skin));
        }

        return add(sections.get(currentSection - 1));
    }

    private void updateSection() {
        sectionCell.setActor(sections.get(currentSection - 1));
    }

    /**
     * Класс навигации снизу под таблицей для смены секций
     */
    public class LevelsNav extends Table {
        private Label sectionName;
        private ImageButton leftArrow;
        private ImageButton rightArrow;

        private float navPad = UI_WORLD_HEIGHT * 0.08f;
        private float arrowSize = UI_WORLD_WIDTH * 0.04f;

        public LevelsNav(Skin skin) {
            TextureAtlas icons = Globals.assetManager.get("ui/gameicons/icons.atlas",
                    TextureAtlas.class);

            defaults().space(LevelsTab.tabSpace);
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(
                    skin.get(ImageButton.ImageButtonStyle.class));
            style.imageUp = new TextureRegionDrawable(icons.findRegion("arrowleft"));
            leftArrow = new ImageButton(style);
            leftArrow.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LevelsTab.this.currentSection--;
                    sectionName.setText("Section " + LevelsTab.this.currentSection);
                    LevelsTab.this.updateSection();
                    checkVisible();
                }
            });
            add(leftArrow).size(arrowSize);

            sectionName = new Label("Section " + LevelsTab.this.currentSection, skin);
            sectionName.setColor(Color.WHITE);
            add(sectionName);

            style = new ImageButton.ImageButtonStyle(
                    skin.get(ImageButton.ImageButtonStyle.class));
            style.imageUp = new TextureRegionDrawable(icons.findRegion("arrowright"));
            rightArrow = new ImageButton(style);
            rightArrow.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LevelsTab.this.currentSection++;
                    sectionName.setText("Section " + LevelsTab.this.currentSection);
                    LevelsTab.this.updateSection();
                    checkVisible();
                }
            });
            add(rightArrow).size(arrowSize);

            checkVisible();
        }

        private void checkVisible() {
            if (currentSection == 1) {
                leftArrow.setVisible(false);
            } else {
                leftArrow.setVisible(true);
            }

            if (currentSection == LevelsParser.sectionLevelsPaths.size) {
                rightArrow.setVisible(false);
            } else {
                rightArrow.setVisible(true);
            }
        }
    }
}
