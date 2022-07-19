package steelUnicorn.laplacity.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import steelUnicorn.laplacity.Laplacity;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.*;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
	
	 private InterstitialAd mInterstitialAd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
		initialize(new Laplacity(), configuration);

        MobileAds.initialize(this);
		
      AdRequest adRequest = new AdRequest.Builder().build();

      InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
        new InterstitialAdLoadCallback() {
      @Override
      public void onAdLoaded(InterstitialAd interstitialAd) {
        // The mInterstitialAd reference will be null until
        // an ad is loaded.
        mInterstitialAd = interstitialAd;
       // Log.i("TestActivity", "onAdLoaded");
      }

      @Override
      public void onAdFailedToLoad(LoadAdError loadAdError) {
        // Handle the error
       // Log.d("TestActivity", loadAdError.toString());
        mInterstitialAd = null;
      }
    });
	}
}