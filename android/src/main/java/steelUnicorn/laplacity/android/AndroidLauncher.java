package steelUnicorn.laplacity.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import barsoosayque.libgdxoboe.OboeAudio;
import steelUnicorn.laplacity.core.Laplacity;
import steelUnicorn.laplacity.utils.AdHandler;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.badlogic.gdx.backends.android.AsynchronousAndroidAudio;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.ToastConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;

import com.ironsource.adapters.supersonicads.SupersonicConfig;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.impressionData.ImpressionData;
import com.ironsource.mediationsdk.impressionData.ImpressionDataListener;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.sdk.OfferwallListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.ironsource.mediationsdk.utils.IronSourceUtils;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication implements AdHandler {

	private static final String AD_UNIT_ID_BANNER = "ca-app-pub-3299479021580908/9211056129";
	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-3299479021580908/5567641319";
	private static final String AD_UNIT_ID_REWARDED = "ca-app-pub-3299479021580908/1017784530";

	protected AdView adView;
	protected View gameView;
	private InterstitialAd interstitialAd;
	private RewardedAd rewardedAd;

	private static boolean useIron = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
		configuration.useImmersiveMode = true;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // if current version supports screen cutouts
			getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
		}

		RelativeLayout layout = new RelativeLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		AdView admobView = createAdView();
		layout.addView(admobView);
		View gameView = createGameView(configuration);
		layout.addView(gameView);

		setContentView(layout);
		startAdvertising(admobView);

		MobileAds.initialize(this);
		
		if (useIron)
			IronSource.init(this, "15fc33d15l");

        ACRA.init(getApplication(), new CoreConfigurationBuilder()
			//core configuration:
			.withReportFormat(StringFormat.JSON)
			.withPluginConfigurations(
				//each plugin you chose above can be configured with its builder like this:
				new MailSenderConfigurationBuilder()
				//required
				.withMailTo("support@steel-uni.com")
				//defaults to true
				.withReportAsFile(true)
				//defaults to ACRA-report.stacktrace
				.withReportFileName("Crash.json")
				//defaults to "<applicationId> Crash Report"
				.withSubject("Error in Cataway")
				//defaults to empty
				.withBody("Error message for Cataway game")
				.build()
			)
		);
   
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
					ACRA.getErrorReporter().handleException(paramThrowable);
				}
			});
			
		if (useIron)
			IronSource.setInterstitialListener(new InterstitialListener() {
			/**
			 * Invoked when Interstitial Ad is ready to be shown after load function was called.
			 */
			@Override
			public void onInterstitialAdReady() {
				message("ready");
			}
			/**
			 * invoked when there is no Interstitial Ad available after calling load function.
			 */
			@Override
			public void onInterstitialAdLoadFailed(IronSourceError error) {
				message(error.toString());
			}
			/**
			 * Invoked when the Interstitial Ad Unit is opened
			 */
			@Override
			public void onInterstitialAdOpened() {
			}
			/*
			 * Invoked when the ad is closed and the user is about to return to the application.
			 */
			@Override
			public void onInterstitialAdClosed() {
			}
			/**
			 * Invoked when Interstitial ad failed to show.
			 * @param error - An object which represents the reason of showInterstitial failure.
			 */
			@Override
			public void onInterstitialAdShowFailed(IronSourceError error) {
				message(error.toString());
			}
			/*
			 * Invoked when the end user clicked on the interstitial ad, for supported networks only. 
			 */
			@Override
			public void onInterstitialAdClicked() {
			}
		   /** Invoked right before the Interstitial screen is about to open. 
			*  NOTE - This event is available only for some of the networks. 
			*  You should NOT treat this event as an interstitial impression, but rather use InterstitialAdOpenedEvent 
			*/ 
			@Override 
			public void onInterstitialAdShowSucceeded() { 
			}
		});
		
		IntegrationHelper.validateIntegration(this);
	}

	private AdView createAdView() {
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID_BANNER);
		// adView.setId(12345); // this is an arbitrary id, allows for relative
		// positioning in createGameView()
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		adView.setLayoutParams(params);
		adView.setBackgroundColor(Color.BLACK);
		return adView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg) {
		gameView = initializeForView(new Laplacity(this), cfg);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
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
	
	private void message(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showOrLoadInterstital() {
		try {
			runOnUiThread(new Runnable() {
				
				public void run() {
					if (useIron) {
						IronSource.showInterstitial("secondAd");
					} else {
						AdRequest interstitialRequest = new AdRequest.Builder().build();
						InterstitialAd.load(AndroidLauncher.this, AD_UNIT_ID_INTERSTITIAL, interstitialRequest, new InterstitialAdLoadCallback() {
									@Override
									public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
										message("finished loading interstitial!");
										AndroidLauncher.this.interstitialAd = interstitialAd;
										interstitialAd.show(AndroidLauncher.this);
									}

									@Override
									public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
										message("error while loading interstitial: " + loadAdError);
									}
						});
						message("loading interstitial");
					}
				}
			});
		} catch (Exception e) {
		}
	}

	@Override
	public void showOrLoadRewarded() {
		
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if (useIron) {
						IronSource.showRewardedVideo("mainAd");
					} else {
						AdRequest adRequest = new AdRequest.Builder().build();

						RewardedAd.load(AndroidLauncher.this, AD_UNIT_ID_REWARDED, adRequest, new RewardedAdLoadCallback() {
									@Override
									public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
										message("error while loading rewarded: " + loadAdError);
										rewardedAd = null;
									}

									@Override
									public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
										rewardedAd = rewardedAd;
										message("finished loading rewarded!");
										rewardedAd.show(AndroidLauncher.this, new OnUserEarnedRewardListener() {
											@Override
											public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
												message("user earned reward: " + rewardItem);
											}
										});
									}
								});	
					}
								
				}
			});

		} catch (Exception e) {
		}
	}

	@Override
	public AndroidAudio createAudio(Context context, AndroidApplicationConfiguration config) {
		return  new OboeAudio(context.getAssets());
	}
	
	@Override
	protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }
	
	@Override
	protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

}