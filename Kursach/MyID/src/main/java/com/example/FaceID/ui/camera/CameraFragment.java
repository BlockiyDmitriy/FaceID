package com.example.FaceID.ui.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.FaceID.R;

public class CameraFragment extends Fragment implements View.OnClickListener {

    Button faceId_btn;
    Button camera_btn;

    private CameraViewModel cameraViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
        cameraViewModel = ViewModelProviders.of(this).get(CameraViewModel.class);

        faceId_btn = (Button) root.findViewById(R.id.faceId_btn);
        camera_btn = (Button) root.findViewById(R.id.camera_btn);
        camera_btn.setOnClickListener(this);
        faceId_btn.setOnClickListener(this);

        return root;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.faceId_btn: {
                Intent intent = new Intent(getContext(), CameraIdBtnActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.camera_btn: {
                Intent intent = new Intent(getContext(), CameraOpenBtnActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
