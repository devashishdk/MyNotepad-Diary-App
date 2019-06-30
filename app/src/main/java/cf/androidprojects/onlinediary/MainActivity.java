package cf.androidprojects.onlinediary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    RecyclerView mDiaryList;
    List<Diary> DiaryList;
    DiaryAdapter DiaryAdapter;
    FirebaseUser firebaseUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton floatingPostButton;
    Toolbar toolbar;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        setUpToolBar();

        pd = new ProgressDialog(MainActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setTitle("Loading....");
        pd.setMessage("Please Wait");
        pd.show();

        floatingPostButton = (FloatingActionButton) findViewById(R.id.floatingPostButton);

        floatingPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewPostActivity.class);
                startActivity(intent);
            }
        });

        firebaseUser = mAuth.getCurrentUser();

        mDiaryList = (RecyclerView) findViewById(R.id.diary_list);
        mDiaryList.setHasFixedSize(false);
        mDiaryList.setLayoutManager(new LinearLayoutManager(this));

        DiaryList = new ArrayList<>();

        if(firebaseUser != null) {
            db.collection("Users").document(firebaseUser.getUid()).collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Diary p = doc.toObject(Diary.class);
                            DiaryList.add(p);
                        }

                        DiaryAdapter = new DiaryAdapter(MainActivity.this, DiaryList);
                        mDiaryList.setAdapter(DiaryAdapter);
                        pd.dismiss();

                    } else {
                        pd.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            Intent startIntent = new Intent(MainActivity.this,LoginActivity.class);
            if(pd.isShowing())
                pd.dismiss();
            startActivity(startIntent);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.logout):
                FirebaseAuth.getInstance().signOut();
                Intent intentL = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intentL);
                finish();
                break;
        }
        return true;
    }


    void setUpToolBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}