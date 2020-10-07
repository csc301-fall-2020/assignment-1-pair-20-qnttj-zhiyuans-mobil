package com.example.checkoutmachine.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.checkoutmachine.R;
import com.example.checkoutmachine.SQLiteDatabaseHandler;

public class deleteItemActivity extends AppCompatActivity {

    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleteitemlayout);

        db = new SQLiteDatabaseHandler(getApplicationContext());

        final EditText barCodeInput = findViewById(R.id.Barcodedelete);

        Button confirmButton = findViewById(R.id.confirmButton);
        Button backButton = findViewById(R.id.Back2Button);
        Button deleteAllButton = findViewById(R.id.deleteAllButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barCode = barCodeInput.getText().toString();
                if(db.checkbarCode(barCode)){
                    db.deleteOne(barCode);
                }else {
                    Log.e("CART", "delete nothing");
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.cleanTable();
            }
        });
    }
}
