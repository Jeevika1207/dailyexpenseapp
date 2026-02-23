package com.example.dailyexpenseapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText etAmount, etDesc;
    Spinner spnCategory;
    RadioGroup rgPayment;
    ListView lvExpenses;
    Button btnAdd, btnCall, btnSMS, btnEmail;

    ArrayList<String> expenseList;
    ArrayAdapter<String> adapter;
    String accountantPhone = "9876543210"; // Replace with actual number if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etAmount = findViewById(R.id.etAmount);
        etDesc = findViewById(R.id.etDesc);
        spnCategory = findViewById(R.id.spnCategory);
        rgPayment = findViewById(R.id.rgPayment);
        lvExpenses = findViewById(R.id.lvExpenses);
        btnAdd = findViewById(R.id.btnAdd);
        btnCall = findViewById(R.id.btnCall);
        btnSMS = findViewById(R.id.btnSMS);
        btnEmail = findViewById(R.id.btnEmail);


        String[] categories = {"Office Supplies", "Travel / Taxi", "Client Meeting", "Printing / Xerox", "Internet / Phone Bill"};
        spnCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));


        expenseList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        lvExpenses.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String amount = etAmount.getText().toString();
            String desc = etDesc.getText().toString();
            String category = spnCategory.getSelectedItem().toString();

            if(!amount.isEmpty()) {
                String entry = desc + " (" + category + ") - $" + amount;
                expenseList.add(entry);
                adapter.notifyDataSetChanged();

                etAmount.setText("");
                etDesc.setText("");
                Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();
            } else {
                etAmount.setError("Please enter an amount");
            }
        });

        btnCall.setOnClickListener(v -> showConfirmDialog("Call", () -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + accountantPhone));
            startActivity(callIntent);
        }));

        btnSMS.setOnClickListener(v -> showConfirmDialog("SMS", () -> {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + accountantPhone));
            smsIntent.putExtra("sms_body", "New expense update from Reg No: 732923ITR045");
            startActivity(smsIntent);
        }));

        btnEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:accountant@example.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Expense Report - 732923ITR045");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Expense List:\n" + expenseList.toString());
            startActivity(emailIntent);
        });
    }

    private void showConfirmDialog(String actionType, Runnable onConfirm) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm " + actionType)
                .setMessage("Do you want to " + actionType.toLowerCase() + " the accountant?")
                .setPositiveButton("Yes", (dialog, which) -> onConfirm.run())
                .setNegativeButton("No", null)
                .show();
    }
}