package com.clearone.testconnectmeeting.sequences;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clearone.sptimpublicsdk.ISptIMSDK;
import com.clearone.sptimpublicsdk.ISptSchMeetingSequence;
import com.clearone.sptimpublicsdk.SptIMSDKApp;
import com.clearone.sptimpublicsdk.SptSchMeetingSequenceID;
import com.clearone.testconnectmeeting.R;
import com.clearone.testconnectmeeting.SptIMObserver;
import com.clearone.testconnectmeeting.meetings.MeetingsDialogFragment;

import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SequencesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SequencesFragment extends Fragment implements SequencesAdapter.ISequenceItemObserver
{

    private Handler _handler = new Handler(Looper.getMainLooper());

    @Override
    public void onSequenceMeetingListRequest(int contactId)
    {
        MeetingsDialogFragment d = MeetingsDialogFragment.newInstance(new SptSchMeetingSequenceID(contactId));
        d.show(getChildFragmentManager(), "dfgd");
    }

    class SequencesSptIMObserver extends SptIMObserver
    {

        @Override
        public void onSchMeetingsSynchronized()
        {
            _handler.post(new Runnable() {
                @Override
                public void run()
                {
                    loadMeetings();
                }
            });
        }

        @Override
        public void onSchMeetingUpdated(final ISptSchMeetingSequence meetingSequence)
        {
            if(meetingSequence != null )
                _handler.post(new Runnable() {
                    @Override
                    public void run()
                    {
                        _adapter.updateElement(meetingSequence);
                    }
                });
        }

        @Override
        public void onSchMeetingCreated(final ISptSchMeetingSequence meetingSequence)
        {
            _handler.post(new Runnable() {
                @Override
                public void run()
                {
                    _adapter.updateElement(meetingSequence);
                }
            });
        }
    }

    SequencesAdapter _adapter;
    SequencesSptIMObserver _sptIMObserver;

    private OnFragmentInteractionListener mListener;

    public SequencesFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _sptIMObserver = new SequencesSptIMObserver();
        ISptIMSDK sdk = ((SptIMSDKApp)getActivity().getApplication()).getSptIMSDK();
        sdk.addObserver(_sptIMObserver);
        _adapter = new SequencesAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView list = (RecyclerView)v.findViewById(R.id.contacts_list);
        list.setAdapter(_adapter);
        loadMeetings();
        return v;
    }

    private void loadMeetings()
    {
        Activity a = getActivity();
        if(a != null)
        {
            ISptIMSDK sdk = ((SptIMSDKApp) a.getApplication()).getSptIMSDK();
            Collection<ISptSchMeetingSequence> sequences = sdk.getMeetigsSequences();
            if (sequences != null)
            {
                _adapter.loadElements(sequences);
            }
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
