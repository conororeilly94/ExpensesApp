package com.conor.expensesapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.conor.expensesapp.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

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

        // Firebase
        private FirebaseAuth mAuth;
        private DatabaseReference mIncomeDb;
        private DatabaseReference mExpenseDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dash_board, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDb = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDb = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

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

                addData();

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

    // Floating btn animation
    private void ftAnimation() {
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

    private void addData() {

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();
            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });

    }

    public void incomeDataInsert() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.insert_data, null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        final EditText editAmount = myview.findViewById(R.id.amount);
        final EditText editType = myview.findViewById(R.id.type);
        final EditText editNote = myview.findViewById(R.id.note);

        Button saveBtn = myview.findViewById(R.id.submitBtn);
        Button cancelBtn = myview.findViewById(R.id.cancelBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = editType.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();
                String note = editNote.getText().toString().trim();

                if (TextUtils.isEmpty(type)) {
                    editType.setError("Required Field");
                    return;
                }
                if (TextUtils.isEmpty(type)) {
                    editAmount.setError("Required Field");
                    return;
                }

                int amountint = Integer.parseInt(amount);

                if (TextUtils.isEmpty(type)) {
                    editNote.setError("Required Field");
                    return;
                }

                String id = mIncomeDb.push().getKey();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(amountint, type, note, id, mDate);

                mIncomeDb.child(id).setValue(data);

                Toast.makeText(getActivity(), "Data Added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();;
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void expenseDataInsert() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.insert_data, null);

        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        dialog.setCancelable(false);

        final EditText amount = myview.findViewById(R.id.amount);
        final EditText type = myview.findViewById(R.id.type);
        final EditText note = myview.findViewById(R.id.note);

        Button saveBtn = myview.findViewById(R.id.submitBtn);
        Button cancelBtn = myview.findViewById(R.id.cancelBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tmAmount = amount.getText().toString().trim();
                String tmType = type.getText().toString().trim();
                String tmNote = note.getText().toString().trim();

                if (TextUtils.isEmpty(tmAmount)) {
                    amount.setError("Required Field");
                    return;
                }

                int amountint = Integer.parseInt(tmAmount);

                if (TextUtils.isEmpty(tmType)) {
                    type.setError("Required Field");
                    return;
                }
                if (TextUtils.isEmpty(tmNote)) {
                    type.setError("Required Field");
                    return;
                }

                String id = mExpenseDb.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(amountint, tmType, tmNote, id, mDate);
                mExpenseDb.child(id).setValue(data);

                Toast.makeText(getActivity(), "Data Added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}