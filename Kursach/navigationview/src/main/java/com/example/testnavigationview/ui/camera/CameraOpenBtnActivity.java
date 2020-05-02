package com.example.testnavigationview.ui.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.testnavigationview.R;

import java.io.File;

public class CameraOpenBtnActivity extends AppCompatActivity {

    private Button buttonImage;
    private Button buttonVideo;

    private VideoView videoView;
    private ImageView imageView;

    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_ID_VIDEO_CAPTURE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_open_btn);

        this.buttonImage = (Button) this.findViewById(R.id.button_image);
        this.buttonVideo = (Button) this.findViewById(R.id.button_video);
        this.videoView = (VideoView) this.findViewById(R.id.videoView);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);

        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        this.buttonVideo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissionAndCaptureVideo();
            }
        });
    }
    private void captureImage() {
        // Создайте неявное намерение для захвата изображения.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Запустите камеру и дождитесь результатов.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    private void askPermissionAndCaptureVideo() {

        // With Android Level >= 23, you have to ask the user
        // for permission to read/write data on the device.
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have read/write permission
            int readPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (writePermission != PackageManager.PERMISSION_GRANTED ||
                    readPermission != PackageManager.PERMISSION_GRANTED) {
                //Если у вас нет разрешения, подскажите пользователю.
                this.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_ID_READ_WRITE_PERMISSION
                );
                return;
            }
        }
        this.captureVideo();
    }

    private void captureVideo() {
        try {
            // Создать неявное намерение для захвата видео.
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            //Каталог внешнего хранилища.
            File dir = Environment.getExternalStorageDirectory();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // file:///storage/emulated/0/myvideo.mp4
            String savePath = dir.getAbsolutePath() + "/myvideo.mp4";
            File videoFile = new File(savePath);
            Uri videoUri = Uri.fromFile(videoFile);

            //Укажите, где сохранять видеофайлы.
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // ================================================================================================
            // To Fix Error (**)
            // ================================================================================================

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            // ================================================================================================
            // You may get an Error (**) If your app targets API 24+
            // "android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through.."
            //  Explanation: https://stackoverflow.com/questions/38200282
            // ================================================================================================

            //Запустите камеру и дождитесь результатов.
            this.startActivityForResult(intent, REQUEST_ID_VIDEO_CAPTURE); // (**)

        } catch(Exception e)  {
            Toast.makeText(this, "Error capture video: " +e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    //Когда у вас есть результаты запроса
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_READ_WRITE_PERMISSION: {
                // Запись: Если запрос отменен, результирующие массивы пусты.
                //Разрешения предоставлены (read/write).
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                    this.captureVideo();
                }
                // Отменено или отказано.
                else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    //Когда результаты вернулись
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.imageView.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_ID_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
                Log.i("MyLog", "Video saved to: " + videoUri);
                Toast.makeText(this, "Video saved to:\n" +
                        videoUri, Toast.LENGTH_LONG).show();
                this.videoView.setVideoURI(videoUri);
                this.videoView.start();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
