package com.bardiademon.CyrusMessenger.ServerSocket.EventName;

public enum EventName
{
    // firstr => first request

    // request
    firstr, firstr_answer, firstr_status_online,

    // event_name => request
    // e_event_name => answer to client

    set_offline, e_set_offline,

    // answer
    firstr_last_seen,

    // pvgp
    pvgp_send_message, pvgp_receive_message
}
