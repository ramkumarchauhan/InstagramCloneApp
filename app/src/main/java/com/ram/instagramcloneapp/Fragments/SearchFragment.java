package com.ram.instagramcloneapp.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.ram.libray.SocialAutoCompleteTextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ram.instagramcloneapp.Adapter.TagAdapter;
import com.ram.instagramcloneapp.Adapter.UserAdapter;
import com.ram.instagramcloneapp.Model.User;
import com.ram.instagramcloneapp.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> mUsers;
    private UserAdapter userAdapter;

    private RecyclerView recyclerViewTags;
    private List<String> mHashTags;
    private List<String> mHashTagsCount;
    private TagAdapter tagAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewTags = view.findViewById(R.id.recycler_view_tags);
        recyclerViewTags.setHasFixedSize(true);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(getContext()));

        mHashTags = new ArrayList<>();

        mHashTagsCount = new ArrayList<>();

        tagAdapter = new TagAdapter(getContext(),mHashTags,mHashTagsCount);

        mUsers = new ArrayList<>();

        userAdapter = new UserAdapter(getContext(),mUsers,true);
        recyclerView.setAdapter(userAdapter);

        recyclerViewTags.setAdapter(tagAdapter);

        readTags();

        return view;
    }

    private void readTags() {

        //reading tags and setting it on tags rv

        FirebaseDatabase.getInstance().getReference().child("HashTags")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        mHashTags.clear();
                        mHashTagsCount.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            mHashTags.add(dataSnapshot.getKey()); //here key is hashtag
                            //getting that hashtag count
                            mHashTagsCount.add(dataSnapshot.getChildrenCount()+"");
                        }
                        tagAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

//    private void readUsers() {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(TextUtils.isEmpty(searchBar.getText().toString().trim()))
//                {
//                    mUsers.clear();
//                    for (DataSnapshot dataSnapshot: snapshot.getChildren())
//                    {
//                        User user = dataSnapshot.getValue(User.class);
//                        mUsers.add(user);
//                    }
//                    userAdapter.notifyDataSetChanged();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    //search user
    private void searchUser(String keySearch)
    {
        //Query is subclass of Firebase Database Reference
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username")
                .startAt(keySearch).endAt(keySearch + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    User user = dataSnapshot.getValue(User.class);
                    mUsers.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //search tags and after searching filter that tag and set on adapter
    private void filter(String text)
    {
        List<String> mSearchTags = new ArrayList<>();
        List<String> mSearchTagsCount = new ArrayList<>();
        for(String s:mHashTags)
        {
            if(s.toLowerCase().contains(text.toLowerCase()))
            {
                mSearchTags.add(s);
                mSearchTagsCount.add(mHashTagsCount.get(mHashTags.indexOf(s)));
            }
        }
        tagAdapter.filter(mSearchTags,mSearchTagsCount);

    }
}