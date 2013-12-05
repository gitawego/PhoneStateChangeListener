/**
 *  PhoneStateChangeListener cordova plugin (Android)
 * 
 * 	@author Stéfano Stypulkowski Zanata
 * 	@see http://szanata.com
 *  @reference https://github.com/madeinstefano/PhoneStateChangeListener
 * 	@license MIT <http://szanata.com/MIT.txt>
 * 	@license GNU <http://szanata.com/GNU.txt>
 *  
 * 	Based upon PhoneListener by authored by Tommy-Carlos Williams <https://github.com/devgeeks>
 * 
 */
package com.szanata.cordova.phonestatechangelistener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;

/**
 * 
 *
 */
public class PhoneStateChangeListener extends CordovaPlugin {

	private static final String TAG = "PhoneStateChangeListener";
	private final String NONE = "NONE";
	private Context context;
	private BroadcastReceiver receiver = null;
	
	/**
	 * Expecting no parameters from javascript
	 * Expecting only success callback from javascript
	 */
	@Override
	public boolean execute(final String action,final JSONArray args, final CallbackContext callbackContext) throws JSONException {	
		
		this.context = cordova.getActivity().getApplicationContext();
		
		if ("start".equals(action)) {
			startPhoneListener(callbackContext);
			return true;
			
		}else if ("stop".equals(action)) {
			removePhoneListener(callbackContext);
			this.callbackContext = null;
			return true;
		}
		
		return false;
	}

	/**
	 * creates a new BroadcastReceiver to listen whether the Telephony State changes
	 */
	public void startPhoneListener(final CallbackContext callbackContext) {
		
		if (this.receiver == null) {
			this.receiver = new BroadcastReceiver() {

				@Override
				public void onReceive(final Context context, final Intent intent) {	

					if (intent != null && intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
						String state = intent.hasExtra(TelephonyManager.EXTRA_STATE) ? intent.getStringExtra(TelephonyManager.EXTRA_STATE) : NONE;
						String number = "";
						
						if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
							number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
						}
						
						final JSONObject data = new JSONObject();
						try{
							data.put("state", state);
							data.put("number", number);
							callbackContext.success(data);
						}catch(final JSONException e){
							callbackContext.error(e.getMessage());
						}
						
					}
				}
			};
			
			this.context.registerReceiver(this.receiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));
		}
	}
	
	
	/**
	 * removes the Receiver
	 */
	private void removePhoneListener(final CallbackContext callbackContext) {
		if (this.receiver != null) {
			try {
				this.context.unregisterReceiver(this.receiver);
				this.receiver = null;
				callbackContext.success();
			} catch (final Exception e) {
				callbackContext.error(e.getMessage());
			}
		}
	}
	
	@Override
	public void onDestroy() {
		removePhoneListener();
	}
}
