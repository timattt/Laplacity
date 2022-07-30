package steelUnicorn.laplacity.field.structures.rigid;

import com.badlogic.gdx.graphics.Pixmap;

import steelUnicorn.laplacity.core.LaplacityAssets;

public class Gift extends RigidStructure {

	public Gift(int left, int bottom, Pixmap pm) {
		super(left, bottom, pm, new int[] {2021130495}, 10f, "rigidObjects/gift.json", LaplacityAssets.GIFT_TEXTURE, "gift");
	}

}
