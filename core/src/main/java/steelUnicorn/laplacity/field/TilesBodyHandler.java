package steelUnicorn.laplacity.field;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.tiles.BarrierTile;
import steelUnicorn.laplacity.field.tiles.DeadlyTile;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.field.tiles.FieldTile;
import steelUnicorn.laplacity.field.tiles.FinishTile;
import steelUnicorn.laplacity.field.tiles.IntRect;
import steelUnicorn.laplacity.field.tiles.TileColumn;
import steelUnicorn.laplacity.field.tiles.TileType;
import steelUnicorn.laplacity.field.tiles.WallTile;

public class TilesBodyHandler {

	private static ArrayList<TileColumn> columns = new ArrayList<>();

	/**
	 * Returns tile type as listed in {@link TileType}.
	 */
	public static TileType typeOf(EmptyTile tile) {
		// Лучше от этого метода избавиться, но пока тип тайла иначе не выяснить
		if (tile instanceof BarrierTile) {
			return TileType.barrier;
		} else if (tile instanceof DeadlyTile) {
			return TileType.deadly;
		} else if (tile instanceof WallTile) {
			return TileType.wall;
		} else if (tile instanceof FinishTile) {
			return TileType.finish;
		} else {
			return TileType.nonPhysical;
		}
	}

	private static void fillColumns(EmptyTile[][] tiles) {
		columns.clear();
		TileColumn curColumn = new TileColumn(0, 0, TileType.nonPhysical);

		for (int i = 0; i < GameProcess.field.getFieldWidth(); i++) {
			for (int j = 0; j < GameProcess.field.getFieldHeight(); j++) {
				if (tiles[i][j] instanceof FieldTile) {
					if (curColumn.getType() == TileType.nonPhysical) { // create new column
						curColumn.set(i, j, typeOf(tiles[i][j]));
					} else if (typeOf(tiles[i][j]) == curColumn.getType()) { // continue current column
						curColumn.increment();
					} else { // from column of one type to another type
						// push current column and start new
						columns.add(new TileColumn(curColumn));
						curColumn.set(i, j, typeOf(tiles[i][j]));
					}
				} else {
					if (curColumn.getType() == TileType.nonPhysical) continue; // from empty to empty
					// else: from physical tile to empty. Push current column to main array
					columns.add(new TileColumn(curColumn));
					curColumn.reset();
				}
			}
			// push last column (if exists), do so on each iteration including the final one
			if (curColumn.getType() != TileType.nonPhysical) {
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
		IntRect bodyTemplate = new IntRect();
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).getType() != TileType.depleted) { // if the column hasn't been merged yet
				bodyTemplate.set(columns.get(i)); // creating new rectangle
				columns.get(i).deplete(); // make depleted as it's already a part of a rectangle
				expectedIndex = columns.get(i).getHorizontalIndex() + 1;
				if (i < columns.size() - 1) { // if it's not the last column (usually it's the right wall)
					for (int j = i + 1; j < columns.size(); j++) {
						if (columns.get(j).getHorizontalIndex() > expectedIndex) continue; // continue if we haven't found a mergeable column
						// column is mergeable if following conditions are satisfied:
						if ((columns.get(j).getType() == bodyTemplate.type) &&
							(columns.get(j).getTop() == bodyTemplate.top) &&
							(columns.get(j).getBottom() == bodyTemplate.bottom)) { // then merge this column into the rectangle template
								bodyTemplate.extend();
								columns.get(j).deplete(); // make depleted as it's already a part of a rectangle
								expectedIndex++;
							}
					}
				}
				// Здесь нужно написать создание физического тела
				Gdx.app.log(String.valueOf(bodyTemplate.type), "(" + String.valueOf(bodyTemplate.left) + ", " + String.valueOf(bodyTemplate.bottom) + ") -- (" + String.valueOf(bodyTemplate.right) + ", " + String.valueOf(bodyTemplate.top) + ")");
			}	
		}

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
