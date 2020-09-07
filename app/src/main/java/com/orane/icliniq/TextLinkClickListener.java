package com.orane.icliniq;

import android.view.View;

public interface TextLinkClickListener {

    //  This method is called when the TextLink is clicked from LinkEnabledTextView

    void onTextLinkClick(View textView, String clickedString);
}