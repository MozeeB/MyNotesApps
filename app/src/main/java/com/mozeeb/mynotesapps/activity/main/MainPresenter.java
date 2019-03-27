package com.mozeeb.mynotesapps.activity.main;

import com.mozeeb.mynotesapps.model.Note;
import com.mozeeb.mynotesapps.network.ApiClient;
import com.mozeeb.mynotesapps.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {
    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    void getData(){
        view.showLoading();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Note>> call = apiInterface.getNotes();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null){
                    view.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
}
