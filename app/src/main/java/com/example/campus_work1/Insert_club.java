package com.example.campus_work1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Insert_club extends AppCompatActivity {
    EditText name,ct;
    DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_club);

        name=(EditText)findViewById(R.id.club_name);
        ct=(EditText)findViewById(R.id.add_ctg);

        RelativeLayout relativeLayout1 = findViewById(R.id.addclub);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    processinsert();

            }
        });
    }
    private void processinsert()
    {
        String cname=name.getText().toString();
        String cat=ct.getText().toString();
        //saving the data
        User user=new User(cname,cat);
        mref= FirebaseDatabase.getInstance().getReference("clubs");
        String UserId=mref.push().getKey();
        mref.child(UserId).setValue(user);
        Toast.makeText(this, " Club sucessfully Saved: ", Toast.LENGTH_SHORT).show();
    }
    class User {
        public User(String cname, String cat) {
            this.cname = cname;
            this.cat = cat;
        }
        public User() {

        }
        public String cname;
        public String cat;
    }
}
