package com.psychology.MainUI;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.psychology.EntriesUI.ERTEntryActivity;
import com.psychology.EntriesUI.MREntryActivity;
import com.psychology.EntriesUI.PAEntryActivity;
import com.psychology.R;


public class HomeFragment extends Fragment {

    CardView ERTEntry, MREntry, PAEntry;

    TextView ERTEntries, MREntries, PAEntries;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ERTEntry = view.findViewById(R.id.ERTButton);
        MREntry = view.findViewById(R.id.MRButton);
        PAEntry = view.findViewById(R.id.PAButton);

        ERTEntries = view.findViewById(R.id.ERTEntriesNumber);
        MREntries = view.findViewById(R.id.MREntriesNumber);
        PAEntries = view.findViewById(R.id.PAEntriesNumber);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("ERT").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                if(value.getDocuments().isEmpty()){
                    ERTEntries.setText("0");
                }
                else{
                    ERTEntries.setText(String.valueOf(value.getDocuments().size()));
                }
            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("MR").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                if(value.getDocuments().isEmpty()){
                    MREntries.setText("0");
                }
                else{
                    MREntries.setText(String.valueOf(value.getDocuments().size()));
                }
            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("PA").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                if(value.getDocuments().isEmpty()){
                    PAEntries.setText("0");
                }
                else{
                    PAEntries.setText(String.valueOf(value.getDocuments().size()));
                }
            }
        });

        ERTEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ERTEntryActivity.class);
                startActivity(intent);
            }
        });

        MREntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MREntryActivity.class);
                startActivity(intent);
            }
        });

        PAEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PAEntryActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}