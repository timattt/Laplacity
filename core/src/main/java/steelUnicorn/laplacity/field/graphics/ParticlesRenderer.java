package steelUnicorn.laplacity.field.graphics;

import static steelUnicorn.laplacity.GameProcess.*;
import steelUnicorn.laplacity.particles.ChargedParticle;

public class ParticlesRenderer {

	public static void render(float delta) {
		mainParticle.update(delta);
		mainParticle.draw();
		for (ChargedParticle cp : particles) {
			cp.draw();
			cp.update(delta);
		}
	}

}
