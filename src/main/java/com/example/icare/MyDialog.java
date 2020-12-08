package com.example.icare;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {

    /** Declaring variables*/
    public static final String DIALOG_NOTE_ADD = "addNote";
    public static final String DIALOG_NOTE_UPDATE = "updateNote";
    private String text;
    /** Send an onclick listener to the dialog box*/

    private OnDialogClickListener onDialogClickListener;

    public MyDialog(String text) {
        this.text = text;
    }

    /** Constructors*/
    public MyDialog() {
    }

    @NonNull
    @Override
    /** gets a tag on the clicked option of the dialog and sends to the right method*/
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if (getTag().equals(DIALOG_NOTE_ADD)) dialog = getAddNoteDialog();
        if (getTag().equals(DIALOG_NOTE_UPDATE)) dialog = getUpdateNoteDialog();
        return dialog;
    }

    /** Method of updating the note dialog */
    private Dialog getUpdateNoteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_note, null);
        EditText note_edt = layout.findViewById(R.id.note_edt);

        note_edt.setText(text);
        note_edt.setSelection(text.length());
        builder.setView(layout)
                .setTitle("Update Note")
                .setPositiveButton("update", (a, b) ->
                        onDialogClickListener.onPositiveButtonClick(note_edt.getText().toString()))
                .setNegativeButton("cancel", null);
                 return builder.create();
    }

    /** Method of adding a new note dialog */
    private Dialog getAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_note, null);
        EditText note_edt = layout.findViewById(R.id.note_edt);

        builder.setView(layout)
                .setTitle("New Note")
                .setPositiveButton("create", (a, b) ->
                        onDialogClickListener.onPositiveButtonClick(note_edt.getText().toString()))
                .setNegativeButton("cancel", null);
                 return builder.create();
    }

    public interface OnDialogClickListener {
        void onPositiveButtonClick(String string);
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }
}
