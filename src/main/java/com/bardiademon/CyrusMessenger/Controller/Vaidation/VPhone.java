package com.bardiademon.CyrusMessenger.Controller.Vaidation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class VPhone implements Validation
{
    private String phone, region;

    public VPhone (String Phone , String Region)
    {
        this.phone = Phone;
        this.region = Region;
    }

    @Override
    public boolean check ()
    {
        if ((phone == null || phone.equals ("")) || (region == null || region.equals (""))) return false;
        else
        {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance ();
            try
            {
                Phonenumber.PhoneNumber parse = phoneNumberUtil.parse (phone , region);
                phone = phoneNumberUtil.format (parse , PhoneNumberUtil.PhoneNumberFormat.E164);
                return phoneNumberUtil.isValidNumber (parse);
            }
            catch (NumberParseException e)
            {
                return false;
            }
        }
    }

    public String getPhone ()
    {
        return phone;
    }
}
