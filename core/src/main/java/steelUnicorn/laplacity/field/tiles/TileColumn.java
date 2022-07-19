package steelUnicorn.laplacity.field.tiles;

public class TileColumn {
    private int horizontalIndex;
    private int bottomIndex;
    private int topIndex;
    private TileType type;

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

    public void set(int _horizontalIndex, int _verticalIndex, TileType _type) {
        this.horizontalIndex = _horizontalIndex;
        this.bottomIndex = _verticalIndex;
        this.topIndex = _verticalIndex;
        this.type = _type;
    }

    public void increment() {
        this.topIndex++;
    }

    public void reset() {
        this.type = TileType.nonPhysical;
    }

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
