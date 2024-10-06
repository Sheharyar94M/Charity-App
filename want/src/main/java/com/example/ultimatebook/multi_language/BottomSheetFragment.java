package com.example.ultimatebook.multi_language;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.charity.R;
import com.example.ultimatebook.multi_language.adapter.ListViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private ArrayList<String> langauge;


    public void setLangauge(ArrayList<String> langauge) {
        this.langauge = langauge;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public static BottomSheetFragment newInstance() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();

        return bottomSheetFragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {

        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);

        dialog.setContentView(contentView);

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        ListViewAdapter adapter = new ListViewAdapter(requireActivity(),requireContext(),langauge);

        ListView LanguageList = contentView.findViewById(R.id.language_list);

        LanguageList.setAdapter(adapter);

    }
}