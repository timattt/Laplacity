package steelUnicorn.laplacity.field;

import steelUnicorn.laplacity.field.tiles.EmptyTile;

public class TilesBodyHandler {

	public static void createBoddies(EmptyTile[][] tiles) {
		/*
		 if (tiles[i][j] instanceof FieldTile) {
		 	// тогда это значит, что клетка плотная и ее нужно учитывать в физике 
		 }
		 */
		
		/*
		 Вот такие методы были для создаения коробки в каждой клетке
		 
		 
		 
		// physics
		BodyDef bodydef = new BodyDef();
		bodydef.type = BodyType.StaticBody;
		
		field.fromGridToWorldCoords(gridX, gridY, TMP1);
		bodydef.position.set(TMP1);

		body = registerPhysicalObject(bodydef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(field.getTileSize() / 2, field.getTileSize() / 2);

		FixtureDef fxt = new FixtureDef();
		fxt.shape = shape;
		fxt.density = 10000;
		fxt.restitution = 1f;

		body.createFixture(fxt);
		body.setUserData(this);
 		
		shape.dispose();
		 */
	}

}
