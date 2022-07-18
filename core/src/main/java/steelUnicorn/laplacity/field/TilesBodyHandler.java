package steelUnicorn.laplacity.field;

import java.util.Vector;

import com.badlogic.gdx.Gdx;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.tiles.BarrierTile;
import steelUnicorn.laplacity.field.tiles.DeadlyTile;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.field.tiles.FieldTile;
import steelUnicorn.laplacity.field.tiles.FinishTile;
import steelUnicorn.laplacity.field.tiles.TileColumn;
import steelUnicorn.laplacity.field.tiles.TileType;
import steelUnicorn.laplacity.field.tiles.WallTile;

public class TilesBodyHandler {

	private static Vector<TileColumn> columns = new Vector<>();

	// Лучше от этого метода избавиться, но пока тип тайла иначе не выяснить
	private static TileType typeOf(EmptyTile tile) {
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
				// do stuff
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

	public static void createBodies(EmptyTile[][] tiles) {
		fillColumns(tiles);
		for(TileColumn k : columns) {
			Gdx.app.log("clm", "at [" + String.valueOf(k.getHorizontalIndex()) + "] top [" + String.valueOf(k.getTop()) + "] bottom [" + String.valueOf(k.getBottom()) + "] type " + String.valueOf(k.getType()));
		}
		//IntRect bodyTemplate = new IntRect();

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
