package com.ram.instagramcloneapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.ram.instagramcloneapp.Fragments.PostDetailFragment;
import com.ram.instagramcloneapp.Model.User;
import com.ram.instagramcloneapp.Model.Post;
import com.ram.instagramcloneapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private static final String TAG = "PostAdapter";

    private Context mContext;
    private List<Post> mPosts;
    private FirebaseUser firebaseUser;

    //Constructor of Post Adapter
    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Started ");


        Post post = mPosts.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.postImage);

        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                if(user.getImageurl().equals("default"))
                {
                    holder.imgProfile.setImageResource(R.mipmap.ic_launcher);
                }
                else
                    {
                        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imgProfile);
                    }
                holder.username.setText(user.getUsername());
                holder.auther.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        isLiked(post.getPostid(), holder.like);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.getTag().equals("like"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);

                }else
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                }

            }
        });

        noOfLikes(post.getPostid(), holder.noOfLikes);

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit().putString("postid",post.getPostid())
                        .apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new PostDetailFragment()).commit();
            }
        });

    }
    private void noOfLikes(String postId,TextView text)
    {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() > 0)
                        text.setText(snapshot.getChildrenCount() + " Likes");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    //if post is already liked by current user then clicking on liked button will unlike it and vice versa...
    private void isLiked(String poitId,ImageView imageView)
    {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(poitId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {

                        if(snapshot.child(firebaseUser.getUid()).exists())
                        {
                            imageView.setImageResource(R.drawable.ic_liked);
                            imageView.setTag("liked");
                        }
                        else
                        {
                            imageView.setImageResource(R.drawable.ic_like);
                            imageView.setTag("like");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public ImageView imgProfile;
        public ImageView postImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public ImageView more;

        public TextView username;
        public TextView noOfLikes;
        public TextView auther;
        public TextView noOfComments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.image_profile);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            more = itemView.findViewById(R.id.more);
            username = itemView.findViewById(R.id.username);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            auther = itemView.findViewById(R.id.author);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
        }


    }
}
