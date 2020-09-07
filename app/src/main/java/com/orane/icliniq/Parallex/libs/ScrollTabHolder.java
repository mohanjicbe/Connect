package com.orane.icliniq.Parallex.libs;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.ScrollView;

public interface ScrollTabHolder {

    void adjustScroll(int scrollHeight, int headerHeight);

    void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                          int totalItemCount, int pagePosition);

    void onScrollViewScroll(ScrollView view, int x, int y,
                            int oldX, int oldY, int pagePosition);

    void onRecyclerViewScroll(RecyclerView view, int dx, int dy, int scrollY, int pagePosition);
}
