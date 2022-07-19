package steelUnicorn.laplacity.field.tiles;

public class IntRect {
    public int left;
    public int right;
    public int top;
    public int bottom;
    public TileType type;

    public IntRect() {
    }

    public IntRect(TileColumn column) {
        this.left = column.getHorizontalIndex();
        this.right = column.getHorizontalIndex();
        this.top = column.getTop();
        this.bottom = column.getBottom();
        this.type = column.getType();
    }

    public IntRect(IntRect that) {
        this.left = that.left;
        this.right = that.right;
        this.top = that.top;
        this.bottom = that.bottom;
        this.type = that.type;
    }

    public void extend() {
        this.right++;
    }

    public void set(TileColumn column) {
        this.left = column.getHorizontalIndex();
        this.right = column.getHorizontalIndex();
        this.top = column.getTop();
        this.bottom = column.getBottom();
        this.type = column.getType();
    }
}
