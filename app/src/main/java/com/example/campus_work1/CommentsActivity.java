package com.example.campus_work1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campus_work1.Adapter.CommentAdapter;

import com.example.campus_work1.Model.Comment;
import com.example.campus_work1.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    EditText addComment;
    ImageView image_profile;
    TextView post;

    String postId;
    String publisherId;
    Toolbar toolbar;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Comment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this,commentList);
        recyclerView.setAdapter(commentAdapter);


        addComment = findViewById(R.id.add_comment);
        image_profile =findViewById(R.id.image_profile);
        post = findViewById(R.id.post);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        postId = intent.getStringExtra("postid");
        publisherId = intent.getStringExtra("publisherid");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(addComment.getText().toString().equals(""))
                {
                    Toast.makeText(com.example.campus_work1.CommentsActivity.this, "You Can's send empty comment", Toast.LENGTH_SHORT).show();
                }else{
                   addComment();
                }
            }
        });

        getImage();
        readComments();
    }

    private void addComment() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);

        HashMap<String , Object> hashMap = new HashMap<>();

        hashMap.put("comment",addComment.getText().toString());
        hashMap.put("publisher",firebaseUser.getUid());


        reference.push().setValue(hashMap);

        addComment.setText("");
    }

    private void getImage()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                if(user!=null) {
                    Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readComments()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    commentList.clear();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Comment comment = snapshot.getValue(Comment.class);
                        commentList.add(comment);

                    }

                    commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}