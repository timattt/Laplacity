package steelUnicorn.laplacity.ui.levels;

import static steelUnicorn.laplacity.core.Globals.UI_WORLD_HEIGHT;
import static steelUnicorn.laplacity.core.Globals.UI_WORLD_WIDTH;
import static steelUnicorn.laplacity.core.Globals.progress;
import static steelUnicorn.laplacity.core.LaplacityAssets.sectionLevels;

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
 * Интерфейс выбора уровней.
 * Отображает уровни секции LevelSection и навигацию по секциям LevelsNav.
 *
 * Уровни подгружаются из папки /assets/levels/ и хранятся в переменной
 * {@link LaplacityAssets#sectionLevels}.
 *
 * @see LevelSection
 * @see LevelsNav
 */
public class LevelsTab extends MainMenuTab {
    private Array<LevelSection> sections;
    /** Клетка секции для навигации по секциям */
    private final Cell<LevelSection> sectionCell;

    public LevelsNav nav;
    private int currentSection;

    /**
     * Создает интерфейс экранав выбора уровней
     * @param skin скин с текстурами кнопок
     */
    public LevelsTab(Skin skin) {
        super();
        currentSection = progress.getLastAvailableSection();

        sectionCell = addSections(skin);

        row();
        nav = new LevelsNav(skin);
        add(nav).expandY().bottom().padTop(nav.navPad).padBottom(nav.navPad);
    }

    /**
     * Создает массив объектов LevelSection, каждый из которых содержит таблицу с уровнями.
     * @param skin скин с текстурами кнопок.
     * @return клетка таблицы для изменения секций.
     */
    private Cell<LevelSection> addSections(Skin skin) {
        sections = new Array<>(sectionLevels.size);

        for (int i = 0; i < sectionLevels.size; i++) {
            sections.add(new LevelSection(i + 1, skin));
        }

        return add(sections.get(currentSection - 1));
    }

    /**
     * Меняет секцию в клетке.
     * @see #sectionCell
     */
    private void updateSection() {
        LevelSection section = sections.get(currentSection - 1);
        section.show();
        sectionCell.setActor(section);
    }

    /**
     * Обрабатывает прохождение уровня на интерфейсе.
     * Открывает следующий уровень и одновляет текущий (звезды над уровнем).
     *
     * @param section номер секции.
     * @param level номер пройденного уровня
     */
    public void levelFinished(int section, int level) {
        openNextLevel(section, level);
        sections.get(section - 1).updateLevel(level);
    }

    /**
     * Открывает кнопку выбора следующего уровня.
     * @param section текущая секция
     * @param level текущий уровень
     * @see LevelSection#openLevel(int)
     */
    private void openNextLevel(int section, int level) {
        if (section <= sections.size && level < sections.get(section - 1).secSize) {
            sections.get(section - 1).openLevel(level + 1);
        }
    }

    /**
     * Открывает все уровни. Вспомогательная функция для дебага.
     */
    public void openLevels() {
        for (LevelSection section : sections) {
            section.openLevels();
        }
    }

    /**
     * Навигация смены секций.
     */
    public class LevelsNav extends Table {
        private static final float navPad = UI_WORLD_HEIGHT * 0.06f;
        private static final float arrowSize = UI_WORLD_WIDTH * 0.06f;

        private final Label sectionName;
        private final ImageButton leftArrow;
        private final ImageButton rightArrow;

        /**
         * Создает интерфейс навигации.
         * @param skin скин с текстурами кнопок и надписей.
         */
        public LevelsNav(Skin skin) {
            defaults().space(LevelsTab.tabSpace);
            leftArrow = new ImageButton(skin, "leftarrow");
            leftArrow.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LaplacityAssets.playSound(LaplacityAssets.clickSound);
                    prev();
                }
            });
            add(leftArrow).size(arrowSize);

            sectionName = new Label("Section 00" + LevelsTab.this.currentSection, skin);
            sectionName.setAlignment(Align.center);
            add(sectionName).size(sectionName.getPrefWidth(), sectionName.getPrefHeight());
            sectionName.setText("Section " + LevelsTab.this.currentSection);

            rightArrow = new ImageButton(skin, "rightarrow");
            rightArrow.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    LaplacityAssets.playSound(LaplacityAssets.clickSound);
                    next();
                }
            });
            add(rightArrow).size(arrowSize);

            checkVisible();
        }

        /**
         * Меняет надпись на текущую секцию.
         */
        private void update() {
            sectionName.setText("Section " + LevelsTab.this.currentSection);
            updateSection();
            checkVisible();
        }

        /**
         * Переключает секцию вперед.
         */
        public void next() {
            currentSection++;
            update();
        }

        /**
         * Переключает секцию назад.
         */
        public void prev() {
            currentSection--;
            update();
        }

        /**
         * Убирает кнопку leftArrow на первой секции и rightArrow на последней.
         */
        private void checkVisible() {
            leftArrow.setVisible(currentSection != 1);
            rightArrow.setVisible(currentSection != sectionLevels.size);
        }
    }
}
