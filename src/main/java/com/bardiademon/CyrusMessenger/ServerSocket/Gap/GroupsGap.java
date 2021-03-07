package com.bardiademon.CyrusMessenger.ServerSocket.Gap;

import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.NewPrivateMessage;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.RequestPrivateGap;
import com.corundumstudio.socketio.SocketIOClient;

public final class GroupsGap extends NewPrivateMessage
{
    public GroupsGap (final SocketIOClient Client , final RequestPrivateGap Request)
    {
        super (Client , Request);
    }
}
