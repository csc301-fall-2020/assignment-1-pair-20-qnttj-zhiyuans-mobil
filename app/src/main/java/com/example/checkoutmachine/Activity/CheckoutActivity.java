package com.example.checkoutmachine.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.checkoutmachine.R;
import com.example.checkoutmachine.SQLiteDatabaseHandler;

public class CheckoutActivity extends AppCompatActivity {
    private SQLiteDatabaseHandler db;
    private String cart;
    private double total;

    String converttoString(String[] cart){
        String s = "";
        for(int i = 0; i <2000; i++) {
            if(cart[i] == null){
                return s;
            }else{
                s += cart[i] + "\n";
            }
        }
        return s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkoutlayout);

        db = new SQLiteDatabaseHandler(getApplicationContext());
        final String[] cart = new String[2000];
        final int[] index = {0};
        total = 0;

        final TextView text = findViewById(R.id.menu);
        final TextView textTotal = findViewById(R.id.total);
        final EditText barcodeinput = findViewById(R.id.customerbarcodeinput);

        Button confirmButton = findViewById(R.id.addButton);
        Button deleteButton = findViewById(R.id.removeButton);
        Button backButton = findViewById(R.id.BackButton);
        Button toPayButton = findViewById(R.id.topayButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barCode = barcodeinput.getText().toString();
                if(db.checkbarCode(barCode)){
                    String name = db.getName(barCode);
                    double price = db.getPrice(barCode);
                    total += price;
                    String s = barCode + " " + name + " " + Double.toString(price);
                    cart[index[0]]= s;
                    index[0] += 1;
                    text.setText(converttoString(cart));
                    textTotal.setText(Double.toString(total));
                }else{
                    Log.e("CART", "NOT FOUND  " + barCode);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barCode = barcodeinput.getText().toString();
                int removeIndex = -1;
                for(int i = 0; i < 2000; i ++){
                    if(cart[i] == null){
                        return;
                    }
                    String s[] = cart[i].split(" ");
                    if(s[0].equals(barCode)){
                        removeIndex = i;
                        total -= Double.valueOf(s[2]);
                        break;
                    }
                }
                if(removeIndex == -1){
                    return;
                }
                int i;
                for(i = removeIndex; i < 2000 - 1; i++){
                    if(cart[i+1] != null) {
                        cart[i] = cart[i + 1];
                    }else{
                        break;
                    }
                }
                cart[i] = null;
                index[0] -= 1;
                text.setText(converttoString(cart));
                textTotal.setText(Double.toString(total));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        toPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PayActivity.class);
                intent.putExtra("Total", total);
                startActivity(intent);
            }
        });

    }
}
