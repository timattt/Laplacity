package steelUnicorn.laplacity.ui.handler;

import steelUnicorn.laplacity.chargedParticles.ChargedParticle;

public class ParticleHandler {
    public ChargedParticle particle;

    private boolean wasPlaced;

    public ParticleHandler() {
        wasPlaced = false;
    }

    public void placeParticle(ChargedParticle particle) {
        this.particle = particle;
        wasPlaced = true;
    }

    public boolean wasParticlePlaced() {
        if (wasPlaced) {
            wasPlaced = false;
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "ParticleHandler{" +
                "particle=" + particle +
                ", wasPlaced=" + wasPlaced +
                "\n";
    }
}
