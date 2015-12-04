package com.puzzlegame.me;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.appota.gamesdk.v4.callback.AppotaGameSDKCallback;
import com.appota.gamesdk.v4.commons.ColorParser;
import com.appota.gamesdk.v4.core.AppotaGameSDK;
import com.appota.gamesdk.v4.core.AppotaGameSDKException;
import com.appota.gamesdk.v4.model.AppotaPaymentResult;
import com.appota.gamesdk.v4.model.AppotaUserLoginResult;


public class MainActivity extends Activity {
	private SharedPreferences shared;
	private TextView coinAmount,tymAmount;
	private int currentCoin,currentTym;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
//        logKeyHash();
        initViews();
		AppotaGameSDK.getInstance().setHideWelcomeView(false)
				.autoCatchException().setKeepLoginSession(true)
				.setSDKButtonVisibility(true).setAutoShowLoginDialog(false)
				.configure(this, new MyImplApptaGameSDKCallback());
    	AppotaGameSDK.getInstance().sendView("Home Puzzle Game "+getAppVersion(this));
    	AppotaGameSDK.getInstance().sendEvent("Start Game", "Open Application", "My custom label");
    }
    
    @Override
    protected void onDestroy() {
    	AppotaGameSDK.getInstance().finishSDK();
    	super.onDestroy();
    }

	private void initViews() {
		StateListDrawable btnPayBackground = new StateListDrawable();
		btnPayBackground.addState(new int[] {android.R.attr.state_pressed},ColorParser.getRoundBackgroudDrawable("#898e8c"));
		btnPayBackground.addState(new int[] { },ColorParser.getRoundBackgroudDrawable("#16abef"));
		StateListDrawable btnPayPackageBackground = new StateListDrawable();
		btnPayPackageBackground.addState(new int[] {android.R.attr.state_pressed},ColorParser.getRoundBackgroudDrawable("#898e8c"));
		btnPayPackageBackground.addState(new int[] { },ColorParser.getRoundBackgroudDrawable("#16abef"));
		StateListDrawable btnPayCoinPackageBackground = new StateListDrawable();
		btnPayCoinPackageBackground.addState(new int[] {android.R.attr.state_pressed},ColorParser.getRoundBackgroudDrawable("#898e8c"));
		btnPayCoinPackageBackground.addState(new int[] { },ColorParser.getRoundBackgroudDrawable("#16abef"));
		
        Button makePayment = (Button) findViewById(R.id.makePayment);
        makePayment.setBackgroundDrawable(btnPayBackground);
        Button makePaymentPackage = (Button) findViewById(R.id.makePaymentPackage);
        makePaymentPackage.setBackgroundDrawable(btnPayPackageBackground);
        
        Button makePaymentPackageCoin = (Button) findViewById(R.id.makePaymentPackageCoin);
        makePaymentPackageCoin.setBackgroundDrawable(btnPayCoinPackageBackground);
        
        CheckBox floatingButtonToogle = (CheckBox) findViewById(R.id.floatingButtonToogle);
        floatingButtonToogle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.e("Checkbox", "Hide floating button:"+isChecked);
				AppotaGameSDK.getInstance().setSDKButtonVisibility(!isChecked);
			}
		});
        shared = getPreferences(MODE_PRIVATE);
        coinAmount = (TextView)findViewById(R.id.coinAmount);
        tymAmount = (TextView)findViewById(R.id.tymAmount);
        currentCoin = shared.getInt("COIN", 0);
        currentTym = shared.getInt("TYM", 0);
        coinAmount.setText(currentCoin+"");
        tymAmount.setText(currentTym+"");
        TextView versionInfo = (TextView) findViewById(R.id.versionInfo);
        versionInfo.setText("Version:"+getAppVersionName(this)+", build:"+getAppVersionCode(this));
	}
    
    public void logOut(View view){
    	AppotaGameSDK.getInstance().sendEvent("On Click", "Click logout button", "");
    	AppotaGameSDK.getInstance().logout();
    }

    public void makePayment(View view){
    	AppotaGameSDK.getInstance().sendEvent("On Click", "Click show payment", "");
    	AppotaGameSDK.getInstance().showPaymentView();
    }
    
    public void inviteFBFriend(View view){
    	AppotaGameSDK.getInstance().inviteFacebookFriends();
    }
    
    public void showUserInfo(View view){
    	AppotaGameSDK.getInstance().showUserInfoView();
    }
    
    public void makePaymentPackage(View view){
    	AppotaGameSDK.getInstance().showPaymentViewWithPackageID("app.pkid.tym4K");
    }
    
    public void makePaymentPackageCoin(View view){
    	AppotaGameSDK.getInstance().showPaymentViewWithPackageID("app.pkid.coin100");
    }
    
	public class MyImplApptaGameSDKCallback extends AppotaGameSDKCallback {
		@Override
		public String getPaymentState(String packageID) {
			//You can set any string value if you want, in my case, return package id, package value, username & server
		    //Note :Length of Payment State does not exceed 150 characters
			String userName = AppotaGameSDK.getInstance().getUserInfo().username;
		    String server = "Server_Name";
		    String packageAmout = "1000_gold";//package of 1000 gold, game amount
			return packageID+"_"+packageAmout+"_"+userName+"_"+server;
		}

		@Override
		public void onPaymentSuccess(AppotaPaymentResult paymentResult,String packageID) {
			Log.e("PuzzleGame", "onPaymentSuccess:"+paymentResult.toString()+" with packageID:"+packageID);
		}
		
		@Override
		public void onUserLoginSuccess(AppotaUserLoginResult userInfo) {}
		
		@Override
		public void onUserLogout(String userName) {
			Log.e("PuzzleGame", "user "+userName+" has logged out..");
		}
		
		@Override
		public void onUserLoginError(String errorMessage) {}

		@Override
		public void onPaymentError(String errorMessage) {
			Log.e("PuzzleGame", "onPaymentError:"+errorMessage);
		}

		@Override
		public void onCloseLoginView() {
			
		}

		@Override
		public void onPackageSelected(String packageID) {
			
		}

		@Override
		public void onClosePaymentView() {
			
		}
	}


    
    private int getAppVersionCode(Context context) {
		int versionCode = -1;
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			versionCode = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.e("getAppVersionCode", "ERROR. "+e.getMessage()+" ");
		}
		return versionCode;
	}
    
    private String getAppVersionName(Context context){
		PackageInfo pInfo;
		String versionName = "";
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionName = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
    
    private String getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
    
    private void logKeyHash(){
    	PackageInfo info;
    	try {
    	    info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
    	    for (Signature signature : info.signatures) {
    	        MessageDigest md;
    	        md = MessageDigest.getInstance("SHA");
    	        md.update(signature.toByteArray());
    	        String keyhash = new String(Base64.encode(md.digest(), 0));
    	        //string something is what you should paste as key hash
    	        Log.e("hash key:", keyhash);
    	    }
    	} catch (NameNotFoundException e1) {
    	    Log.e("name not found", e1.toString());
    	} catch (NoSuchAlgorithmException e) {
    	    Log.e("no such an algorithm", e.toString());
    	} catch (Exception e) {
    	    Log.e("exception", e.toString());
    	}
    }
}
