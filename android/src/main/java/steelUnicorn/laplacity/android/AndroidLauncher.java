package steelUnicorn.laplacity.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import steelUnicorn.laplacity.Laplacity;
import steelUnicorn.laplacity.AdHandler;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication implements AdHandler {
	
	private static final String AD_UNIT_ID_BANNER = "ca-app-pub-3299479021580908/9211056129";
	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-3299479021580908/5567641319";
	
	
	protected AdView adView;
	protected View gameView;
	private InterstitialAd interstitialAd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
		//initialize(new Laplacity(), configuration);

        RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		AdView admobView = createAdView();
		layout.addView(admobView);
		View gameView = createGameView(configuration);
		layout.addView(gameView);

		setContentView(layout);
		startAdvertising(admobView);

		MobileAds.initialize(this);
	}
	
	private AdView createAdView() {
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID_BANNER);
		//adView.setId(12345); // this is an arbitrary id, allows for relative positioning in createGameView()
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		adView.setLayoutParams(params);
		adView.setBackgroundColor(Color.BLACK);
		return adView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg) {
		gameView = initializeForView(new Laplacity(this), cfg);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.BELOW, adView.getId());
		gameView.setLayoutParams(params);
		return gameView;
	}

	private void startAdvertising(AdView adView) {
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	@Override
	public void showOrLoadInterstital() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if (interstitialAd != null) {
						interstitialAd.show(AndroidLauncher.this);
						Toast.makeText(getApplicationContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
					} else {
						AdRequest interstitialRequest = new AdRequest.Builder().build();
						InterstitialAd.load(AndroidLauncher.this, AD_UNIT_ID_INTERSTITIAL, interstitialRequest, new InterstitialAdLoadCallback() {
							@Override
							public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
								AndroidLauncher.this.interstitialAd = interstitialAd;
								Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
								interstitialAd.show(AndroidLauncher.this);
							}

							@Override
							public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
								Toast.makeText(getApplicationContext(), "Error Loading Interstitial " + loadAdError, Toast.LENGTH_SHORT).show();
							}
						});
						Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (Exception e) {
		}
	}
	
}