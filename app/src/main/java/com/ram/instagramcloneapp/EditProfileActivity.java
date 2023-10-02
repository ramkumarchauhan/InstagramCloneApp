package com.ram.instagramcloneapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.ram.instagramcloneapp.Fragments.ProfileFragment;
import com.ram.instagramcloneapp.Model.User;

import java.util.HashMap;


public class EditProfileActivity extends AppCompatActivity {

    private ImageView close;
    private TextView save;
    private TextView changePhoto;
    private EditText fullName;
    private EditText username;
    private EditText bio;
    private Uri mImageUri;
    private StorageTask uploadTask;
    private StorageReference storageRef;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //init
        init();

        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user = snapshot.getValue(User.class);
                        fullName.setText(user.getName());
                        username.setText(user.getUsername());
                        bio.setText(user.getBio());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        setClose();
        setChangePhoto();
        setImageProfile();
        setSave();
    }
    private void setSave()
    {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
                startActivity(new Intent(EditProfileActivity.this, ProfileFragment.class));
            }

        });

    }

    private void UpdateProfile() {
        HashMap<String,Object> map = new HashMap<>();
       //
         map.put("username",username.getText().toString());
         map.put("name",fullName.getText().toString());
         map.put("bio",bio.getText().toString());

         FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).updateChildren(map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            uploadImage();

        }else
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    private void uploadImage() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        Toast.makeText(this, "Uploading Image", Toast.LENGTH_SHORT).show();
        pd.show();
        if(mImageUri !=null)
        {
            StorageReference fileRef = storageRef.child(System.currentTimeMillis()+".jpeg");
            uploadTask = fileRef.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }else
                    {
                        return fileRef.getDownloadUrl();
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        String uri = downloadUri.toString();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid())
                                .child("imageurl").setValue(uri);
                        pd.dismiss();
                    }else
                    {
                        Toast.makeText(EditProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            
        }else
        {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    //
    private void setImageProfile()
    {

    }

    private void setChangePhoto()
    {
        changePhoto.setOnClickListener(v -> {

        });


    }

    private void setClose()
    {
        close.setOnClickListener(v -> finish());
    }

    private void init()
    {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child("Uploads");
        close = findViewById(R.id.close);
        save = findViewById(R.id.save);
        changePhoto = findViewById(R.id.change_photo);
        fullName = findViewById(R.id.full_name);
        username = findViewById(R.id.username);
        bio = findViewById(R.id.bio);
    }

}