package com.clearone.testconnectmeeting.contacts;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clearone.sptimpublicsdk.ISptIMContact;
import com.clearone.testconnectmeeting.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactItemView extends RecyclerView.ViewHolder
{

    public interface IAdapterItemObserver
    {
        public void onItemSelected(int pos, boolean bSelected);
        public ContactsAdapter.IContactItemObserver getContactObserver();
    }

    CircleImageView _thumbnail;
    ImageView _statusView;
    TextView _nameView;
    TextView _initialsView;
    View _buttonBar;
    View _audioCallButton;
    View _videoCallButton;
    IAdapterItemObserver _observer;
    int _contactId;
    boolean _bCanAudioCall;
    boolean _bCanVideoCall;


    public ContactItemView(@NonNull View itemView, IAdapterItemObserver observer)
    {
        super(itemView);
        _observer = observer;
    }

    public void bind(ISptIMContact contact, boolean bSelected)
    {
        if(_thumbnail == null)
        {
            _thumbnail = (CircleImageView) itemView.findViewById(R.id.contact_thumbnail);
            _statusView = (ImageView) itemView.findViewById(R.id.contact_status);
            _nameView = (TextView) itemView.findViewById(R.id.contact_name);
            _initialsView = (TextView)itemView.findViewById(R.id.contact_initials);
            _buttonBar =  itemView.findViewById(R.id.contact_button_bar);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view)
                {
                    if(_bCanAudioCall || _bCanVideoCall)
                    {
                        //_buttonBar.setAlpha(0);
                        _buttonBar.setTranslationX(100);
                        _audioCallButton.setVisibility(_bCanAudioCall?View.VISIBLE:View.GONE);
                        _videoCallButton.setVisibility(_bCanVideoCall?View.VISIBLE:View.GONE);
                        _buttonBar.setVisibility(View.VISIBLE);
                        //_buttonBar.animate().alpha(1).setDuration(200).start();
                        _buttonBar.animate().translationX(0).setDuration(200).start();
                        if (_observer != null)
                            _observer.onItemSelected(getAdapterPosition(), true);
                    }
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    _buttonBar.setVisibility(View.GONE);
                    if(_observer != null)
                        _observer.onItemSelected(getAdapterPosition(), false);
                }
            });

            _audioCallButton = itemView.findViewById(R.id.audio_call);
            _audioCallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(_observer != null)
                        _observer.getContactObserver().onContactAudioCallRequest(_contactId);
                }
            });
            _videoCallButton = itemView.findViewById(R.id.video_call);
            _videoCallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(_observer != null)
                        _observer.getContactObserver().onContactVideoCallRequest(_contactId);
                }
            });
        }
        _buttonBar.setVisibility(bSelected?View.VISIBLE:View.GONE);
        if(contact != null)
        {
            _contactId = contact.getContatcID().intValue();
            _bCanAudioCall = contact.canIAudioCall();
            _bCanVideoCall = contact.canIVideoCall();
            updatePicture(contact);
            updateStatus(contact);
            String name = contact.getDisplayName();
            if (name == null)
                name = "";
            _nameView.setText(name);
        }

    }

    private void updatePicture(ISptIMContact contact)
    {
        if(contact != null)
        {
            Bitmap b = contact.getpicture();
            if (b == null)
            {
                int c = contact.getColor();
                if (c == 0)
                    c = Color.LTGRAY;
                else
                    c = fromBGRToARGB(c);
                _thumbnail.setImageDrawable(new ColorDrawable(c));
                String initials = contact.getInitials();
                if(initials != null)
                {
                    _initialsView.setText(initials);
                    _initialsView.setVisibility(View.VISIBLE);
                }
                else
                    _initialsView.setVisibility(View.GONE);
            }
            else
            {
                _thumbnail.setImageBitmap(b);
                _initialsView.setVisibility(View.GONE);
            }
        }
    }

    private void updateStatus(ISptIMContact contact)
    {
        if(contact != null)
        {
            switch (contact.getState())
            {
                case eSptIMStateAvailable:
                    _statusView.setColorFilter(Color.GREEN);
                    break;
                case eSptIMStateDND:
                    _statusView.setColorFilter(Color.RED);
                    break;
                case eSptIMStateAway:
                    _statusView.setColorFilter(Color.YELLOW);
                    break;
                    default:
                        _statusView.setColorFilter(Color.GRAY);
            }
        }
    }
    public static int fromBGRToARGB(int color)
    {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int colorWithAlpha = Color.argb(0xff,  blue, green, red);
        return colorWithAlpha;
    }
}
