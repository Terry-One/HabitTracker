package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.habittracker.listener.EventListClickListener;
import com.example.habittracker.listener.NavigationBarClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HabitEventListActivity extends AppCompatActivity {
    // UI view objects
    ListView habitEventListView;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    ArrayList<HabitEvent> habitEventList;

    BottomNavigationView bottomNavigationView;

    HabitEvent passedEvent;

    private FirebaseAuth authentication;
    private String uid; // unique id for each user


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event_list);

//----------------------------UI Setup----------------------------------------------------------------------
        getSupportActionBar().setTitle("Habit Events");

        habitEventListView = findViewById(R.id.lv_habit_event);

        habitEventList = new ArrayList<HabitEvent>();



        habitEventAdapter = new HabitEventListAdapter(this, habitEventList);
        habitEventListView.setAdapter(habitEventAdapter); // Sets the adapter for event list, used for showing list items


//-------------------------------------------------- FireBase-------------------------------------------------------------------------------------------------------------

        authentication = FirebaseAuth.getInstance();
        if (authentication.getCurrentUser() != null){
            uid = authentication.getCurrentUser().getUid();
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(uid).child("HabitEvent");


//----------------------------------update listView -----------------------------------------------

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                habitEventList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HabitEvent habitE = (HabitEvent) dataSnapshot.getValue(HabitEvent.class);
                    habitEventList.add(habitE);

                }
                habitEventAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read Data Failed");
            }
        });


//--------------------------------------------- Process List View-----------------------------------------------------------------------------------------------------


//  /*-------
        AdapterView.OnItemClickListener habitEventListListener = new EventListClickListener(getApplicationContext(),this,habitEventAdapter);
        habitEventListView.setOnItemClickListener(habitEventListListener);
//-------------------*/

//        habitEventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                goToEventEditActivity(i);
//            }
//        });



//---------------------------------------- Process Navigation Bar-----------------------------------------------------------------------------------------------------
        bottomNavigationView = findViewById(R.id.bottom_navigation_event);
        bottomNavigationView.setSelectedItemId(R.id.navigation_habitEvent);

//  /*-------
        NavigationBarView.OnItemSelectedListener bottomNavigationViewOnItemSelectedListener = new NavigationBarClickListener(getApplicationContext(),this);
        bottomNavigationView.setOnItemSelectedListener(bottomNavigationViewOnItemSelectedListener);
//-------------------*/



//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.navigation_habit:
//                        Intent intent1 = new Intent(HabitEventListActivity.this, HabitListActivity.class);
//                        startActivity(intent1);
//                        finish();
//                        return true;
//                    case R.id.navigation_homePage:
//                        Intent intent2 = new Intent(HabitEventListActivity.this, MainPageActivity.class);
//                        intent2.putExtra("StartMode", "normal");
//                        startActivity(intent2);
//                        finish();
//                        return true;
//                    case R.id.navigation_following:
//                        Intent intent3 = new Intent(HabitEventListActivity.this, FollowingActivity.class);
//                        startActivity(intent3);
//                        finish();
//                        return true;
//                    case R.id.navigation_settings:
//                        Intent intent4 = new Intent(HabitEventListActivity.this, ProfileActivity.class);
//                        startActivity(intent4);
//                        finish();
//                        return true;
//                }
//                return false;
//            }
//        });


    }

    /**
     * Here are the steps we should take everytime we return to this activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Get passed-in data-----------------------------------------------------------------------------------------------------
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
    }

    /**
     * This method is used to ensure the getIntent() method always returns the latest intent
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }



//    /**
//     * This method is used to shift to event edit activity from event list activity
//     * @param index the index of pressed event in the event list
//     */
//    public void goToEventEditActivity(int index) {
//        Intent intent = new Intent(this, HabitEventEditActivity.class);
//        intent.putExtra("HabitEventForEdit", habitEventAdapter.getItem(index));
//        intent.putExtra("EventIndex", index);
//        startActivity(intent);
//    }

}