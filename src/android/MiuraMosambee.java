package org.betterlife.miura.mosambee;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.FrameLayout;

import com.mosambee.lib.*;

public class MiuraMosambee extends CordovaPlugin {
    
    TransactionCallback tc;
    
    private Context context;
    
    @Override
    public boolean execute(String action, final JSONArray data,final CallbackContext callbackContext) throws JSONException {
        
        if (action.equals("sale")) {
            
            //callbackContext.success();
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    context = cordova.getActivity().getApplicationContext();
                    final Result resultClass = new Result();
                    resultClass.setContext(context);
                    FrameLayout frameLayout = (FrameLayout) webView.getView().getParent();
                    JSONObject arg_object;
                    try {
                        arg_object = data.getJSONObject(0);
                        Double amount = Double.parseDouble(arg_object.getString("amount"));
                        String orderid = arg_object.getString("orderid");
                        String username = arg_object.getString("username");
                        String password = arg_object.getString("password");
                        resultClass.startTransaction(amount,orderid, username, password , frameLayout,callbackContext);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        callbackContext.error(e.getMessage());
                    }
                    //callbackContext.success(); // Thread-safe.
                }
            });
            return true;

        } else {
            
            return false;

        }
    }
}