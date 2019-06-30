package cf.androidprojects.onlinediary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostDisplayActivity extends AppCompatActivity {

    TextView title,description;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;
    Toolbar toolbar;
    ProgressDialog pd;
    FloatingActionButton floatingEditButton,floatingDeleteButton;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_display);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        pd = new ProgressDialog(PostDisplayActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();

        floatingEditButton = (FloatingActionButton) findViewById(R.id.floatingEditButton);
        floatingDeleteButton = (FloatingActionButton) findViewById(R.id.floatingDeleteButton);

        setUpToolBar();
        id = getIntent().getStringExtra("Key");

        title = (TextView) findViewById(R.id.title_text);
        description = (TextView) findViewById(R.id.description_text);

        db.collection("Users").document(firebaseUser.getUid()).collection("Posts").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                title.setText(documentSnapshot.get("title").toString());
                description.setText(documentSnapshot.get("description").toString());
                pd.dismiss();
            }
        });

        floatingEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDisplayActivity.this,EditPostActivity.class);
                intent.putExtra("title_prev",title.getText().toString());
                intent.putExtra("desc_prev",description.getText().toString());
                intent.putExtra("key",id);
                startActivity(intent);
                finish();
            }
        });

        floatingDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                db.collection("Users").document(firebaseUser.getUid()).collection("Posts").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Intent intent = new Intent(PostDisplayActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
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
