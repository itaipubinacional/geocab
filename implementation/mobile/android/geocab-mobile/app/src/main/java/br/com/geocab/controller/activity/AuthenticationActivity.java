package br.com.geocab.controller.activity;

import android.app.Activity;
import android.content.Context;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.geocab.R;
import br.com.geocab.controller.delegate.AbstractDelegate;
import br.com.geocab.controller.delegate.AccountDelegate;
import br.com.geocab.entity.User;

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
	private GoogleApiClient mGoogleApiClient;

    /**
     * Connection result of sign in google
     */
	private ConnectionResult mConnectionResult;

    /**
     * Button sign in google
     */
	private SignInButton btnSignInGoogle;

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

        loginButton = (LoginButton) findViewById(R.id.btn_sign_in_facebook);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                AuthenticationActivity.this.user = user;
                if( user != null)
                Toast.makeText(AuthenticationActivity.this, "LOGADO PELO FACEBOOK COM: " + user.getName(), Toast.LENGTH_LONG).show();
            }
        });

        // Getting the button from view
        btnSignInGoogle = (SignInButton) findViewById(R.id.btn_sign_in_google);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);

        setGooglePlusButtonText(btnSignInGoogle, "Google");

        showHashKey(this);

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
     *
     * @param context
     */
    //GENERATE KEY HASH ( REMOVE LATER )
    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "br.com.geocab", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    /**
     * Called after onCreate
     */
	protected void onStart()
    {
		super.onStart();
		mGoogleApiClient.connect();
	}

    /**
     * Called when you are no longer visible to the user
     */
	protected void onStop()
    {
		super.onStop();
		if (mGoogleApiClient.isConnected())
        {
			mGoogleApiClient.disconnect();
		}
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
            try
            {
                mConnectionResult.startResolutionForResult(this, CODE_SIGN_IN_GOOGLE);
            }
            catch (SendIntentException e)
            {
                // Try connect again
                mConnectionResult = null;
                mGoogleApiClient.connect();
            }
        }
        else if( view.getId() == R.id.btn_sign_in )
        {
            AccountDelegate accountDelegate = new AccountDelegate(this);
            final byte[] credentials = ( editTextUsername.getText() + ":" + editTextPassword.getText() ).getBytes();
            AbstractDelegate.loggedUser = new User(editTextPassword.getText().toString());
            accountDelegate.checkLogin(Base64.encodeToString(credentials, Base64.NO_WRAP));
        }
    }

    /*-------------------------------------------------------------------
	 *				 		   FACEBOOk HANDLERS
	 *-------------------------------------------------------------------*/

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d("TEste", String.format("Error: %s", "TESTE"));
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
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
            Intent mapIntent = new Intent(this, MapActivity.class);
            startActivity(mapIntent);
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
        getProfileInformation();
    }

    /**
     * Called when the client is temporarily in a disconnected state
     *
     * @param arg0
     */
    @Override
    public void onConnectionSuspended(int arg0)
    {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_LONG).show();
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

        // Store the ConnectionResult for later usage
        mConnectionResult = result;
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
            Toast.makeText(this, "User disconnected!", Toast.LENGTH_LONG).show();
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
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Toast.makeText(AuthenticationActivity.this, "LOGADO PELO GOOGLE COM: " + personName, Toast.LENGTH_LONG).show();

				Log.e(TAG, "Name: " + personName + ", plusProfile: "
						+ personGooglePlusProfile + ", email: " + email
						+ ", Image: " + personPhotoUrl);

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
