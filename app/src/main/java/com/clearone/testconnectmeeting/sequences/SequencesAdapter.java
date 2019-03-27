package com.clearone.testconnectmeeting.sequences;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clearone.sptcore.sptim.meetings.SptSchMeeting;
import com.clearone.sptimpublicsdk.ISptSchMeeting;
import com.clearone.sptimpublicsdk.ISptSchMeetingSequence;
import com.clearone.testconnectmeeting.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class SequencesAdapter extends RecyclerView.Adapter<SequenceItemView> implements SequenceItemView.IAdapterItemObserver
{
    int _selectedItem = -1;
    ISequenceItemObserver _observer;

    public interface ISequenceItemObserver
    {
        public void onSequenceMeetingListRequest(int sequenceId);
    }

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
    public ISequenceItemObserver getSequenceObserver()
    {
        return _observer;
    }

    public SequencesAdapter(ISequenceItemObserver observer)
    {
        super();
        _observer = observer;
        _list = new SortedList<ISptSchMeetingSequence>(ISptSchMeetingSequence.class, new SortedList.Callback<ISptSchMeetingSequence>() {
            @Override
            public int compare(ISptSchMeetingSequence c1, ISptSchMeetingSequence c2)
            {
                int iRet = 0;
                if(c1 == null && c2 == null)
                    iRet = 0;
                else if(c1 == null)
                    iRet = -1;
                else if(c2 == null)
                    iRet = 1;
                else
                {
                    if(c1.getMeetingSequenceID().equals(c2.getMeetingSequenceID()))
                        iRet = 0;
                    else
                    {
                        // Compare nearest meeting in time of each sequence
                        ISptSchMeeting m1 = c1.getCurrentMeeting();
                        if(m1 == null)
                            m1 = c1.getNextMeeting();
                        if(m1 == null)
                            m1 = c1.getLastMeeting();
                        ISptSchMeeting m2 = c2.getCurrentMeeting();
                        if(m2 == null)
                            m2 = c2.getNextMeeting();
                        if(m2 == null)
                            m2 = c2.getLastMeeting();
                        if(m1 == null && m2 == null)
                            iRet = 0;
                        else if(m1 == null)
                            iRet = -1;
                        else if(m2 == null)
                            iRet = 1;
                        else
                        {
                            Calendar date1 = m1.getStartDate();
                            Calendar date2 = m2.getStartDate();
                            if (date1 != null && date2 != null)
                            {
                                long diff = (date1.getTimeInMillis() - date2.getTimeInMillis());
                                if(diff != 0)
                                    iRet = diff>0?1:-1;
                            }
                        }
                        // If same start date order by sequence id
                        if (iRet == 0)
                            iRet = c1.getMeetingSequenceID().intValue() - c2.getMeetingSequenceID().intValue();
                    }
                }
                return -iRet;
            }

            @Override
            public void onChanged(int i, int count)
            {
                notifyItemRangeChanged(i, count);
            }

            @Override
            public boolean areContentsTheSame(ISptSchMeetingSequence c1, ISptSchMeetingSequence c2)
            {
                return false;
            }

            @Override
            public boolean areItemsTheSame(ISptSchMeetingSequence c1, ISptSchMeetingSequence c2)
            {
                boolean bRet = false;
                if(c1 != null && c2 != null)
                    bRet = c1.getMeetingSequenceID().equals(c2.getMeetingSequenceID());
                else if(c1 == null && c2 == null)
                    bRet = true;
                return bRet;
            }

            @Override
            public void onInserted(int i, int count)
            {
                notifyItemRangeChanged(i, count);
            }

            @Override
            public void onRemoved(int i, int count)
            {
                notifyItemRangeRemoved(i, count);
            }

            @Override
            public void onMoved(int from, int to)
            {
                notifyItemMoved(from, to);
            }
        });
    }


    SortedList<ISptSchMeetingSequence> _list;
    @NonNull
    @Override
    public SequenceItemView onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_custom_sequence_view, parent, false);
        return new SequenceItemView(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SequenceItemView viewHolder, int i)
    {
        viewHolder.bind(_list.get(i));
    }

    @Override
    synchronized public int getItemCount()
    {
        return _list.size();
    }

    public synchronized void loadElements(Collection<ISptSchMeetingSequence> contacts)
    {
        _list.beginBatchedUpdates();
        _list.clear();
        _list.addAll(contacts);
        _list.endBatchedUpdates();
    }

    public synchronized void addElement(ISptSchMeetingSequence seq)
    {
      /*  int pos = _list.size();
        _list.add(seq);
        notifyItemInserted(pos);*/
        _list.add(seq);
    }

    public synchronized void updateElement(ISptSchMeetingSequence seq)
    {
        int pos = _list.indexOf(seq);
        if(pos != -1)
        {
            _list.updateItemAt(pos, seq);

        }
        else
            _list.add(seq);
        /*int i = 0;
        int pos = -1;
        if(seq != null)
        {
            for (ISptSchMeetingSequence c : _list)
            {
                if (c.getMeetingSequenceID().equals(seq.getMeetingSequenceID()))
                {
                    _list.set(i, seq);
                    notifyItemChanged(i);
                    pos = i;
                    break;
                }
                i++;
            }
            if(pos == -1)
            {
                _list.add(0, seq);
                notifyItemInserted(0);
            }
        }*/

    }
}
