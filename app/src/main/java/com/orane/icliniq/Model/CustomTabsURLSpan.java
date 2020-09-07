package com.orane.icliniq.Model;

import android.os.Parcel;
import android.text.style.URLSpan;
import android.view.View;

public class CustomTabsURLSpan extends URLSpan {
    public CustomTabsURLSpan(String url) {
        super(url);
    }

    public CustomTabsURLSpan(Parcel src) {
        super(src);
    }

    @Override
    public void onClick(View widget) {
        String url = getURL();
        // attempt to open with custom tabs, if that fails, call super.onClick
    }
}