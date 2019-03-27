package com.clearone.testconnectmeeting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.clearone.sptimpublicsdk.ISptCallData;
import com.clearone.sptimpublicsdk.ISptIMSDK;
import com.clearone.sptimpublicsdk.ISptSchMeeting;
import com.clearone.sptimpublicsdk.ISptSchMeetingSequence;
import com.clearone.sptimpublicsdk.SptCallID;
import com.clearone.sptimpublicsdk.SptIMSDKApp;
import com.clearone.sptimpublicsdk.SptSchJoinMeeting;
import com.clearone.sptimpublicsdk.SptSchMeetingSequenceID;
import com.clearone.testconnectmeeting.contacts.ContactsFragment;
import com.clearone.testconnectmeeting.sequences.SequencesFragment;

public class MainActivity extends AppCompatActivity
{
    public static final String EXTRA_JOIN_TO_MEETING = "EXTRA_JOIN_TO_MEETING";
    MainActivitySptIMObserver _sptIMObserver;
    TestConnectSptCallObserver _callObserver;
    ViewPager _viewPager;
    MainPagerAdapter _pageAdapter;
    TabLayout _tabLayout;
    SptSchMeetingSequenceID _tokenSequenceID;
    ISptIMSDK _sdk;
    SptCallID _callID;
    AlertDialog _callDialog;

    class TestConnectSptCallObserver extends SptCallObserver
    {
        @Override
        public void onCallEventConnected(SptCallID sptCallID, ISptCallData iSptCallData) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this, CallActivity.class);
                    if(_callID != null)
                        i.putExtra(CallActivity.EXTRA_CALL_ID, _callID.intValue());
                    startActivity(i);
                }
            });
        }

        @Override
        public void onCallEventDisconnected(SptCallID sptCallID, ISptCallData iSptCallData) {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    Log.d("call state", "onCallEventDisconnected");
                    if(_callDialog != null)
                    {
                        _callDialog.cancel();
                        _callDialog = null;
                        _callID = null;
                    }
                }
            });
        }

        @Override
        public void onCallEventStateUpdated(final SptCallID sptCallID, final ISptCallData iSptCallData) {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    if(_callID == null)
                        _callID = sptCallID;
                    Log.d("call state", iSptCallData.getCallState().name());
                    switch (iSptCallData.getCallState())
                    {

                        case eCallStateCreating:
                            showCallDialog();
                            break;
                        case eCallStateRinging:
                            break;
                        case eCallStateAccepted:
                            break;
                        case eCallStateConnecting:
                            break;
                        case eCallStateWaitingParticipants:
                            break;
                        case eCallStateReconnecting:
                            break;
                        case eCallStateConnected:
                            break;
                        case eCallStateConnectedInOtherDevice:
                            break;
                        case eCallStateDisconnecting:
                            break;
                        case eCallStateDisconnected:
                            _callID = null;
                            break;
                        case eCallStateRejected:
                            _callID = null;
                            break;
                        case eCallStateError:
                            _callID = null;
                            break;
                    }
                }
            });
        }

        @Override
        public void onCallEventIncomingCall(SptCallID sptCallID, ISptCallData iSptCallData)
        {
            if(_callID == null)
            {
                _callID = sptCallID;
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showIncomingCallDialog();
                    }
                });
            }
        }

        @Override
        public void onCallEventCallConnectivityUpdated(SptCallID sptCallID, ISptCallData iSptCallData)
        {
            super.onCallEventCallConnectivityUpdated(sptCallID, iSptCallData);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _sptIMObserver = new MainActivitySptIMObserver();
        _callObserver = new TestConnectSptCallObserver();
        _sdk = ((SptIMSDKApp)getApplication()).getSptIMSDK();
        _sdk.addObserver(_sptIMObserver);
        _sdk.addCallObserver(_callObserver);
        _pageAdapter = new MainPagerAdapter(getSupportFragmentManager());
        _viewPager = findViewById(R.id.main_view_pager);
        _viewPager.setAdapter(_pageAdapter);
        _tabLayout = (TabLayout)findViewById(R.id.tablayout);
        _tabLayout.setupWithViewPager(_viewPager);
        Bundle args = getIntent().getExtras();
        if(args != null)
        {
            _tokenSequenceID = new SptSchMeetingSequenceID(args.getInt(EXTRA_JOIN_TO_MEETING, SptSchMeetingSequenceID.SPT_INVALID_MEETING_SEQUENCE_ID));
        }
        processLaunchFromToken(_tokenSequenceID);
    }

    private void processLaunchFromToken(SptSchMeetingSequenceID sequenceID)
    {
        if(sequenceID != null && sequenceID.intValue() != SptSchMeetingSequenceID.SPT_INVALID_MEETING_SEQUENCE_ID)
        {
            ISptSchMeetingSequence seq = _sdk.getSchMeetingSequenceByID(sequenceID);
            if(seq != null)
            {
                ISptSchMeeting m = seq.getCurrentMeeting();
                if(m!= null)
                {
                    SptSchJoinMeeting joinMeeting = new SptSchJoinMeeting(sequenceID, m.getSchMeetingID(), true);
                    _sdk.joinSchMeeting(joinMeeting);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.contacts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_contacts_logout:
                ((SptIMSDKApp)getApplication()).getSptIMSDK().forgetCredentials();
                ((SptIMSDKApp)getApplication()).getSptIMSDK().disconnect();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        _sptIMObserver = new MainActivitySptIMObserver();
        ISptIMSDK sdk = ((SptIMSDKApp)getApplication()).getSptIMSDK();
        sdk.removeObserver(_sptIMObserver);
        sdk.removeCallObserver(_callObserver);
    }

    private void showCallDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Call In Progress...").setPositiveButton("Hang", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
               _sdk.hangUpCall(_callID);
                _callDialog = null;
            }
        }).setCancelable(false);
        _callDialog = builder.show();
    }

    private void showIncomingCallDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Call In Progress...")
                .setPositiveButton("Video", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        _sdk.answerCall(_callID, true, true);
                        _callDialog = null;
                    }
                }).setNegativeButton("Audio", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                _sdk.answerCall(_callID, true, false);
                _callDialog = null;
            }
        }).setNeutralButton("Hang", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                _sdk.answerCall(_callID, false, true);
                _callDialog = null;
            }
        }).setCancelable(false);
        _callDialog = builder.show();
    }

    public class MainPagerAdapter extends FragmentPagerAdapter
    {
        public static final int POS_CONTACTS = 0;
        public static final int POS_SEQUENCES = 1;
        public static final int POS_COUNT = 2;

        public MainPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int i)
        {
            Fragment f = null;
            switch (i)
            {
                case POS_CONTACTS:
                    f = new ContactsFragment();
                    break;
                case POS_SEQUENCES:
                    f = new SequencesFragment();
                    break;
            }
            return f;
        }

        @Override
        public int getCount()
        {
            return POS_COUNT;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position)
        {
            String title;
            switch (position)
            {
                case POS_CONTACTS:
                    title = "Contacts";
                    break;
                case POS_SEQUENCES:
                    title = "Meetings";
                    break;
                    default:
                        title = "";
            }
            return title;
        }
    }


    class MainActivitySptIMObserver extends SptIMObserver
    {
        @Override
        public void onDisconnected()
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    Intent restartIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(restartIntent);
                    finish();
                }
            });
        }
    }
}
