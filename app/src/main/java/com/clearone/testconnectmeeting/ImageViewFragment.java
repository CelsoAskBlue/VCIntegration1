package com.clearone.testconnectmeeting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.support.media.ExifInterface;


import com.clearone.sptcore.sptim.SptSize;
import com.clearone.sptimpublicsdk.ISptCallData;
import com.clearone.sptimpublicsdk.ISptCallStreamData;
import com.clearone.sptimpublicsdk.ISptIMSDK;
import com.clearone.sptimpublicsdk.SptCallID;
import com.clearone.sptimpublicsdk.SptCallWhiteboardFragment;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageViewFragment extends SptCallWhiteboardFragment
{
    public static final String ARG_CONTENT_URI = "ARG_CONTENT_URI";

    public static final String ARG_ANNOTATION_ENABLED = "ARG_ANNOTATION_ENABLED";

    public static String TAG = "ImageViewFragment";

    private boolean _bSharingStarted;

    private OnFragmentInteractionListener _listener;

    public static final String EXTRA_CONTENT_URI = "EXTRA_CONTENT_URI";

    private String _contentUri;

    private SptCallID _callID;

    private ISptIMSDK _sdk;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private View mContentView;

    private boolean _annotationEnabled = true;


    private SptSize getScreenResolution()
    {
        WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        SptSize ret = new SptSize(metrics.widthPixels, metrics.heightPixels);
        return ret;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth)
            {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    public Bitmap decodeSampledBitmapFromUri(Uri uri,
                                             int reqWidth, int reqHeight) throws FileNotFoundException
    {


        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
    }


    public ImageViewFragment()
    {
        // Required empty public constructor
        super(false);
    }

    public static ImageViewFragment newInstance(SptCallID callID, boolean bAnnotationEnabled)
    {
        ImageViewFragment res = new ImageViewFragment();
        Bundle args = new Bundle();
        if(callID != null)
            args.putInt(ARG_CALLID, callID.intValue());
        args.putBoolean(ARG_ANNOTATION_ENABLED, bAnnotationEnabled);
        res.setArguments(args);
        return res;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setZoomEnabled(false);
        if (getArguments() != null)
        {
            _callID = new SptCallID(getArguments().getInt(ARG_CALLID, SptCallID.SPT_INVALID_CALLID));
            _contentUri = getArguments().getString(ARG_CONTENT_URI, null);
            _annotationEnabled = getArguments().getBoolean(ARG_ANNOTATION_ENABLED, false);
        }
        setAnnotationEnabled(_annotationEnabled);
    }


    @Override
    public FrameLayout getContainerView()
    {
        FrameLayout res = null;
        View v = getView();
        if(v != null)
            res = (FrameLayout) v.findViewById(R.id.fragment_content);
        return res;
    }

    @Override
    public void onWhiteboardTapDetected()
    {
        if(_listener != null)
            _listener.onFragmentClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v =  inflater.inflate(R.layout.fragment_image_view, container, false);

        mContentView = v.findViewById(R.id.fullscreen_content);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_listener != null)
                    _listener.onFragmentClick();
            }
        });
        _sdk = ((TestConnectMeetingApplication)getActivity().getApplication()).getSptIMSDK();
        SptSize screenSize = getScreenResolution();
        if(savedInstanceState != null)
            _bSharingStarted = savedInstanceState.getBoolean("SAVE_SHARING_STARTED", false);
        if(_contentUri != null && !_contentUri.isEmpty())
        {
            try {
                final Uri uri = Uri.parse(_contentUri);
                int rotation = getPictureRotation(getContext(),uri);
                boolean bNinetyDegreeRotation = rotation == 90 || rotation == -90 || rotation == 270 || rotation == -270;
                int maxZoom = 2;
                Bitmap bitmap = null;
                int width = (bNinetyDegreeRotation?screenSize.getHeight():screenSize.getWidth());
                int height = (bNinetyDegreeRotation?screenSize.getWidth():screenSize.getHeight());
                try {

                    bitmap = decodeSampledBitmapFromUri(uri, width*maxZoom, height*maxZoom);
                }
                catch(OutOfMemoryError e)
                {
                    bitmap = decodeSampledBitmapFromUri(uri, width, height);
                }

                Drawable d = ((ImageView)mContentView).getDrawable();
                if(d != null && d instanceof BitmapDrawable)
                {
                    ((BitmapDrawable)d).getBitmap().recycle();
                    ((ImageView)mContentView).setImageBitmap(null);
                }
                ((ImageView)mContentView).setImageBitmap(bitmap);
                if(rotation != 0)
                    ((ImageView)mContentView).setRotation(rotation);
                if(!_bSharingStarted)
                {
                    _sdk.startSharingView(_callID, mContentView);
                    _bSharingStarted = true;
                }
                else
                    _sdk.updateSharingView(_callID, mContentView);

                ((ImageView)mContentView).setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return v;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            _listener = (OnFragmentInteractionListener) context;
        }
        if(_listener == null) {
            Fragment parent = getParentFragment();
            if (parent != null && parent instanceof OnFragmentInteractionListener)
                _listener = (OnFragmentInteractionListener) parent;
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        _listener = null;
        if(mContentView != null)
        {
            Drawable d = ((ImageView) mContentView).getDrawable();
            if (d != null && d instanceof BitmapDrawable && ((BitmapDrawable) d).getBitmap() != null)
            {
                ((BitmapDrawable) d).getBitmap().recycle();
                ((ImageView) mContentView).setImageBitmap(null);
            }
        }
        Log.d("STREAM DATA", "ImageViewFragment OnDettach");
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
        void onUriSet(Uri uri);
        void onFragmentClick();
        void onStopLocalSharing();
    }

    @Override
    public void onCallEventCallsSynchronized() {

    }

    @Override
    public void onCallEventStateReconnecting(SptCallID callID, ISptCallData callData) {

    }

    @Override
    public void onCallEventStreamStopped(SptCallID callID, ISptCallStreamData streamData)
    {
        super.onCallEventStreamStopped(callID, streamData);
    }

    @Override
    public void onCallEventRecreateTextureView(SptCallID callID, ISptCallStreamData streamData) {

    }

    @Override
    public void onCallEventSendSharingStopped(SptCallID callID, ISptCallData callData)
    {
        super.onCallEventSendSharingStopped(callID, callData);
        mHandler.post(new Runnable() {
            @Override
            public void run()
            {
                if(_listener != null)
                    _listener.onStopLocalSharing();
            }
        });
    }

    @Override
    public void onCallEventCallConnectivityUpdated(SptCallID callID, ISptCallData callData) {

    }
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("SAVE_SHARING_STARTED", _bSharingStarted);
    }

    public static int getPictureRotation(Context c, Uri uri) {
        int res = 0;
        if(uri == null)
        {
            Log.e("STREAM DATA", "getPictureRotation: uri must not be null!!!!");
            return res;
        }

        try
        {
            InputStream is = c.getContentResolver().openInputStream(uri);
            if(is == null)
                return res;
            res = getPictureRotation(is);
            is.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public static int getPictureRotation(InputStream is)
    {
        int res = 0;

        ExifInterface ei = null;
        if(is == null)
            return 0;
        try
        {
            ei = new ExifInterface(is);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    res = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    res = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    res =270;
                    break;

                case ExifInterface.ORIENTATION_NORMAL:

                default:
                    break;
            }
        } catch (IOException e)
        {
            Log.e("STREAM DATA", "Exception for input stream: " + is);
            e.printStackTrace();
        }

        return res;
    }

}
