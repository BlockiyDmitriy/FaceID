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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.text.format.Time;

import com.example.testnavigationview.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CameraIdBtnActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView myImageView;
    private Button saveDetectImage;
    private final int Pick_image = 1;
    FloatingActionButton fab_gallery;
    String folderToSave = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_id_btn);

        saveDetectImage = (Button) findViewById(R.id.saveDetectImage_btn);
        myImageView = (ImageView) findViewById(R.id.image);
        fab_gallery = (FloatingActionButton) findViewById(R.id.fab_gallery);

        saveDetectImage.setOnClickListener(this);
        fab_gallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_gallery: {
                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, Pick_image);
            }
            case R.id.saveDetectImage_btn: {
                String res = SavePicture(myImageView, folderToSave);
                if (res == "") {
                    Snackbar.make(v, "Image saved", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
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
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        Detected(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    public void Detected(Bitmap selectedImage){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(15);
        myRectPaint.setColor(Color.RED);
        myRectPaint.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap = Bitmap.createBitmap(selectedImage.getWidth(), selectedImage.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(selectedImage, 0, 0, null);

        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).build();
        if (!faceDetector.isOperational()) {
            Toast.makeText(this, "Cold not set up the Face Detector!", Toast.LENGTH_LONG).show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(selectedImage).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        new DrowRectangle(faces, tempCanvas, myRectPaint);

        myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }

    private String SavePicture(ImageView myImageView, String folderToSave)
    {
        OutputStream fOut = null;
        Time time = new Time();
        time.setToNow();

        try {
            File file = new File(folderToSave, Integer.toString(time.year) + Integer.toString(time.month) + Integer.toString(time.monthDay) + Integer.toString(time.hour) + Integer.toString(time.minute) + Integer.toString(time.second) +".jpg"); // создать уникальное имя для файла основываясь на дате сохранения
            fOut = new FileOutputStream(file);

            BitmapDrawable bitmapDrawable = (BitmapDrawable) myImageView.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(),  file.getName()); // регистрация в фотоальбоме
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Missing image", Toast.LENGTH_SHORT).show();
            return e.getMessage();
        }
        return "";
    }
}

