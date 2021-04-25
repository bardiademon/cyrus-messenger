package com.bardiademon.CyrusMessenger.ServerSocket.Gap;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient.CUV;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.IsManager;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityGap.GroupSecurityGap;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.IsJoined;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.NewPrivateMessage;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.RequestPrivateGap;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;

public final class GroupsGap extends NewPrivateMessage
{
    public GroupsGap (final SocketIOClient Client , final RequestPrivateGap Request)
    {
        super (Client , Request);
        super.event = EventName.ssg_send_group_message;
    }

    public void _do ()
    {
        final CheckRequestResult checkRequestResult = checkRequest ();
        if (checkRequestResult.ok)
        {
//            if ((!request.isHasFile () || Str.IsEmpty (securityUserGap.getCanSendFileTypes ()) || checkAccessType ()))
//            {
//
//            }
        }

        client.sendEvent (EventName.e_ssg_send_group_message.name () , ToJson.To (answer));
        l.n (ToJson.To (request) , EventName.ssg_send_group_message , checkRequestResult.mainAccount , answer , Thread.currentThread ().getStackTrace () , null , "Send result to client");
    }

    private CheckRequestResult checkRequest ()
    {
        final CBSIL both = CBSIL.Both (super.request , super.request.getCodeLogin () , super.event);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            final MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            if (!Str.IsEmpty (request.getCodeOnline ()))
            {
                final ID groupId = new ID (request.getTo ());
                if (groupId.isValid ())
                {
                    final ILUGroup iluGroup = new ILUGroup ();
                    iluGroup.setId (groupId.getId ());
                    if (iluGroup.isValid ())
                    {
                        /*
                         * agar channel bashe faghat modir mitone payam bede dakhel group
                         */
                        final Groups group = iluGroup.getGroup ();

                        assert group != null;
                        if (group.isChannel ())
                        {
                            final IsManager isManager = new IsManager (mainAccount , This.GetService (GroupManagementService.class));
                            isManager.setILUGroup (iluGroup);

                            if ((isManager.isOwner () || isManager.isManager ()) && isManager.hasAccess (AccessLevel.send_message))
                            {
                                try
                                {
                                    l.n (ToJson.To (request) , event , mainAccount , null , Thread.currentThread ().getStackTrace () , null , "Access was granted from " + getClass ().getDeclaredMethod ("checkRequest").getName ());
                                }
                                catch (NoSuchMethodException e)
                                {
                                    l.n (Thread.currentThread ().getStackTrace () , e);
                                }
                                return new CheckRequestResult (isManager.isOwner () , isManager.isManager () , group , mainAccount , true);
                            }
                            else
                            {
                                answer = AnswerToClient.AccessDenied ();
                                l.n (ToJson.To (request) , event , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (CUV.access_denied));
                            }
                        }
                        else
                        {
                            final IsJoined isJoined = new IsJoined (mainAccount , groupId);
                            if (isJoined.is ())
                            {
                                final GroupSecurityGap groupSecurityGap = group.getGroupSecurityGap ();
                                if (groupSecurityGap.isSendMessage ())
                                {
                                    try
                                    {
                                        l.n (ToJson.To (request) , event , mainAccount , null , Thread.currentThread ().getStackTrace () , null , "Access was granted from " + getClass ().getDeclaredMethod ("checkRequest").getName ());
                                    }
                                    catch (NoSuchMethodException e)
                                    {
                                        l.n (Thread.currentThread ().getStackTrace () , e);
                                    }
                                    return new CheckRequestResult (group , mainAccount , true);
                                }
                                else
                                {
                                    answer = AnswerToClient.OneAnswer (AnswerToClient.AccessDenied () , ValAnswer.there_is_no_access_to_send_the_message);
                                    l.n (ToJson.To (request) , event , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.there_is_no_access_to_send_the_message));
                                }
                            }
                            else
                            {
                                answer = AnswerToClient.OneAnswer (AnswerToClient.AccessDenied () , ValAnswer.you_are_not_a_member);
                                l.n (ToJson.To (request) , event , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.you_are_not_a_member));
                            }
                        }


                        return new CheckRequestResult (group , mainAccount , false);
                    }
                    else
                    {
                        answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_group);
                        l.n (ToJson.To (request) , event , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.not_found_group));
                    }
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_group_id);
                    l.n (ToJson.To (request) , event , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.invalid_group_id));
                }
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , CUV.online_code_invalid);
                l.n (ToJson.To (request) , event , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (CBSIL.class.getName ()));
            }
            return new CheckRequestResult (null , mainAccount , false);
        }
        else
        {
            answer = both.getAnswerToClient ();
            l.n (ToJson.To (request) , event , null , answer , Thread.currentThread ().getStackTrace () , l.e (CBSIL.class.getName ()));
        }

        return new CheckRequestResult ();
    }

    private final static class CheckRequestResult
    {
        private final boolean isOwner;
        private final boolean isManagement;
        private final Groups group;
        private final MainAccount mainAccount;
        private final boolean ok;

        private CheckRequestResult ()
        {
            this (false , false , null , null , false);
        }

        private CheckRequestResult (final Groups Group , final MainAccount _MainAccount , final boolean OK)
        {
            this (false , false , Group , _MainAccount , OK);
        }

        private CheckRequestResult (final boolean IsOwner , final boolean IsManagement , final Groups Group , final MainAccount _MainAccount)
        {
            this (IsOwner , IsManagement , Group , _MainAccount , true);
        }

        private CheckRequestResult (final boolean IsOwner , final boolean IsManagement , final Groups Group , final MainAccount _MainAccount , final boolean OK)
        {
            this.isOwner = IsOwner;
            this.isManagement = IsManagement;
            this.group = Group;
            this.mainAccount = _MainAccount;
            this.ok = OK;
        }
    }

    private enum ValAnswer
    {
        invalid_group_id, not_found_group, you_are_not_a_member, there_is_no_access_to_send_the_message
    }
}
