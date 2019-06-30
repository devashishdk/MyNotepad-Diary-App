package cf.androidprojects.onlinediary;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    EditText userName, userID, userPass;
    Button signUpButton;

    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        userID = (EditText) findViewById(R.id.UserId);
        userName = (EditText) findViewById(R.id.UserName);
        userPass = (EditText) findViewById(R.id.UserPass);

        signUpButton = (Button) findViewById(R.id.SignUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int flag = 1;
                String userId = userID.getText().toString();
                String pass = userPass.getText().toString();
                pd = new ProgressDialog(SignUpActivity.this);
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(true);
                pd.setTitle("Loading....");
                pd.setMessage("Please Wait");
                pd.show();

                //register user
                registerUser(userId, pass);

            }
        });
    }

    void registerUser(String email, String password) {

        //Register user with this email and pass
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...


                    }


                });

    }

}