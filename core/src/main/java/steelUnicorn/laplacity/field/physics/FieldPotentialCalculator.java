package steelUnicorn.laplacity.field.physics;

import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.tiles.EmptyTile;

/**
 * Класс, содержащий функции для расчёта физических параметров
 * электростатического поля.
 */
public class FieldPotentialCalculator {

	// Constants
	public static final float precision = 0.001f;
	public static final int maxIter = 1000;

	// Buffers for potential calculation
	private static int bufLength = 0;
	private static float[] residualsBuffer;
	private static float[] densityVector;
	private static float[] potentialVector;
	private static float[] tmpBuffer;

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

	private static void gradDescend(float[] coeffs, int N, int M, float h, float[] result) {
		int n = coeffs.length;
		if (M * N != n) {
			throw new RuntimeException("Fatal error: mismatched field and intermediate buffer dimensions");
		}
		Arrays.fill(result, 0.0f);
		for (int k = 0; k < maxIter; k++) {
			intermediateConvolution(result, N, M, h, residualsBuffer); // now "rk_buffer" is "a @ x" in Python notation
			for (int i = 0; i < n; i++)
				residualsBuffer[i] -= coeffs[i];
			intermediateConvolution(residualsBuffer, N, M, h, tmpBuffer);
			float residualsDotResiduals = 0, tmpDotResiduals = 0;
			// now let's calculate residual (element-wise) and descend step value ("tk" variable)
			for (int i = 0; i < n; i++) {
				residualsDotResiduals += residualsBuffer[i] * residualsBuffer[i];
				tmpDotResiduals += tmpBuffer[i] * residualsBuffer[i];
			}
			/*
			 * After this loop "rk_buffer" holds a residuals vector (rk),
			 * tk_numerator is dot(rk, rk),
			 * and tk_denumerator is dot((a @ x), rk)
			 */
			// Check if denumarator is zero
			if (Math.abs(tmpDotResiduals) < precision) { // (= is close to zero, may cause NAN or INF)
				if (Math.abs(residualsDotResiduals) < precision) { // if rk is close to zero too
					break; // then we have already solved the problem
				} else {
					throw new RuntimeException("Fatal computational error: redraw your field");
				}
			}
			float descendStepValue = residualsDotResiduals / tmpDotResiduals;
			float maxDifference = 0f, difference = 0f, nextIterOfResult = 0f;
			for (int i = 0; i < n; i++) {
				nextIterOfResult = result[i] - (descendStepValue * residualsBuffer[i]);
				difference = Math.abs(result[i] - nextIterOfResult);
				if (difference > maxDifference)
					maxDifference = difference;
				result[i] = nextIterOfResult;
			}
			if (maxDifference < precision)
				break;
		}
		/*
		 * The case when it does not converge is not handled.
		 * Well, it *must* converge with that given matrix, but still it may take more iterations that n_iter.
		 * Maybe here we should throw an exception that would show a message like "Your field is too powerful:( , move your charges a bit!"
		 * and switch the gamemode back from simulation to edit?
		 * TODO: exception handling
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
	 * Рассчитать 
	 * Подробное описание методов и алгоритма расчёта можно найти в
	 * {@link https://github.com/timattt/Steel-unicorn/blob/master/About/Laplacity.md}
	 * @param tiles
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

	private static float twoPointScheme(float fMinus, float fPlus, float step) {
		return 0.5f * (fPlus - fMinus) / step;
	}
	
	/**
	 * Считаем напряженность поля в заданной точке. И кладем ее в result.
	 * Перед вызовом убедись, что поле -- это не палка размерности M*1 или 1*N
	 * Потенциал для такой фигни скорее всего считается, а сила нет
	 * Можно сделать, чтобы считалось, но это по-моему лишнее
	 */
	public static void calculateFieldIntensity(float x, float y, EmptyTile[][] tiles, Vector2 result) {
		// Get integer indices of the tile the (x,y) poitn currently in\
		float h = LaplacityField.tileSize;
		int i = (int)(x / h);
		int j = (int)(y / h);
		// Check if we aren't out of boundaries:
		if ((i < 0) || (j < 0) || (i >= LaplacityField.fieldWidth) || (j >= LaplacityField.fieldHeight)) {
			result.setZero();
			return;
			//throw new RuntimeException("Attempt to calculate force outside the game field");
		}
		// Separately calculate derivatives:
			if (i == 0) { // (x,y) is adjacent to the lower edge
				result.x = -twoPointScheme(0.0f, tiles[i + 1][j].getPotential(), h);
			} else if (i == LaplacityField.fieldWidth - 1) { // Upper edge
				result.x = -twoPointScheme(tiles[i - 1][j].getPotential(), 0.0f, h);
			} else { // Inner point
				result.x = -twoPointScheme(tiles[i - 1][j].getPotential(), tiles[i + 1][j].getPotential(), h);
			}
			// Repeat this for y
			if (j == 0) { //Left edge
				result.y = -twoPointScheme(0.0f, tiles[i][j + 1].getPotential(), h);
			} else if (j == LaplacityField.fieldHeight - 1) { // Right edge
				result.y = -twoPointScheme(tiles[i][j - 1].getPotential(), 0.0f, h);
			} else { // Inner point
				result.y = -twoPointScheme(tiles[i][j - 1].getPotential(), tiles[i][j+1].getPotential(), h);
			}
		}

}
