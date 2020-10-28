package com.bardiademon.CyrusMessenger.ServerSocket.EventName;

public enum EventName
{
    // firstr => first request

    // event_name => request
    // e_event_name => answer to client

    // request
    firstr, firstr_answer,

    last_seen, e_last_seen,

    set_offline, e_set_offline,

    // pvgp => private gap
    pvgp_send_message, e_pvgp_send_message,

    // baraye payam haye daryafti > private gaps
    pvgp_new_message, e_pvgp_new_message,

    // baraye vaseyat payam > khande shode , daryaft shode , ...
    pvgp_status_message, e_pvgp_status_message,

    pvgp_receive_message,

    pvgp_typing, e_pvgp_typing, pvgp_is_typing,

    get_messages, e_get_messages,

    delete_personal_gap
}
