package steelUnicorn.laplacity.field.tiles;

/**
 * Represents a rectangle of tiles in an integral-valued tile grid.
 */
public class IntRect {
    public int left;
    public int right;
    public int top;
    public int bottom;
    public int id;

    public IntRect() {
    }

    /**
     * Derive a rectangle from the given column.
     * @param column
     */
    public IntRect(TileColumn column) {
        this.left = column.getHorizontalIndex();
        this.right = column.getHorizontalIndex();
        this.top = column.getTop();
        this.bottom = column.getBottom();
        this.id = column.getId();
    }

    public IntRect(IntRect that) {
        this.left = that.left;
        this.right = that.right;
        this.top = that.top;
        this.bottom = that.bottom;
        this.id = that.id;
    }

    /**
     * Extend the rectangle to the right by one
     */
    public void extend() {
        this.right++;
    }

    /**
     * Reset the rectangle so it would be equivalent to the given column
     * @param column
     */
    public void set(TileColumn column) {
        this.left = column.getHorizontalIndex();
        this.right = column.getHorizontalIndex();
        this.top = column.getTop();
        this.bottom = column.getBottom();
        this.id = column.getId();
    }

    public int height() {
        return this.top - this.bottom + 1;
    }

    public int width() {
        return this.right - this.left + 1;
    }
}
