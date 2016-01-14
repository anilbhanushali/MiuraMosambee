package org.betterlife.miura.mosambee;
import com.mosambee.lib.*;

import org.apache.cordova.*;

import android.content.Context;
import android.widget.FrameLayout;

import org.apache.cordova.PluginResult;

public class Result implements TransactionResult {

	private Context context;
	TransactionCallback tc;
	
	void startTransaction(Double amount,String orderid,String username,String password,FrameLayout frameLayout, CordovaInterface cordova){
		setContext(cordova.getActivity().getApplicationContext());
		tc = new MosCallback(context);
		tc.initialise(username,password , this);
		//tc.initializeSignatureView(frameLayout, "#55004A", "#750F5A");
		tc.processTransaction(orderid, amount, null, null);
	}
	
	@Override
	public void onResult(ResultData resultData) {
		MiuraMosambee.setData(resultData);
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
}