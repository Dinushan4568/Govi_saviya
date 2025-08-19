package com.s23010535.govisaviya;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.*;

public class ExpertAdviceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_advice);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.adviceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ExpertAdvice> adviceList = getSampleAdvice();
        recyclerView.setAdapter(new AdviceAdapter(adviceList));

        FloatingActionButton fab = findViewById(R.id.askQuestionFab);
        fab.setOnClickListener(v -> Toast.makeText(this, "Ask a Question feature coming soon!", Toast.LENGTH_SHORT).show());
    }

    private List<ExpertAdvice> getSampleAdvice() {
        List<ExpertAdvice> list = new ArrayList<>();
        list.add(new ExpertAdvice("How to prevent rice blast disease?", "Keep your fields well-drained and use resistant varieties.", "Dr. S. Perera", R.drawable.ic_user_placeholder));
        list.add(new ExpertAdvice("Best fertilizer for tea plants?", "Use a balanced NPK fertilizer and organic compost.", "Prof. A. Fernando", R.drawable.ic_user_placeholder));
        list.add(new ExpertAdvice("How to control pests organically?", "Introduce natural predators and use neem oil spray.", "Dr. K. Silva", R.drawable.ic_user_placeholder));
        return list;
    }

    static class ExpertAdvice {
        String title;
        String summary;
        String expertName;
        int expertPhotoRes;
        ExpertAdvice(String t, String s, String n, int p) {
            title = t;
            summary = s;
            expertName = n;
            expertPhotoRes = p;
        }
    }

    static class AdviceAdapter extends RecyclerView.Adapter<AdviceAdapter.AdviceViewHolder> {
        List<ExpertAdvice> adviceList;
        AdviceAdapter(List<ExpertAdvice> list) { adviceList = list; }
        @NonNull
        @Override
        public AdviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expert_advice, parent, false);
            return new AdviceViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull AdviceViewHolder holder, int position) {
            ExpertAdvice advice = adviceList.get(position);
            holder.title.setText(advice.title);
            holder.summary.setText(advice.summary);
            holder.expertName.setText(advice.expertName);
            holder.photo.setImageResource(advice.expertPhotoRes);
        }
        @Override
        public int getItemCount() { return adviceList.size(); }
        static class AdviceViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView summary;
            TextView expertName;
            ImageView photo;
            AdviceViewHolder(View v) {
                super(v);
                title = v.findViewById(R.id.adviceTitle);
                summary = v.findViewById(R.id.adviceSummary);
                expertName = v.findViewById(R.id.expertName);
                photo = v.findViewById(R.id.expertPhoto);
            }
        }
    }
}

