package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.UI_WORLD_WIDTH;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import steelUnicorn.laplacity.core.LaplacityAssets;
import steelUnicorn.laplacity.ui.mainmenu.tabs.MainMenuTab;

/**
 * Класс создает интерфейс выбора уровней.
 * Сверху интерфейса: кнопк возврата в меню и описание Levels:
 * Далее идут уровни секции, по 4 в ряд
 * И снизу навигационная панель для переключения секций
 *
 * Секции хранятся в массиве sections.
 *
 * Уровни подгружаются из папки /assets/levels/
 */
public class LevelsTab extends MainMenuTab {
    private LevelsNav nav;
    private int currentSection;

    private Array<LevelSection> sections;

    private Cell<LevelSection> sectionCell;

    public LevelsTab(Skin skin) {
        super();
        currentSection = 1;

        sectionCell = addSections(skin);

        row();
        nav = new LevelsNav(skin);
        add(nav).expandY().bottom().padTop(nav.navPad).padBottom(nav.navPad);
    }

    private Cell<LevelSection> addSections(Skin skin) {
        sections = new Array<>(sectionLevels.size);

        for (int i = 0; i < sectionLevels.size; i++) {
            sections.add(new LevelSection(i + 1, skin));
        }

        return add(sections.get(currentSection - 1));
    }

    private void updateSection() {
        LevelSection section = sections.get(currentSection - 1);
        section.show();
        sectionCell.setActor(section);
    }

    /**
     * Функция обрабатывает прохождение уровня на интерфейсе
     *
     * @param section - секция
     * @param level - уровень
     */
    public void levelFinished(int section, int level) {
        openNextLevel(section, level);  //открытие следующего
        sections.get(section - 1).updateLevel(level);   //обновление пройденного
    }

    /**
     * Function that enable level button of next level
     * @param section - current section
     * @param level - current level
     */
    public void openNextLevel(int section, int level) {
        if (section <= sections.size && level < sections.get(section - 1).secSize) {
            sections.get(section - 1).openLevel(level + 1);
        }
    }

    /**
     * Класс навигации снизу под таблицей для смены секций
     */
    public class LevelsNav extends Table {
        private Label sectionName;
        private ImageButton leftArrow;
        private ImageButton rightArrow;

        private float navPad = UI_WORLD_HEIGHT * 0.06f;
        private float arrowSize = UI_WORLD_WIDTH * 0.06f;

        public LevelsNav(Skin skin) {
            defaults().space(LevelsTab.tabSpace);
            leftArrow = new ImageButton(skin.get("leftarrow", ImageButton.ImageButtonStyle.class));
            leftArrow.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LaplacityAssets.playSound(LaplacityAssets.clickSound);
                    LevelsTab.this.currentSection--;
                    updateSectionName();
                    LevelsTab.this.updateSection();
                    checkVisible();
                }
            });
            add(leftArrow).size(arrowSize);

            sectionName = new Label("Section 00" + LevelsTab.this.currentSection, skin);
            sectionName.setColor(Color.WHITE);
            sectionName.setAlignment(Align.center);
            add(sectionName).size(sectionName.getPrefWidth(), sectionName.getPrefHeight());
            updateSectionName();

            rightArrow = new ImageButton(skin.get("rightarrow", ImageButton.ImageButtonStyle.class));
            rightArrow.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LaplacityAssets.playSound(LaplacityAssets.clickSound);
                    LevelsTab.this.currentSection++;
                    LevelsTab.this.updateSection();
                    updateSectionName();
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

            if (currentSection == sectionLevels.size) {
                rightArrow.setVisible(false);
            } else {
                rightArrow.setVisible(true);
            }
        }

        private void updateSectionName() {
            sectionName.setText("Section " + LevelsTab.this.currentSection);
        }
    }
}
