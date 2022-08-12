package steelUnicorn.laplacity.field.physics;

import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.tiles.EmptyTile;

/**
 * Класс, содержащий функции для расчёта физических параметров
 * электростатического поля.
 */
public class FieldCalculator {

	// Вычислительные константы
	// Можно перенести их в другое место из этого класса
	public static final float PRECISION = 0.05f;
	public static final int MAX_ITER_COUNT = 3000;
	public static final int ITER_PER_FRAME = (int) (MAX_ITER_COUNT / 60f);

	// Буферы для промежуточных вычислений
	private static int bufLength = 0;
	private static float[] residualsBuffer;
	private static float[] densityVector;
	private static float[] potentialVector;
	private static float[] tmpBuffer;

	// Параметры текущей сессии вычислений
	private static boolean calculating = false;
	private static int iterationsSpent = 0;
	private static int fWidth = 0;
	private static int fHeight = 0;
	private static float h = 0f;
	private static EmptyTile[][] tiles;

	/**
	 * Функция, вычисляющая i-й элемент свёртки заданного вектора с блочной матрицей свойств поля.
	 * В данной задаче матрица определяется всего тремя числами; более пордробно о задаче и
	 * методах решения можно прочитать в документе {@link https://github.com/timattt/Steel-unicorn/blob/master/About/Laplacity.md}.
	 * @param vec Вектор, с которым сворачивается матрица
	 * @param N Параметр матрицы: размерность одного блока (Здесь она равна ширине игрового поля)
	 * @param M Параметр матрицы: размерность матрицы в блоках (Здесь: высота игрового поля)
	 * @param h Параметр матрицы: шаг вычислительной сетки (Здесь: размер клетки игрового поля)
	 * @param convolved Вектор, в который записывается результат свёртки
	 */
	private static void intermediateConvolution(float[] vec, int N, int M, float h, float[] convolved) {
		int n = vec.length;
		if ((M * N != n) || (convolved.length != n)) {
			throw new RuntimeException("Fatal error: mismatched field and/or intermediate buffer dimensions");
		}
		if (M == 1) { // Вырожденный случай: поле единичной высоты
			convolved[0] = -4 * vec[0] + vec[1];
			for (int i = 1; i < N - 1; i++)
				convolved[i] = vec[i - 1] - 4 * vec[i] + vec[i + 1];
			convolved[N - 1] = vec[N - 2] - 4 * vec[N - 1];
		}
		else if (N == 1) { // Вырожденный случай: поле единичной ширины
			convolved[0] = -4 * vec[0] + vec[1];
			for (int j = 1; j < M - 1; j++)
				convolved[j] = vec[j - 1] - 4 * vec[j] + vec[j + 1];
			convolved[M - 1] = vec[M - 2] - 4 * vec[M - 1];
		} else { // Нормальный случай: поле имеет форму "матрицы", а не столбца или строки
			// Сворачиваем первый блок матрицы (Здесь нет левой единичной матрицы, см. схему по ссылке выше)
			convolved[0] = -4 * vec[0] + vec[1] + vec[N]; // первая, вырожденная строка блока
			for (int i = 1; i < N - 1; i++)
				convolved[i] = vec[i - 1] - 4 * vec[i] + vec[i + 1] + vec[i + N];
			convolved[N - 1] =  vec[N - 2] - 4 * vec[N - 1] + vec [N + N - 1]; // последняя, вырожденная строка блока

			// Сворачиваем нормальные блоки между первым и последним
			for (int j = 1; j < M - 1; j++){
				int l = j * N; // вспомогательный индекс, отвечает за выбор нужного блока
				convolved[l] = vec[l - N] - 4 * vec[l] + vec[l + 1] + vec[l + N]; // первая, вырожденная строка блока
				for (int i = 1; i < N - 1; i++)
					convolved[l + i] = vec[l + i - N] + vec[l + i - 1] - 4 * vec[l + i] + vec[l + i + 1] + vec[l + i + N];
				convolved[l + N - 1] =  vec[l - 1] + vec[l + N - 2] - 4 * vec[l + N - 1] + vec [l + N + N - 1]; // последняя, вырожденная строка блока
			}
			// Сворачиваем последний блок
			int l = (M - 1) * N;
			convolved[l] = vec[l - N] - 4 * vec[l] + vec[l + 1]; // первая, вырожденная строка блока
			for (int i = 1; i < N - 1; i++)
				convolved[l + i] = vec[l + i - N] + vec[l + i - 1] - 4 * vec[l + i] + vec[l + i + 1];
			convolved[l + N - 1] =  vec[l - 1] + vec[l + N - 2] - 4 * vec[l + N - 1]; // первая, вырожденная строка блока
			}
		// Нормируем вектор на величину шага сетки
		for (int i = 0; i < n; i++)
			convolved[i] /= (h * h);
	}

