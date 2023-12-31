package com.bardiademon.CyrusMessenger.bardiademon;

import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;

public final class ToJson
{
    private Object obj;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private ToJson (Object obj)
    {
        this.obj = obj;
    }

    private ToJson (HttpServletRequest request)
    {
        this.request = request;
    }

    private ToJson (HttpServletResponse response)
    {
        this.response = response;
    }

    public ToJson setObj (Object obj)
    {
        this.obj = obj;
        return this;
    }

    private String to ()
    {
        try
        {
            final String result = (new ObjectMapper ()).writeValueAsString (obj);
            if (obj instanceof CreateClass)
                return (new JSONObject (result)).getJSONObject ("create_class").toString ();
            else return result;
        }
        catch (JsonProcessingException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e);
            System.out.println (e.getMessage ());
            return null;
        }
    }

    public static String To (Object o)
    {
        return new ToJson (o).to ();
    }

    private String requestToJson ()
    {
        JSONObject jsonObject = new JSONObject ();
        jsonObject
                .put ("parameter_map" , request.getParameterMap ())
                .put ("request_uri" , request.getRequestURI ())
                .put ("query_string" , request.getQueryString ())
                .put ("auth_type" , request.getAuthType ())
                .put ("context_path" , request.getContextPath ())
                .put ("method" , request.getMethod ())
                .put ("remote_user" , request.getRemoteUser ())
                .put ("remote_addr" , request.getRemoteAddr ())
                .put ("remote_host" , request.getRemoteHost ())
                .put ("remote_port" , request.getRemotePort ())
                .put ("servlet_path" , request.getServletPath ());
        try
        {
            jsonObject.put ("session" , To (request.getSession ()));
        }
        catch (Exception ignored)
        {
            jsonObject.put ("session" , "");
        }

//        try
//        {
//            jsonObject.put ("cookies" , setObj (request.getCookies ()).to ());
//        }
//        catch (Exception ignored)
//        {
//            jsonObject.put ("cookies" , "");
//        }

        Enumeration <String> headerNames = request.getHeaderNames ();
        CreateClass header = new CreateClass ();
        if (headerNames != null)
        {
            String nameHeader;
            do
            {
                nameHeader = headerNames.nextElement ();
                header.put (nameHeader , request.getHeader (nameHeader));
            }
            while (headerNames.hasMoreElements ());

            jsonObject.put ("headers" , To (header));
        }
        return jsonObject.toString ();
    }

    public static String RequestToJson (HttpServletRequest request)
    {
        return new ToJson (request).requestToJson ();
    }

    public static String ResponseToJson (HttpServletResponse response)
    {
        return new ToJson (response).responseToJson ();
    }

    private String responseToJson ()
    {
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("status" , response.getStatus ());
        try
        {
            if (response.getHeaderNames () != null)
            {
                Collection <String> headerNames = response.getHeaderNames ();
                CreateClass header = new CreateClass ();
                if (headerNames != null)
                {
                    String nameHeader;
                    for (String headerName : headerNames)
                    {
                        nameHeader = headerName;
                        header.put (nameHeader , response.getHeader (nameHeader));
                    }

                    jsonObject.put ("headers" , setObj (header).to ());
                }
            }
        }
        catch (NullPointerException ignored)
        {
        }

        return jsonObject.toString ();
    }

    public static class CreateClass
    {
        @JsonProperty ("create_class")
        public Map <String, Object> createClass = new LinkedHashMap <> ();

        public CreateClass put (Enum <?> key , Object value)
        {
            return put (key.name () , value);
        }

        public CreateClass put (String key , Object value)
        {
            createClass.put (key , value);
            return this;
        }

        @JsonProperty ("create_class")
        public Map <String, Object> getCreateClass ()
        {
            return createClass;
        }

        @JsonIgnore
        public String toJson ()
        {
            return To (this);
        }

        @JsonIgnore
        public JSONObject toJsonObject ()
        {
            try
            {
                return new JSONObject (To (this));
            }
            catch (JSONException e)
            {
                return null;
            }
        }

        @JsonIgnore
        @Override
        public String toString ()
        {
            return toJson ();
        }

        @JsonIgnore
        public static CreateClass OCLogin (String CLogin)
        {
            CreateClass createClass = new CreateClass ();
            createClass.put (MCookie.KEY_CODE_LOGIN_COOKIE , CLogin);
            return createClass;
        }

        @JsonIgnore
        // n => New
        public static CreateClass n (Enum <?> key , Object value)
        {
            return n (key.name () , value);
        }

        @JsonIgnore
        // n => New
        public static CreateClass n (String key , Object value)
        {
            return ((new CreateClass ()).put (key , value));
        }

        @JsonIgnore
        // n => New , j => to json
        public static String nj (Enum <?> key , Object value)
        {
            return nj (key.name () , value);
        }

        @JsonIgnore
        // n => New , j => to json
        public static String nj (String key , Object value)
        {
            return ((new CreateClass ()).put (key , value)).toJson ();
        }

        @JsonIgnore
        public static String SCLogin (String CLogin)
        {
            return OCLogin (CLogin).toJson ();
        }
    }
}
