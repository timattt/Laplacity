package steelUnicorn.laplacity.field.structures;

import com.badlogic.gdx.graphics.Pixmap;

import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.CollisionListener;
import steelUnicorn.laplacity.field.physics.IntRect;

/**
 * Класс, который представляет собой тайловую структуру на карте. В конструктор передаются координаты клетки,
 * которая принадлежит структуре. И дальше структура уже инициализирует сама себя. В ней есть поле bounds.
 * Оно отпределяет границы ее. Тут еще есть методы для регистрации на поле, обновление, уничтожение и т.д.
 * @author timat
 *
 */
public class FieldStructure implements CollisionListener {

	protected IntRect bounds;
	
	public FieldStructure(int left, int bottom, Pixmap pm, int[] structureCodes) {
		bounds = new IntRect();
		findRect(pm, left, bottom, structureCodes, bounds);
	}
	
	public boolean contains(int x, int y) {
		return bounds.containsPoint(x, y);
	}

	protected static void findRect(Pixmap pm, int startX, int startY, int[] codes, IntRect result) {
		int x = startX;
		int y = startY;
		
		// right
		A:while (true) {
			for (int c : codes) {
				if (c == pm.getPixel(x, LaplacityField.fieldHeight - y - 1)) {
					x++;
					continue A;
				}
			}
			break A;
		}	
		x--;
		result.right = x;
		
		// left
		A:while (true) {
			for (int c : codes) {
				if (c == pm.getPixel(x, LaplacityField.fieldHeight - y - 1)) {
					x--;
					continue A;
				}
			}
			break A;
		}	
		x++;
		result.left = x;
		
		// top
		A:while (true) {
			for (int c : codes) {
				if (c == pm.getPixel(x, LaplacityField.fieldHeight - y - 1)) {
					y++;
					continue A;
				}
			}
			break A;
			
		}
		y--;
		result.top = y;

		// bottom
		A:while (true) {
			for (int c : codes) {
				if (c == pm.getPixel(x, LaplacityField.fieldHeight - y - 1)) {
					y--;
					continue A;
				}
			}
			break A;
			
		}
		y++;
		result.bottom = y;
		
	}
	
	protected static void findSubRect(Pixmap pm, IntRect main, int code, IntRect result) {
		int x = main.left;
		int y = main.bottom;
		A:while (y <= main.top) {
			x = main.left;
			while (x <= main.right) {
				if (pm.getPixel(x, LaplacityField.fieldHeight - y - 1) == code) {
					break A;
				}
				x++;
			}
			y++;
		}
		
		// right
		while (pm.getPixel(x, LaplacityField.fieldHeight - y - 1) == code) {
			x++;
		}
		x--;
		result.right = x;
		
		// left
		while (pm.getPixel(x, LaplacityField.fieldHeight - y - 1) == code) {
			x--;
		}
		x++;
		result.left = x;
		
		// top
		while (pm.getPixel(x, LaplacityField.fieldHeight - y - 1) == code) {
			y++;
		}
		y--;
		
		result.top = y;

		// bottom
		while (pm.getPixel(x, LaplacityField.fieldHeight - y - 1) == code) {
			y--;
		}
		y++;
		
		result.bottom = y;	
	}
	
	public void register() {
		
	}

	public void render(float timeFromStart) {
		
	}
	
	public void cleanup() {
		
	}
	
	public void reset() {
		
	}

	@Override
	public boolean isDeadly() {
		return false;
	}

	@Override
	public void collidedWithDeadly() {
	}

	@Override
	public boolean isFinish() {
		return false;
	}

	@Override
	public void collidedWithFinish() {
	}

	@Override
	public boolean isMainParticle() {
		return false;
	}

	@Override
	public void collidedWithMainParticle() {
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public void collidedWithStructure() {
	}

	@Override
	public boolean isTile() {
		return false;
	}

	@Override
	public void collidedWithTile() {
	}
}
