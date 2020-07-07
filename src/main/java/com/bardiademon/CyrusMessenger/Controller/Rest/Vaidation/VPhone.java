package com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation;

import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class VPhone implements Validation
{
    private final String phone, region;

    private String nPhone, nRegion;

    private boolean valid;

    public VPhone (String Phone , String Region)
    {
        this.phone = Phone;
        if (!Str.IsEmpty (Region)) this.region = Region;
        else this.region = "IR";
    }

    @Override
    public boolean check ()
    {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance ();
        try
        {
            Phonenumber.PhoneNumber parse = phoneNumberUtil.parse (phone , region);
            nPhone = phoneNumberUtil.format (parse , PhoneNumberUtil.PhoneNumberFormat.E164);
            nRegion = phoneNumberUtil.getRegionCodeForNumber (parse);
            return (valid = phoneNumberUtil.isValidNumber (parse));
        }
        catch (NumberParseException e)
        {
            return (valid = false);
        }
    }

    public String getRegion ()
    {
        if (valid) return nRegion;
        else return region;
    }

    public String getPhone ()
    {
        if (valid) return nPhone;
        else return phone;
    }
}
