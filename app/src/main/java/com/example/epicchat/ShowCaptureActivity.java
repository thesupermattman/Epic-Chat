package com.example.epicchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShowCaptureActivity extends AppCompatActivity
{
    Bitmap bitmap;
    String Uid;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_capture);

        progressBar = findViewById(R.id.progressBar);

        // Code to open the image in the new activity
        try
        {
            bitmap = BitmapFactory.decodeStream(getApplication().openFileInput("imageToSend"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            finish();
            return;
        }

        ImageView mImage = findViewById(R.id.imageCaptured);
        mImage.setImageBitmap(bitmap);

        Uid = FirebaseAuth.getInstance().getUid();
        ImageButton mStory = findViewById(R.id.story);
        mStory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                saveToStories();
            }
        });
    }

    private void saveToStories()
    {
        final DatabaseReference userStoryDb = FirebaseDatabase.getInstance().getReference().child("Users").child(Uid).child("story");
        final String key = userStoryDb.push().getKey();

        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("captures").child(key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] dataToUpload = baos.toByteArray();
        UploadTask uploadTask = filePath.putBytes(dataToUpload);

        progressBar.setVisibility(View.VISIBLE);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {

                Long currentTimestamp = System.currentTimeMillis();
                Long endTimestamp = currentTimestamp + (24*60*60*1000);

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
                Date date = new Date(currentTimestamp);

                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        Map newImage = new HashMap();
                        newImage.put("imageUrl", uri.toString());
                        newImage.put("timestampBeg", currentTimestamp);
                        newImage.put("timestampEnd", endTimestamp);
                        newImage.put("date", sdf.format(date));
                        userStoryDb.child(key).updateChildren(newImage);

                        finish();
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception exception)
                    {
                        progressBar.setVisibility(View.GONE);
                        finish();
                        return;
                    }
                });
            }
        });
    }
}