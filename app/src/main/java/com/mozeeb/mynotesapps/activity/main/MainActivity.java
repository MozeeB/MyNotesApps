package com.mozeeb.mynotesapps.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.mozeeb.mynotesapps.R;
import com.mozeeb.mynotesapps.activity.editor.EditorActivity;
import com.mozeeb.mynotesapps.activity.main.Adapter.MainAdapter;
import com.mozeeb.mynotesapps.model.Note;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final int INTENT_EDIT = 200;
    private static final int INTENT_ADD = 100;
    @BindView(R.id.add)
    FloatingActionButton add;

    @BindView(R.id.rv_note)
    RecyclerView rvNote;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    MainPresenter presenter;
    MainAdapter adapter;
    MainAdapter.ItemClickListener itemClickListener;

    List<Note> noteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        rvNote.setLayoutManager(new LinearLayoutManager(this));

        presenter = new MainPresenter(this);
        presenter.getData();

        swipeRefresh.setOnRefreshListener(() -> presenter.getData());

        itemClickListener = ((view, position) -> {
            int id = noteList.get(position).getId();
            String title = noteList.get(position).getTitle();
            String note = noteList.get(position).getNote();
            int color = noteList.get(position).getColor();

            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("note", note);
            intent.putExtra("color", color);
            startActivityForResult(intent, INTENT_EDIT);
        });
    }

    @OnClick(R.id.add)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, EditorActivity.class), INTENT_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_ADD && resultCode == RESULT_OK){
            presenter.getData();
        }else if (requestCode == INTENT_EDIT && resultCode == RESULT_OK){
            presenter.getData();
        }
    }

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);

    }

    @Override
    public void onGetResult(List<Note> notes) {
        adapter = new MainAdapter( notes,this, itemClickListener);
        adapter.notifyDataSetChanged();
        rvNote.setAdapter(adapter);

        noteList =  notes;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
