package org.kashmirworldfoundation.snowleopardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextView Back, Email;
    Button Submit;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Email=findViewById(R.id.EmailRecoveryInput);
        Submit=findViewById(R.id.RecoverySubmit);
        fAuth=FirebaseAuth.getInstance();
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email.getText().toString().trim();
                fAuth.sendPasswordResetEmail(Email.getText().toString().trim());
                Toast.makeText(getApplicationContext(),"Email sent to"+email+" reset Password",Toast.LENGTH_LONG).show();
            }
        });

    }
}