package steelUnicorn.laplacity.android;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.VideoView;

import steelUnicorn.laplacity.R;

public class SplashScreen extends Activity implements MediaPlayer.OnCompletionListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // для мультика и так сойдёт
            getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.systemBars());
            // хуяк-хуяк и в продакшн
        }

        setContentView(R.layout.splash);

        String fileName = "android.resource://"+  getPackageName() +"/" + R.raw.intro;
        VideoView vv = (VideoView) this.findViewById(R.id.surface);
        vv.setVideoURI(Uri.parse(fileName));
        vv.setOnCompletionListener(this);
        vv.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        Intent intent = new Intent(this, AndroidLauncher.class);
        startActivity(intent);
        finish();
    }
}
