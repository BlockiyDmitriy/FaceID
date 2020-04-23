package com.example.testnavigationview.ui.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.testnavigationview.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class CameraFragment extends Fragment implements View.OnClickListener{

    Button faceId_btn;
    Button camera_btn;

    FragmentTransaction fTrans;

    CameraFragment cameraFragment;
    private CameraViewModel cameraViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cameraViewModel = ViewModelProviders.of(this).get(CameraViewModel.class);
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
        final TextView textView = root.findViewById(R.id.text_camera);
        cameraViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        faceId_btn = (Button) root.findViewById(R.id.faceId_btn);
        camera_btn = (Button) root.findViewById(R.id.camera_btn);
        camera_btn.setOnClickListener(this);
        faceId_btn.setOnClickListener(this);
        return root;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faceId_btn: {
                ImageView myImageView = (ImageView) getView().findViewById(R.id.image);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                Bitmap myBitmap = BitmapFactory.decodeResource(
                        getActivity().getApplicationContext().getResources(),
                        R.drawable.image,
                        options);

                Paint myRectPaint = new Paint();
                myRectPaint.setStrokeWidth(15);
                myRectPaint.setColor(Color.RED);
                myRectPaint.setStyle(Paint.Style.STROKE);

                Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas tempCanvas = new Canvas(tempBitmap);
                tempCanvas.drawBitmap(myBitmap, 0, 0, null);

                FaceDetector faceDetector = new FaceDetector.Builder(getActivity().getApplicationContext()).setTrackingEnabled(false).build();
                if (!faceDetector.isOperational()) {
                    Toast.makeText(getContext(), "Cold not set up the Face Detector!", Toast.LENGTH_LONG).show();
                    return;
                }

                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Face> faces = faceDetector.detect(frame);

                new DrowRectangle(faces, tempCanvas, myRectPaint);

                myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                break;
            }
            case R.id.camera_btn: {
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.nav_host_fragment, cameraFragment);
                fTrans.commit();

                //Intent intent = new Intent(getContext(), CameraBtnFragment.class);
                //startActivity(intent);
                break;
            }
        }
    }
}
