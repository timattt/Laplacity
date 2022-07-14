package steelUnicorn.laplacity.field;

import java.util.Arrays;
import com.badlogic.gdx.math.Vector2;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.field.tiles.FieldTile;

public class FieldPotentialCalculator {

	public static final float precision = 0.001f;
	public static final int n_iter = 1000;

	private static int buf_length = 0;
	private static float[] rk_buffer;
	private static float[] density_vector;
	private static float[] potential_vector;
	private static float[] a_conv_rk;

	private static void intermediateConvolution(float[] dst, float[] u, int N, int M, float h) {
		int n = u.length;
		if ((M * N != n) || (dst.length != n)) {
			throw new RuntimeException("Fatal error: mismatched field and/or intermediate buffer dimensions");
		}
		if (M == 1) { // degenerate case, this means a three-diagonal matrix
			dst[0] = -4 * u[0] + u[1];
			for (int i = 1; i < N - 1; i++)
				dst[i] = u[i - 1] - 4 * u[i] + u[i + 1];
			dst[N - 1] = u[N - 2] - 4 * u[N - 1];
		}
		else if (N == 1) { // another degenerate case, this is also a three-diagonal matrix
			dst[0] = -4 * u[0] + u[1];
			for (int j = 1; j < M - 1; j++)
				dst[j] = u[j - 1] - 4 * u[j] + u[j + 1];
			dst[M - 1] = u[M - 2] - 4 * u[M - 1];
		} else { // M and N are at least two, the "normal" case
			// Multiplying the first block (the first "row" of the grid)
			dst[0] = -4 * u[0] + u[1] + u[N]; // the first, "degenerate" row
			for (int i = 1; i < N - 1; i++)
				dst[i] = u[i - 1] - 4 * u[i] + u[i + 1] + u[i + N];
			dst[N - 1] =  u[N - 2] - 4 * u[N - 1] + u [N + N - 1]; // the last, "degenerate" row

			// Multiplying the normal blocks in between
			for (int j = 1; j < M - 1; j++){
				int l = j * N; // auxilary variable, see below how it's used
				dst[l] = u[l - N] - 4 * u[l] + u[l + 1] + u[l + N]; // the first, "degenerate" row
				for (int i = 1; i < N - 1; i++)
					dst[l + i] = u[l + i - N] + u[l + i - 1] - 4 * u[l + i] + u[l + i + 1] + u[l + i + N];
				dst[l + N - 1] =  u[l - 1] + u[l + N - 2] - 4 * u[l + N - 1] + u [l + N + N - 1]; // the last, "degenerate" row
			}
			// Multiplying the last block
			int l = (M - 1) * N;
			dst[l] = u[l - N] - 4 * u[l] + u[l + 1]; // the first, "degenerate" row
			for (int i = 1; i < N - 1; i++)
				dst[l + i] = u[l + i - N] + u[l + i - 1] - 4 * u[l + i] + u[l + i + 1];
			dst[l + N - 1] =  u[l - 1] + u[l + N - 2] - 4 * u[l + N - 1]; // the last, "degenerate" row
			}
		for (int i = 0; i < n; i++)
			dst[i] /= (h * h);
	}

	private static void gradDescend(float[] f, float[] dst, float precision, int N, int M, float h, int n_iter) {
		int n = f.length;
		if (M * N != n) {
			throw new RuntimeException("Fatal error: mismatched field and intermediate buffer dimensions");
		}
		Arrays.fill(dst, 0.0f);
		for (int k = 0; k < n_iter; k++) {
			intermediateConvolution(rk_buffer, dst, N, M, h); // now "rk_buffer" is "a @ x" in Python notation
			for (int i = 0; i < n; i++)
				rk_buffer[i] -= f[i];
			intermediateConvolution(a_conv_rk, rk_buffer, N, M, h);
			float tk_numerator = 0, tk_denumerator = 0;
			// now let's calculate residual (element-wise) and descend step value ("tk" variable)
			for (int i = 0; i < n; i++) {
				tk_numerator += rk_buffer[i] * rk_buffer[i];
				tk_denumerator += rk_buffer[i] * a_conv_rk[i];
			}
			/*
			 * After this loop "rk_buffer" holds a residuals vector (rk),
			 * tk_numerator is dot(rk, rk),
			 * and tk_denumerator is dot((a @ x), rk)
			 */
			float tk = tk_numerator / tk_denumerator;
			float delta = 0, local_delta = 0;
			for (int i = 0; i < n; i++) {
				float next_x = dst[i] - (tk * rk_buffer[i]);
				local_delta = Math.abs(dst[i] - next_x);
				if (local_delta > delta)
					delta = local_delta;
				dst[i] = next_x;
			}
			if (delta < precision)
				break;
			/*
			 * The case when it does not converge is not handled.
			 * Well, it *must* converge with that given matrix, but still it may take more iterations that n_iter.
			 * Maybe here we should throw an exception that would show a message like "Your field is too powerful:( , move your charges a bit!"
			 * and switch the gamemode back from simulation to edit?
			 * TODO: exception handling
			 */
		}
	}

