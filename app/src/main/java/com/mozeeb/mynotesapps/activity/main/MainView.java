package com.mozeeb.mynotesapps.activity.main;

import com.mozeeb.mynotesapps.model.Note;

import java.util.List;

public interface MainView {
    void showLoading();
    void hideLoading();
    void onGetResult(List<Note> notes);
    void onErrorLoading(String message);
}
