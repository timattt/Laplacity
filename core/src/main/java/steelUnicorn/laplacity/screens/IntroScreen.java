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

public class IntroScreen extends ManagedScreen {
    SpriteBatch batch;
    OrthographicCamera camera;
    VideoPlayer videoPlayer;

    @Override
    public void hide() {
    }

    @Override
    public void resize(int width, int height) {

    }

    public IntroScreen() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener (FileHandle file) {
                Gdx.app.log("VideoTest", file.name() + " fully played.");
            }
        });
        videoPlayer.setOnVideoSizeListener(new VideoPlayer.VideoSizeListener() {
            @Override
            public void onVideoSize (float width, float height) {
                Gdx.app.log("VideoTest", "The video has a size of " + width + "x" + height + ".");
            }
        });


    }

    @Override
    protected void create() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            try {
                videoPlayer.play(Gdx.files.internal("ui/intro.ogg"));
            } catch (FileNotFoundException e) {
                Gdx.app.error("gdx-video", "Oh no! " + e.getMessage());
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        videoPlayer.update();
        batch.begin();
        Texture frame = videoPlayer.getTexture();
        if (frame != null) {
            batch.draw(frame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.app.log("frame", "frame getted");
        }
        batch.end();
    }

    @Override
    public void dispose() {
        videoPlayer.dispose();
    }
}
