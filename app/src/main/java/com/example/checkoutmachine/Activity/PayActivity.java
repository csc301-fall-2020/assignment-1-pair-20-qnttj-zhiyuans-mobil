package com.example.checkoutmachine.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.checkoutmachine.R;

public class PayActivity extends AppCompatActivity {

    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paylayout);

        Intent mIntent = getIntent();
        total = mIntent.getDoubleExtra("Total", 0.0);

        final EditText tax = findViewById(R.id.discount);
        final EditText discount = findViewById(R.id.tax);
        final TextView text = findViewById(R.id.paytotal);
        Button confirm = findViewById(R.id.confirm3);
        Button back = findViewById(R.id.back3Button);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double taxValue;
                String s = tax.getText().toString();
                Log.e("CARTT" , s);
                if(s.isEmpty()){
                    taxValue = 0.0;
                }else{
                    taxValue = Double.valueOf(s);
                }

                double discountValue;
                s = discount.getText().toString();
                if(s.isEmpty()){
                    discountValue = 0.0;
                }else {
                    discountValue = Double.valueOf(s);
                }
                total = total * (1 + taxValue/100) * (1 - discountValue/100);
                text.setText("Your total is " + Double.toString(total));
            }
        }
        );

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }
}
