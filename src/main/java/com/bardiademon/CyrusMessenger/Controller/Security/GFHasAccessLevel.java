package com.bardiademon.CyrusMessenger.Controller.Security;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesSecurity.GapsFilesSecurity;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.SendGapsFilesTo.SendGapsFilesToService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;

// GF => GapsFiles
public final class GFHasAccessLevel
{
    public GFHasAccessLevel ()
    {

    }

    public boolean hasAccess (final MainAccount applicant , final Groups groups , final GapsFiles gapsFiles , final Which which)
    {
        if (applicant.getId () == gapsFiles.getUploadedFiles ().getUploadedBy ().getId ()) return true;
        else
        {
            final SendGapsFilesToService sendGapsFilesToService = This.GetService (SendGapsFilesToService.class);
            final long numberOfSubmissions = sendGapsFilesToService.numberOfSubmissions (applicant.getId ());

            if (numberOfSubmissions == 0)
                return false;

            final GapsFilesSecurity security = gapsFiles.getSecurity ();

            if (security == null) return false;

            if (security.isJustOnce ())
            {
                if (numberOfSubmissions > 1)
                {
                    l.n (Thread.currentThread ().getStackTrace () , new Exception ("GapsFilesSecurity.isJustOnce () === true") , ToJson.CreateClass.nj ("gaps_files_security_id" , security.getId ()));
                    return false;
                }
            }

            if (groups != null)
            {
                if (security.isCanSendToGroups ())
                {
                    final JoinGroupService joinGroupService = This.GetService (JoinGroupService.class);
                    return joinGroupService.isJoined (groups.getId () , applicant.getId ()) != null;
                }
                else return false;
            }
            else
            {
                if (which.equals (Which.forward))
                    return security.isCanForward ();
                else if (which.equals (Which.download)) return security.isCanDownload ();
            }

        }
        return true;
    }

    public enum Which
    {
        forward, download, nll;

        public static Which to (String value)
        {
            try
            {
                return valueOf (value);
            }
            catch (Exception e)
            {
                return nll;
            }
        }
    }
}
