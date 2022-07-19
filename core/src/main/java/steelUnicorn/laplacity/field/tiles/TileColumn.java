package steelUnicorn.laplacity.field.tiles;

/**
 * Represent a column of tiles in an integral-valued tile array.
 * Columns can only begin at a certain point (tile) and then grow upwards.
 * This is done intentionally to limit usage of this class by
 * {@link TilesBodyHandler} only.
 */
public class TileColumn {
    private int horizontalIndex;
    private int bottomIndex;
    private int topIndex;
    private int id;

    /**
     * Start a new column at given point
     * @param _horizontalIndex
     * @param _verticalIndex
     * @param _id
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
     * Start a new column at given point.
     * Identical to a constructor, but reusing the same instance.
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
     * Increase height by 1 upwards.
     */
    public void increment() {
        this.topIndex++;
    }

    /**
     * Render tile type as non-physical.
     * All non-physical tiles are equivalent to {@link EmptyTile} for {@link TilesBodyHandler} class.
     */
    public void reset() {
        this.id = 1; // empty tile id
    }

    /** Make column depleted so it would be ignored when merging columns into rectangles.
     * Call it after the column has been successfully added to a rectangle.
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
