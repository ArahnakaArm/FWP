package com.example.deimos.fwp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Demonstrates retrieving an offline access one-time code for the current Google user, which
 * can be exchanged by your server for an access token and refresh token.
 */
public class TestGoogle extends AppCompatActivity implements
        View.OnClickListener {
    private String authCodeGoogle;
    public static final String TAG = "ServerAuthCodeActivity";
    private static final int RC_GET_AUTH_CODE = 9003;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView mAuthCodeTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testgoogle);

        mAuthCodeTextView = findViewById(R.id.detail);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        validateServerClientIDGoogle();

        String serverClientId = "138956954782-2vpo3krp7om6ruuacbiparld63u5a13f.apps.googleusercontent.com";
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void getAuthCode() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE);
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUI(null);
            }
        });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_AUTH_CODE) {
            // [START get_auth_code]
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String authCode = account.getServerAuthCode();
                Log.d("Test",authCode);
                authCodeGoogle = authCode;
                // Show signed-un UI
                getTokenGoogle();
                updateUI(account);

                // TODO(developer): send code to server and exchange for access/refresh/ID tokens
            } catch (ApiException e) {
                Log.w(TAG, "Sign-in failed", e);
                updateUI(null);
            }
            // [END get_auth_code]
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            ((TextView) findViewById(R.id.status)).setText("signin");

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);

            String authCode = account.getServerAuthCode();
            mAuthCodeTextView.setText(authCode);
        } else {
            ((TextView) findViewById(R.id.status)).setText("signout");
            mAuthCodeTextView.setText("null");

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    /**
     * Validates that there is a reasonable server client ID in strings.xml, this is only needed
     * to make sure users of this sample follow the README.
     */
    private void validateServerClientIDGoogle() {
        String serverClientId = "138956954782-2vpo3krp7om6ruuacbiparld63u5a13f.apps.googleusercontent.com";
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                getAuthCode();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    public void getTokenGoogle(){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("grant_type", "authorization_code")
                .add("client_id", "138956954782-2vpo3krp7om6ruuacbiparld63u5a13f.apps.googleusercontent.com")
                .add("client_secret", "m9NhWM4uKEAxtL-awlfpr9M8")
                .add("redirect_uri","")
                .add("code", authCodeGoogle)
                .build();
        final Request request = new Request.Builder()
                .url("https://www.googleapis.com/oauth2/v4/token")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                Log.e("Test", e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    //JSONArray arr = new JSONArray(response);
                  //  JSONObject jObj = arr.getJSONObject(0);
                   //
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String token = jsonObject.getString("access_token");
                    final String message = jsonObject.toString(5);
                    Log.i("Test", message);
                    Log.i("Test", token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}