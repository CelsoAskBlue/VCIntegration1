package com.clearone.testconnectmeeting;

import com.clearone.sptcore.sptim.SptLoginData;
import com.clearone.sptcore.sptim.SptTokenDataResult;
import com.clearone.sptimpublicsdk.ISptContactPhoneRate;
import com.clearone.sptimpublicsdk.ISptCredit;
import com.clearone.sptimpublicsdk.ISptIMContact;
import com.clearone.sptimpublicsdk.ISptIMContactProfile;
import com.clearone.sptimpublicsdk.ISptIMEvent;
import com.clearone.sptimpublicsdk.ISptIMFavorite;
import com.clearone.sptimpublicsdk.ISptIMFile;
import com.clearone.sptimpublicsdk.ISptIMGroup;
import com.clearone.sptimpublicsdk.ISptIMModifiableContactProfile;
import com.clearone.sptimpublicsdk.ISptIMRoom;
import com.clearone.sptimpublicsdk.ISptIMSDK;
import com.clearone.sptimpublicsdk.ISptIMSDKObserver;
import com.clearone.sptimpublicsdk.ISptIMSearchContact;
import com.clearone.sptimpublicsdk.ISptIMSearchPublicChannels;
import com.clearone.sptimpublicsdk.ISptSchMeetingCreate;
import com.clearone.sptimpublicsdk.ISptSchMeetingSequence;
import com.clearone.sptimpublicsdk.SptIMContactID;
import com.clearone.sptimpublicsdk.SptLoadSearchedEventRes;
import com.clearone.sptimpublicsdk.SptPhoneCallRate;
import com.clearone.sptimpublicsdk.SptSchFileID;
import com.clearone.sptimpublicsdk.SptSchMeetingID;
import com.clearone.sptimpublicsdk.SptSchMeetingSequenceID;

import java.util.Set;

public class SptIMObserver implements ISptIMSDKObserver
{
    @Override
    public void onLicenseUpdated()
    {

    }

    @Override
    public void onConnected()
    {

    }

    @Override
    public void onConnectionError(ISptIMSDK.eSptConnectionResult eResult)
    {

    }

    @Override
    public void onDisconnected()
    {

    }

    @Override
    public void onGetTokenDataRes(SptTokenDataResult tokenDataRes)
    {

    }

    @Override
    public void onPhoneCallRateRes(SptPhoneCallRate phoneCallRate)
    {

    }

    @Override
    public void onContactPhoneCallRateRes(ISptContactPhoneRate contactPhoneRate)
    {

    }

    @Override
    public void onCreateAccountResult(eCreateAccountResult eResult, SptLoginData loginData)
    {

    }

    @Override
    public void onPasswordRecoveryResult(ePassordRecoveryResult eResult, String email)
    {

    }

    @Override
    public void onCreditUpdated(ISptCredit credit)
    {

    }

    @Override
    public void onCloudStorageUpdated()
    {

    }

    @Override
    public void onRosterList(Set<ISptIMContact> rosterList)
    {

    }

    @Override
    public void onContactRosterRemoved(ISptIMContact contactData)
    {

    }

    @Override
    public void onContactRosterAdded(ISptIMContact contactData)
    {

    }

    @Override
    public void onContactUpdated(ISptIMContact contactData)
    {

    }

    @Override
    public void onLocalProfileRequired(boolean bRequires)
    {

    }

    @Override
    public void onContactPresenceRequest(ISptIMContact contactData)
    {

    }

    @Override
    public void onPersonalToDosUpdated()
    {

    }

    @Override
    public void onGroupCreated(ISptIMGroup groupData)
    {

    }

    @Override
    public void onGroupContactAdded(SptIMContactID contactID, ISptIMGroup groupData)
    {

    }

    @Override
    public void onGroupContactRemoved(SptIMContactID contactID, ISptIMGroup groupData)
    {

    }

    @Override
    public void onGroupRemoved(ISptIMGroup groupData)
    {

    }

    @Override
    public void onSearchAvailable()
    {

    }

    @Override
    public void onSearchResult(ISptIMSearchContact searchContact)
    {

    }

    @Override
    public void onSearchResultContactUpt(ISptIMContact contact)
    {

    }

    @Override
    public void onSearchChannelResult(ISptIMSearchPublicChannels searchChannels)
    {

    }

    @Override
    public void onLocalContactProfileStored(ISptIMContactProfile contatcProfile)
    {

    }

    @Override
    public void onLocalContactProfileError(ISptIMModifiableContactProfile contatcProfile)
    {

    }

    @Override
    public void onRoomsLoaded()
    {

    }

    @Override
    public void onRoomCreated(SptIMContactID contactID, ISptIMRoom roomCreatedData)
    {

    }

    @Override
    public void onRoomJoined(SptIMContactID contactID, ISptIMRoom roomJoinedData)
    {

    }

    @Override
    public void onRoomLeft(SptIMContactID contactID, ISptIMRoom roomJoinedData)
    {

    }

    @Override
    public void onEventChatReceived(SptIMContactID contactID, ISptIMEvent eventData)
    {

    }

    @Override
    public void onEventChatSent(ISptIMEvent eventData)
    {

    }

    @Override
    public void onEventFileSending(ISptIMEvent eventData)
    {

    }

    @Override
    public void onEventUpdated(ISptIMEvent eventData)
    {

    }

    @Override
    public void onEventFileDownloaded(ISptIMEvent eventData)
    {

    }

    @Override
    public void onSearchEventsResult()
    {

    }

    @Override
    public void onFileUpdated(ISptIMFile file, int iUpdatedFields)
    {

    }

    @Override
    public void onRoomWritingUpdated(ISptIMRoom room)
    {

    }

    @Override
    public void onRoomMoreHistoryLoaded(ISptIMRoom room)
    {

    }

    @Override
    public void onRoomBlockedContactsUpdated(ISptIMRoom room)
    {

    }

    @Override
    public void onRoomUpdated(ISptIMRoom room, int updatedFields)
    {

    }

    @Override
    public void onChannelDataUpdated(ISptIMRoom roomData)
    {

    }

    @Override
    public void onRoomSynchronized(ISptIMRoom roomData)
    {

    }

    @Override
    public void onFavoriteAdded(ISptIMFavorite favorite)
    {

    }

    @Override
    public void onFavoriteRemoved(ISptIMFavorite favorite)
    {

    }

    @Override
    public void onIncomingCall()
    {

    }

    @Override
    public void onPasswordUpdatedResult(eSptPasswordUpdResult eResult)
    {

    }

    @Override
    public void onSchMeetingCreationError(ISptSchMeetingCreate.eSptSchResult eResult, ISptSchMeetingCreate meetingCreate)
    {

    }

    @Override
    public void onSchMeetingCreationProgressUpdated(int iProgress, ISptSchMeetingCreate meetingCreate)
    {

    }

    @Override
    public void onSchDownloadFileProgressUpdated(SptSchMeetingSequenceID seqID, SptSchMeetingID meetingID, SptSchFileID fileID)
    {

    }

    @Override
    public void onSchMeetingsSynchronized()
    {

    }

    @Override
    public void onSchMeetingCreated(ISptSchMeetingSequence meetingSequence)
    {

    }

    @Override
    public void onSchMeetingUpdated(ISptSchMeetingSequence meetingSequence)
    {

    }

    @Override
    public void onEmailContactsAdded(boolean bSuccess)
    {

    }

    @Override
    public void onLoadSearchedEventRes(SptLoadSearchedEventRes loadSearchedEvetsRes)
    {

    }

    @Override
    public void onCanIStartCallUpdated()
    {

    }
}
