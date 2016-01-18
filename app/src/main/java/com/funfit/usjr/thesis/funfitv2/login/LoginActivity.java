package com.funfit.usjr.thesis.funfitv2.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.healthPreference.HealthStatisticsSetupPager;
import com.funfit.usjr.thesis.funfitv2.main.MainActivity;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.tutorial.TutorialActivity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    //firebase
    private Firebase mFirebaseRef;
    private AuthData mAuthData;
    private Firebase.AuthStateListener mAuthStateListener;
    public static final int RC_GOOGLE_LOGIN = 1;
    //google
    private GoogleApiClient mGoogleApiClient;
    private boolean mGoogleIntentInProgress;
    private boolean mGoogleLoginClicked;
    private ConnectionResult mGoogleConnectionResult;
    //facebook
    private CallbackManager mFacebookCallbackManager;
    /* Used to track user logging in/out off Facebook */
    private AccessTokenTracker mFacebookAccessTokenTracker;
    private ProgressDialog mAuthProgressDialog;

    @Bind(R.id.text_funfit)
    TextView mTextFunfit;
    @Bind(R.id.img_login_bg)
    ImageView mImageBg;

    @Bind(R.id.facebookBtn)
    LoginButton mButtonFacebook;
    @Bind(R.id.googleBtn)
    Button mButtonGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Glide.with(this)
                .load(Uri.parse(Constants.LOGINBG_IMG_URL))
                .centerCrop()
                .into(mImageBg);
        mTextFunfit.setTypeface(Typeface.createFromAsset(getAssets(), "HelveticaBold.otf"));


        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        //Facebook
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.i(TAG, "Facebook.AccessTokenTracker.OnCurrentAccessTokenChanged");
                LoginActivity.this.onFacebookAccessTokenChange(currentAccessToken);
            }
        };

        //Google
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();
                setAuthenticatedUser(authData);
            }
        };
        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
        mFirebaseRef.addAuthStateListener(mAuthStateListener);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if user logged in with Facebook, stop tracking their token
        if (mFacebookAccessTokenTracker != null) {
            mFacebookAccessTokenTracker.stopTracking();
        }

        // if changing configurations, stop tracking firebase session.
        mFirebaseRef.removeAuthStateListener(mAuthStateListener);
    }

//    @OnClick(R.id.facebookBtn)
//    public void loginFacebook() {
//        Intent intent = new Intent(this, HealthStatisticsSetupPager.class);
//        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        Toast.makeText(this, "CLick", Toast.LENGTH_LONG).show();
//    }

    @OnClick(R.id.googleBtn)
    public void loginGoogle() {
//        mGoogleLoginClicked = true;
//        if (!mGoogleApiClient.isConnecting()) {
//            if (mGoogleConnectionResult != null) {
//                resolveSignInError();
//            } else if (mGoogleApiClient.isConnected()) {
//                getGoogleOAuthTokenAndLogin();
//            } else {
//                    /* connect API now */
//                Log.d(TAG, "Trying to connect to Google API");
//                mGoogleApiClient.connect();
//            }
//        }


        Intent intent = new Intent(this, TutorialActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Map<String, String> options = new HashMap<String, String>();
        if (requestCode == RC_GOOGLE_LOGIN) {
            /* This was a request by the Google API */
            if (resultCode != RESULT_OK) {
                mGoogleLoginClicked = false;
            }
            mGoogleIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }else {
            /* Otherwise, it's probably the request by the Facebook login button, keep track of the session */
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void logout() {
        if (this.mAuthData != null) {
            /* logout of Firebase */
            mFirebaseRef.unauth();
            /* Logout of any of the Frameworks. This step is optional, but ensures the user is not logged into
             * Facebook/Google+ after logging out of Firebase. */
            if (this.mAuthData.getProvider().equals("facebook")) {
                /* Logout from Facebook */
                LoginManager.getInstance().logOut();
            } else if (this.mAuthData.getProvider().equals("google")) {
                /* Logout from Google+ */
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
            }
            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);
        }
    }

    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            //Login Successful
            Intent intent = new Intent(this, HealthStatisticsSetupPager.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            /* show a provider specific status text */
            String name = null;
            if (authData.getProvider().equals("facebook")
                    || authData.getProvider().equals("google")) {
                name = (String) authData.getProviderData().get("displayName");
            } else if (authData.getProvider().equals("anonymous")
                    || authData.getProvider().equals("password")) {
                name = authData.getUid();
            } else {
                Log.e(TAG, "Invalid provider: " + authData.getProvider());
            }
            if (name != null) {
                Toast.makeText(this,"Logged in as " + name + " (" + authData.getProvider() + ")",Toast.LENGTH_LONG).show();
            }
        } else {
            /* No authenticated user show all the login buttons */
            mButtonFacebook.setVisibility(View.VISIBLE);
            mButtonGoogle.setVisibility(View.VISIBLE);
        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        supportInvalidateOptionsMenu();
    }

    /* ************************************
     *             FACEBOOK               *
     **************************************
     */
    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            mAuthProgressDialog.show();
            mFirebaseRef.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    //TODO:DISPLAY EMAIL
                    Log.v(TAG,authData.getProviderData().get("displayName").toString());
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {

                }
            });
        } else {
            // Logged out of Facebook and currently authenticated with Firebase using Facebook, so do a logout
            if (this.mAuthData != null && this.mAuthData.getProvider().equals("facebook")) {
                mFirebaseRef.unauth();
                setAuthenticatedUser(null);
            }
        }
    }

    /* ************************************
     *              GOOGLE                *
     **************************************
     */
    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mGoogleConnectionResult.hasResolution()) {
            try {
                mGoogleIntentInProgress = true;
                mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mGoogleIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getGoogleOAuthTokenAndLogin() {
        mAuthProgressDialog.show();
        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(LoginActivity.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                mGoogleLoginClicked = false;
                if (token != null) {
                    /* Successfully got OAuth token, now login with Google */
                    mFirebaseRef.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Log.v(TAG,authData.getProviderData().get("displayName").toString());
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Log.e(TAG,firebaseError.getMessage().toString());
                        }
                    });
                } else if (errorMessage != null) {
                    mAuthProgressDialog.hide();
                    Log.e(TAG,errorMessage.toString());
                }
            }
        };
        task.execute();
    }

    @Override
    public void onConnected(final Bundle bundle) {
        /* Connected with Google API, use this to authenticate with Firebase */
        getGoogleOAuthTokenAndLogin();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mGoogleIntentInProgress) {
            /* Store the ConnectionResult so that we can use it later when the user clicks on the Google+ login button */
            mGoogleConnectionResult = result;

            if (mGoogleLoginClicked) {
                /* The user has already clicked login so we attempt to resolve all errors until the user is signed in,
                 * or they cancel. */
                resolveSignInError();
            } else {
                Log.e(TAG, result.toString());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ignore
    }
}