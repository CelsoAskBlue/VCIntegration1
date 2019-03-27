package com.clearone.testconnectmeeting.meetings;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.clearone.sptimpublicsdk.ISptSchMeeting;
import com.clearone.sptimpublicsdk.ISptSchMeetingSequence;
import com.clearone.testconnectmeeting.R;
import com.clearone.testconnectmeeting.contacts.ContactsAdapter;

import java.util.Calendar;

public class MeetingItemView extends RecyclerView.ViewHolder
{
    public interface IAdapterItemObserver
    {
        public void onItemSelected(int pos, boolean bSelected);
        public MeetingsAdapter.IMeetingItemObserver getContactObserver();
    }
    TextView _yearView;
    TextView _monthView;
    TextView _nameView;
    TextView _dayView;
    FrameLayout _iconArea;
    View _dateArea;
    View _buttonsArea;
    View _audioCallButton;
    View _videoCallButton;
    IAdapterItemObserver _observer;
    int _sequenceId;
    int _meetingId;

    public MeetingItemView(@NonNull View itemView, IAdapterItemObserver observer)
    {
        super(itemView);
        _observer = observer;
    }

    public void bind(ISptSchMeeting meeting)
    {
        if(_yearView == null)
        {
            _yearView = (TextView) itemView.findViewById(R.id.seq_year);
            _monthView = (TextView) itemView.findViewById(R.id.seq_month);
            _nameView = (TextView) itemView.findViewById(R.id.seq_name);
            _dayView = (TextView)itemView.findViewById(R.id.seq_day);
            _iconArea = itemView.findViewById(R.id.icon_area);
            _dateArea = itemView.findViewById(R.id.date_area);
            _buttonsArea = itemView.findViewById(R.id.button_bar);
            _audioCallButton = itemView.findViewById(R.id.audio_call);
            _videoCallButton = itemView.findViewById(R.id.video_call);
            _audioCallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(_observer != null)
                    {
                        MeetingsAdapter.IMeetingItemObserver itemObserver = _observer.getContactObserver();
                        if(itemObserver != null)
                            itemObserver.onMeetingJoinRequest(_sequenceId, _meetingId, false);
                    }
                }
            });
            _videoCallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(_observer != null)
                    {
                        MeetingsAdapter.IMeetingItemObserver itemObserver = _observer.getContactObserver();
                        if(itemObserver != null)
                            itemObserver.onMeetingJoinRequest(_sequenceId, _meetingId, true);
                    }
                }
            });
        }

        if(meeting != null)
        {
            updateDate(meeting);
            String name = meeting.getMeetingTitle();
            if (name == null)
                name = "";
            _nameView.setText(name);
            updateCallButtons(meeting);
            _sequenceId = meeting.getSchMeetingSequenceID().intValue();
            _meetingId = meeting.getSchMeetingID().intValue();
        }
    }

    private void updateDate(ISptSchMeeting m)
    {
        if(m != null)
        {
            Calendar date = m.getStartDate();
            _yearView.setText("" + date.get(Calendar.YEAR));
            _monthView.setText(android.text.format.DateFormat.format("MMM", date).toString().toUpperCase());
            _dayView.setText("" + date.get(Calendar.DATE));
        }
    }

    private void updateCallButtons(ISptSchMeeting m)
    {
        if(m.getSchMeetingState() == ISptSchMeeting.eMeetingState.eSptSchMeetingStateActive)
        {
            _buttonsArea.setVisibility(View.VISIBLE);
        }
        else
            _buttonsArea.setVisibility(View.GONE);
    }
}
