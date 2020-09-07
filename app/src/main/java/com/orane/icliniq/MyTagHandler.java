package com.orane.icliniq;

import android.text.Editable;
import android.text.Html.TagHandler;

import org.xml.sax.XMLReader;


public class MyTagHandler implements TagHandler {
    boolean first = true;
    String parent = null;
    int index = 1;
    int count = 0;

    @Override
    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {

        if (tag.equals("ul")) parent = "ul";
        else if (tag.equals("ol")) parent = "ol";
        if (tag.equals("li")) {
            if (parent.equals("ul")) {
                if (first) {
                    if(count >= 1){
                        output.append("\n\n");
                    }

                    output.append("→ ");
                    first = false;
                    count++;


                } else {
                    first = true;
                }
            } else {
                if (first) {
                    output.append("\n" + index + ". ");
                    first = false;
                    index++;
                } else {
                    first = true;
                }
            }
        }
    }
}