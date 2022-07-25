package steelUnicorn.laplacity.field.physics;

/**
 * Прямоугольник с целочисленными координатами и целочисленным полем данных
 */
public class IntRect {
    public int left;
    public int right;
    public int top;
    public int bottom;

    public IntRect() {
    }

    /**
     * Создаёт прямоугольник из заданного столбца
     * @param column Столбец
     */
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

    /**
     * Продолжить прямоугольник вправо на 1
     */
    public void extend() {
        this.right++;
    }

    /**
     * Скопировать данные столбца в прямоугольник
     * @param column Столбец
     */
    public void set(TileColumn column) {
        this.left = column.getHorizontalIndex();
        this.right = column.getHorizontalIndex();
        this.top = column.getTop();
        this.bottom = column.getBottom();
    }
    
    public boolean containsPoint(int x, int y) {
    	return x >= left && x <= right && y >= bottom && y <= top;
    }

	@Override
	public String toString() {
		return "IntRect [left=" + left + ", right=" + right + ", top=" + top + ", bottom=" + bottom + "]";
	}

	/**
     * @return Высота прямоугольника в клетках
     */
    public int height() {
        return this.top - this.bottom + 1;
    }

    /**
     * @return Ширина прямоугольника в клетках
     */
    public int width() {
        return this.right - this.left + 1;
    }
}
