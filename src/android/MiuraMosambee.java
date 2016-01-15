package org.betterlife.miura.mosambee;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.FrameLayout;

public class MiuraMosambee extends CordovaPlugin {
    
    private static final String LOG_TAG = "MiuraPlugin";
    private FrameLayout frameLayout = null;
    private Double amount;
    private String orderid;
    private String username;
    private String password;
    
    
    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        
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
            Result resultClass = new Result();
            resultClass.startTransaction(amount, orderid, username, password, frameLayout,cordova,callbackContext);
            //prompt(callbackContext);
            return true;

        } else {
            
            return false;

        }
        
    }
}