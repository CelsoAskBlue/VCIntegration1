package com.clearone.testconnectmeeting.sequences;

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

import java.util.Calendar;

public class SequenceItemView extends RecyclerView.ViewHolder
{
    interface IAdapterItemObserver
    {
        public void onItemSelected(int pos, boolean bSelected);
        public SequencesAdapter.ISequenceItemObserver getSequenceObserver();
    }

    TextView _yearView;
    TextView _monthView;
    TextView _nameView;
    TextView _dayView;
    FrameLayout _iconArea;
    View _dateArea;
    IAdapterItemObserver _observer;
    int _sequenceId;

    public SequenceItemView(@NonNull View itemView, IAdapterItemObserver itemObserver)
    {
        super(itemView);
        _observer = itemObserver;
    }

    public void bind(ISptSchMeetingSequence seq)
    {
        if(_yearView == null)
        {
            _yearView = (TextView) itemView.findViewById(R.id.seq_year);
            _monthView = (TextView) itemView.findViewById(R.id.seq_month);
            _nameView = (TextView) itemView.findViewById(R.id.seq_name);
            _dayView = (TextView)itemView.findViewById(R.id.seq_day);
            _iconArea = itemView.findViewById(R.id.icon_area);
            _dateArea = itemView.findViewById(R.id.date_area);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(_observer != null)
                    {
                        SequencesAdapter.ISequenceItemObserver seqObserver = _observer.getSequenceObserver();
                        if(seqObserver != null)
                            seqObserver.onSequenceMeetingListRequest(_sequenceId);
                    }
                }
            });
        }

        if(seq != null)
        {
            updateDate(seq);
            //  updateStatus(seq);
            String name = seq.getTitle();
            if (name == null)
                name = "";
            _nameView.setText(name);
            _sequenceId = seq.getMeetingSequenceID().intValue();
        }

    }

    private void updateDate(ISptSchMeetingSequence seq)
    {
        if(seq != null)
        {
            switch (seq.getRepeatType())
            {

                case eSptRepeatMeetingNow:
                    _iconArea.getBackground().setColorFilter(
                            Color.argb(255, 0xee, 0xee, 0xee),
                            PorterDuff.Mode.SRC_ATOP);
                    _dateArea.setVisibility(View.VISIBLE);
                    ISptSchMeeting m = seq.getCurrentMeeting();
                    if(m == null)
                        m = seq.getNextMeeting();
                    if(m == null)
                        m = seq.getLastMeeting();
                    if(m != null)
                    {
                        Calendar date = m.getStartDate();
                        _yearView.setText("" + date.get(Calendar.YEAR));
                        _monthView.setText(android.text.format.DateFormat.format("MMM", date).toString().toUpperCase());
                        _dayView.setText("" + date.get(Calendar.DATE));
                    }
                    break;
                case eSptRepeatMeetingDay:
                case eSptRepeatMeetingWeekly:
                case eSptRepeatMeetingMonthlyWeek:
                case eSptRepeatMeetingMonthlyDay:
                    _iconArea.getBackground().setColorFilter(
                            itemView.getResources().getColor(android.R.color.holo_red_dark),
                            PorterDuff.Mode.SRC_ATOP);

                    _dateArea.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    private void updateStatus(ISptSchMeetingSequence seq)
    {
        if(seq != null)
        {
            switch (seq.getState())
            {

                case eMeetingSeqUnknown:
                    break;
                case eMeetingSeqAlive:
                    break;
                case eMeetingSeqFinished:
                    break;
                case eMeetingSeqCancelled:
                    break;
            }
        }
    }
}
