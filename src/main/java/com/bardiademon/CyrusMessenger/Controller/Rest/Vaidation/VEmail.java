package com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation;

public class VEmail implements Validation
{

    private String email;

    public VEmail (String Email)
    {
        this.email = Email;
    }

    @Override
    public boolean check ()
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        if (email == null || email.equals ("")) return false;
        else return email.matches (emailRegex);
    }
}
