package steelUnicorn.laplacity.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LevelButton extends TextButton {
	private FileHandle lvlImg;

	public LevelButton(String text, Skin skin, FileHandle lvlImg) {
		super(text, skin);
		this.lvlImg = lvlImg;
	}

	public FileHandle getLevel() {
		return lvlImg;
	}
}
