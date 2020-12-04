package com.example.gmaillayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ComposeActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView emailAddress, subject;
    private EditText emailContent;
    private Button sendBtn, cancelBtn;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        //
        emailAddress = findViewById(R.id.email_address);
        subject = findViewById(R.id.subject);
        emailContent = findViewById(R.id.email_content);
        cancelBtn = findViewById(R.id.cancel_button);
        sendBtn = findViewById(R.id.send_button);
        //
        emailAddress.append(bundle.getString("email_address"));
        //
        cancelBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        if(id == R.id.send_button||id == R.id.cancel_button){
            Bundle bundle = new Bundle();
            if(v.getId() == R.id.send_button){
                bundle.putString("result", "Reply sent");
                bundle.putString("email_address", emailAddress.getText().subSequence(4, emailAddress
                        .getText().length()) + "");
                bundle.putString("email_content",
                                 subject.getText().toString() + "\n" + emailContent.getText() + "");
                bundle.putBoolean("new_outbox", true);
            }else if(v.getId() == R.id.cancel_button){
                bundle.putString("result", "Reply cancelled");
                bundle.putBoolean("new_outbox", false);
            }
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}