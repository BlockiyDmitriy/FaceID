package com.example.faceid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button faceId_btn = (Button) findViewById(R.id.faceId_btn);
        Button choiceImg_btn = (Button) findViewById(R.id.choiceImg_btn);

        faceId_btn.setOnClickListener(this);
        choiceImg_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faceId_btn: {
                ImageView myImageView = (ImageView) findViewById(R.id.image);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                Bitmap myBitmap = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(),
                        R.drawable.image,
                        options);

                Paint myRectPaint = new Paint();
                myRectPaint.setStrokeWidth(15);
                myRectPaint.setColor(Color.RED);
                myRectPaint.setStyle(Paint.Style.STROKE);

                Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas tempCanvas = new Canvas(tempBitmap);
                tempCanvas.drawBitmap(myBitmap, 0, 0, null);

                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).build();
                if (!faceDetector.isOperational()) {
                    Toast.makeText(this, "Cold not set up the Face Detector!", Toast.LENGTH_LONG).show();
                    return;
                }

                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Face> faces = faceDetector.detect(frame);

                new DrowRectangle(faces, tempCanvas, myRectPaint);

                myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
            }
            case R.id.choiceImg_btn: {
                Intent intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}