package steelUnicorn.laplacity.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.VideoView;

import steelUnicorn.laplacity.R;

public class SplashScreen extends Activity {
    private VideoView introView;
    private int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // для мультика и так сойдёт
            getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.systemBars());
            // хуяк-хуяк и в продакшн
        }


        introView = this.findViewById(R.id.video_view);
        introView.setVideoURI(Uri.parse(
                    "android.resource://" + getPackageName() + "/" + R.raw.intro));

        introView.setOnPreparedListener(mp -> {
            introView.seekTo(position);
            introView.start();
        });

        introView.setOnCompletionListener(mp -> {
            Intent intent = new Intent(SplashScreen.this, AndroidLauncher.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        position = introView.getCurrentPosition();
        introView.pause();
    }
}
