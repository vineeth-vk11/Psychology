package com.psychology.MainUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.psychology.AccountUI.ContactUsActivity;
import com.psychology.AccountUI.EditAccountActivity;
import com.psychology.AccountUI.KnowMoreActivity;
import com.psychology.LoginUI.LoginMainActivity;
import com.psychology.R;

import java.util.ArrayList;

public class AccountFragment extends Fragment {

    Button aboutTheApp, knowMore, contactUs, privacyPolicy, logout;

    TextView userName, userMobileNumber, userWorkWeek, userWorkTimings;
    Button editWorkCycle, rateApp;
    ImageButton editName;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        logout = view.findViewById(R.id.logout);

        userName = view.findViewById(R.id.userName);
        userMobileNumber = view.findViewById(R.id.userMobileNumber);
        userWorkWeek = view.findViewById(R.id.userWorkWeek);
        userWorkTimings = view.findViewById(R.id.userWorkTimings);
        editWorkCycle = view.findViewById(R.id.editWorkingCycle);
        rateApp = view.findViewById(R.id.rateApp);
        editName = view.findViewById(R.id.editName);

        aboutTheApp = view.findViewById(R.id.aboutTheApp);
        knowMore = view.findViewById(R.id.knowMore);
        contactUs = view.findViewById(R.id.contactUs);
        privacyPolicy = view.findViewById(R.id.privacyPolicy);

        aboutTheApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://docs.google.com/document/d/1QneXgRnA73Zgxuhgly_MaTJsLZbfWfD9/edit?usp=sharing&ouid=113224595630128607317&rtpof=true&sd=true"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://docs.google.com/document/d/13Ht2OoE9B8cPaxLhvb1jfTrMaYnATYYH/edit?usp=sharing&ouid=113224595630128607317&rtpof=true&sd=true"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), KnowMoreActivity.class);
                startActivity(intent);
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ContactUsActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getContext(), LoginMainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditAccountActivity.class);
                startActivity(intent);
            }
        });

        editWorkCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditAccountActivity.class);
                startActivity(intent);
            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                userName.setText(value.getString("name"));
                userMobileNumber.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                userWorkWeek.setText(value.getString("workingDaysText"));
                userWorkTimings.setText(value.getString("workStartTime") + " - " + value.getString("workEndingTime"));
            }
        });

        return view;
    }
}