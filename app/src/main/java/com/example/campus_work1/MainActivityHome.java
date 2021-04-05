package com.example.campus_work1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campus_work1.Model.User;
import com.example.campus_work1.view.ui.HomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivityHome extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private TextView txtNameHeader;
    private User userdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivityHome.this, layout_login.class));
                    finish();
                }
            }
        };
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
//                Intent intent = new Intent(MainActivityHome.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userdata=snapshot.getValue(User.class);
                View headerView = navigationView.getHeaderView(0);
                txtNameHeader = headerView.findViewById(R.id.txtNameHeader);
                txtNameHeader.setText(userdata.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        View headerView = navigationView.getHeaderView(0);
                        txtNameHeader = headerView.findViewById(R.id.txtNameHeader2);
                        txtNameHeader.setText(user.getEmail());
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                    String username = ds.child("username").getValue(String.class);
//
//                    Log.d("TAG", username);
//
//                    try {
//                        View headerView = navigationView.getHeaderView(0);
//                        txtNameHeader = headerView.findViewById(R.id.txtNameHeader);
//                        txtNameHeader.setText(username);
//
//                    } catch (Exception ex) {
//
//                    }
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        usersdRef.addListenerForSingleValueEvent(eventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.action_settings:
            postfun();
            return(true);
        case R.id.logout:
            logoutfun();
            return(true);
        case R.id.add_post:
            addpostfun();
            return(true);
        case R.id.userpost:
            userpostfun();
            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }

    private void userpostfun() {
        startActivity(new Intent(MainActivityHome.this, History.class));
    }

    private void addpostfun() {
        startActivity(new Intent(MainActivityHome.this, PostActivity.class));
    }

    private void postfun() {
        startActivity(new Intent(MainActivityHome.this, MainActivity2.class));
    }

    private void logoutfun() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivityHome.this, layout_login.class));
        finish();
        Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }




}