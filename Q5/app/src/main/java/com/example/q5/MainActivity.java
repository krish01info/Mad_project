package com.example.q5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextTo = findViewById(R.id.edit_text_to);
        mEditTextSubject = findViewById(R.id.edit_text_subject);
        mEditTextMessage = findViewById(R.id.edit_text_message);

        Button buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        // Check if the activity was started with an email intent (demonstrating "receiving" an intent)
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) return;

        String action = intent.getAction();
        Uri data = intent.getData();

        if (Intent.ACTION_SENDTO.equals(action) && data != null && "mailto".equals(data.getScheme())) {
            String email = data.getSchemeSpecificPart();
            mEditTextTo.setText(email);
            
            String subject = intent.getStringExtra(Intent.EXTRA_SUBJECT);
            if (subject != null) mEditTextSubject.setText(subject);
            
            String body = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (body != null) mEditTextMessage.setText(body);
            
            Toast.makeText(this, "Received email intent for: " + email, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMail() {
        String recipientList = mEditTextTo.getText().toString().trim();
        if (recipientList.isEmpty()) {
            Toast.makeText(this, "Please enter at least one recipient", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] recipients = recipientList.split(",");
        for (int i = 0; i < recipients.length; i++) {
            recipients[i] = recipients[i].trim();
        }

        String subject = mEditTextSubject.getText().toString();
        String message = mEditTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Fallback if no specific mailto handler is found
            Intent backupIntent = new Intent(Intent.ACTION_SEND);
            backupIntent.setType("message/rfc822");
            backupIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
            backupIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            backupIntent.putExtra(Intent.EXTRA_TEXT, message);
            try {
                startActivity(Intent.createChooser(backupIntent, "Send Email"));
            } catch (Exception e) {
                Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}