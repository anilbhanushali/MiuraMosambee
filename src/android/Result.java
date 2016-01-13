package org.betterlife.miura.mosambee;
import com.mosambee.lib.*;
import org.apache.cordova.*;

import android.content.Context;
import android.widget.FrameLayout;

public class Result implements TransactionResult {

	private Context context;
	TransactionCallback tc;
	CallbackContext cb;
	
	void startTransaction(Double amount,String orderid,String username,String password,FrameLayout frameLayout,CallbackContext callbackContext){
		cb = callbackContext;
		tc = new MosCallback(context);
		tc.initialise(username,password , this);
		tc.initializeSignatureView(frameLayout, "#55004A", "#750F5A");
		tc.processTransaction(orderid, amount, null, null);
	}
	
	@Override
	public void onResult(ResultData result) {
		if (result== null) {
			cb.success("");
		} else {
			String text = "Result: " + result.getResult() + "\nReason code: "
					+ result.getReasonCode() + "\nReason: "
					+ result.getReason() + "\nTranaction Id: "
					+ result.getTransactionId() + "\nTransactin amount: "+
					result.getAmount()+"\nTransactin data: "
					+ result.getTransactionData();
			cb.success(text);
		}
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
}