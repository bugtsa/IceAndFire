package com.bugtsa.iceandfire.ui.activities.interfaces;

/**
 * Слушает собития прикосания к графическим элементам в RecyclerView
 */
public interface CustomClickListener {
    void onUserItemClickListener(String action, int position);
}
