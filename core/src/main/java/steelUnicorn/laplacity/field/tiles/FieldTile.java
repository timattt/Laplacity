package steelUnicorn.laplacity.field.tiles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class FieldTile extends EmptyTile {
	
	// Body
	protected Body body;

	public FieldTile(int gridX, int gridY) {
		super(gridX, gridY);
		setParent(field);
		this.gridX = gridX;
		this.gridY = gridY;
		setColor(new Color(0.3f, 0.3f, 0.3f, 1));
		
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.StaticBody;
		
		field.fromGridToWorldCoords(gridX, gridY, TMP1);
		bodydef.position.set(TMP1);
		setPosition(TMP1.x, TMP1.y);

		body = registerPhysicalObject(this, bodydef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(field.getTileSize() / 2, field.getTileSize() / 2);

		FixtureDef fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 10000;
		fxt.restitution = 1f;

		body.createFixture(fxt);
		body.setUserData(this);
 		
		shape.dispose();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float sz = field.getTileSize();
		int w = field.getFieldWidth();
		int h = field.getFieldHeight();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(getColor());
		shapeRenderer.rect((gridX - w/2)*sz, (gridY - h/2)*sz, sz, sz);
		shapeRenderer.end();
		
		drawArrow(); 
	}
	
}