package org.betterlife.miura.mosambee;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mosambee.lib.*;

public class MiuraMosambee extends CordovaPlugin {
    
    TransactionCallback tc;
    private static final String LOG_TAG = "MiuraPlugin";
    
    private FrameLayout frameLayout;
    
    public AlertDialog.Builder dlg = null;
    
    private static String currentStatus = "Processing";
    public static TextView input;
    private Double amount;
    private String orderid;
    private String username;
    private String password;
    
    
    @Override
    public boolean execute(String action, final JSONArray data,final CallbackContext callbackContext) throws JSONException {
        
        if (action.equals("sale")) {
            
            JSONObject arg_object;
            try {
                arg_object = data.getJSONObject(0);
                amount = Double.parseDouble(arg_object.getString("amount"));
                orderid = arg_object.getString("orderid");
                username = arg_object.getString("username");
                password = arg_object.getString("password");
            } catch (JSONException e) {
                LOG.d(LOG_TAG,"JSONException", e);
                e.printStackTrace();
            }
            prompt(callbackContext);
            return true;

        } else {
            
            return false;

        }
        
    }
    
    
    
    public void prompt(final CallbackContext callbackContext) {
        
        final MiuraMosambee notification = this;
        final CordovaInterface cordova = this.cordova;
        final Result resultClass = new Result();
        
        input = new TextView(cordova.getActivity().getApplicationContext());
        
        Runnable runnable = new Runnable() {
            public void run() {
                //AlertDialog.Builder dlg = createDialog(cordova); // new AlertDialog.Builder(cordova.getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                notification.dlg = createDialog(cordova);
                notification.dlg.setMessage(currentStatus);
                notification.dlg.setTitle("Capture Payment");
                notification.dlg.setCancelable(false);
                notification.dlg.setView(input);
                
                notification.dlg.setNeutralButton("Abort",
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Aborted"));
                            }
                    });
            
                notification.dlg.show();
                
                Runnable childRunnable = new Runnable(){
                    public void run(){
                        Looper.prepare();
                        resultClass.startTransaction(amount, orderid, username, password, frameLayout,cordova);
                        Looper.loop();
                    }
                };
                
                cordova.getThreadPool().execute(childRunnable);
                
            };
        };
        this.cordova.getActivity().runOnUiThread(runnable);
    }
    
    
    public static void setData(ResultData result) {
        
        String text = "";
        if (result== null) {
            text = "NULL";
        } else {
            text =  "Result: " + result.getResult() + "\nReason code: "
                    + result.getReasonCode() + "\nReason: "
                    + result.getReason() + "\nTranaction Id: "
                    + result.getTransactionId() + "\nTransactin amount: "+
                    result.getAmount()+"\nTransactin data: "
                    + result.getTransactionData();
        }
        input.setText(text);
    }
    
    
    private AlertDialog.Builder createDialog(CordovaInterface cordova) {
        return new AlertDialog.Builder(cordova.getActivity());
    }

   private ProgressDialog createProgressDialog(CordovaInterface cordova) {
        
        return new ProgressDialog(cordova.getActivity());
    }
   
}