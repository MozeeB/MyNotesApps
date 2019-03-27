package com.mozeeb.mynotesapps.activity.editor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.mozeeb.mynotesapps.R;
import com.mozeeb.mynotesapps.model.Note;
import com.mozeeb.mynotesapps.network.ApiClient;
import com.mozeeb.mynotesapps.network.ApiInterface;
import com.thebluealliance.spectrum.SpectrumPalette;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity implements EditorView{

    @BindView(R.id.edt_title)
    EditText title;
    @BindView(R.id.edt_note)
    EditText note;

    ProgressDialog progressDialog;

    EditorPresenter presenter;

    int color;
    @BindView(R.id.palette)
    SpectrumPalette palette;

    String tv_title, note_baru;
    int id;

    Menu actionMenu;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        palette.setOnColorSelectedListener(clr -> color = clr);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        presenter = new EditorPresenter(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        tv_title = intent.getStringExtra("title");
        note_baru = intent.getStringExtra("note");
        color = intent.getIntExtra("color", 0);

        setDataFromIntentExtra();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        actionMenu = menu;

        if (id != 0){
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);

        }else {
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title1 = title.getText().toString().trim();
        String note2 = note.getText().toString().trim();
        int color1 = this.color;

        switch (item.getItemId()) {
            case R.id.save:

                if (title1.isEmpty()) {
                    title.setError("Please enter title");
                } else if (note2.isEmpty()) {
                    note.setError("Please enter notes");
                } else {
                    presenter.saveNote(title1, note2, color1);
                }
                return true;
            case R.id.edit:
                editMode();
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);

                return true;
            case R.id.update:
                if (title1.isEmpty()) {
                    title.setError("Please enter title");
                } else if (note2.isEmpty()) {
                    note.setError("Please enter notes");
                } else {
                    presenter.updateNote(id,title1, note2, color1);
                }
                return true;
            case R.id.delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Confirm!");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setNegativeButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    presenter.deleteNote(id);
                });
                alertDialog.setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss());

                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void showProgress() {
        progressDialog.show();

    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();

    }

    @Override
    public void onRequestSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRequestError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();


    }
    private void setDataFromIntentExtra() {
        if (id  != 0){
            title.setText(tv_title);
            note.setText(note_baru);
            palette.setSelectedColor(color);

            getSupportActionBar().setTitle("Update Note");
            readMode();
        }else {
            //default color setup
            palette.setSelectedColor(getResources().getColor(R.color.white));
            color = getResources().getColor(R.color.white);
            editMode();
        }
    }

    private void editMode() {
        title.setFocusableInTouchMode(true);
        note.setFocusableInTouchMode(true);
        palette.setEnabled(true);
    }

    private void readMode() {
        title.setFocusableInTouchMode(false);
        note.setFocusableInTouchMode(false);
        title.setFocusable(false);
        note.setFocusable(false);
        palette.setEnabled(false);

    }

}

