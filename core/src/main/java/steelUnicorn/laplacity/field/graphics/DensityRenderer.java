package steelUnicorn.laplacity.field.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.field.LaplacityField;
import steelUnicorn.laplacity.field.physics.IntRect;

/**
 * Класс, который быстро умеет рисовать плотность.
 * Кроме обычных методов для рендеринга (init, render, cleanup)
 * тут есть два, необходимые для взаимодействия с tilemap.
 * 
 * updateDensity - его нужно вызвать, когда требуется перерисовать текстуру с плотностью.
 * Нужно вызывать редко. После большого кол. вызовов следующего метода.
 * setTileDensity - его нужно вызывать, когда меняется плотность одного тайла.
 * 
 * @author timat
 *
 */
public class DensityRenderer {

	private static int blockWidth;
	private static int blockHeight;
	
	private static DensityBlock[][] blocks;
	
	private static ShaderProgram shader;
	
	private static int up(int a, int b) {
		return a / b + (a % b == 0 ? 0 : 1);
	}
	
	public static void init() {
		shader = new ShaderProgram(Gdx.files.internal("shaders/Density.vert"), Gdx.files.internal("shaders/Density.frag"));
		
		if (!shader.isCompiled()) {
			Gdx.app.log("shader compile", shader.getLog());
		}
			
			
		int fieldWidth = LaplacityField.fieldWidth;
		int fieldHeight = LaplacityField.fieldHeight;
		
		blockWidth = (int) Math.sqrt(fieldWidth);
		blockHeight = (int) Math.sqrt(fieldHeight);
		
		blocks = new DensityBlock[up(fieldWidth, blockWidth)][up(fieldHeight, blockHeight)];

		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				blocks[i][j] = new DensityBlock(new IntRect(i * blockWidth, j * blockHeight, (i + 1) * blockWidth - 1, (j + 1) * blockHeight - 1));
			}
		}
		
		updateDensity();
	}
	
	private static DensityBlock getBlock(int gridX, int gridY) {
		return blocks[gridX / blockWidth][gridY / blockHeight];
	}
	
	public static void updateDensity() {
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				if (blocks[i][j].repaintRequested) {
					blocks[i][j].repaintRequested = false;
					blocks[i][j].repaint();
				}
			}
		}
	}
	
	public static void setTileDensity(int x, int y, float val) {
		getBlock(x, y).repaintRequested = true;
	}
	
	public static void render() {
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				blocks[i][j].render();
			}
		}
	}
	
	public static void cleanup() {		
		if (shader != null) {
			shader.dispose();
			shader = null;
		}
		if (blocks == null) {
			return;
		}
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				blocks[i][j].dispose();
			}
		}
		blocks = null;

	}
	
	private static class DensityBlock {
		
		private IntRect bounds;
		private boolean repaintRequested;
		
		private Mesh mesh;
		private float[] verts;
		private short[] inds;
		
		public DensityBlock(IntRect bounds) {
			super();
			this.bounds = bounds;
			repaintRequested = true;			
			
			verts = new float[bounds.width() * bounds.height() * 5 * 3];
			inds = new short[verts.length * 2];
			repaint();
		}

		@Override
		public String toString() {
			return "DensityBlock [bounds=" + bounds + ", repaintRequested=" + repaintRequested + "]";
		}
		
		private static float dens(int x, int y) {
			if (x < LaplacityField.fieldWidth && y < LaplacityField.fieldHeight) {
				return LaplacityField.tiles[x][y].getChargeDensity();
			}
			return 0;
		}
		
		public void repaint() {
			dispose();
			float sz = LaplacityField.tileSize;
			
			int i = 0;
			int j = 0;
			for (int x = bounds.left; x <= bounds.right; x++) {
				for (int y = bounds.bottom; y <= bounds.top; y++) {
					int beg = (short) (i / 3);
					
					float v1 = dens(x, y);
					float v2 = dens(x, y + 1);
					float v3 = dens(x + 1, y + 1);
					float v4 = dens(x + 1, y);
					
					float v = (v1 + v2 + v3 + v4) / 4;
					
					verts[i] = x * sz;
					i++;
					verts[i] = y * sz;
					i++;
					verts[i] = v1;
					i++;

					verts[i] = x * sz;
					i++;
					verts[i] = (y + 1) * sz;
					i++;
					verts[i] = v2;
					i++;

					verts[i] = (x + 1) * sz;
					i++;
					verts[i] = (y + 1) * sz;
					i++;
					verts[i] = v3;
					i++;
					
					verts[i] = (x + 1) * sz;
					i++;
					verts[i] = y * sz;
					i++;
					verts[i] = v4;
					i++;
					
					verts[i] = (x + 0.5f) * sz;
					i++;
					verts[i] = (y + 0.5f) * sz;
					i++;
					verts[i] = v;
					i++;
				
					inds[j] = (short) beg; j++;
					inds[j] = (short) (beg+1); j++;
					inds[j] = (short) (beg+4); j++;
					
					inds[j] = (short) (beg+1); j++;
					inds[j] = (short) (beg+2); j++;
					inds[j] = (short) (beg+4); j++;
					
					inds[j] = (short) (beg+2); j++;
					inds[j] = (short) (beg+3); j++;
					inds[j] = (short) (beg+4); j++;
					
					inds[j] = (short) (beg+3); j++;
					inds[j] = (short) (beg); j++;
					inds[j] = (short) (beg+4); j++;
				}
			}

			mesh = new Mesh(true, verts.length / 3, inds.length,
					VertexAttribute.Position());
			mesh.setVertices(verts);
			mesh.setIndices(inds);
		}

		public void render() {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			shader.bind();
			shader.setUniformMatrix("u_projTrans", CameraManager.camMat());
			mesh.render(shader, GL20.GL_TRIANGLES);
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}

		public void dispose() {
			if (mesh != null) {
				mesh.dispose();
			}
		}
	}

}
