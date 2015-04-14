#  Appota Game SDK for Android (version 4)	

Version 4 of the Appota Game for Android has published in this reposity and in <b> Developer Preview </b>. Version 3 will continue to be available in our [appota-android-game-sdk3](https://github.com/appota/android-game-sdk3) repository.


[1. Import Appota SDK 4](#1-import-appota-sdk4)

[2. Client APIs](#2-client-apis)
 
[2.1. Initial Appota SDK4 trong project](#2-1-kh-i-t-o-appota-sdk4-trong-project)

[2.2. Configure SDK](#2-2-configure-sdk)

[2.3. Các chức năng liên quan đến người dùng](#2-3-c-c-ch-c-n-ng-li-n-quan-n-ng-i-d-ng)

[2.3.1. Register](#2-3-1-register)

[2.3.2. Login](#2-3-2-login)

[2.3.3. Show user information](#2-3-3-show-user-information)

[2.3.4. Get user information](#2-3-4-get-user-information)

[2.3.5. Logout](#2-3-4-logout)

[2.3.6. Invite facebook friends](#2-3-6-invite-facebook-friends)

[2.3.7. Login session](#2-3-7-login-session)

[2.4. Các chức năng liên quan đến thanh toán](#2-4-c-c-ch-c-n-ng-li-n-quan-n-thanh-to-n)

[2.5. Các chức năng liên quan đến push notification](#2-5-c-c-ch-c-n-ng-li-n-quan-n-push-notification)

[2.6. Các chức năng liên quan đến thống kê](#2-6-c-c-ch-c-n-ng-li-n-quan-n-th-ng-k)

[3. Exception](#3-exception)


## [1. Import Appota SDK4](#header1)

- Copy appota_sdk4.jar into libs folder.

  	a.Working with Eclipse

	![eclipse_import_sdk_lib](docs/images/eclipse_import_sdk_lib.PNG)

  	b.Working with Android Studio

	![android_studio_add_sdk_lib](docs/images/android_studio_add_sdk_lib.PNG)

- Included libraries in Android Game SDK (if you are using these libs in your project, please remove it ):
	
		- khandroid-httpclient-4.2.3.jar (khandroid.ext.apache.http)
		- twitter4j-core-3.0.5.jar ( twitter4j )

	*Note* : If you use Proguard, add flowing config into your proguard config :

	`-dontwarn com.appota.** ` 

	`-dontwarn twitter4j.**`  

	`-keep class com.appota.**{*;} `

## [2. Client APIs](#header2)
### [2.1. Khởi tạo Appota SDK4 trong project](#header21)

* Calling `AppotaGameSDK.getInstance().init(activity,appotaSDKCallback)`  in onCreate of the first starting activity.
	
	```java
	public interface AppotaGameSDKCallback {

		public String getPaymentState(String packageID); // if you don't set specific Payment Package, packageID will be an empty string.
	
		public void onUserLoginSuccess(AppotaUserLoginResult userInfo);

		public void onUserLoginError(String errorMessage);
	
		public void onUserLogout(String userName);
	
		public void onPaymentSuccess(AppotaPaymentResult paymentResult,String packageId);
		
		public void onPaymentError(String errorMessage);

		public void onCloseLoginView();
		
	}
	```
	

	[See detail AppotaPaymentResult at 2.4.c](#2-4-c-c-ch-c-n-ng-li-n-quan-n-thanh-to-n)	


	Example:

	![appota_sdk_callback_impl_new_17122014](docs/images/impl_get_payment_state.PNG)


* Extra Methods:

      a. Call init SDK with inited sdk callback:

		AppotaGameSDK.getInstance().init(activity,appotaSDKCallback, new OnInitedSDKCallback() {
			
			@Override
			public void onSDKInited(boolean success) {

                           //Do your stuffs if needed

			}
		}); 

      b. Hide welcome view: (default is visible)

		AppotaGameSDK.getInstance().hideWelcomeView(true).init(activity,sdkCallback);


	Note.

	Call **AppotaGameSDK.getInstance().finishSDK()**  before exit your application.

	
### [2.2. Configure SDK](#header22)

*	Configure Manifest:
	
	* Open file AndroidManifest.xml in your Android project.
	
	![manifest_file](docs/images/manifest_file.PNG)
	    

	* Set basic permissions for SDK

		`<uses-permission android:name="android.permission.INTERNET" />`
    
		`<uses-permission  android:name="android.permission.ACCESS_WIFI_STATE" />`

    	`<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`

    	`<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`

    	`<uses-permission android:name="android.permission.GET_ACCOUNTS" />`

    	`<uses-permission android:name="android.permission.USE_CREDENTIALS" />`

    * Permission for fixing login Google by Google Play services landscape with auto rotation off

    	`<uses-permission android:name="android.permission.WRITE_SETTINGS" />`

    * Permission for Google Play payment

    	`<uses-permission android:name="com.android.vending.BILLING" />`

	![permission](docs/images/permission.PNG)


	* Declare BaseSDKActivity of SDK:

		 `<activity
            android:name="com.appota.gamesdk.v4.ui.BaseSDKActivty"
            android:configChanges="screenSize|orientation|keyboardHidden"
            android:theme="@android:style/Theme.Dialog" >
        </activity>`

	* Declare APPOTA API KEY:
	
		`<meta-data
            android:name="com.appota.apiKey"
            android:value="your_appota_api_key" />`

	* Declare activity for Facebook login:
	
		 `<activity
            android:name="com.appota.facebook.LoginActivity"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />`

		Declare meta-data tag for Facebook Application ID

		`<meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/your_facebook_app_id" />`

	* Declare meta-data tag for Twitter login:
		
		`<meta-data
            android:name="com.appota.twitter.consumer.key"
            android:value="@string/your_twitter_consumer_key" />`

        `<meta-data
            android:name="com.appota.twitter.consumer.secret"
            android:value="@string/your_twitter_consumer_secret" />`

	
		![delare_activity_metadata](docs/images/delare_activity_metadata.PNG)


		if these tags not set, twitter configuration will be set by SDK.


### [2.3. Các chức năng liên quan đến người dùng](#header23)

>- SDK provides methods for creating account, login account(using facebook,google & twitter)  on Appota system so  that user can use  their account to make payment.
>
>- **AppotaSDKCallback** is a listener for successful registration, log-in as well as payment transaction ([detail AppotaSDKCallback at 2.1 ](#2-1-kh-i-t-o-appota-sdk4-trong-project)).
>

#### [2.3.1. Register](#header231)


Register screen can be navigated from login screen.
	

#### [2.3.2. Login](#header232)

 Call `AppotaGameSDK.getInstance().showLoginView();`   to open login screen.


##### [a. Login by Facebook.](#header232a)

	AppotaGameSDK.getInstance().showLoginFacebook();
	
- if user’s device has installed Facebook application then some dialogs require basic permissions the SDK needs to be allowed will popup.

	![facebook_require_permission](docs/images/facebook_require_permission.png)

- if user’s device hasn’t installed Facebook application then a web dialog login for Facebook will show up.


	![facebook_webdialog](docs/images/facebook_webdialog.PNG)



##### [b. Login by Google.](#header232b)


`AppotaGameSDK.getInstance().showLoginGoogle();`

	
Select any google acount(gmail address) you want to use login:


![gmail_select_accout](docs/images/gmail_select_accout.png)




##### [c. Login by Twitter.](#header232c)


`AppotaGameSDK.getInstance().showLoginTwitter();`


Use your twitter account to fill the Twitter login form:


![login_twitter](docs/images/login_twitter.PNG)


#### [2.3.3. Show user information](#header233)
	
	AppotaGameSDK.getInstance().showUserInfo();


#### [2.3.4. Get user information](#header2334)
	
	AppotaGameSDK.getInstance().getUserInfo();


	
return  **AppotaUserLoginResult** ( includes : userId, username, email..)
	

#### [2.3.5. Logout](#header235)

`AppotaGameSDK.getInstance().logout(true);`

if you want to show login form after calling logout method then pass true if not pass false
	

#### [2.3.6. Invite facebook friends](#header2352)

`AppotaGameSDK.getInstance().inviteFacebookFriends();`


#### [2.3.7. Login session](#header2352)

Default login session will be kept util logout function called.

If you want users have to login every time they open game/app and their login session will be cleared after they are exit game/app, use below function:

`AppotaGameSDK.getInstance().setKeepLoginSession(false);`

**Notice** : Don't forget to call `AppotaGameSDK.getInstance().finishSDK();` in order to make sure this feature works normally as well as some resources of SDK will be released.


### [2.4. Các chức năng liên quan đến thanh toán](#header24)

>1.Payments implementation guide:
>
>-After logging in SDK, user can select method to do payment(SMS, Card, Bank, Google Play,Bao Kim..).
>
>-After user confirm to make payment, Appota payment system will check the transaction, if success, the system will call **Notify URL** ( declare in dev.appota.com for developer's server process data) if have.
>
>2.Payment State (state) :
>
>-Payment state is a extra field. It is assigned value by developer( empty string if not assigned) before making any payment and returned itself after transaction successfully. You can use it for checking, validation & compararision  in your game(apps).
>You must override getPaymentState(AppotaPaymentPackage) method to set its value when init SDK.([see example at 2.1 ](#2-1-kh-i-t-o-appota-sdk4-trong-project)).


- a. Show payment dialog:

	`AppotaGameSDK.getInstance().makePayment();`
	

	`AppotaGameSDK.getInstance().makePayment(String packageID);`

	- packageID : ID of package payment. If you want to show only one specific package payment, pass its package id, if not, just pass null or empty string value for show all packages. This packageID will be received on [getPaymentState(String packageID)](#2-1-kh-i-t-o-appota-sdk4-trong-project)
		

- b. Methods for showing only one specific payment method separately aren't available in SDK4(SMS,Phone Card,Bank,Google Play payment..);

- c. After making payment successfully, SDK will trigger AppotaGameSDKCallback so you can receive **AppotaPaymentResult**.

	>AppotaPaymentResult is a class contains properties of transaction.
	> 
	>AppotaPaymentResult includes:
	>
	>- transactionId (string): Id of successful transaction.
	>
	>- type (string): method which user just used to make payment(SMS,Bank..)
	>- amount (string): amount of money.
	>
	>- time (Unix time): time that user made payment.
	>
	>- status (int, 0 or 1): success ->1
	>
	>- target(string) : set by developer same as state.
	>
	>- state (string): knows as Payment State, see above.
	>
	>- errorMessage (string): error message if occur.


- d.	Other methods:
	
	+ `AppotaGameSDK.getInstance().setCharacter(String name,String server,String serverIdentify);`

		for creating charging money system on mobile web.

### [2.5. Các chức năng liên quan đến push notification](#header25)
Set push notification:

	AppotaGameSDK.getInstance().setPushDeviceToken("device_token_for_push_notification").init(activity,sdkCallback);

Set push notification to a group:
	
	AppotaGameSDK.getInstance().setPushGroup("device_tokens(group1,group2....)").init(activity,sdkCallback);

### [2.6. Các chức năng liên quan đến thống kê](#header26)

#### [2.6.1 Send activity log](#header261)

	AppotaGameSDK.getInstance().sendView(activityName)

-actvityName: name of activity(screen)



#### [2.6.2 Send event log](#header262)

	AppotaGameSDK.getInstance().sendEvent(category,action,label)

-category: The name you supply for the group of objects you want to track.

-action : A string that is uniquely paired with each category, and commonly used to define the type of user interaction.

-label : An optional string to provide additional dimensions to the event data.

	AppotaGameSDK.getInstance().sendEvent(category,action,label,value)

-value : An integer that you can use to provide numerical data about the user event.


## 3. Exception##


AppotaGameSDK will throw exceptions if SDK configuration is insufficient or incorrect.

Facebook, Google, Twitter exception only raises if developer use them for login.

Here are exceptions:

|Name|Message|
|----|-------|
| `AppotaAPIKeyException`| There is no com.appota.apiKey found in AndroidManifest.xml|
|`AppotaFacebookAppIDException`|There is no com.facebook.sdk.ApplicationId found in AndroidManifest.xml|
|`AppotaInvalidOperationException`|This exception will be thrown depends on specific situation. 

* Advanced configuration:
	
	Appota Game SDK4 has got crash exception handler itself.
	
	All exceptions are caught by SDK4 will be sent to server & available for dev for viewing & analyzing.

	If you're using Crashlytics or other crash reporting but you want to using Apota exception handler of SDK call autoCatchException method (default auto catch exception if you are not using any crash reporting):

	`AppotaGameSDK.getInstance().autoCatchException();`

	*You must call autoCatchException()* **before** call AppotaGameSDK.init()