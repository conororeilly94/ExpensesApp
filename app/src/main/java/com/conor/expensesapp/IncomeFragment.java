package com.conor.expensesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {

    // Firebase

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDb;

    // Recycler View
    private RecyclerView recyclerView;

    // Text view
    private TextView incomeTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_income, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDb = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid); // Lect70

        incomeTotal = myview.findViewById(R.id.income_result);

        recyclerView = myview.findViewById(R.id.recycler_income);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalValue = 0;

                for (DataSnapshot mySnapshot:snapshot.getChildren()){

                    Data data = mySnapshot.getValue(Data.class);
                    totalValue +=data.getAmount();
                    String stTotal = String.valueOf(totalValue);
                    incomeTotal.setText(stTotal);

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
                (Data.class, R.layout.income_recycler, MyViewHolder.class, mIncomeDb)
        {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Data model, int position) {
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());
                viewHolder.setAmount(model.getAmount());
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

        private void setType (String type) {
            TextView mType = mView.findViewById(R.id.type_income);
            mType.setText(type);
        }

        private void setNote (String note) {
            TextView mType = mView.findViewById(R.id.note_income);
            mType.setText(note);
        }

        private void setDate (String date) {
            TextView mType = mView.findViewById(R.id.date_income);
            mType.setText(date);
        }

        private void setAmount (int amount) {
            TextView mAmount = mView.findViewById(R.id.total_income);
            String stamount = String.valueOf(amount);
            mAmount.setText(stamount);
        }
    }
}