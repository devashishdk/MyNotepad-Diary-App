package cf.androidprojects.onlinediary;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context mCtx;
    List<Diary> DiaryList;

    DiaryAdapter(Context mCtx, List<Diary> DiaryList)
    {
        this.mCtx = mCtx;
        this.DiaryList = DiaryList;
    }
    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.single_post,
                parent, false);
        DiaryViewHolder DiaryViewHolder = new DiaryViewHolder(view);
        return DiaryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DiaryViewHolder holder, final int position) {
        final Diary diary = DiaryList.get(position);
        holder.title.setText(diary.getTitle());
        holder.description.setText(diary.getDescription());
        holder.date.setText(diary.getDate());

        final String PushId = DiaryList.get(position).getId();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cardView.setClickable(false);
                Intent mIntent = new Intent(mCtx, PostDisplayActivity.class);
                mIntent.putExtra("Key", PushId);
                mCtx.startActivity(mIntent);
                holder.cardView.setClickable(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return DiaryList.size();
    }

    class DiaryViewHolder extends RecyclerView.ViewHolder
    {
        TextView title,description,date;
        CardView cardView;
        public DiaryViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.post_title);
            date = (TextView) itemView.findViewById(R.id.post_date);
            description = (TextView) itemView.findViewById(R.id.post_description);
            cardView = (CardView) itemView.findViewById(R.id.card_layout);
        }
    }
}
