package steelUnicorn.laplacity.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import steelUnicorn.laplacity.CameraManager;
import steelUnicorn.laplacity.GameProcess;

public class ParticlesManager {

	private static ShaderProgram shader = null;
	
	private static final Pool<Particle> pool = new Pool<Particle>() {
		@Override
		protected Particle newObject() {
			return new Particle();
		}};
	private static final Array<Particle> particles = new Array<Particle>();

	public static Particle createParticle(float x, float y, float w, float h, long durat, float angle, TextureRegion reg) {
		Particle p = pool.obtain();
		p.setX(x);
		p.setY(y);
		p.setStartTime(TimeUtils.millis());
		p.setTextureRegion(reg);
		p.setWidth(w);
		p.setHeight(h);
		p.setLifeTime(durat);
		p.setAngle(angle);
		p.setAlive(true);
		p.setScale(1f);
		particles.add(p);
		return p;
	}
	
	public static Particle createParticleInRandomCircle(float x, float y, float rad, float w, float h, long durat, TextureRegion[] regs, float angle) {
		float a = (float) (2.0*Math.PI * Math.random());
		float nx = (float) (x + rad * Math.cos(a));
		float ny = (float) (y + rad * Math.sin(a));
		return createParticle(nx, ny, w, h, durat, angle, regs[(int) (regs.length * Math.random())]);
	}
	
	public static void updateParticleSystem(float dt) {
		for (int i = particles.size; --i >= 0;) {
			Particle item = particles.get(i);
    	    item.update(dt);
    	    if (TimeUtils.millis() > item.getStartTime() + item.getLifeTime()) {
    	    	item.setAlive(false);
    	    }
    	    if (!item.isAlive()) {
    	    	particles.removeIndex(i);
    	    	pool.free(item);
    	    }
    	}	
	}
	
	public static void init() {
		shader = new ShaderProgram(Gdx.files.internal("shaders/Particles.vert"), Gdx.files.internal("shaders/Particles.frag"));
	
		if (!shader.isCompiled()) {
			Gdx.app.log("shader compile", shader.getLog());
		}
	}
	
	public static void cleanup() {
		if (shader != null) {
			shader.dispose();
			shader = null;
		}
	}
	
	public static void renderAllParticles() {
		GameProcess.gameBatch.enableBlending();
		shader.bind();
		GameProcess.gameBatch.setShader(shader);
		for (Particle p : particles) {
			if (p.getColor() != null) {
				shader.setUniformf("color", p.getColor());
			} else {
				shader.setUniformf("color", Vector3.Zero);
			}
			GameProcess.gameBatch.draw(p.getTextureRegion(), p.getX()-p.getWidth()/2, p.getY()-p.getHeight()/2, p.getWidth()/2, p.getHeight()/2, p.getWidth(), p.getHeight(), p.getScale(), p.getScale(), p.getAngle());
		}
		GameProcess.gameBatch.disableBlending();
		GameProcess.gameBatch.setShader(null);
	}

	public static Particle createParticle(float x, float y, float w, float h, long durat, float angle, TextureRegion[] regs) {
		return createParticle(x, y, w, h, durat, angle, regs[(int) (regs.length * Math.random())]);
	}
	
	

}
