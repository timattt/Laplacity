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
    private TileType type;

    /**
     * Start a new column at given point
     * @param _horizontalIndex
     * @param _verticalIndex
     * @param _type
     */
    public TileColumn(int _horizontalIndex, int _verticalIndex, TileType _type) {
        this.horizontalIndex = _horizontalIndex;
        this.bottomIndex = _verticalIndex;
        this.topIndex = _verticalIndex;
        this.type = _type;
    }

    public TileColumn(TileColumn that) {
        this.horizontalIndex = that.horizontalIndex;
        this.bottomIndex = that.bottomIndex;
        this.topIndex = that.topIndex;
        this.type = that.type;
    }

    /**
     * Start a new column at given point.
     * Identical to a constructor, but reusing the same instance.
     * @param _horizontalIndex
     * @param _verticalIndex
     * @param _type
     */
    public void set(int _horizontalIndex, int _verticalIndex, TileType _type) {
        this.horizontalIndex = _horizontalIndex;
        this.bottomIndex = _verticalIndex;
        this.topIndex = _verticalIndex;
        this.type = _type;
    }

    /**
     * Increase height by 1 upwards.
     */
    public void increment() {
        this.topIndex++;
    }

    /**
     * Render tile type as non-physical.
     * Think of {@link TileType.nonPhysical} as of a zero value for tile type.
     */
    public void reset() {
        this.type = TileType.nonPhysical;
    }

    /** Make column depleted so it would be ignored when merging columns into rectangles.
     * Call it after the column has been successfully added to a rectangle.
     * Think of {@link TileType.depleted} as of null value for tile types.
    */
    public void deplete() {
        this.type = TileType.depleted;
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

    public TileType getType() {
        return this.type;
    }
}
