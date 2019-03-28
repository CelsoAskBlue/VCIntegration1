package com.clearone.testconnectmeeting;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.clearone.sptimpublicsdk.SptCallFragment;

/**
 * Created by laura on 21/10/2018.
 */

public class SharingOptionsDialog extends DialogFragment implements View.OnClickListener
{
    public interface SharingOptionsDialogListener {
        void onFinisSharingOptionsDialog(SptCallFragment.eSharingType sharingSelected);
    }

    private SharingOptionsDialogListener _listener;
//    Button _shareScreenButton;
    Button _shareGalleryButton;


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof SharingOptionsDialogListener)
        {
            _listener = (SharingOptionsDialogListener)context;
        }
        else

            throw new RuntimeException( context.toString() + " must implement SharingOptionsDialogListener");

    }

    public SharingOptionsDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SharingOptionsDialog newInstance(String title) {
        SharingOptionsDialog frag = new SharingOptionsDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sharing_options_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        _shareScreenButton = (Button)view.findViewById(R.id.fragment_sharing_sreen);
        _shareGalleryButton = (Button)view.findViewById(R.id.fragment_sharing_gallery);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            _shareScreenButton.setOnClickListener(this);
//            _shareScreenButton.setEnabled(true);
        }
        else
        {
//            _shareScreenButton.setEnabled(false);
        }
        _shareGalleryButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        SptCallFragment.eSharingType selected = SptCallFragment.eSharingType.eSharingTypeUnknown;
        /*if(v == _shareScreenButton)
        {
            selected = SptCallFragment.eSharingType.eSharingTypeScreen;
        }
        else*/
        if(v == _shareGalleryButton)
        {
            selected = SptCallFragment.eSharingType.eSharingTypeGallery;
        }

        if(selected !=  SptCallFragment.eSharingType.eSharingTypeUnknown)
        {
            if(_listener != null)
                _listener.onFinisSharingOptionsDialog(selected);
            dismiss();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        if(_listener != null)
            _listener.onFinisSharingOptionsDialog(SptCallFragment.eSharingType.eSharingTypeUnknown);
    }
}
