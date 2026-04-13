package com.example.q5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText mEditTextTo, mEditTextCc, mEditTextBcc, mEditTextSubject, mEditTextMessage;
    private TextInputLayout mLayoutTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextTo = findViewById(R.id.edit_text_to);
        mEditTextCc = findViewById(R.id.edit_text_cc);
        mEditTextBcc = findViewById(R.id.edit_text_bcc);
        mEditTextSubject = findViewById(R.id.edit_text_subject);
        mEditTextMessage = findViewById(R.id.edit_text_message);
        mLayoutTo = findViewById(R.id.layout_to);

        MaterialButton buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(v -> sendMail());

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) return;

        String action = intent.getAction();
        Uri data = intent.getData();

        if (Intent.ACTION_SENDTO.equals(action) && data != null && "mailto".equals(data.getScheme())) {
            String email = data.getSchemeSpecificPart();
            mEditTextTo.setText(email);
            
            if (intent.hasExtra(Intent.EXTRA_CC)) {
                mEditTextCc.setText(TextUtils.join(",", intent.getStringArrayExtra(Intent.EXTRA_CC)));
            }
            if (intent.hasExtra(Intent.EXTRA_BCC)) {
                mEditTextBcc.setText(TextUtils.join(",", intent.getStringArrayExtra(Intent.EXTRA_BCC)));
            }

            String subject = intent.getStringExtra(Intent.EXTRA_SUBJECT);
            if (subject != null) mEditTextSubject.setText(subject);
            
            String body = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (body != null) mEditTextMessage.setText(body);
            
            Toast.makeText(this, getString(R.string.toast_received, email), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMail() {
        String recipientList = mEditTextTo.getText().toString().trim();
        if (TextUtils.isEmpty(recipientList)) {
            mLayoutTo.setError(getString(R.string.error_empty_recipient));
            return;
        } else {
            mLayoutTo.setError(null);
        }

        String subject = mEditTextSubject.getText().toString();
        String message = mEditTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, splitEmails(recipientList));
        intent.putExtra(Intent.EXTRA_CC, splitEmails(mEditTextCc.getText().toString().trim()));
        intent.putExtra(Intent.EXTRA_BCC, splitEmails(mEditTextBcc.getText().toString().trim()));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.error_no_email_client), Toast.LENGTH_SHORT).show();
        }
    }

    private String[] splitEmails(String emailString) {
        if (TextUtils.isEmpty(emailString)) return new String[0];
        String[] emails = emailString.split(",");
        for (int i = 0; i < emails.length; i++) {
            emails[i] = emails[i].trim();
        }
        return emails;
    }
}