# Change logs - Upgrade from SDK version 3 to version 4:

## SDK
 

1.1 Changes in configure Manifest:

1.1.a. REMOVE those declarations in SDK 3:

		<activity
            android:name="com.appota.gamesdk.UserActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@style/Theme.Appota.GameSDK"
            android:windowSoftInputMode="adjustPan" />
        <activity
            android:name="com.appota.gamesdk.UserInfoActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@style/Theme.Appota.GameSDK"
            android:windowSoftInputMode="adjustPan" />
        <activity
            android:name="com.appota.gamesdk.PaymentActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@style/Theme.Appota.GameSDK" />
		
1.1.b. CHANGE name meta-data tag for Twitter login:

		 <meta-data android:name="com.appota.gamesdk.twitter.consumer.key"
			 android:value="YOUR_CONSUMER_KEY" />
    	<meta-data android:name="com.appota.gamesdk.twitter.consumer.secret"
			 android:value="YOUR_SECRET_KEY" />

to:
		
		<meta-data android:name="com.appota.twitter.consumer.key" 
			android:value="YOUR_CONSUMER_KEY" />

		<meta-data android:name="com.appota.twitter.consumer.secret" 
			android:value="YOUR_SECRET_KEY" />


1.1.c. ADD declaration for SDK version 4:
		
		
		<activity
            android:name="com.appota.gamesdk.v4.ui.BaseSDKActivty"
            android:theme="@android:style/Theme.Dialog" >
        </activity>

		<!-- START Configuration for Push Notification -->
		<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
		<permission
	        android:name="your_application_packagename.permission.C2D_MESSAGE"
	        android:protectionLevel="signature" />
	
	    <uses-permission android:name="your_application_packagename.permission.C2D_MESSAGE" />
	    <uses-permission android:name="android.permission.WAKE_LOCK" />
        <service android:name="com.appota.gamesdk.v4.widget.PushHandler" />
		<receiver
            android:name="com.appota.gamesdk.v4.network.GCMBroadcastReceiver"
            android:permission="com.google.android.c2dm.permission.SEND" >
            <intent-filter>

                <!-- Receives the actual messages. -->
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
                <!-- Receives the registration id. -->
                <action android:name="com.google.android.c2dm.intent.REGISTRATION" />

                <category android:name="your_application_packagename" />
            </intent-filter>
        </receiver>
		<!-- Sender ID for push notification -->
        <meta-data
            android:name="com.appota.push.gcm_sender_id"
            android:value="id:YOUR_GOOGLE_PROJECT_ID" />
        <!-- END configuration for Push Notification -->


1.2 Changes in SDK Configuration:

1.2.1 CHANGE initial SDK version 3:

a.	Initial SDK:

	AppotaGameSDK.getInstance().init(Activity activity, String apiKey, String YOUR_NOTICE_URL, String YOUR_CONFIG_URL);

to:

	AppotaGameSDK.getInstance().configure(Activity activity, AppotaGameSDKCallback sdkCallback);

	

**AppotaSDKCallback** is a listener for successful registration, log-in as well as payment transaction ([detail AppotaSDKCallback at 2.1 ](#21-init-sdk-4)).


b. Finish SDK, clear resources:

	AppotaGameSDK.getInstance().finish();

to: 

	AppotaGameSDK.getInstance().finishSDK();

c. Show login dialog:
	AppotaGameSDK.getInstance().showLogin();

to:

	AppotaGameSDK.getInstance().showLoginView();

d. Show payment dialog:

	AppotaGameSDK.getInstance().makePayment();

to:

	AppotaGameSDK.getInstance().showPaymentView();

	or

	AppotaGameSDK.getInstance().showPaymentViewWithPackageID(String package_id);

e. Show user information dialog:

	AppotaGameSDK.getInstance().showUserInfo();

to:

	AppotaGameSDK.getInstance().showUserInfoView();

f. Logout:

	AppotaGameSDK.getInstance().logout();

to:

	AppotaGameSDK.getInstance().logout(boolean state);

	Set state is true if you want to show login dialog after logout successfully.

g.	Close payment dialog:

	AppotaGameSDK.getInstance().closePayment();

to:

	AppotaGameSDK.getInstance().closePaymentView();

h.	

	AppotaGameSDK.getInstance().checkUserLogin();

to:

	AppotaGameSDK.getInstance().isUserLoggedIn();


1.2.2 NO longer use class AppotaReceiver, use AppotaSDKCallback instead.

1.2.3 Many function added.(see above).

## 2. Server

- Add `revenue` parameter in IPN callback to measure revenue of current payment method type `CARD`, `BANK`, ...
- Reimplement your hash checking function to add `revenue` parameter (it will be add in `a-z` order)
- For detail please read wiki about IPN for each payment method [https://github.com/appota/android-game-sdk/wiki/instant-payment-notification](https://github.com/appota/android-game-sdk/wiki/instant-payment-notification)