package steelUnicorn.laplacity.tutorial;

import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.GameProcess;
import steelUnicorn.laplacity.core.LaplacityAssets;

public class Pointer {

	private static final float DEFAULT_SIZE = 15f;
	
	// transform
	private float x;
	private float y;
	
	private float width = DEFAULT_SIZE;
	private float height = DEFAULT_SIZE;
	
	// linear animation
	private float startX;
	private float startY;
	
	private float endX;
	private float endY;
	
	private long cycleTime;
	private long startTime;
	
	private boolean linearAnimation = false;
	
	public void pointTo(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void hide() {
		x = 100000f;
		y = 100000f;
		linearAnimation = false;
	}
	
	public void linearAnimation(float startX, float startY, float endX, float endY, long cycle) {
		this.startX = startX;
		this.startY = startY;
		
		this.endX = endX;
		this.endY = endY;
		
		cycleTime = cycle;
		startTime = TimeUtils.millis();
		
		linearAnimation = true;
	}
	
	public void moveTo(float endX, float endY, long cycle) {
		linearAnimation = false;
		if (x > 10000f) {
			x = endX;
			y = endY;
			return;
		}
		
		linearAnimation(x, y, endX, endY, cycle);
	}
	
	public void draw() {
		GameProcess.gameBatch.enableBlending();
		GameProcess.gameBatch.draw(LaplacityAssets.HINT_POINTER, x - width * 0.4f, y, width, height);
		GameProcess.gameBatch.disableBlending();
	}

	public void update() {
		if (linearAnimation) {
			long cur = TimeUtils.millis();

			long delta = cur - startTime;

			float arg = 2 * 3.1415f * (float) delta / (float) cycleTime;
			float coef = (1f + (float) Math.sin(arg)) / 2f;

			x = startX + coef * (endX - startX);
			y = startY + coef * (endY - startY);
		}
	}
	
}