package com.radiantkey.findmycar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class RecordDialog extends AppCompatDialogFragment {

    private EditText name, cat;
    private RecordDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view).setTitle("Test").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String sName = name.getText().toString();
                String sCat = cat.getText().toString();
                listener.applyTexts(sName, sCat);
            }
        });

        name = view.findViewById(R.id.dialog_name);
        cat = view.findViewById(R.id.dialog_note);
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (RecordDialogListener) ((MainActivity) context).getCurFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement recorddialoglistener");
        }
    }

    public interface RecordDialogListener{
        void applyTexts(String name, String cat);
    }
}
