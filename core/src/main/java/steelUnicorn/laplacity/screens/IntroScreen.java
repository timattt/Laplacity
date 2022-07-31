package steelUnicorn.laplacity.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import java.io.FileNotFoundException;

import de.eskalon.commons.screen.ManagedScreen;
import steelUnicorn.laplacity.core.Globals;
import steelUnicorn.laplacity.core.LaplacityAssets;

/**
 * Скрин подгружающий интро!
 */
public class IntroScreen extends ManagedScreen {
    private SpriteBatch batch;
    private VideoPlayer videoPlayer;

    @Override
    protected void create() {
        Gdx.app.log("IntroScreen", "start creation");
        batch = new SpriteBatch();
        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener (FileHandle file) {
                Gdx.app.log("VideoTest", file.name() + " fully played.");
                LaplacityAssets.changeTrack("music/main_menu.mp3");
                Globals.game.getScreenManager().pushScreen(Globals.nameMainMenuScreen,
                        Globals.nameSlideIn);
            }
        });

        try {
            videoPlayer.play(Gdx.files.internal("ui/intro.ogv"));
        } catch (FileNotFoundException e) {
            Gdx.app.error("IntroScreen", e.getMessage());
        }

        Gdx.app.log("IntroScreen", "Created " + videoPlayer.getClass().getName());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        videoPlayer.update();

        batch.begin();
        Texture frame = videoPlayer.getTexture();
        if (frame != null) {
            batch.draw(frame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.end();
    }


    @Override
    public void pause() {
        super.pause();
        videoPlayer.pause();
    }

    @Override
    public void resume() {
        super.resume();
        videoPlayer.resume();
    }

    @Override
    public void hide() {
    }

    @Override
    public void resize(int width, int height) {

    }
    @Override
    public void dispose() {
        batch.dispose();
        videoPlayer.dispose();
    }
}
