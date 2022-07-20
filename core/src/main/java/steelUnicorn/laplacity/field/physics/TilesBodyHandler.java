package steelUnicorn.laplacity.field.physics;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.field.tiles.SolidTile;

public class TilesBodyHandler {

	private static ArrayList<TileColumn> columns = new ArrayList<>();

	private static void fillColumns(EmptyTile[][] tiles) {
		columns.clear();
		TileColumn curColumn = new TileColumn(0, 0, 1); // 1 means empty tile

		for (int i = 0; i < LaplacityField.fieldWidth; i++) {
			for (int j = 0; j < LaplacityField.fieldHeight; j++) {
				if (tiles[i][j] instanceof SolidTile) {
					if (curColumn.getId() == 1) { // create new column
						curColumn.set(i, j, tiles[i][j].getId());
					} else if (tiles[i][j].getId() == curColumn.getId()) { // continue current column
						curColumn.increment();
					} else { // from column of one type to another type
						// push current column and start new
						columns.add(new TileColumn(curColumn));
						curColumn.set(i, j, tiles[i][j].getId());
					}
				} else {
					if (curColumn.getId() == 1) continue; // from empty to empty, 1 is an empty tile ID
					// else: from physical tile to empty. Push current column to main array
					columns.add(new TileColumn(curColumn));
					curColumn.reset();
				}
			}
			// push last column (if exists), do so on each iteration including the final one
			if (curColumn.getId() != 1) { // 1 is an empty tile ID
				columns.add(new TileColumn(curColumn));
				curColumn.reset();
			}
		}
	}

	/**
	 * Reads tile array and tries to create as many rectangles as possible.
	 * The rectangles are then registered as Box2D solid bodies.
	 * @param tiles Tile array
	 */
	public static void createBodies(EmptyTile[][] tiles) {
		fillColumns(tiles);
		int expectedIndex = 0;
		IntRect bodyTemplate;
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).getId() != 0) { // if the column hasn't been merged yet (merged columns have type of 0)
				bodyTemplate = new IntRect(columns.get(i)); // creating new rectangle
				columns.get(i).deplete(); // make depleted as it's already a part of a rectangle
				expectedIndex = columns.get(i).getHorizontalIndex() + 1;
				if (i < columns.size() - 1) { // if it's not the last column (usually it's the right wall)
					for (int j = i + 1; j < columns.size(); j++) {
						if (columns.get(j).getHorizontalIndex() > expectedIndex) continue; // continue if we haven't found a mergeable column
						// column is mergeable if following conditions are satisfied:
						if ((columns.get(j).getId() == bodyTemplate.id) &&
							(columns.get(j).getTop() == bodyTemplate.top) &&
							(columns.get(j).getBottom() == bodyTemplate.bottom)) { // then merge this column into the rectangle template
								bodyTemplate.extend();
								columns.get(j).deplete(); // make depleted as it's already a part of a rectangle
								expectedIndex++;
							}
					}
				}
				// Здесь нужно написать создание физического тела
				Gdx.app.log(String.valueOf(bodyTemplate.id), "(" + String.valueOf(bodyTemplate.left) + ", " + String.valueOf(bodyTemplate.bottom) + ") -- (" + String.valueOf(bodyTemplate.right) + ", " + String.valueOf(bodyTemplate.top) + ")");
				
				BodyDef bodydef = new BodyDef();
				bodydef.type = BodyType.StaticBody;

				LaplacityField.fromGridToWorldCoords(bodyTemplate.left, bodyTemplate.bottom, Globals.TMP1);
				LaplacityField.fromGridToWorldCoords(bodyTemplate.right, bodyTemplate.top, Globals.TMP2);
				Globals.TMP1.add(Globals.TMP2);
				Globals.TMP1.x /= 2;
				Globals.TMP1.y /= 2;

				bodydef.position.set(Globals.TMP1);
				Body body = GameProcess.registerPhysicalObject(bodydef);

				PolygonShape shape = new PolygonShape();
				float radius = LaplacityField.tileSize / 2;
				shape.setAsBox(radius* bodyTemplate.width(), radius * bodyTemplate.height());
				
				FixtureDef fxt = new FixtureDef();
				fxt.shape = shape;
				fxt.density = 10000;
				fxt.restitution = 1f;
				body.createFixture(fxt);
				body.setUserData((Integer) bodyTemplate.id);
				shape.dispose();
			}
		}
	}

}
