package com.mozeeb.mynotesapps.activity.editor;

import com.mozeeb.mynotesapps.model.Note;
import com.mozeeb.mynotesapps.network.ApiClient;
import com.mozeeb.mynotesapps.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorPresenter {
    private EditorView view;

    public EditorPresenter(EditorView view){
        this.view = view;
    }

     void saveNote(final String title, final String note, final int color) {

        view.showProgress();

        ApiInterface apiInterface = ApiClient.getClient()
                .create(ApiInterface.class);
        Call<Note> call = apiInterface.saveNote(title, note, color);

        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                view.hideProgress();
                if (response.isSuccessful()) {
                    view.onRequestSuccess(response.body().getMessage());
//                    Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    finish();
                } else {
                    view.onRequestError(response.body().getMessage());
//                    Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                }

            }


            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }
    void updateNote(int id, String title, String note, int color){
        view.showProgress();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Note> call = apiInterface.updateNote(id, title, note, color);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null);
                Boolean success = response.body().getSucces();
                if (success){
                    view.onRequestSuccess(response.body().getMessage());
                }else {
                    view.onRequestError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());

            }
        });
    }

    void deleteNote(int id){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Note> call = apiInterface.deleteNote(id);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSucces();
                    if (success){
                        view.onRequestSuccess(response.body().getMessage());
                    }else {
                        view.onRequestError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());

            }
        });
    }

}
