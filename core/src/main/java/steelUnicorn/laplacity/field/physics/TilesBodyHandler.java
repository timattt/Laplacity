package steelUnicorn.laplacity.field.physics;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.tiles.EmptyTile;
import steelUnicorn.laplacity.field.tiles.SolidTile;
import steelUnicorn.laplacity.field.tiles.TileCollisionListener;

/**
 * Класс, отвечающий за создание из одиночных клеток твёрдых тел большего размера
 */
public class TilesBodyHandler {

	private static ArrayList<TileColumn> columns = new ArrayList<>();

	/**
	 * Разбивает игровое поле на вертикальные столбцы и помещает их во временный массив
	 * внутри класса. Пустые клетки поля игнорируются.
	 * @param tiles Клетки игрового поля
	 */
	private static void fillColumns(EmptyTile[][] tiles) {
		columns.clear();
		TileColumn curColumn = new TileColumn(0, 0, 1, 0); // 1 - id пустой клетки
		for (int i = 0; i < LaplacityField.fieldWidth; i++) {
			for (int j = 0; j < LaplacityField.fieldHeight; j++) {
				if (tiles[i][j] instanceof SolidTile) { // если мы попали на твёрдую клетку...
					if (curColumn.getId() == 1) { // (1) с клетки пустого поля,
						curColumn.set(i, j, tiles[i][j].getId(), ((SolidTile) tiles[i][j]).getRestitution()); // то мы начинаем строить новый столбец
					} else if (tiles[i][j].getId() == curColumn.getId()) { // (2) с клетки с тем же id
						curColumn.increment(); // тогда продолжаем текущий столбец
					} else { // (3) с твёрдой клетки с другим id
						columns.add(new TileColumn(curColumn)); // тогда завершаем текущий столбец
						curColumn.set(i, j, tiles[i][j].getId(), ((SolidTile) tiles[i][j]).getRestitution()); // и начинаем новый
					}
				} else { // если мы попали на пустую клетку
					if (curColumn.getId() == 1) continue; // (1) с пустой - просто идём дальше
					// (2) с твёрдой - записываем текущий столбец и обнуляем его, чтобы знать, что дальше мы заходим с пустой клетки
					columns.add(new TileColumn(curColumn));
					curColumn.reset();
				}
			}
			// В конце каждого прохода по полю по вертикали нужно записать текущий столбец, если такой имеется
			if (curColumn.getId() != 1) { // 1 - id пустой клетки
				columns.add(new TileColumn(curColumn));
				curColumn.reset();
			}
		}
	}

	/**
	 * Создаёт создаёт твёрдое тело (Box2D) прямоугольной формы по указанному шаблону.
	 * Координаты, передаваемы в шаблоне должны быть координатами на игровой сетке.
	 * @param bodyTemplate Шаблон; определяет координаты вершин и тип прямоугольника
	 */
	public static void createRectangle(BodyIntRect bodyTemplate) {
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
		fxt.restitution = bodyTemplate.restitution;
		body.createFixture(fxt);
		body.setUserData(new TileCollisionListener(bodyTemplate.id));
		shape.dispose();
	}

	/**
	 * Находит в массиве клеток прямоугольники и регистрирует их как твёрдые тела Box2D.
	 * @param tiles Клетки игрового поля
	 */
	public static void createBodies(EmptyTile[][] tiles) {
		fillColumns(tiles);
		int expectedIndex = 0;
		BodyIntRect bodyTemplate = new BodyIntRect();
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).getId() != 0) { // нас интересуют столбцы, которые ещё не присоединили к прямоугольникам (у присоединённых id = 0)
				bodyTemplate.set(columns.get(i)); // Создаём прямоугольник из каждого такого столбца
				columns.get(i).deplete(); // помечаем этот столбец как присоединённый
				// Столбец, который можно будет присоединить должен иметь такой индекс (горизонтальную координату)
				expectedIndex = columns.get(i).getHorizontalIndex() + 1; 
				if (i < columns.size() - 1) { // Пробуем расширить прямоугольник, добавляя к нему новые столбцы:
					for (int j = i + 1; j < columns.size(); j++) {
						/*
						 * если в следующем столбце игрового поля не нашлось подходящих столбцов,
						 * создаём прямоугольник из того, что уже есть
						 */
						if (columns.get(j).getHorizontalIndex() > expectedIndex) break;
						// Столбец подходящий, если выполнены эти условия:
						if ((columns.get(j).getId() == bodyTemplate.id) &&
							(columns.get(j).getTop() == bodyTemplate.top) &&
							(columns.get(j).getBottom() == bodyTemplate.bottom)
							) {
							bodyTemplate.extend();
							columns.get(j).deplete();
							expectedIndex++;
						}
					}
				}
				createRectangle(bodyTemplate);
			}
		}
	}
}
