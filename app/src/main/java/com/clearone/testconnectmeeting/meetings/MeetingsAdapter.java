package com.clearone.testconnectmeeting.meetings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clearone.sptimpublicsdk.ISptSchMeeting;
import com.clearone.sptimpublicsdk.ISptSchMeetingSequence;
import com.clearone.testconnectmeeting.R;
import com.clearone.testconnectmeeting.sequences.SequenceItemView;

import java.util.ArrayList;
import java.util.Collection;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingItemView> implements MeetingItemView.IAdapterItemObserver
{
    IMeetingItemObserver _observer;
    int _selectedItem = -1;

    @Override
    public void onItemSelected(int pos, boolean bSelected)
    {
        int oldSelected = _selectedItem;
        if(bSelected)
            _selectedItem = pos;
        else
            _selectedItem = -1;
        if(oldSelected != -1)
            notifyItemChanged(oldSelected);
    }

    @Override
    public IMeetingItemObserver getContactObserver()
    {
        return _observer;
    }

    public interface IMeetingItemObserver
    {
        public void onMeetingJoinRequest(int sequenceId, int meetingId, boolean bVideo);
    }

    ArrayList<ISptSchMeeting> _list = new ArrayList();

    public MeetingsAdapter(IMeetingItemObserver observer)
    {
        super();
        _observer = observer;
    }
    @NonNull
    @Override
    public MeetingItemView onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_custom_sequence_view, parent, false);
        return new MeetingItemView(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingItemView viewHolder, int i)
    {
        viewHolder.bind(_list.get(i));
    }

    @Override
    synchronized public int getItemCount()
    {
        return _list.size();
    }

    public synchronized void loadElements(Collection<ISptSchMeeting> contacts)
    {
        _list.clear();
        _list.addAll(contacts);
        notifyDataSetChanged();
    }

    public synchronized void updateElement(ISptSchMeeting meeting)
    {
        int i = 0;
        int pos = -1;
        if(meeting != null)
        {
            for (ISptSchMeeting m : _list)
            {
                if (m.getSchMeetingID().equals(meeting.getSchMeetingID()))
                {
                    _list.set(i, meeting);
                    notifyItemChanged(i);
                    pos = i;
                    break;
                }
                i++;
            }
            if(pos == -1)
            {
                _list.add(meeting);
                notifyItemInserted(0);
            }
        }

    }
}
