package br.com.geocab.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.facebook.LoginActivity;

import java.io.IOException;
import java.util.Map;

import br.com.geocab.R;
import br.com.geocab.controller.delegate.AbstractDelegate;
import br.com.geocab.controller.delegate.AccountDelegate;
import br.com.geocab.entity.User;

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

                    User user = new User();

                    user.setEmail(settings.getAll().get("email").toString());
                    user.setPassword(settings.getAll().get("password").toString());

                    final byte[] credentials = (user.getEmail() + ":" + user.getPassword()).getBytes();
                    accountDelegate.checkLogin(Base64.encodeToString(credentials, Base64.NO_WRAP));

                }
                else
                {
                    startActivity(new Intent(SplashScreenActivity.this, AuthenticationActivity.class));
                    SplashScreenActivity.this.finish();
                }
            }
        }, 1000);

    }

    /**
     *  Responsável por carregar os preferences do APK e Realizar o Login do Usuario.
     */
    private class InitiliazeApp extends AsyncTask<Void, Void, Boolean>
    {
        /**
         *
         * @param voids
         * @return
         */
        @Override
        protected Boolean doInBackground( Void... voids )
        {
            try
            {
                settings = getSharedPreferences( GEOCAB_PREFERENCES, MODE_PRIVATE );
                prefEditor = settings.edit();
                Thread.sleep( 1000 );
                progressBar.setProgress( 60 );

                if (settings.getAll().get("email") != null && settings.getAll().get("password") != null) {
                    accountDelegate = new AccountDelegate(SplashScreenActivity.this);

                    User user = new User();

                    user.setEmail(settings.getAll().get("email").toString());
                    user.setPassword(settings.getAll().get("password").toString());

                    final byte[] credentials = (user.getEmail() + ":" + user.getPassword()).getBytes();
                    accountDelegate.checkLogin(Base64.encodeToString(credentials, Base64.NO_WRAP));

                }
                else
                {
                    return false;
                }

            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }

            return true;

        }

        /**
         *
         * @param aVoid
         */
        @Override
        protected void onPostExecute( final Boolean aVoid )
        {
            progressBar.setProgress( 100 );
            if ( !aVoid )
            {
                startActivity(new Intent(SplashScreenActivity.this, AuthenticationActivity.class));
                SplashScreenActivity.this.finish();
            }
        }
    }
}
