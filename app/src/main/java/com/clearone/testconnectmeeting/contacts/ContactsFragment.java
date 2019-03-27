package com.clearone.testconnectmeeting.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clearone.sptimpublicsdk.ISptCallData;
import com.clearone.sptimpublicsdk.ISptIMContact;
import com.clearone.sptimpublicsdk.ISptIMSDK;
import com.clearone.sptimpublicsdk.SptCallID;
import com.clearone.sptimpublicsdk.SptIMContactID;
import com.clearone.sptimpublicsdk.SptIMSDKApp;
import com.clearone.sptimpublicsdk.SptMakeCallData;
import com.clearone.testconnectmeeting.LoginActivity;
import com.clearone.testconnectmeeting.MainActivity;
import com.clearone.testconnectmeeting.R;
import com.clearone.testconnectmeeting.SptCallObserver;
import com.clearone.testconnectmeeting.SptIMObserver;

import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ContactsFragment extends Fragment implements ContactsAdapter.IContactItemObserver
{

    private Handler _handler = new Handler(Looper.getMainLooper());
    private SptCallID _callID;
    private  AlertDialog _callDialog;

    @Override
    public void onContactAudioCallRequest(int contactId)
    {
        SptMakeCallData data = new SptMakeCallData(ISptCallData.eSptCallType.eSptCallTypeAudio);
        data.addIMParticipant(new SptIMContactID(contactId));
        _callID = ((SptIMSDKApp)getActivity().getApplication()).getSptIMSDK().makeCall(data);
    }

    @Override
    public void onContactVideoCallRequest(int contactId)
    {
        SptMakeCallData data = new SptMakeCallData(ISptCallData.eSptCallType.eSptCallTypeVideo);
        data.addIMParticipant(new SptIMContactID(contactId));
        _callID = ((SptIMSDKApp)getActivity().getApplication()).getSptIMSDK().makeCall(data);
    }

    class ContactsSptIMObserver extends SptIMObserver
    {

        @Override
        public void onRosterList(final Set<ISptIMContact> rosterList)
        {
            _handler.post(new Runnable() {
                @Override
                public void run()
                {
                    _adapter.loadContacts(rosterList);
                }
            });
        }

        @Override
        public void onContactUpdated(final ISptIMContact contactData)
        {
            if(contactData != null && contactData.getContatcID().intValue() != SptIMContactID.LOCAL_CONTACTID)
                _handler.post(new Runnable() {
                    @Override
                    public void run()
                    {
                        _adapter.updateContact(contactData);
                    }
                });
        }
    }

    ContactsAdapter _adapter;
    ContactsSptIMObserver _sptIMObserver;

    private OnFragmentInteractionListener mListener;

    public ContactsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _sptIMObserver = new ContactsSptIMObserver();
        ISptIMSDK sdk = ((SptIMSDKApp)getActivity().getApplication()).getSptIMSDK();
        sdk.addObserver(_sptIMObserver);
        _adapter = new ContactsAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView list = (RecyclerView)v.findViewById(R.id.contacts_list);
        list.setAdapter(_adapter);
        loadContacts();
        return v;
    }

    private void loadContacts()
    {
        ISptIMSDK sdk = ((SptIMSDKApp)getActivity().getApplication()).getSptIMSDK();
        Set<ISptIMContact> contacts = sdk.getRosterContacts();
        if(contacts != null)
        {
            _adapter.loadContacts(contacts);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
           /* throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
