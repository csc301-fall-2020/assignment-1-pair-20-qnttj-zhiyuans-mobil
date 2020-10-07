package com.example.checkoutmachine.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.checkoutmachine.R;
import com.example.checkoutmachine.SQLiteDatabaseHandler;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menulayout);
        Button back = findViewById(R.id.back4Button);

        SQLiteDatabaseHandler db = new SQLiteDatabaseHandler(getApplicationContext());
        TextView view = findViewById(R.id.sMenu);
        view.setText(db.getAllElements());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
