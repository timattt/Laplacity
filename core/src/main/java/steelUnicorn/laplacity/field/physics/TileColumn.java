package steelUnicorn.laplacity.field.physics;

import steelUnicorn.laplacity.field.tiles.EmptyTile;

/**
 * Столбец клеток, находящийся внутри целочисленного двумерного массива клеток.
 * Столбцы могут только создаваться на определённой клетке и расти вверх.
 * Этот класс должен использоваться только классом {@link TilesBodyHandler}.
 */
public class TileColumn {
    private int horizontalIndex;
    private int bottomIndex;
    private int topIndex;
    private int id;

    /**
     * Начать столбец, отсчитвая его вверх от данной клетки
     * @param _horizontalIndex
     * @param _verticalIndex
     * @param _id ID клетки
     */
    public TileColumn(int _horizontalIndex, int _verticalIndex, int _id) {
        this.horizontalIndex = _horizontalIndex;
        this.bottomIndex = _verticalIndex;
        this.topIndex = _verticalIndex;
        this.id = _id;
    }

    public TileColumn(TileColumn that) {
        this.horizontalIndex = that.horizontalIndex;
        this.bottomIndex = that.bottomIndex;
        this.topIndex = that.topIndex;
        this.id = that.id;
    }

    /**
     * Начать столбец, отсчитвая его вверх от данной клетки
     * Функционал аналогичен конструктору, только с использованием уже созданного экземпляра
     * @param _horizontalIndex
     * @param _verticalIndex
     * @param _id
     */
    public void set(int _horizontalIndex, int _verticalIndex, int _id) {
        this.horizontalIndex = _horizontalIndex;
        this.bottomIndex = _verticalIndex;
        this.topIndex = _verticalIndex;
        this.id = _id;
    }

    /**
     * Увеличить высоту столбца на 1 вверх
     */
    public void increment() {
        this.topIndex++;
    }

    /**
     * Пометить тип столбца как неосязаемый.
     * {@link TilesBodyHandler} считает неосязаемыми клетками только класс {@link EmptyTile}.
     */
    public void reset() {
        this.id = 1; // empty tile id
    }

    /**
     * Пометить столбец как удалённый. Удалённые столбцы игнорируются при объединении столбцов в прямоугольники.
     */
    public void deplete() {
        this.id = 0; // 0 is reserved for depleted columns
    }

    public int getHorizontalIndex() {
        return this.horizontalIndex;
    }

    public int getBottom() {
        return this.bottomIndex;
    }

    public int getTop() {
        return this.topIndex;
    }

    public int getId() {
        return this.id;
    }
}
