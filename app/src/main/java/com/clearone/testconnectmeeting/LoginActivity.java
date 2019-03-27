package com.clearone.testconnectmeeting;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clearone.sptcore.sptim.SptTokenDataResult;
import com.clearone.sptimpublicsdk.ISptCallData;
import com.clearone.sptimpublicsdk.ISptIMSDK;
import com.clearone.sptimpublicsdk.SptCallID;
import com.clearone.sptimpublicsdk.SptSchMeetingSequenceID;

import java.util.ArrayList;

import static com.clearone.sptimpublicsdk.ISptIMSDK.eSptResult.eSptIMResultError;

public class LoginActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private final static String UNCHANGED_STORED_PASSWORD = "FYW452RFBDF";

    EditText _serverView;
    EditText _emailView;
    EditText _passwordView;
    Button _connectButton;
    EditText _tokenView;
    Button _connectByTokenButton;
    View _progressView;
    SptCallID _callID;
    SptSchMeetingSequenceID _tokenSequenceID;

    ISptIMSDK _sdk;
    TestConnectSptCallObserver _callObserver;
    TextConnectSptIMObserver _sptObserver;

    private final TextWatcher _watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            boolean bEnabled = (_serverView.length() > 0 &&
            _emailView.length()>0 &&
            _passwordView.length()>0);
            _connectButton.setEnabled(bEnabled);
            bEnabled = _tokenView.length() > 0;
            _connectByTokenButton.setEnabled(bEnabled);
        }
    };

    class TestConnectSptCallObserver extends SptCallObserver
    {
        @Override
        public void onCallEventConnected(SptCallID sptCallID, ISptCallData iSptCallData) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(LoginActivity.this, CallActivity.class);
                    if(_callID != null)
                        i.putExtra(CallActivity.EXTRA_CALL_ID, _callID.intValue());
                    startActivity(i);
                    finish();
                }
            });
        }

        @Override
        public void onCallEventDisconnected(SptCallID sptCallID, ISptCallData iSptCallData) {
            super.onCallEventDisconnected(sptCallID, iSptCallData);
        }

        @Override
        public void onCallEventStateUpdated(SptCallID sptCallID, ISptCallData iSptCallData) {
            super.onCallEventStateUpdated(sptCallID, iSptCallData);
        }
    }
    
    class TextConnectSptIMObserver extends SptIMObserver
    {
        @Override
        public void onConnected()
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    if(_tokenSequenceID == null)
                    {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else if(_sdk.areMeetingsSynchronized())
                    {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra(MainActivity.EXTRA_JOIN_TO_MEETING, _tokenSequenceID.intValue());
                        startActivity(i);
                        finish();
                    }
                }
            });
        }

        @Override
        public void onSchMeetingsSynchronized()
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    if(_tokenSequenceID != null)
                    {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra(MainActivity.EXTRA_JOIN_TO_MEETING, _tokenSequenceID.intValue());
                        startActivity(i);
                        finish();
                    }
                }
            });
        }

        @Override
        public void onDisconnected()
        {
            super.onDisconnected();
        }

        @Override
        public void onConnectionError(final ISptIMSDK.eSptConnectionResult eResult)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    _progressView.setVisibility(View.GONE);
                    switch (eResult)
                    {
                        case eSptIMConnect_AuthError:
                            showToast("eSptIMConnect_AuthError");
                            break;
                        case eSptIMConnect_GDPRPending:
                            showGdprDialog();
                            break;
                        case eSptIMConnect_CredentialsError:
                            _emailView.setError("Credentials Error");
                            _passwordView.setError("Credentials Error");
                            break;
                        case eSptIMConnect_NetworkError:
                            _serverView.setError("Server Not Reachable");
                            break;
                    }
                }
            });
        }

        @Override
        public void onGetTokenDataRes(final SptTokenDataResult tokenDataRes)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    switch(tokenDataRes.getResult())
                    {
                        case SptTokenDataResultJoinMeeting:
                            _tokenSequenceID = tokenDataRes.getMeetingSequenceID();
                            if(!tokenDataRes.contactAlreadyLogged())
                                _sdk.loginWithTokenDataResult(tokenDataRes);
                            break;
                        case SptTokenDataResultLogin:
                            if(!tokenDataRes.contactAlreadyLogged())
                                _sdk.loginWithTokenDataResult(tokenDataRes);
                            break;
                        case SptTokenDataResultInvalidToken:
                            _tokenView.setError("Invalid Token");
                            _progressView.setVisibility(View.GONE);
                            break;
                        case SptTokenDataResultServerNotReachable:
                            _passwordView.setError("Server Not Reachable");
                            _progressView.setVisibility(View.GONE);
                            break;
                        case SptTokenDataResultError:
                            showToast("SptTokenDataResultError");
                            break;
                    }

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _sdk = ((TestConnectMeetingApplication)getApplication()).getSptIMSDK();
        _callObserver = new TestConnectSptCallObserver();
        _sdk.addCallObserver(_callObserver);
        _sptObserver = new TextConnectSptIMObserver();
        _sdk.addObserver(_sptObserver);
        _progressView = findViewById(R.id.activity_login_progress);
        
        _serverView = (EditText)findViewById(R.id.activity_login_server);
        _emailView = (EditText)findViewById(R.id.activity_login_email);
        _passwordView = (EditText)findViewById(R.id.activity_login_password);
        _connectButton = (Button)findViewById(R.id.activity_login_connect_button);
        _connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String server = _serverView.getText().toString();
                String email = _emailView.getText().toString();
                String password = _passwordView.getText().toString();
                boolean bManualConnect = true;
                if(server.length() > 0 && email.length() > 0 && password.length() > 0)
                {
                    if(_sdk.hasCredentials())
                    {
                        if(email.compareToIgnoreCase(_sdk.getStoredAccountEmail()) == 0
                                && _passwordView.getText().toString().equals(UNCHANGED_STORED_PASSWORD))
                        {
                            if (_sdk.connect() != eSptIMResultError)
                                _progressView.setVisibility(View.VISIBLE);
                            bManualConnect = false;
                        }
                    }
                    if(bManualConnect)
                    {
                        _sdk.setLoginData(true, email, password, server);
                        if (_sdk.connect() != eSptIMResultError)
                            _progressView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        _tokenView = (EditText)findViewById(R.id.activity_login_token);
        _connectByTokenButton = (Button)findViewById(R.id.activity_login_connect_button_by_token);
        _connectByTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = _tokenView.getText().toString();
                String server = _serverView.getText().toString();
                if(server.length() == 0)
                    server = null;
                if(token.length() > 0)
                {
                    _sdk.getTokenData(token, server);
                    _progressView.setVisibility(View.VISIBLE);
                }
            }
        });

        _serverView.addTextChangedListener(_watcher);
        _emailView.addTextChangedListener(_watcher);
        _passwordView.addTextChangedListener(_watcher);
        _tokenView.addTextChangedListener(_watcher);

        manageMainPermissions();
        loadInitialValues();
    }

    void loadInitialValues()
    {
        if(_sdk.hasCredentials())
        {
            _emailView.setText(_sdk.getStoredAccountEmail());
            _passwordView.setText(UNCHANGED_STORED_PASSWORD);
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _sdk.removeCallObserver(_callObserver);
        _sdk.removeObserver(_sptObserver);
    }

    void manageMainPermissions()
    {

        ArrayList<String> permissionsArray = new ArrayList<>(2);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            permissionsArray.add(Manifest.permission.CAMERA);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)
            permissionsArray.add(Manifest.permission.RECORD_AUDIO);
        if(permissionsArray.size()>0)
            ActivityCompat.requestPermissions(this, permissionsArray.toArray(new String[permissionsArray.size()]),
                    REQUEST_CODE_ASK_PERMISSIONS);
    }

    void showGdprDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("GDPR")
                .setMessage("Do you accept license terms?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        _sdk.answerGDPRAgreement(true);
                        _progressView.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        _progressView.setVisibility(View.GONE);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showToast(final String message)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                CharSequence text = message;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0,10);
                toast.show();
            }
        });

    }

}
