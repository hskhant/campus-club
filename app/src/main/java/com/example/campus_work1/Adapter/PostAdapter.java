package com.example.campus_work1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campus_work1.CommentsActivity;
import com.example.campus_work1.Model.Post;
import com.example.campus_work1.Model.User;
import com.example.campus_work1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public Context mContext;
    public List<Post> mPost;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

         final Post post = mPost.get(position);

        Log.e("descriptionId",""+post.getDescription());
        Log.e("postId",""+post.getPostId());

        Glide.with(mContext).load(post.getPostimage()).into(holder.post_image);

         if(post.getDescription().equals(' '))
         {
             holder.description.setVisibility(View.GONE);
         }else {
             holder.description.setVisibility(View.VISIBLE);
             holder.description.setText(post.getDescription());
         }

         publisherInfo(holder.image_profile,holder.username,holder.publisher,post.getPublisher());
         isLiked(post.getPostId(),holder.like);
         nrLikes(holder.likes,post.getPostId());
         getComments(post.getPostId(),holder.comments);

         holder.like.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(holder.like.getTag().equals("like"))
                 {
                     FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                             .child(firebaseUser.getUid()).setValue(true);
                 }else{
                     FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                             .child(firebaseUser.getUid()).removeValue();
                 }
             }
         });

         holder.comment.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(mContext, CommentsActivity.class);
                 intent.putExtra("postid",post.getPostId());
                 intent.putExtra("publisherid",post.getPublisher());
                 mContext.startActivity(intent);
             }
         });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid",post.getPostId());
                intent.putExtra("publisherid",post.getPublisher());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile,post_image,like,comment,save;
        public TextView username,likes,publisher,description,comments;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            comments = itemView.findViewById(R.id.comments);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.description);


        }
    }

    private void getComments(String postid , final TextView comments)
    {
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  comments.setText("View All "+dataSnapshot.getChildrenCount() +" Comments");
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void isLiked(String postId, final ImageView imageView)
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists())
                {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                }else {
                    imageView.setImageResource(R.drawable.ic_heart);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void nrLikes(final TextView likes, String postId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+" likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, String userId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!=null) {
                    Glide.with(mContext).load(user.getImageurl()).into(image_profile);
                    username.setText(user.getUsername());
                    publisher.setText(user.getUsername());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
