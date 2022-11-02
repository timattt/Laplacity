package steelUnicorn.laplacity.field.graphics;

import static steelUnicorn.laplacity.GameProcess.*;

import steelUnicorn.laplacity.chargedParticles.ChargedParticle;

public class ParticlesRenderer {

	public static void render(float delta) {
		cat.draw();
		for (ChargedParticle cp : particles) {
			cp.draw();
		}
	}

}
