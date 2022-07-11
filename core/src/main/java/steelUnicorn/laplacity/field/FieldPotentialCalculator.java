package steelUnicorn.laplacity.field;

import steelUnicorn.laplacity.field.tiles.FieldTile;

public class FieldPotentialCalculator {

	private static float[] intermediateConvolution(float[] u, int N, int M) {
		int n = u.length;
		if (M * N != n) { // this is impossible, but...
			// throw exception here!
		}
		float[] f = new float[n];

		if (M == 1) { // degenerate case, this means a three-diagonal matrix
			f[0] = -4 * u[0] + u[1];
			for (int i = 1; i < N - 1; i++)
				f[i] = u[i - 1] - 4 * u[i] + u[i + 1];
			f[N - 1] = u[N - 2] - 4 * u[N - 1];
		}
		else if (N == 1) { // another degenerate case, this is also a three-diagonal matrix
			f[0] = -4 * u[0] + u[1];
			for (int j = 1; j < M - 1; j++)
				f[j] = u[j - 1] - 4 * u[j] + u[j + 1];
			f[M - 1] = u[M - 2] - 4 * u[M - 1];
		} else { // M and N are at least two, the "normal" case
			// Multiplying the first block (the first "row" of the grid)
			f[0] = -4 * u[0] + u[1] + u[N]; // the first, "degenerate" row
			for (int i = 1; i < N - 1;)
				f[i] = u[i - 1] - 4 * u[i] + u[i + 1] + u[i + N];
			f[N - 1] =  u[N - 2] - 4 * u[N - 1] + u [N + N - 1]; // the last, "degenerate" row

			// Multiplying the normal blocks in between
			for (int j = 1; j < M - 1; j++){
				int l = j * N; // auxilary variable, see below how it's used
				f[l] = u[l - N] - 4 * u[l] + u[l + 1] + u[l + N]; // the first, "degenerate" row
				for (int i = 1; i < N - 1; i++)
					f[l + i] = u[l + i - N] + u[l + i - 1] - 4 * u[l + i] + u[l + i + 1] + u[l + i + N];
				f[l + N - 1] =  u[l - 1] + u[l + N - 2] - 4 * u[l + N - 1] + u [l + N + N - 1]; // the last, "degenerate" row
			}
			// Multiplying the last block
			int l = (M - 1) * N;
			f[l] = u[l - N] - 4 * u[l] + u[l + 1]; // the first, "degenerate" row
			for (int i = 1; i < N - 1; i++)
				f[l + i] = u[l + i - N] + u[l + i - 1] - 4 * u[l + i] + u[l + i + 1];
			f[l + N - 1] =  u[l - 1] + u[l + N - 2] - 4 * u[l + N - 1]; // the last, "degenerate" row
			}
		return f;
	}

	private static float[] gradDescend(float[] f, float precision, int N, int M, float h, int n_iter) {
		int n = f.length;
		if (M * N != n) {
			// Throw exception here!
		}
		float[] x = new float[n];
		float[] rk_buffer = new float[n];
		for (int k = 0; k < n_iter; k++) {
			rk_buffer = intermediateConvolution(x, N, M); // now "rk_buffer" is "a @ x" in Python notation
			float tk_numerator = 0, tk_denumerator = 0;
			// now let's calculate residual (element-wise) and descend step value ("tk" variable)
			for (int i = 0; i < n; i++) {
				float res = rk_buffer[i] - f[i];
				tk_numerator += res * res;
				tk_denumerator += res * rk_buffer[i];
				rk_buffer[i] = res;
			}
			/*
			 * After this loop "rk_buffer" holds a residuals vector (rk),
			 * tk_numerator is dot(rk, rk),
			 * and tk_denumerator is dot((a @ x), rk)
			 */
			float tk = tk_numerator / tk_denumerator;
			float delta = 0, local_delta = 0;
			for (int i = 0; i < n; i++) {
				float next_x = x[i] - (tk * rk_buffer[i]);
				local_delta = Math.abs(x[i] - next_x);
				if (local_delta > delta)
					delta = local_delta;
				x[i] = next_x;
			}
			if (delta < precision)
				break;
		}
		return x;
	}

	public static void calculateFieldPotential(FieldTile[][] tiles) {
		// TODO calculate Dirichlet task here
	}

}
