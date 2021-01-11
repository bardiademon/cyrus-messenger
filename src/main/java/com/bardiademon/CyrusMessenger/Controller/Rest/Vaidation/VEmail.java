package com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation;

public final class VEmail implements Validation
{

    private final String email;

    public VEmail (String Email)
    {
        this.email = Email;
    }

    @Override
    public boolean check ()
    {
        if (email == null || email.equals ("")) return false;
        else
        {
            final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";
            return email.matches (emailRegex);
        }
    }
}
