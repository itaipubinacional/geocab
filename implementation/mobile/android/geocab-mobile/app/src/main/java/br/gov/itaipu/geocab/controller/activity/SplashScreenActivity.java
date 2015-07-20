package br.gov.itaipu.geocab.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;

import br.gov.itaipu.geocab.R;
import br.gov.itaipu.geocab.controller.delegate.AccountDelegate;
import br.gov.itaipu.geocab.entity.User;
import br.gov.itaipu.geocab.util.DelegateHandler;

public class SplashScreenActivity extends Activity {

    /**
     * Nome das preferências do apk
     */
    public static final String GEOCAB_PREFERENCES = "GeocabPrefs";
    /**
     * Preferências do apk
     */
    public static SharedPreferences settings;
    /**
     * Editor de preferências do apk
     */
    public static SharedPreferences.Editor prefEditor;
    /**
     * ProgressBar que indica o andamento do processo de Login.
     */
    public ProgressBar progressBar;

    private AccountDelegate accountDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        progressBar = ( ProgressBar ) findViewById( R.id.progress_login );
        progressBar.setProgress( 10 );

        settings = getSharedPreferences( GEOCAB_PREFERENCES, MODE_PRIVATE );
        prefEditor = settings.edit();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (settings.getAll().get("email") != null && settings.getAll().get("password") != null) {
                    accountDelegate = new AccountDelegate(SplashScreenActivity.this);

                    final User user = new User();
                    user.setEmail(settings.getAll().get("email").toString());
                    user.setPassword(settings.getAll().get("password").toString());
                    user.setBasicAuthorization(user.getEmail(), user.getPassword());

                    accountDelegate.authenticateUser(user, new DelegateHandler() {
                        public void responseHandler(Object error) {
                            startActivity(new Intent(SplashScreenActivity.this, AuthenticationActivity.class));
                            SplashScreenActivity.this.finish();
                        }
                    });
                }
                else
                {
                    startActivity(new Intent(SplashScreenActivity.this, AuthenticationActivity.class));
                    SplashScreenActivity.this.finish();
                }
            }
        }, 1000);

    }
}
