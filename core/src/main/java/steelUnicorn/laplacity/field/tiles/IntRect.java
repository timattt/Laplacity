package steelUnicorn.laplacity.field.tiles;

public class IntRect {
    private int left;
    private int right;
    private int top;
    private int bottom;

    public IntRect() {
    }

    public IntRect(TileColumn column) {
        this.left = column.getHorizontalIndex();
        this.right = column.getHorizontalIndex();
        this.top = column.getTop();
        this.bottom = column.getBottom();
    }

    public IntRect(IntRect that) {
        this.left = that.left;
        this.right = that.right;
        this.top = that.top;
        this.bottom = that.bottom;
    }

    public void extend() {
        this.right++;
    }

    public void set(TileColumn column) {
        this.left = column.getHorizontalIndex();
        this.right = column.getHorizontalIndex();
        this.top = column.getTop();
        this.bottom = column.getBottom();
    }
}