	/**
	 * Реализует метод градиентного спуска для уравнения Пуассона
	 * в прямоугольной области с нулевыми граничными условиями и
	 * квадратной равномерной вычислительной сеткой.
	 * @param coeffs Столбец коэффициентов
	 * @param N Ширина области в клетках
	 * @param M Высота области в клетках
	 * @param h Сторона клетки
	 * @param result Вектор, в который будет записан ответ
	 */
	private static void gradDescend(float[] coeffs, int N, int M, float h, float[] result) {
		int n = coeffs.length;
		if (M * N != n) {
			throw new RuntimeException("Fatal error: mismatched field and intermediate buffer dimensions");
		}

		// Задание начального приближения
		Arrays.fill(result, 0.0f);

		// Главный цикл итерационного метода
		for (int k = 0; k < MAX_ITER_COUNT; k++) {

			// Вычисление вектора невязки
			intermediateConvolution(result, N, M, h, residualsBuffer);
			for (int i = 0; i < n; i++)
				residualsBuffer[i] -= coeffs[i];

			// Временный вектор для вычисления шага итерации
			intermediateConvolution(residualsBuffer, N, M, h, tmpBuffer);

			// Вычисление шага итерации: числитель отдельно от знаменателя
			float residualsDotResiduals = 0, tmpDotResiduals = 0;
			for (int i = 0; i < n; i++) {
				residualsDotResiduals += residualsBuffer[i] * residualsBuffer[i];
				tmpDotResiduals += tmpBuffer[i] * residualsBuffer[i];
			}
			// Проверим, насколько близок к нулю знаменатель
			if (Math.abs(tmpDotResiduals) < PRECISION) {// Если невязка тоже небольшая, то скорее всего мы уже решили задачу
					break; // Эта ветвь часто достигается, когда поле изначально нулевое
			}
			float descendStepValue = residualsDotResiduals / tmpDotResiduals;

			// Записываем новую итерацию и проверяем условие нормального выхода из цикла
			float maxDifference = 0f, difference = 0f, nextIterOfResult = 0f;
			for (int i = 0; i < n; i++) {
				nextIterOfResult = result[i] - (descendStepValue * residualsBuffer[i]);
				difference = Math.abs(result[i] - nextIterOfResult);
				if (difference > maxDifference)
					maxDifference = difference;
				result[i] = nextIterOfResult;
			}
			if (maxDifference < PRECISION)
				break;
		}
		/*
		 * Случай, когда метод не сходится, никак не покрыт
		 * Опять же, задача хорошо изучена, и вероятность этого маленькая, но не ноль
		 * TODO: исключение для этого случая
		 */
	}

	private static void resizeBuffers(int newLength) {
		densityVector = new float[newLength];
		potentialVector = new float[newLength];
		residualsBuffer = new float[newLength];
		tmpBuffer = new float[newLength];
		bufLength = newLength;
	}

	/**
	 * Рассчитать потенциал электростатического поля для заданной плотности и нулевых граничных
	 * условий (граница начинается за пределами игрового поля). Метод перезаписывает
	 * поле {@linkplain EmptyTile#potential}. Подробное описание методов и алгоритма расчёта
	 * можно найти в {@link https://github.com/timattt/Steel-unicorn/blob/master/About/Laplacity.md}
	 * @param tiles Клетки игрового поля 
	 */
	public static void calculateFieldPotential(EmptyTile[][] tiles) {
		int fieldWidth = LaplacityField.fieldWidth, fieldHeight = LaplacityField.fieldHeight;
		if ((fieldWidth == 0) || (fieldHeight == 0))
			throw new RuntimeException("Fatal error: zero field dimensions");
		int n = fieldWidth * fieldHeight;
		if (n != bufLength)
			resizeBuffers(n);
		int k = 0;
		for (int i = 0; i < fieldWidth; i++)
			for (int j = 0; j < fieldHeight; j++)
				densityVector[k++] = -tiles[i][j].getTotalChargeDensity();
		gradDescend(densityVector, fieldHeight, fieldWidth, LaplacityField.tileSize, potentialVector);
		k = 0;
		for (int i = 0; i < fieldWidth; i++)
			for (int j = 0; j < fieldHeight; j++)
				tiles[i][j].setPotential(potentialVector[k++]);
	}

	public static void initPotentialCalculation(EmptyTile[][] _tiles) {
		fWidth = LaplacityField.fieldWidth;
		fHeight = LaplacityField.fieldHeight;
		h = LaplacityField.tileSize;
		iterationsSpent = 0;
		tiles = _tiles;
		if ((fWidth == 0) || (fHeight == 0))
			throw new RuntimeException("Fatal error: zero field dimensions");
		int n = fWidth * fHeight;
		if (n != bufLength)
			resizeBuffers(n);
		int k = 0;
		for (int i = 0; i < fWidth; i++)
			for (int j = 0; j < fHeight; j++)
				densityVector[k++] = -tiles[i][j].getTotalChargeDensity();
		calculating = true;
	}

