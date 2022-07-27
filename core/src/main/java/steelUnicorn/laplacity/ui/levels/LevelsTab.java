package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_WIDTH;
import static steelUnicorn.laplacity.core.Globals.nameMainMenuScreen;
import static steelUnicorn.laplacity.core.Globals.nameSlideOut;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;


import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.mainmenu.MainMenu;
import steelUnicorn.laplacity.ui.mainmenu.tabs.MainMenuTab;
import steelUnicorn.laplacity.utils.LevelsParser;

/**
 * Класс LevelTab при создании создает вкладку с уровнями в главном меню.
 * Вкладка начинается с названия, и под названием табилца с уровнями.
 * Уровни подгружаются из папки /assets/levels/
 */
public class LevelsTab extends MainMenuTab {
    private static final float menuBtnWidth = UI_WORLD_WIDTH * 0.08f;
    private LevelsNav nav;
    private int currentSection;

    private Array<LevelSection> sections;

    private Cell sectionCell;

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
        add(nav);
    }

    private Cell addReturnButton(Skin skin) {
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

    private Cell addSections(Skin skin) {
        sections = new Array<>(LevelsParser.sectionLevelsPaths.size);

        for (int i = 0; i < LevelsParser.sectionLevelsPaths.size; i++) {
            sections.add(new LevelSection(i + 1, skin));
        }

        return add(sections.get(currentSection - 1));
    }

    private void updateSection() {
        sectionCell.setActor(sections.get(currentSection - 1));
    }

    public class LevelsNav extends Table {
        private Label sectionName;
        private Button leftArrow;
        private Button rightArrow;

        public LevelsNav(Skin skin) {
            defaults().space(LevelsTab.tabSpace);
            leftArrow = new Button(skin);
            leftArrow.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LevelsTab.this.currentSection--;
                    sectionName.setText("Section " + LevelsTab.this.currentSection);
                    LevelsTab.this.updateSection();
                    checkVisible();
                }
            });
            add(leftArrow);

            sectionName = new Label("Section " + LevelsTab.this.currentSection, skin);
            sectionName.setColor(Color.BLACK);
            add(sectionName);

            rightArrow = new Button(skin);
            rightArrow.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LevelsTab.this.currentSection++;
                    sectionName.setText("Section " + LevelsTab.this.currentSection);
                    LevelsTab.this.updateSection();
                    checkVisible();
                }
            });
            add(rightArrow);

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
