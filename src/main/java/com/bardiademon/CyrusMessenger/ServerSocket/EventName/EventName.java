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

    // ssg => Server Socket Gap
    ssg_send_message, e_ssg_send_message,

    status_of_sent_message, e_status_of_sent_message,

    // baraye payam haye daryafti > private gaps
    ssg_new_message, e_ssg_new_message,

    // baraye vaseyat payam > khande shode , daryaft shode , ...
    ssg_status_message, e_ssg_status_message,
    ssg_answer_question_text, e_ssg_answer_question_text,

    ssg_receive_message, ssg_get_all_answer_question_text, e_ssg_get_all_answer_question_text,

    ssg_typing, e_ssg_typing, ssg_is_typing,

    get_messages, e_get_messages,

    delete_personal_gap

}
