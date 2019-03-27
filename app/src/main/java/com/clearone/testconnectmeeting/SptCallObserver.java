package com.clearone.testconnectmeeting;

import android.content.Intent;

import com.clearone.sptimpublicsdk.ISptAudioDevice;
import com.clearone.sptimpublicsdk.ISptCallChat;
import com.clearone.sptimpublicsdk.ISptCallData;
import com.clearone.sptimpublicsdk.ISptCallParticipant;
import com.clearone.sptimpublicsdk.ISptCallRequest;
import com.clearone.sptimpublicsdk.ISptCallStreamData;
import com.clearone.sptimpublicsdk.ISptIMEvent;
import com.clearone.sptimpublicsdk.ISptSDKCallObserver;
import com.clearone.sptimpublicsdk.ISptVideoDevice;
import com.clearone.sptimpublicsdk.SptCallID;

import java.util.Collection;

public class SptCallObserver implements ISptSDKCallObserver {
    @Override
    public void onCallEventCallsSynchronized() {

    }

    @Override
    public void onDeviceListUpdated(Collection<ISptVideoDevice> collection, Collection<ISptAudioDevice> collection1) {

    }

    @Override
    public void onAudioDevicesSet(ISptAudioDevice iSptAudioDevice, ISptAudioDevice iSptAudioDevice1) {

    }

    @Override
    public void onVideoDeviceSet(ISptVideoDevice iSptVideoDevice) {

    }

    @Override
    public void onAudioDevicesUpdated(ISptAudioDevice iSptAudioDevice) {

    }

    @Override
    public void onVideoDeviceUpdated(ISptVideoDevice iSptVideoDevice) {

    }

    @Override
    public void onCallEventConnected(SptCallID sptCallID, ISptCallData iSptCallData) {

    }

    @Override
    public void onCallEventDisconnected(SptCallID sptCallID, ISptCallData iSptCallData) {

    }

    @Override
    public void onCallEventStateUpdated(SptCallID sptCallID, ISptCallData iSptCallData) {

    }

    @Override
    public void onCallEventStateReconnecting(SptCallID callID, ISptCallData callData) {

    }

    @Override
    public void onCallEventParticipantConnected(SptCallID sptCallID, ISptCallParticipant iSptCallParticipant) {

    }

    @Override
    public void onCallEventParticipantDisconnected(SptCallID sptCallID, ISptCallParticipant iSptCallParticipant) {

    }

    @Override
    public void onCallEventParticipantUpdated(SptCallID sptCallID, ISptCallParticipant iSptCallParticipant) {

    }

    @Override
    public void onCallEventParticipantSpeakingUpt(SptCallID sptCallID, ISptCallParticipant iSptCallParticipant) {

    }

    @Override
    public void onCallEventStreamStarted(SptCallID sptCallID, ISptCallStreamData iSptCallStreamData) {

    }

    @Override
    public void onCallEventStreamStopped(SptCallID sptCallID, ISptCallStreamData iSptCallStreamData) {

    }

    @Override
    public void onCallEventStreamSpeakingUpt(SptCallID sptCallID, ISptCallStreamData iSptCallStreamData) {

    }

    @Override
    public void onCallEventStreamPropertiesUpt(SptCallID sptCallID, ISptCallStreamData iSptCallStreamData) {

    }

    @Override
    public void onCallEventStreamNewFrame(SptCallID sptCallID, ISptCallStreamData iSptCallStreamData) {

    }

    @Override
    public void onCallEventAnnotationOverStreamStarted(SptCallID sptCallID, ISptCallStreamData iSptCallStreamData) {

    }

    @Override
    public void onCallEventAnnotationOverStreamStopped(SptCallID sptCallID, ISptCallStreamData iSptCallStreamData) {

    }

    @Override
    public void onCallEventRecreateTextureView(SptCallID sptCallID, ISptCallStreamData iSptCallStreamData) {

    }

    @Override
    public void onCallEventIncomingCall(SptCallID sptCallID, ISptCallData iSptCallData) {

    }

    @Override
    public void onCallEventRecordingStateUpdated(SptCallID sptCallID, ISptCallData iSptCallData) {

    }

    @Override
    public void onCallEventRecommendedPaintListUpdated(SptCallID sptCallID, ISptCallData iSptCallData) {

    }

    @Override
    public void onCallEventSpeakingParticipantListUpdated(SptCallID sptCallID, ISptCallData iSptCallData) {

    }

    @Override
    public void onCallEventRequestUpdated(SptCallID sptCallID, ISptCallRequest iSptCallRequest) {

    }

    @Override
    public void requestUserAutorization(Intent intent, int i) {

    }

    @Override
    public void onCallEventPrivateChatUpdated(ISptCallChat iSptCallChat) {

    }

    @Override
    public void onCallEventPublicChatUpdated(ISptCallChat iSptCallChat) {

    }

    @Override
    public void onCallEventChatReceived(ISptCallChat iSptCallChat, ISptIMEvent iSptIMEvent) {

    }

    @Override
    public void onCallEventChatSent(ISptCallChat iSptCallChat, ISptIMEvent iSptIMEvent) {

    }

    @Override
    public void onCallEventChatEventUpdated(ISptCallChat iSptCallChat, ISptIMEvent iSptIMEvent) {

    }

    @Override
    public void onCallEventChatFileSending(ISptCallChat iSptCallChat, ISptIMEvent iSptIMEvent) {

    }

    @Override
    public void onCallEventChatFileDownloaded(ISptCallChat iSptCallChat, ISptIMEvent iSptIMEvent) {

    }

    @Override
    public void onCallEventSendSharingStopped(SptCallID sptCallID, ISptCallData iSptCallData) {

    }

    @Override
    public void onCallEventDefaultManageServicesUpt(SptCallID sptCallID, ISptCallData iSptCallData) {

    }

    @Override
    public void onCallEventCallConnectivityUpdated(SptCallID sptCallID, ISptCallData iSptCallData) {

    }

    @Override
    public void onCallEventHistoryEvent(eSptCallHistoryEvent eSptCallHistoryEvent) {

    }
}
