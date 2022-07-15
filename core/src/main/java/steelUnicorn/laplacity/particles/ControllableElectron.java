package steelUnicorn.laplacity.particles;

import static steelUnicorn.laplacity.GameProcess.*;
import static steelUnicorn.laplacity.Globals.*;
import steelUnicorn.laplacity.field.FieldPotentialCalculator;

public class ControllableElectron extends Electron {

	public ControllableElectron(float x, float y) {
		super(x, y);
	}

	@Override
	public void act(float delta) {
		FieldPotentialCalculator.calculateForce(getX(), getY(), field.getTiles(), TMP1);
		body.applyForceToCenter(TMP1.scl(charge), true);
		super.act(delta);
	}

}
