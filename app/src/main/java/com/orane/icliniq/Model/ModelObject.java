package com.orane.icliniq.Model;


import com.orane.icliniq.R;

public enum ModelObject {

    RED(R.string.app_name, R.layout.home_fragment),
    BLUE(R.string.app_name, R.layout.doctors_list),
    GREEN(R.string.app_name, R.layout.common_activity);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}