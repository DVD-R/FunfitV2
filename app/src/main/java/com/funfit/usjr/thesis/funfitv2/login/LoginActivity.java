package com.funfit.usjr.thesis.funfitv2.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.funfit.usjr.thesis.funfitv2.FunfitApplication;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.main.MainActivity;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.User;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements
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

    @Bind(R.id.text_funfit)
    TextView mTextFunfit;
    @Bind(R.id.img_login_bg)
    ImageView mImageBg;
    @Bind(R.id.login_progress)
    ProgressBar mProgressLogin;
    @Bind(R.id.facebookBtn)
    Button mButtonFacebook;
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
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mProgressLogin.setVisibility(View.GONE);
                mButtonGoogle.setVisibility(View.VISIBLE);
                mButtonFacebook.setVisibility(View.VISIBLE);
                setAuthenticatedUser(authData);
            }
        };
        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
        mFirebaseRef.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressLogin.setVisibility(View.GONE);
        mButtonGoogle.setVisibility(View.VISIBLE);
        mButtonFacebook.setVisibility(View.VISIBLE);

        // if user logged in with Facebook, stop tracking their token
        if (mFacebookAccessTokenTracker != null) {
            mFacebookAccessTokenTracker.stopTracking();
        }

        // if changing configurations, stop tracking firebase session.
        mFirebaseRef.removeAuthStateListener(mAuthStateListener);
    }

    @OnClick(R.id.facebookBtn)
    public void loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, user_about_me", "email, user_birthday"));
    }

    @OnClick(R.id.googleBtn)
    public void loginGoogle() {
        mGoogleLoginClicked = true;
        if (!mGoogleApiClient.isConnecting()) {
            if (mGoogleConnectionResult != null) {
                resolveSignInError();
            } else if (mGoogleApiClient.isConnected()) {
                getGoogleOAuthTokenAndLogin();
            } else {
                    /* connect API now */
                Log.d(TAG, "Trying to connect to Google API");
                mGoogleApiClient.connect();
            }
        }
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
        } else {
            /* Otherwise, it's probably the request by the Facebook login button, keep track of the session */
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            ((FunfitApplication)getApplicationContext()).setLoginAuth(authData);
            ((FunfitApplication)getApplicationContext()).setFirebaseRef(mFirebaseRef);
            ((FunfitApplication)getApplicationContext()).setGoogleApiClient(mGoogleApiClient);

            if(getSharedPreferences(Constants.USER_PREF_ID, MODE_PRIVATE).getString(Constants.PROFILE_EMAIL, null)!=null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

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
                Toast.makeText(this, "Logged in as " + name + " (" + authData.getProvider() + ")", Toast.LENGTH_LONG).show();
            }
        } else {
            /* No authenticated user show all the login buttons */
        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        supportInvalidateOptionsMenu();
    }

    /* ************************************
     *             FACEBOOK               *
     **************************************
     */
    private void onFacebookAccessTokenChange(final AccessToken token) {
        if (token != null) {
            mProgressLogin.setVisibility(View.VISIBLE);
            mButtonGoogle.setVisibility(View.GONE);
            mButtonFacebook.setVisibility(View.GONE);

            mFirebaseRef.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    startSignupFacebook(authData, token);
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

    private void startSignupFacebook(final AuthData authData, AccessToken token) {
        Bundle params = new Bundle();
        params.putString("fields", "picture.type(large)");
        new GraphRequest(token, "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
                                if (data.has("picture")) {
                                    if (authData != null) {
                                        String email, fname, lname, img_url;
                                        String[] fullName = ((String) authData.getProviderData().get("displayName")).split("\\s+");
                                        img_url = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        email = authData.getProviderData().get("email") + "";
                                        fname = fullName[0];
                                        lname = fullName[fullName.length - 1];
                                        isUserOnFirebase(email, fname, lname, img_url);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();
    }

    private void isUserOnFirebase(final String email, final String fname, final String lname, final String img_url) {
        final Firebase userFirebase = new Firebase(Constants.FIREBASE_URL_USERS + "/" + Utils.encodeEmail(email));
        userFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        Log.v(TAG, dataSnapshot.getValue().toString());
                        savePreferenceRegisteredUser(dataSnapshot);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(Constants.PROFILE_IMG_URL, img_url);
                        intent.putExtra(Constants.PROFILE_EMAIL, email);
                        intent.putExtra(Constants.PROFILE_FNAME, fname);
                        intent.putExtra(Constants.PROFILE_LNAME, lname);

                        startActivity(intent);
                        finish();
                    }
                } catch (NullPointerException e) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(Constants.PROFILE_IMG_URL, img_url);
                    intent.putExtra(Constants.PROFILE_EMAIL, email);
                    intent.putExtra(Constants.PROFILE_FNAME, fname);
                    intent.putExtra(Constants.PROFILE_LNAME, lname);

                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void savePreferenceRegisteredUser(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        final SharedPreferences userPreferences = getSharedPreferences(Constants.USER_PREF_ID, MODE_PRIVATE);
        userPreferences.edit().putString(Constants.PROFILE_FNAME, user.getFname()).apply();
        userPreferences.edit().putString(Constants.PROFILE_LNAME, user.getLname()).apply();
        userPreferences.edit().putString(Constants.PROFILE_EMAIL, user.getEmail()).apply();
        userPreferences.edit().putString(Constants.PROFILE_GENDER, user.getGender()).apply();
        userPreferences.edit().putString(Constants.PROFILE_DOB, user.getDob()).apply();
        userPreferences.edit().putString(Constants.PROFILE_IMG_URL, user.getImg_url()).apply();
        userPreferences.edit().putString(Constants.PROFILE_WEIGHT, user.getWeight()).apply();
        userPreferences.edit().putString(Constants.PROFILE_HEIGHT, user.getHeight()).apply();
        userPreferences.edit().putString(Constants.PROFILE_ACTIVITY_LEVEL, user.getActivity_level()).apply();
        userPreferences.edit().putString(Constants.PROFILE_CLUSTER, user.getCluster()).apply();
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
        mProgressLogin.setVisibility(View.VISIBLE);
        mButtonGoogle.setVisibility(View.GONE);
        mButtonFacebook.setVisibility(View.GONE);
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
                            startSignupGoogle();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Log.e(TAG, firebaseError.getMessage().toString());
                        }
                    });
                } else if (errorMessage != null) {
                    mProgressLogin.setVisibility(View.GONE);
                    mButtonGoogle.setVisibility(View.VISIBLE);
                    mButtonFacebook.setVisibility(View.VISIBLE);
                    Log.e(TAG, errorMessage.toString());
                }
            }
        };
        task.execute();
    }

    private void startSignupGoogle() {
        String email, fname, lname, img_url;
        fname = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getName().getGivenName();
        lname = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getName().getFamilyName();
        email = Plus.AccountApi.getAccountName(mGoogleApiClient);
        img_url = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient).getImage().getUrl();
        isUserOnFirebase(email, fname, lname, img_url);
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