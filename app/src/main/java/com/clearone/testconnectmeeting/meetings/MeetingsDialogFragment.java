package com.clearone.testconnectmeeting.meetings;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clearone.sptcore.sptim.meetings.SptSchMeetingSequence;
import com.clearone.sptimpublicsdk.ISptIMSDK;
import com.clearone.sptimpublicsdk.ISptSchMeeting;
import com.clearone.sptimpublicsdk.ISptSchMeetingSequence;
import com.clearone.sptimpublicsdk.SptIMSDKApp;
import com.clearone.sptimpublicsdk.SptSchJoinMeeting;
import com.clearone.sptimpublicsdk.SptSchMeetingID;
import com.clearone.sptimpublicsdk.SptSchMeetingSequenceID;
import com.clearone.testconnectmeeting.R;

import java.util.ArrayList;
import java.util.Collection;

public class MeetingsDialogFragment extends DialogFragment implements MeetingsAdapter.IMeetingItemObserver
{
    private static final String ARG_MEETING_SEQUENCE_ID = "ARG_MEETING_SEQUENCE_ID";

    SptSchMeetingSequenceID _sequenceID;
    MeetingsAdapter _adapter;

    public static MeetingsDialogFragment newInstance(SptSchMeetingSequenceID sequenceID)
    {
        MeetingsDialogFragment res = new MeetingsDialogFragment();
        Bundle args = new Bundle();
        if(sequenceID != null)
            args.putInt(ARG_MEETING_SEQUENCE_ID, sequenceID.intValue());

        res.setArguments(args);
        return res;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null)
        {
            _sequenceID = new SptSchMeetingSequenceID(args.getInt(ARG_MEETING_SEQUENCE_ID,
                    SptSchMeetingSequenceID.SPT_INVALID_MEETING_SEQUENCE_ID));
        }
        _adapter = new MeetingsAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_contacts, null);
        RecyclerView list = (RecyclerView)v.findViewById(R.id.contacts_list);
        list.setAdapter(_adapter);
        loadContacts();
        return v;
    }

    private void loadContacts()
    {
        Activity a = getActivity();
        if(a != null)
        {
            ArrayList<ISptSchMeeting>list = new ArrayList<>();
            ISptSchMeetingSequence seq = ((SptIMSDKApp) a.getApplication()).getSptIMSDK().getSchMeetingSequenceByID(_sequenceID);
            if(seq != null)
            {
                Collection<ISptSchMeeting> meetings = seq.getMeetings();
                if(meetings != null)
                {
                    for(ISptSchMeeting m: meetings)
                    {
                        list.add(m);
                    }
                }
                _adapter.loadElements(list);
            }
        }
    }

    @Override
    public void onMeetingJoinRequest(int sequenceId, int meetingId, boolean bVideo)
    {
        Activity a = getActivity();
        if(a != null)
        {
            ISptIMSDK sdk = ((SptIMSDKApp)a.getApplication()).getSptIMSDK();
            SptSchJoinMeeting joinMeeting = new SptSchJoinMeeting(new SptSchMeetingSequenceID(sequenceId),
                    new SptSchMeetingID(meetingId), bVideo);
            sdk.joinSchMeeting(joinMeeting);
        }
        dismissAllowingStateLoss();
    }
}
