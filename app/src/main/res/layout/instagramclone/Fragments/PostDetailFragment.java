package com.mrash.instagramclone.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mrash.instagramclone.Adapter.PostAdapter;
import com.mrash.instagramclone.Model.Post;
import com.mrash.instagramclone.R;

import java.util.ArrayList;
import java.util.List;


public class PostDetailFragment extends Fragment {

    private String postId;
    private String profileId;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        //here i am just setting single post when user clicked on image of that post
        // ->calling from Post Adapter
        //by the time if idea changes then we can add and show all posts of one user or something other
        postId = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                .getString("postid","none");

        //attach RecyclerView of Fragment_post_detail
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();

        //calling to post adapter again and setting that single post
        postAdapter = new PostAdapter(getContext(),postList);

        recyclerView.setAdapter(postAdapter);

        //getting that post id where user clicked on post activity
        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postList.clear();
                        postList.add(snapshot.getValue(Post.class));
                        postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        return view;
    }


}