package com.conor.expensesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

        // Floating button
        private FloatingActionButton fab_main_btn;
        private FloatingActionButton fab_income_btn;
        private FloatingActionButton fab_expense_btn;

        // Textview button
        private TextView fab_income_txt;
        private TextView fab_expense_txt;

        private boolean isOpen = false;

        private Animation FadeOpen, FadeClose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dash_board, container, false);

        // Connecting the floating btn to layout
        fab_main_btn = myview.findViewById(R.id.main_btn);
        fab_income_btn = myview.findViewById(R.id.income_button);
        fab_expense_btn = myview.findViewById(R.id.expense_button);

        // Connect text
        fab_income_txt = myview.findViewById(R.id.income_text);
        fab_expense_txt = myview.findViewById(R.id.expense_text);

        // Connect animation
        FadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        FadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadeClose);
                    fab_expense_txt.startAnimation(FadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_btn.setClickable(false);
                    isOpen = false;
                } else {
                    fab_income_btn.startAnimation(FadeOpen);
                    fab_expense_btn.startAnimation(FadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.setAnimation(FadeOpen);
                    fab_expense_txt.startAnimation(FadeOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_btn.setClickable(true);
                    isOpen = true;
                }
            }
        });

        return myview;
    }
}