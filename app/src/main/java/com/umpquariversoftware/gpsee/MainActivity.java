package com.umpquariversoftware.gpsee;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umpquariversoftware.gpsee.elements.Spot;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private String TAG ="MainActivity";
    private int RC_SIGN_IN = 69;
    private String userID = "some_user";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Boolean userIsLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticateUser();

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void authenticateUser(){

        /**
         * Authenticates user, and watches for changes.
         *
         * **/

        mAuth = FirebaseAuth.getInstance();
        userIsLoggedIn = false;

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userIsLoggedIn = true;
                    userID = user.getUid();
                    Toast.makeText(MainActivity.this, "Welcome, " + user.getDisplayName()
                            , Toast.LENGTH_SHORT).show();
                    saveSpotToFirebase(mockSpot());
                    listenUp();
                } else {
                    userIsLoggedIn = false;
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private Spot mockSpot(){
        Spot spot = new Spot();

        spot.setName("test spot");
        spot.setSignature("John Hancock");
        spot.setDescription("This is a test. Don't worry.");
        spot.setLatitude((long) 123.456);
        spot.setLongitude((long) 69.77);

        ArrayList<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        spot.setTags(tags);

        ArrayList<Integer> type = new ArrayList<>();
        type.add(Spot.LOCATION_TYPE_RAW_LAND);
        type.add(Spot.LOCATION_TYPE_DIRT_ROAD);
        type.add(Spot.LOCATION_TYPE_REQUIRES_SNOW_GEAR);
        spot.setType(type);

        ArrayList<String> otherSpots = new ArrayList<>();
        otherSpots.add("Aruba");
        otherSpots.add("Jamaica");
        otherSpots.add("Fiji");
        spot.setAssociatedLocations(otherSpots);

        return spot;
    }

    private void saveSpotToFirebase(Spot spot){
        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("spots")
                .child("users")
                .child(userID)
                .child(spot.getSignature())
                .setValue(spot);
    }

    private void listenUp(){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("spots").child("users").child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Spot> spots = new ArrayList<>();
                        spots.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Spot spot = child.getValue(Spot.class);
                            if (spot != null) {
                                spots.add(spot);
                            }
                        }
                        for (Spot spot : spots){
                            Log.e(TAG, "singleValueEvent spot.getName(): " + spot.getName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "database error");
                    }
                });

        mDatabase.child("jams").child("users").child(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Spot> spots = new ArrayList<>();
                        spots.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Spot spot = child.getValue(Spot.class);
                            if (spot != null) {
                                spots.add(spot);
                            }
                        }
                        for (Spot spot : spots){
                            Log.e(TAG, "valueEventListener spot.getName(): " + spot.getName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "database error");
                    }
                });
    }

    public void signoutUser() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        // do something if you need to
                        finish();
                    }
                });
    }


}
