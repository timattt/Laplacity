package steelUnicorn.laplacity.field.physics;

public class BodyIntRect extends IntRect {

    public int id;
    public float restitution;
	
    public BodyIntRect() {
    }
    
	public BodyIntRect(TileColumn column) {
		super(column);
		this.id = column.getId();
		this.restitution = column.getRestitution();
	}

	public BodyIntRect(BodyIntRect that) {
		super(that);
		this.id = that.id;
		this.restitution = that.restitution;
	}

	@Override
	public void set(TileColumn column) {
		super.set(column);
		this.id = column.getId();
		this.restitution = column.getRestitution();
	}

}
