package com.example.samppleimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    public Uri imguri;
    Button ch , up , show , r;
    ImageView img , dimg;
    private StorageTask mUploadTask;
    ProgressBar progressBar;
    Datas datas;
    EditText txtname ;
    DatabaseReference mDatabaseRef;
    String s;
    public static String s1;
    RatingBar ratingBar;

    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");


        ch = findViewById(R.id.button);
        up = findViewById(R.id.button2);
        show = findViewById(R.id.button3);
        img = findViewById(R.id.imageView2);
        dimg = findViewById(R.id.imageshow);
        txtname = findViewById(R.id.editText);
        ratingBar = findViewById(R.id.ratingBar);
        r = findViewById(R.id.getRating);

        progressBar  = findViewById(R.id.progressBar2);
        datas = new Datas();

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = String.valueOf(ratingBar.getRating());
                Toast.makeText(MainActivity.this,n,Toast.LENGTH_SHORT).show();


            }
        });


        s= txtname.getText().toString().trim();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("uploads");



        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUploadTask!=null && mUploadTask.isInProgress())
                {
                    Toast.makeText(MainActivity.this,"in progress",Toast.LENGTH_SHORT).show();
                }
                else
                 fileUploader();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadImage();
            }
        });

    }

    private void downloadImage() {

        StorageReference ref = mStorageRef.child("1584523672136.jpg");



        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(MainActivity.this,uri.toString(),Toast.LENGTH_SHORT).show();
                Picasso.with(MainActivity.this).load(uri.toString()).into(dimg);
            }
        });

    }


    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void fileUploader(){

        if(imguri !=null){

            final String imageid = System.currentTimeMillis()+"."+getExtension(imguri);
            final StorageReference fileReferecnce = mStorageRef.child(imageid);

            mUploadTask =  fileReferecnce.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            },3000);

                            Toast.makeText(MainActivity.this,"Uploaded",Toast.LENGTH_SHORT).show();

                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    hitesh(uri,imageid);
//
//                                     s1 = uri.toString();
//
//                                    datas.setName(txtname.getText().toString().trim());
//                                    datas.setImageid(imageid);
//                                    datas.setImageUrl(s1);
//                                    mDatabaseRef.child(txtname.getText().toString().trim()).setValue(datas);
//                                    Toast.makeText(MainActivity.this,s1,Toast.LENGTH_SHORT).show();

                                    //  datas.setImageUrl( s1);

                                }
                            });



//
//
//                            Picasso.with(MainActivity.this).load(fileReferecnce.getDownloadUrl().toString()).into(dimg);



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(MainActivity.this, exception.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(MainActivity.this,"IN progress",Toast.LENGTH_SHORT).show();


                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int)progress);
                        }
                    });

        }
        else
        {Toast.makeText(MainActivity.this,"CHoose image pls",Toast.LENGTH_SHORT).show();
        }


        /*
        datas.setName(s);
        datas.setBrand(txtbrand.getText().toString().trim());
        datas.setImageid(imageid);
        int p = Integer.parseInt(txtprice.getText().toString().trim());
        datas.setPrice(p);

        dbref.push().setValue(datas);
       */



    }//end of fileUploader

    private void hitesh(Uri uri, String imageid) {

        s1 = uri.toString();

        datas.setName(txtname.getText().toString().trim());
        datas.setImageid(imageid);
        datas.setImageUrl(s1);
        mDatabaseRef.child(txtname.getText().toString().trim()).setValue(datas);
        Toast.makeText(MainActivity.this,s1,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            imguri = data.getData();





            img.setImageURI(imguri);
        }
    }

    private void Filechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }
}
