package com.conor.expensesapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conor.expensesapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    // Firebase

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDb;

    //Recycler View
    private RecyclerView recyclerView;

    // Text view
    private TextView expenseTotal;

    // Edit data
    private EditText editAmount;
    private EditText editType;
    private EditText editNote;

    private Button updateBtn;
    private Button deleteBtn;

    private int amount;
    private String type;
    private String note;

    private String post_key;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mExpenseDb= FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        expenseTotal=myview.findViewById(R.id.expense_result);

        recyclerView=myview.findViewById(R.id.recycler_expense);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalValue=0;

                for (DataSnapshot mySnapshot:snapshot.getChildren()){

                    Data data = mySnapshot.getValue(Data.class);
                    totalValue += data.getAmount();
                    String total = String.valueOf(totalValue);
                    expenseTotal.setText(total + ".00");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data, MyViewHolder>adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (Data.class, R.layout.expense_recycler, MyViewHolder.class, mExpenseDb)
        {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());
                viewHolder.setAmount(model.getAmount());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key = getRef(position).getKey();
                        type = model.getType();
                        note = model.getNote();
                        amount = model.getAmount();

                        updateDataItem();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setType (String type){
            TextView mType = mView.findViewById(R.id.type_expense);
            mType.setText(type);
        }

        private void setNote (String note){
            TextView mNote = mView.findViewById(R.id.note_expense);
            mNote.setText(note);
        }

        private void setDate (String date){
            TextView mDate = mView.findViewById(R.id.date_expense);
            mDate.setText(date);
        }

        private void setAmount (int amount){
            TextView mAmount = mView.findViewById(R.id.total_expense);
            String strAmount=String.valueOf(amount);
            mAmount.setText(strAmount);
        }
    }

    private void updateDataItem() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.update_data_item, null);

        mydialog.setView(myview);

        editAmount = myview.findViewById(R.id.amount);
        editNote = myview.findViewById(R.id.note);
        editType = myview.findViewById(R.id.type);

        editAmount.setText(String.valueOf(amount));
        editAmount.setSelection(String.valueOf(amount).length());

        editType.setText(type);
        editType.setSelection(type.length());

        editNote.setText(note);
        editNote.setSelection(note.length());

        updateBtn = myview.findViewById(R.id.updateBtn);
        deleteBtn = myview.findViewById(R.id.deleteBtn);

        final AlertDialog dialog = mydialog.create();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = editType.getText().toString().trim();
                note = editNote.getText().toString().trim();

                String mamount = String.valueOf(amount);
                mamount = editAmount.getText().toString().trim();

                int myamount = Integer.parseInt(mamount);
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(myamount, type, note, post_key, mDate);

                mExpenseDb.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpenseDb.child(post_key).removeValue();
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}