	private static void resizeBuffers(int new_length) {
		density_vector = new float[new_length];
		potential_vector = new float[new_length];
		rk_buffer = new float[new_length];
		a_conv_rk = new float[new_length];
		buf_length = new_length;
	}

	public static void calculateFieldPotential(FieldTile[][] tiles) {
		int field_width = GameProcess.field.getFieldWidth(), field_height =  GameProcess.field.getFieldHeight();
		if ((field_width == 0) || (field_height == 0))
			throw new RuntimeException("Fatal error: zero field dimensions");
		int n = field_width * field_height;
		if (n != buf_length)
			resizeBuffers(n);
		int k = 0;
		for (int i = 0; i < field_width; i++)
			for (int j = 0; j < field_height; j++)
				density_vector[k++] = tiles[i][j].getChargeDensity();
		gradDescend(density_vector, potential_vector, precision, field_height, field_width, GameProcess.field.getTileSize(), n_iter);
		k = 0;
		for (int i = 0; i < field_width; i++)
			for (int j = 0; j < field_height; j++)
				tiles[i][j].setPotential(potential_vector[k++]);
	}

	private static float twoPointScheme(float f_minus, float f_plus, float step) {
		return 0.5f * (f_plus - f_minus) / step;
	}
	
	/**
	 * Считаем силу в заданной точке. И кладем ее в result.
	 * Перед вызовом убедись, что поле -- это не палка размерности M*1 или 1*N
	 * Потенциал для такой фигни скорее всего считается, а сила нет
	 * Можно сделать, чтобы считалось, но это по-моему лишнее
	 */
	public static void calculateForce(float x, float y, FieldTile[][] tiles, Vector2 result) {
		// Get integer indices of the tile the (x,y) poitn currently in\
		float h = GameProcess.field.getTileSize();
		int i = (int)(x / h);
		int j = (int)(y / h);
		// Check if we aren't out of boundaries:
		if ((i < 0) || (j < 0) || (i >= GameProcess.field.getFieldWidth()) || (j >= GameProcess.field.getFieldHeight() )) {
			throw new RuntimeException("Attempt to calculate force outside the game field");
		}
		// Separately calculate derivatives:
			if (i == 0) { // (x,y) is adjacent to the lower edge
				result.x = -twoPointScheme(0.0f, tiles[i + 1][j].getPotential(), h) * GameProcess.ELECTRON_CHARGE;
			} else if (i == GameProcess.field.getFieldWidth() - 1) { // Upper edge
				result.x = -twoPointScheme(tiles[i - 1][j].getPotential(), 0.0f, h) * GameProcess.ELECTRON_CHARGE;
			} else { // Inner point
				result.x = -twoPointScheme(tiles[i - 1][j].getPotential(), tiles[i + 1][j].getPotential(), h) * GameProcess.ELECTRON_CHARGE;
			}
			// Repeat this for y
			if (j == 0) { //Left edge
				result.y = -twoPointScheme(0.0f, tiles[i][j + 1].getPotential(), h) * GameProcess.ELECTRON_CHARGE;
			} else if (j == GameProcess.field.getFieldHeight() - 1) { // Right edge
				result.y = -twoPointScheme(tiles[i][j - 1].getPotential(), 0.0f, h) * GameProcess.ELECTRON_CHARGE;
			} else { // Inner point
				result.y = -twoPointScheme(tiles[i][j - 1].getPotential(), tiles[i][j+1].getPotential(), h) * GameProcess.ELECTRON_CHARGE;
			}
		}

}
