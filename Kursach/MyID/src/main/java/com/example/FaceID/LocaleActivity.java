package com.example.FaceID;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LocaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale);

        final Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.spinner_item, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                String[] choose = getResources().getStringArray(R.array.spinner_item);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Your choice: " + choose[selectedItemPosition], Toast.LENGTH_SHORT);
                toast.show();
                changeLocale(choose[selectedItemPosition]);
                if (choose[selectedItemPosition].equals("en")) {spinner.setSelection(1);}
                if (choose[selectedItemPosition].equals("ru")) {spinner.setSelection(2);}
            }
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
    @SuppressWarnings("deprecation")
    private void changeLocale(String choose)
    {
        Locale locale = new Locale(choose);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, null);

        setTitle(R.string.choice_language);
        setTitle(R.string.app_name);
        setTitle(R.string.navigation_drawer_open);
        setTitle(R.string.navigation_drawer_close);
        setTitle(R.string.nav_header_title);
        setTitle(R.string.nav_header_subtitle);
        setTitle(R.string.nav_header_desc);
        setTitle(R.string.action_settings);
        setTitle(R.string.menu_home);
        setTitle(R.string.menu_gallery);
        setTitle(R.string.menu_slideshow);
        setTitle(R.string.menu_camera);
        setTitle(R.string.home_second);
        setTitle(R.string.nav_header_name);
        setTitle(R.string.hello_blank_fragment);
        setTitle(R.string.open_camera);
    }
}
