package br.gov.itaipu.geocab.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import br.gov.itaipu.geocab.R;
import br.gov.itaipu.geocab.controller.delegate.AccountDelegate;
import br.gov.itaipu.geocab.entity.User;

public class AuthenticationActivity extends Activity implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener
{
    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     * Button for default sign in
     */
    private Button btnSignIn;

    private EditText editTextUsername, editTextPassword;

    private AccountDelegate accountDelegate;

    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES GOOGLE
	 *-------------------------------------------------------------------*/

    /**
     * Code for request when sign in
     */
	private static final int CODE_SIGN_IN_GOOGLE = 0;

    /**
     * Logcat tag
     */
	private static final String TAG = "AuthenticationActivity";

    /**
     * Google client to interact with Google API
     */
	public static GoogleApiClient mGoogleApiClient;

    /**
     * Connection result of sign in google
     */
	public static ConnectionResult mConnectionResult;

    /**
     * Button sign in google
     */
	private SignInButton btnSignInGoogle;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES FACEBOOK
	 *-------------------------------------------------------------------*/

    private GraphUser user;

    private LoginButton loginButton;

    private UiLifecycleHelper uiHelper;

    /*-------------------------------------------------------------------
	 *				 		     HANDLERS
	 *-------------------------------------------------------------------*/

    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

        editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);

        accountDelegate = new AccountDelegate(this);

        loginButton = (LoginButton) findViewById(R.id.btn_sign_in_facebook);
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                AuthenticationActivity.this.user = user;

                if( SplashScreenActivity.settings.getAll().get("email") == null && SplashScreenActivity.settings.getAll().get("password") == null )
                {
                    if( user != null)
                    {
                        try {
                            User userApplication = new User();
                            userApplication.setName(user.getName());
                            userApplication.setEmail(user.getInnerJSONObject().getString("email"));
                            userApplication.setPassword("none");
                            accountDelegate.insertUserSocial(userApplication);

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }

            }
        });

        // Getting the button from view
        btnSignInGoogle = (SignInButton) findViewById(R.id.btn_sign_in_google);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);

        setGooglePlusButtonText(btnSignInGoogle, "Google");

		// Button click listeners
        btnSignInGoogle.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

        // Initiate Google API
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

	}

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    /**
     * Called after onCreate
     */
    @Override
	protected void onStart()
    {
		super.onStart();
		mGoogleApiClient.connect();

	}

    /**
     * Called when you are no longer visible to the user
     */
    @Override
	protected void onStop()
    {
		super.onStop();
        uiHelper.onStop();
		if (mGoogleApiClient.isConnected())
        {
			mGoogleApiClient.disconnect();
		}
	}


    @Override
    protected void onResume() {
        super.onResume();

        Session session = Session.getActiveSession();

        if(session==null){
            // try to restore from cache
            session = Session.openActiveSessionFromCache(this);
        }

        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    /**
     * Handler on click
     * @param view
     */
    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btn_sign_in_google && !mGoogleApiClient.isConnected())
        {
            if (!mGoogleApiClient.isConnecting()) {
                mSignInClicked = true;
                resolveSignInError();
            }
        }
        else if( view.getId() == R.id.btn_sign_in )
        {
            if( editTextUsername.getText().length() == 0 || editTextPassword.getText().length() == 0 )
            {
                Toast.makeText(this, R.string.fill_login, Toast.LENGTH_LONG).show();
            }
            else
            {
                SplashScreenActivity.prefEditor = SplashScreenActivity.settings.edit();
                SplashScreenActivity.prefEditor.putString("password", editTextPassword.getText().toString());
                SplashScreenActivity.prefEditor.commit();

                final byte[] credentials = ( editTextUsername.getText() + ":" + editTextPassword.getText() ).getBytes();
                accountDelegate.checkLogin(Base64.encodeToString(credentials, Base64.NO_WRAP), true);
            }
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, CODE_SIGN_IN_GOOGLE);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /*-------------------------------------------------------------------
	 *				 		   FACEBOOk HANDLERS
	 *-------------------------------------------------------------------*/

    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {
        if (state.isOpened()) {
            Log.d(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.d(TAG, "Logged out...");
        } else {
            Log.d(TAG, "Unknown state: " + state);
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d("Fb.call", String.format("Error: %s", state.toString()));
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("Fb.onError", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("Fb.onComplete", "Success!");
        }
    };

    /**
     * It's possible start another activity and receive a result back and this event treat the result from activityControl if you already signed in on google
     *
     * @param requestCode
     * @param responseCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent)
    {
        if (requestCode == CODE_SIGN_IN_GOOGLE)
        {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
        else
        {
            uiHelper.onActivityResult(requestCode, responseCode, intent, dialogCallback);
        }
    }

    /*-------------------------------------------------------------------
	 *				 		     GOOGLE HANDLERS
	 *-------------------------------------------------------------------*/

    /**
     * Called when the client connects or is already connected
     * After calling connect(), this method will be invoked asynchronously when the connect request has successfully completed
     *
     * @param arg0
     */
    @Override
    public void onConnected(Bundle arg0)
    {
        // Get user's information

        if( mSignInClicked )
        {
            getProfileInformation();
        }
        else
        {
            signOutFromGoogle();
        }
    }

    /**
     * Called when the client is temporarily in a disconnected state
     *
     * @param arg0
     */
    @Override
    public void onConnectionSuspended(int arg0)
    {
        mGoogleApiClient.connect();
    }

    /**
     * Called when there was an error connecting the client to the service
     *
     * @param result
     */
	@Override
	public void onConnectionFailed(ConnectionResult result)
    {
		if (!result.hasResolution())
        {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

	}

    /**
     * Sign-out from google
     * */
    private void signOutFromGoogle() {
        if (mGoogleApiClient.isConnected())
        {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation()
    {
		try
        {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null)
            {
                if( SplashScreenActivity.settings.getAll().get("email") == null && SplashScreenActivity.settings.getAll().get("password") == null )
                {
                    Person currentPerson = Plus.PeopleApi
                            .getCurrentPerson(mGoogleApiClient);
                    String personName = currentPerson.getDisplayName();
                    String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                    User userApplication = new User();
                    userApplication.setName(personName);
                    userApplication.setEmail(email);
                    userApplication.setPassword("none");

                    accountDelegate.insertUserSocial(userApplication);

                }

			}
            else
            {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}
	}

    /*-------------------------------------------------------------------
	 *				 		   FACEBOOK HANDLERS
	 *-------------------------------------------------------------------*/

    /**
     * Initialize the contents of the Activity's standard options menu
     *
     * @param menu
     * @return
     */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    /**
     * This hook is called whenever an item in your options menu is selected
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_logout:
                signOutFromGoogle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
