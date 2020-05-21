package com.example.FaceID.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.FaceID.R;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        GridView gridView = (GridView) findViewById(R.id.grid_view);

        // устанавливаем адаптер через экземпляр класса ImageAdapter
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // посылаем идентификатор картинки в FullScreenActivity
                Intent i = new Intent(getApplicationContext(),
                        FullImageActivity.class);
                // передаем индекс массива
                i.putExtra("id", position);
                startActivity(i);
            }
        });
    }
}
