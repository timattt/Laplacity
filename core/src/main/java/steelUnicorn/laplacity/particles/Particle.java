package steelUnicorn.laplacity.particles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

public class Particle {

	private TextureRegion textureRegion;
	
	private float x;
	private float y;
	
	private float width;
	private float height;
	
	private long startTime;
	private long lifeTime;
	
	private float angle = 0;
	
	private boolean alive = true;
	
	private float scale = 1f;
	
	public Particle() {
	}
	
	public void update(float dt) {
		scale = 1f - (float)(TimeUtils.millis() - startTime) / (float) lifeTime;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public boolean isAlive() {
		return alive;
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getAngle() {
		return angle;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
