package com.conor.expensesapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.EventListener;

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

        // Dashboard total
        private TextView totalIncome;
        private TextView totalExpense;

        // Firebase
        private FirebaseAuth mAuth;
        private DatabaseReference mIncomeDb;
        private DatabaseReference mExpenseDb;

        // Recycler
        private RecyclerView recyclerIncome;
        private RecyclerView recyclerExpense;

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

        mIncomeDb.keepSynced(true);
        mExpenseDb.keepSynced(true);

        // Connecting the floating btn to layout
        fab_main_btn = myview.findViewById(R.id.main_btn);
        fab_income_btn = myview.findViewById(R.id.income_button);
        fab_expense_btn = myview.findViewById(R.id.expense_button);

        // Connect text
        fab_income_txt = myview.findViewById(R.id.income_text);
        fab_expense_txt = myview.findViewById(R.id.expense_text);

        // Total Income and Expense
        totalIncome = myview.findViewById(R.id.income_total);
        totalExpense = myview.findViewById(R.id.expense_total);

        // Recycler
        recyclerIncome = myview.findViewById(R.id.recycler_income);
        recyclerExpense = myview.findViewById(R.id.recycler_expense);

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
        mIncomeDb.addValueEventListener(new ValueEventListener() {
            // When updating total income in dashboard
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int total = 0;
                for (DataSnapshot mysnap:dataSnapshot.getChildren()) {
                    Data data = mysnap.getValue(Data.class);

                    total += data.getAmount();

                    String result = String.valueOf(total);
                    totalIncome.setText(result + ".00");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mExpenseDb.addValueEventListener(new ValueEventListener() {
            // When updating total expenses in dashboard
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int total = 0;

                for (DataSnapshot mysnapshot:dataSnapshot.getChildren()) {
                    Data data = mysnapshot.getValue(Data.class);
                    total += data.getAmount();

                    String result = String.valueOf(total);
                    totalExpense.setText(result + ".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Recycler
        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        recyclerIncome.setHasFixedSize(true);
        recyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        recyclerExpense.setHasFixedSize(true);
        recyclerExpense.setLayoutManager(layoutManagerExpense);

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
        // Adding new income data to DB
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
        // Adding new expense data to DB
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

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                (
                        Data.class,
                        R.layout.dashboard_income,
                        DashBoardFragment.IncomeViewHolder.class,
                        mIncomeDb
                ) {
            @Override
            protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data model, int i) {
                incomeViewHolder.setIncomeType(model.getType());
                incomeViewHolder.setIncomeAmount(model.getAmount());
                incomeViewHolder.setIncomeDate(model.getDate());
            }
        };
        recyclerIncome.setAdapter(incomeAdapter);

        FirebaseRecyclerAdapter<Data, ExpenseViewHolder> expenseAdapter = new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>
                (
                        Data.class,
                        R.layout.dashboard_expense,
                        DashBoardFragment.ExpenseViewHolder.class,
                        mExpenseDb
                ) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder expenseViewHolder, Data model, int i) {
                expenseViewHolder.setExpenseType(model.getType());
                expenseViewHolder.setExpenseAmount(model.getAmount());
                expenseViewHolder.setExpenseDate(model.getDate());
            }
        };
        recyclerExpense.setAdapter(expenseAdapter);
    }

    // Income data
    public static class IncomeViewHolder extends RecyclerView.ViewHolder {

        View incomeView;

        public IncomeViewHolder(View itemView) {
            super(itemView);
            incomeView = itemView;
        }

        public void setIncomeType(String type) {
            TextView mtype = incomeView.findViewById(R.id.type_income);
            mtype.setText(type);
        }

        public void setIncomeAmount(int amount) {
            TextView mAmount = incomeView.findViewById(R.id.amount_income);

            String strAmount = String.valueOf(amount);
            mAmount.setText(strAmount);
        }

        public void setIncomeDate(String date) {
            TextView mDate = incomeView.findViewById(R.id.date_income);
            mDate.setText(date);
        }
    }

    // Expense data
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        View expenseView;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            expenseView = itemView;
        }

        public void setExpenseType(String type) {
            TextView mtype = expenseView.findViewById(R.id.type_expense);
            mtype.setText(type);
        }

        public void setExpenseAmount(int amount) {
            TextView mAmount = expenseView.findViewById(R.id.amount_expense);

            String strAmount = String.valueOf(amount);
            mAmount.setText(strAmount);
        }

        public void setExpenseDate(String date) {
            TextView mDate = expenseView.findViewById(R.id.date_expense);
            mDate.setText(date);
        }
    }
}