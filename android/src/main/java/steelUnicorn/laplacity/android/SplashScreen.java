package steelUnicorn.laplacity.android;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import steelUnicorn.laplacity.R;

public class SplashScreen extends Activity implements MediaPlayer.OnCompletionListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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
