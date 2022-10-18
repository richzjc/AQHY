package com.micker.helper.text.html;

import android.text.Editable;
import android.text.Html;
import android.util.Log;

import org.xml.sax.XMLReader;

public class BrTagHandler implements Html.TagHandler {
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        Log.i("BrTagHandler", tag);
        if (tag.equalsIgnoreCase("br")) {
            Log.i("BrTagHandler", output.toString());
        }
    }
}
