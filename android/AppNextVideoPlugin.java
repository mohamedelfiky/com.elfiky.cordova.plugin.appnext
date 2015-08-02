package com.elfiky.cordova.plugin.appnext;

import org.apache.cordova.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.appnext.appnextinterstitial.InterstitialManager;
import com.appnext.appnextinterstitial.OnAdError;
import com.appnext.appnextinterstitial.OnAdLoaded;

import android.util.Log;

public class AppNextVideoPlugin extends CordovaPlugin {

	public static final String ACTION_SHOW_VIDEO_AD = "show_video";

	private final String TAG = "appnext_log";

	private static final String DEFAULT_PLACEMENTID = "e5fe57df-9504-480a-a747-17a9f58bb562";

	/* options */
	private static final String OPT_PLACEMENTID = "placement_id";

	private String dev_placement_id = DEFAULT_PLACEMENTID;

	@Override
	public boolean execute(final String action, JSONArray data,
			CallbackContext callbackContext) throws JSONException {

		if (ACTION_SHOW_VIDEO_AD.equals(action)) {
			JSONObject options = data.optJSONObject(0);
			executeCreateInterstitialView(options, callbackContext);
		}

		return true;

	}

	private PluginResult executeCreateInterstitialView(JSONObject options,
			final CallbackContext callbackContext) {
		this.setOptions(options);
		cordova.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					Log.v(TAG,"dev_placement: "+ dev_placement_id);
					InterstitialManager.showInterstitial(cordova.getActivity(),
							dev_placement_id,
							InterstitialManager.REWARDED_VIDEO);
					InterstitialManager.cacheInterstitial(cordova.getActivity(),
							dev_placement_id,
							InterstitialManager.REWARDED_VIDEO);
					InterstitialManager.setOnAdErrorCallback(new OnAdError() {

						@Override
						public void adError(String arg0) {
							// TODO Auto-generated method stub

							Log.e(TAG, "error video: " + arg0);
						}
					});

					InterstitialManager.setOnAdLoadedCallback(new OnAdLoaded() {

						@Override
						public void adLoaded() {
							// TODO Auto-generated method stub
							Log.v(TAG, "Show appnext video ad loaded");
						}
					});
					Log.v(TAG, "Show mobilecore ad Interstitial");
				} catch (Exception ex) {
					Log.e(TAG, "error error error error ");
					Log.e(TAG, ex.getMessage());
				}

				callbackContext.success();
			}
		});

		return null;
	}

	private void setOptions(JSONObject options) {
		if (options == null)
			return;

		if (options.has(OPT_PLACEMENTID))
			this.dev_placement_id = options.optString(OPT_PLACEMENTID);
	}

}