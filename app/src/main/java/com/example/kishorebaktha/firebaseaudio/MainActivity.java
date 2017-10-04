package com.example.kishorebaktha.firebaseaudio;

import android.app.ProgressDialog;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button b;
    TextView t;
    private MediaRecorder mRecorder;
    private String mFileName=null;
    private static final String LOG_TAG="Recorded log";
    private StorageReference mStorage;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b=(Button)findViewById(R.id.button2);
        t=(TextView)findViewById(R.id.textView);
        mProgressDialog=new ProgressDialog(this);
        mStorage= FirebaseStorage.getInstance().getReference();
        mFileName= Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName+="/recorded_audio.3gp";
        b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    startRecording();
                    t.setText("Recording started....");
                }
                else if(event.getAction()== MotionEvent.ACTION_UP)
                {
                    stopRecording();
                    t.setText("Recording stopped....");
                }
                return false;
            }
        });
    }
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //third generation partnership project(3g phones mostly)
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        uploadaudio();
    }

    private void uploadaudio() {
        mProgressDialog.setMessage("UPLOADING......");
        mProgressDialog.show();
        StorageReference fileparh=mStorage.child("audio").child("new audio_3gp");
        Uri uri=Uri.fromFile(new File(mFileName));
        fileparh.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgressDialog.dismiss();
                t.setText("Uploaded to database");
            }
        });

    }

}