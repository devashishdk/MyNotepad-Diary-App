package cf.androidprojects.onlinediary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton floatingSaveButton;
    EditText post_title,post_description;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;
    ProgressDialog pd;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        setUpToolBar();

        String tile_prev = getIntent().getStringExtra("title_prev");
        String desc_prev = getIntent().getStringExtra("desc_prev");
        id = getIntent().getStringExtra("key");

        post_description = (EditText) findViewById(R.id.post_description);
        post_title = (EditText) findViewById(R.id.post_title);

        post_title.setText(tile_prev);
        post_description.setText(desc_prev);

        floatingSaveButton = (FloatingActionButton) findViewById(R.id.floatingSaveButton);
        floatingSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = new ProgressDialog(EditPostActivity.this);
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(true);
                pd.setTitle("Loading....");
                pd.setMessage("Please Wait");
                pd.show();

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                HashMap<String,String> hm = new HashMap<String, String>();
                hm.put("title",post_title.getText().toString());
                hm.put("description",post_description.getText().toString());
                hm.put("date",currentDateTimeString);
                hm.put("id",id);
                
                db.collection("Users").document(firebaseUser.getUid()).collection("Posts").document(id).set(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            pd.dismiss();
                            Intent intent = new Intent(EditPostActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            pd.dismiss();
                            Toast.makeText(EditPostActivity.this,"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
