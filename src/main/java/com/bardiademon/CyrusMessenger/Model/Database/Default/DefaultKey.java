package com.bardiademon.CyrusMessenger.Model.Database.Default;

public enum DefaultKey
{
    /**
     * baraye ConfirmEmail ast , daghighe etebar kode ,
     * code tolid shode va ta in daghighee ke ba in key sabt mishavad etebar darad
     * <p>ce => ConfirmEmail</p>
     */
    ce_min_valid,

    /**
     * w => width
     * h =>  height
     */
    min_w_sticker_image, min_h_sticker_image, max_w_sticker_image, max_h_sticker_image,

    max_get_personal_gaps, max_get_gaps, max_gaps_files_size, max_get_gaps_files,

    max_size_sticker, sticker_max_len_name;

    public static DefaultKey to (String val)
    {
        try
        {
            return valueOf (val);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
