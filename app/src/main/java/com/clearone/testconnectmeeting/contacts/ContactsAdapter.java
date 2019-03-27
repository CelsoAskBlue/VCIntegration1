package com.clearone.testconnectmeeting.contacts;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clearone.sptimpublicsdk.ISptIMContact;
import com.clearone.testconnectmeeting.R;

import java.util.ArrayList;
import java.util.Collection;

public class ContactsAdapter extends RecyclerView.Adapter<ContactItemView> implements ContactItemView.IAdapterItemObserver
{
    public interface IContactItemObserver
    {
        public void onContactAudioCallRequest(int contactId);
        public void onContactVideoCallRequest(int contactId);
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
    public IContactItemObserver getContactObserver()
    {
        return _observer;
    }

    int _selectedItem = -1;
    SortedList<ISptIMContact> _list;
    IContactItemObserver _observer;

    public ContactsAdapter(IContactItemObserver observer)
    {
        super();
        _observer = observer;
        _list = new SortedList<ISptIMContact>(ISptIMContact.class, new SortedList.Callback<ISptIMContact>() {
            @Override
            public int compare(ISptIMContact c1, ISptIMContact c2)
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
                    if(c1.getContatcID().equals(c2.getContatcID()))
                        iRet = 0;
                    else
                    {
                        String name1 = c1.getNormalizedDisplayName();
                        String name2 = c2.getNormalizedDisplayName();
                        if (name1 != null && name2 != null)
                            iRet = name1.toLowerCase().compareTo(name2.toLowerCase());
                        if (iRet == 0)
                            iRet = c1.getContatcID().intValue() - c2.getContatcID().intValue();
                    }
                }
                return iRet;
            }

            @Override
            public void onChanged(int i, int count)
            {
                notifyItemRangeChanged(i, count);
            }

            @Override
            public boolean areContentsTheSame(ISptIMContact c1, ISptIMContact c2)
            {
                return false;
            }

            @Override
            public boolean areItemsTheSame(ISptIMContact c1, ISptIMContact c2)
            {
                boolean bRet = false;
                if(c1 != null && c2 != null)
                    bRet = c1.getContatcID().equals(c2.getContatcID());
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

    private ContactsAdapter()
    {
        super();
    }

    @NonNull
    @Override
    public ContactItemView onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_custom_contact_view, parent, false);
        return new ContactItemView(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactItemView viewHolder, int i)
    {
        viewHolder.bind(_list.get(i), _selectedItem == i);
    }

    @Override
    synchronized public int getItemCount()
    {
        return _list.size();
    }

    public synchronized void loadContacts(Collection<ISptIMContact> contacts)
    {
        _list.beginBatchedUpdates();
        _list.clear();
        _list.addAll(contacts);
        _list.endBatchedUpdates();
    }

    public synchronized void updateContact(ISptIMContact contact)
    {
        int i = 0;
        int pos = -1;
        pos = _list.indexOf(contact);
        if(pos != -1)
        _list.updateItemAt(pos, contact);
        else
            _list.add(contact);
    }
}
