package com.example.checkoutmachine.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.checkoutmachine.R;
import com.example.checkoutmachine.SQLiteDatabaseHandler;

public class AddtoStoreActivity extends AppCompatActivity {

    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlayout);

        db = new SQLiteDatabaseHandler(getApplicationContext());

        final EditText nameInput = findViewById(R.id.NameInput);
        final EditText barCodeInput = findViewById(R.id.barCodeInput);
        final EditText priceInput = findViewById(R.id.priceInput);

        Button confirmButton = findViewById(R.id.confButton);
        Button backButton = findViewById(R.id.Back1Button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "";
                name = nameInput.getText().toString();
                String barCode = "";
                barCode = barCodeInput.getText().toString();
                Double price = 0.0;
                if(!priceInput.getText().toString().isEmpty()){
                    price = Double.valueOf(priceInput.getText().toString());
                }
                Context context = getApplicationContext();
                if(db.checkbarCode(barCode)){
                    db.updateItem(barCode, name, price);
                    Log.e("CART", Double.toString(db.getPrice(barCode)));
                }else {
                    db.addItem(barCode, name, price);
                    Log.e("CART", Double.toString(db.getPrice(barCode)));
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
    }
}

