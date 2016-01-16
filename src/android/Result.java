package org.betterlife.miura.mosambee;
import com.mosambee.lib.*;

import org.apache.cordova.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Looper;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

public class Result implements TransactionResult {

	private static final String LOG_TAG = "MiuraPlugin-Command";
	
	private JSONObject resultObj;
	private TextView progress;
	private AlertDialog.Builder dlg = null;
	private Context context;
	private TransactionCallback tc;
	private CallbackContext callbackContext;
	private CordovaInterface cordova;
	
	
	void startTransaction(Double amount,String orderid,String username,String password,FrameLayout frameLayout, CordovaInterface cordova, CallbackContext callbackContext){
		this.callbackContext = callbackContext;
		this.cordova = cordova;
		progress = new TextView(cordova.getActivity().getApplicationContext());
    		resultObj = new JSONObject();
		setContext(cordova.getActivity().getApplicationContext());
		prompt(username,password,orderid,amount);
		
	}
	
	public void prompt(final String username,final String password,final String orderid, final Double amount ) {
      	
		final Result notification = this;
		
    	Runnable runnable = new Runnable() {
    		public void run() {
		    	Runnable childRunnable = new Runnable(){
	    			public void run(){
	    				Looper.prepare();
	    				processTransaction(username,password,orderid,amount);
	    				Looper.loop();
	    			}
	    		};
	    		cordova.getThreadPool().execute(childRunnable);
            };
        };
        cordova.getActivity().runOnUiThread(runnable);
    }
	
	
	
	private void processTransaction(String username,String password,String orderid,Double amount){
		tc = new MosCallback(context);
		tc.initialise(username,password,this);
		//tc.initializeSignatureView(frameLayout, "#55004A", "#750F5A");
		tc.processTransaction(orderid, amount, null, null);
	}

	@Override
	public void onResult(ResultData resultData) {
		
		boolean success = false;
    	String reasonCode = "";
    	String reason = "";
    	String transactionId = "";
    	String amount = "";
    	String transactionData = "";
    	
    	String text = "";
		if (resultData== null) {
			text = "NULL";
		} else {
			success = resultData.getResult();
			reasonCode = resultData.getReasonCode();
			reason = resultData.getReason();
			transactionId = resultData.getTransactionId();
			amount = resultData.getAmount();
			transactionData = resultData.getTransactionData();
			
			text =  "Result: " + success + "\nReason code: "
					+ reasonCode + "\nReason: "
					+ reason + "\nTranaction Id: "
					+ transactionId + "\nTransactin amount: "
					+ amount +"\nTransactin data: "
					+ transactionData;
		}
		try {
			resultObj.put("success", success);
			resultObj.put("reasonCode", reasonCode);
			resultObj.put("reason", reason);
			resultObj.put("transactionId", transactionId);
			resultObj.put("amount", amount);
			resultObj.put("transactionData", transactionData);
		} catch (JSONException e) {
			e.printStackTrace();
			LOG.d(LOG_TAG,"JSONException result", e);
		}
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, resultObj));
		
	}
	
	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public void onCommand(String arg0) {
		LOG.d(LOG_TAG,arg0);
		try {
			resultObj.put("reason", arg0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PluginResult res = new PluginResult(PluginResult.Status.OK, resultObj);
		res.setKeepCallback(true);
		callbackContext.sendPluginResult(res);
		progress.setText(arg0);
	}
	
	private AlertDialog.Builder createDialog(CordovaInterface cordova) {
        return new AlertDialog.Builder(cordova.getActivity());
    }
}