	public static void resetPotential() {
		if (potentialVector != null)
			Arrays.fill(potentialVector, 0f);
	}

	public static void iterate() {
		if (calculating == false) {
			return;
		}
		for (int k = 0; k < ITER_PER_FRAME; k++) {
			// Вычисление вектора невязки
			intermediateConvolution(potentialVector, fHeight, fWidth, h, residualsBuffer);
			for (int i = 0; i < bufLength; i++)
				residualsBuffer[i] -= densityVector[i];
			// Временный вектор для вычисления шага итерации
			intermediateConvolution(residualsBuffer, fHeight, fWidth, h, tmpBuffer);
			// Вычисление шага итерации: числитель отдельно от знаменателя
			float residualsDotResiduals = 0, tmpDotResiduals = 0;
			for (int i = 0; i < bufLength; i++) {
				residualsDotResiduals += residualsBuffer[i] * residualsBuffer[i];
				tmpDotResiduals += tmpBuffer[i] * residualsBuffer[i];
			}
			// Проверим, насколько близок к нулю знаменатель
			if (Math.abs(tmpDotResiduals) < PRECISION) {// Если невязка тоже небольшая, то скорее всего мы уже решили задачу
				calculating = false;
				break;
			}
			float descendStepValue = residualsDotResiduals / tmpDotResiduals;
			// Записываем новую итерацию и проверяем условие нормального выхода из цикла
			float maxDifference = 0f, difference = 0f, nextIterOfResult = 0f;
			for (int i = 0; i < bufLength; i++) {
				nextIterOfResult = potentialVector[i] - (descendStepValue * residualsBuffer[i]);
				difference = Math.abs(potentialVector[i] - nextIterOfResult);
				if (difference > maxDifference)
					maxDifference = difference;
				potentialVector[i] = nextIterOfResult;
			}
			if (maxDifference < PRECISION) {
				calculating = false;
				break;
			}
		}
		iterationsSpent += ITER_PER_FRAME;
		if (iterationsSpent > MAX_ITER_COUNT) {
			calculating = false;
		}
		int k = 0;
		for (int i = 0; i < fWidth; i++)
			for (int j = 0; j < fHeight; j++)
				tiles[i][j].setPotential(potentialVector[k++]);
	}

	private static float twoPointScheme(float fMinus, float fPlus, float step) {
		return 0.5f * (fPlus - fMinus) / step;
	}
	
	/**
	 * Вычислить напряжённость электростатического поля в точке игрового поля,
	 * заданной вещественными координатами. Внимание: поле должно быть
	 * невырожденным, т.е. иметь высоту и ширину больше 1.
	 * @param x Горизонтальная координата
	 * @param y Вертикальная координата
	 * @param tiles Клетки игрового поля
	 * @param result Результат: напряжённость
	 */
	public static void calculateFieldIntensity(float x, float y, EmptyTile[][] tiles, Vector2 result) {
		// Переводим вещественные координаты в целочисленные индексы массива клеток
		float h = LaplacityField.tileSize;
		int i = (int)(x / h);
		int j = (int)(y / h);
		// Проверка границ:
		if ((i < 0) || (j < 0) || (i >= LaplacityField.fieldWidth) || (j >= LaplacityField.fieldHeight)) {
			result.setZero();
			return;
			//throw new RuntimeException("Attempt to calculate force outside the game field");
		}
		// Раздельно считаем каждую компоненту вектора (напряженность - градиент потенциала с обратным знаком)
		if (i == 0) { // (x,y) прилегает к левой границе
			result.x = -twoPointScheme(0.0f, tiles[i + 1][j].getPotential(), h);
		} else if (i == LaplacityField.fieldWidth - 1) { // к правой границе
			result.x = -twoPointScheme(tiles[i - 1][j].getPotential(), 0.0f, h);
		} else { // это внутрення точка
			result.x = -twoPointScheme(tiles[i - 1][j].getPotential(), tiles[i + 1][j].getPotential(), h);
		}
		// Повторим то же для оси Y
		if (j == 0) { // нижняя граница
			result.y = -twoPointScheme(0.0f, tiles[i][j + 1].getPotential(), h);
		} else if (j == LaplacityField.fieldHeight - 1) { // верхняя граница
			result.y = -twoPointScheme(tiles[i][j - 1].getPotential(), 0.0f, h);
		} else { // внутренняя точка
			result.y = -twoPointScheme(tiles[i][j - 1].getPotential(), tiles[i][j+1].getPotential(), h);
		}
	}

	public static void finishCalculation() {
		while (calculating) {
			iterate();
		}
	}

	public static boolean isCalculating() {
		return calculating;
	}
}
