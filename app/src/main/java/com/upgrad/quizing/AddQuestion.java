package com.upgrad.quizing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AddQuestion extends AppCompatActivity {
    Button submit, upload,next;
    EditText option1, option2, option3, option4, questionTitle, correctOption, score;
    ImageView imageView;
    private StorageReference mStorageRef;
    Uri filePath;
    private String imageURI;
    private int PICK_IMAGE_REQUEST = 101;
    Question question;
    TextView questiontag;
    int j=0;
    ArrayList<Question> setOfQuestions;
    String quizName ="random";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        next = findViewById(R.id.next);
        option1 = findViewById(R.id.op1);
        option2 = findViewById(R.id.op2);
        option3 = findViewById(R.id.op3);
        option4 = findViewById(R.id.op4);
        questionTitle = findViewById(R.id.questiontitle);
        correctOption = findViewById(R.id.answer);
        imageView = findViewById(R.id.imageView);
        submit = findViewById(R.id.submit);
        score = findViewById(R.id.marks);
        upload = findViewById(R.id.imgbutton);
        questiontag = findViewById(R.id.questiontag);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setOfQuestions = new ArrayList<>();

        final Integer noOfQuestion = getIntent().getIntExtra("NUMBER",1);
         quizName = getIntent().getStringExtra("NAME");
         Log.d("TEST","questions are"+noOfQuestion);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (j<noOfQuestion){
                    next.setVisibility(View.VISIBLE);
                    addQuestion();

                }

                if(j==noOfQuestion){

                    next.setVisibility(View.GONE);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Quiz");
                    ScoreClass scoreClass = new ScoreClass();
                    myRef.child(quizName).setValue(setOfQuestions);
                    Toast.makeText(AddQuestion.this, "Details have been saved!",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddQuestion.this,WelcomeActivity.class);
                    startActivity(intent);

                }


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  resetViews((ViewGroup) findViewById(R.id.linear));
                  questiontag.setText("Question : "+(j+1));
                  next.setVisibility(View.GONE);


            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


    }

    private void resetViews(ViewGroup v) {
        for (int i = 0; i < v.getChildCount(); i++) {
            Object child = v.getChildAt(i);
            if(child instanceof EditText){
                EditText e = (EditText) child;
                e.setText("");
            }
            imageView.setVisibility(View.GONE);
        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    private Boolean traverseEditTexts(ViewGroup v) {
        boolean status = true;

        for (int i = 0; i < v.getChildCount(); i++) {
            Object child = v.getChildAt(i);
            if (child instanceof EditText) {
                EditText e = (EditText) child;
                if (e.getText().toString().isEmpty())    // Whatever logic here to determine if valid.
                {
                    e.setError("This field is empty !");
                    status = false;
                }

            }

        }
        return status;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final String locationString = "images/"+ UUID.randomUUID().toString();
            StorageReference ref = mStorageRef.child(locationString);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddQuestion.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            mStorageRef.child(locationString).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                 imageURI = uri.toString();
                                    Log.d("TESTING", "onComplete: Url: "+ uri.toString());

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
//
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddQuestion.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    void addQuestion(){


        if (traverseEditTexts((ViewGroup) findViewById(R.id.linear)).equals(true)) {
            question = new Question();
            question.setQuestionTitle(questionTitle.getText().toString());
            question.setQuestionScore(score.getText().toString());
            question.setOption1(option1.getText().toString());
            question.setOption2(option2.getText().toString());
            question.setOption3(option3.getText().toString());
            question.setOption4(option4.getText().toString());
            question.setQuestionImage(imageURI);
            question.setCorrectOption(correctOption.getText().toString());

            // Write a message to the database
         setOfQuestions.add(question);
         j++;
        } else {
            Toast.makeText(AddQuestion.this, "Please Fill all the details !", Toast.LENGTH_SHORT).show();
        }
    }
}
