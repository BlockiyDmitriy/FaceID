package com.example.testnavigationview.ui.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testnavigationview.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CameraIdBtnActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView myImageView;
    private Button detect_btn;
    private final int Pick_image = 1;
    FloatingActionButton fab_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_id_btn);

        detect_btn = (Button) findViewById(R.id.detect_btn);
        myImageView = (ImageView) findViewById(R.id.image);
        fab_gallery = (FloatingActionButton) findViewById(R.id.fab_galary);

        detect_btn.setOnClickListener(this);
        fab_gallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_galary: {
                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, Pick_image);
            }
            case R.id.detect_btn: {

            }
        }
    }

    //Обрабатываем результат выбора в галерее:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        //myImageView.setImageBitmap(selectedImage);
                        Detected(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    public void Detected(Bitmap myBitmap){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        /*Bitmap myBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.image,
                options);*/

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
}

