package com.s23010535.govisaviya;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.*;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.graphics.Color;

public class FindWorkersActivity extends Activity {
    
    // UI Components
    private ImageButton backButton, filterButton;
    private EditText searchEditText;
    private TextView allWorkersChip, plantingChip, harvestingChip, irrigationChip;
    private TextView availableWorkersText, activeJobsText, avgRatingText;
    private TextView sortButton;
    private RecyclerView workersRecyclerView;
    private FloatingActionButton postJobButton;

    // Data
    private List<Worker> allWorkers;
    private List<Worker> filteredWorkers;
    private WorkerAdapter adapter;
    private String currentFilter = "All";
    private String currentSort = "Rating";

    private static final String PREFS_NAME = "find_workers_prefs";
    private static final String KEY_WORKERS = "workers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_workers);
        
        initializeViews();
        initializeData();
        setupClickListeners();
        setupSearch();
        loadData();
        updateStats();
        setupRecyclerView();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        filterButton = findViewById(R.id.filterButton);
        searchEditText = findViewById(R.id.searchEditText);
        allWorkersChip = findViewById(R.id.allWorkersChip);
        plantingChip = findViewById(R.id.plantingChip);
        harvestingChip = findViewById(R.id.harvestingChip);
        irrigationChip = findViewById(R.id.irrigationChip);
        availableWorkersText = findViewById(R.id.availableWorkersText);
        activeJobsText = findViewById(R.id.activeJobsText);
        avgRatingText = findViewById(R.id.avgRatingText);
        sortButton = findViewById(R.id.sortButton);
        workersRecyclerView = findViewById(R.id.workersRecyclerView);
        postJobButton = findViewById(R.id.postJobButton);
    }

    private void initializeData() {
        allWorkers = new ArrayList<>();
        filteredWorkers = new ArrayList<>();
        
        // Add sample workers
        addSampleWorkers();
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        filterButton.setOnClickListener(v -> showFilterDialog());
        postJobButton.setOnClickListener(v -> showPostJobDialog());
        sortButton.setOnClickListener(v -> showSortDialog());
        
        // Filter chips
        allWorkersChip.setOnClickListener(v -> setFilter("All"));
        plantingChip.setOnClickListener(v -> setFilter("Planting"));
        harvestingChip.setOnClickListener(v -> setFilter("Harvesting"));
        irrigationChip.setOnClickListener(v -> setFilter("Irrigation"));
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterWorkers();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new WorkerAdapter(filteredWorkers);
        workersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workersRecyclerView.setAdapter(adapter);
        filterWorkers();
    }

    private void addSampleWorkers() {
        allWorkers.add(new Worker(
            "John Smith",
            "Colombo, Sri Lanka",
            Arrays.asList("Planting", "Harvesting", "Irrigation"),
            4.8f,
            5,
            800,
            "Experienced farmer with 5 years in organic farming"
        ));
        
        allWorkers.add(new Worker(
            "Maria Silva",
            "Kandy, Sri Lanka",
            Arrays.asList("Planting", "Fertilization"),
            4.6f,
            3,
            750,
            "Specialized in vegetable farming and crop management"
        ));
        
        allWorkers.add(new Worker(
            "Raj Kumar",
            "Galle, Sri Lanka",
            Arrays.asList("Harvesting", "Irrigation", "Pest Control"),
            4.9f,
            7,
            900,
            "Expert in rice farming and water management"
        ));
        
        allWorkers.add(new Worker(
            "Priya Fernando",
            "Jaffna, Sri Lanka",
            Arrays.asList("Planting", "Harvesting"),
            4.7f,
            4,
            850,
            "Skilled in fruit farming and orchard management"
        ));
        
        allWorkers.add(new Worker(
            "David Wilson",
            "Anuradhapura, Sri Lanka",
            Arrays.asList("Irrigation", "Equipment Maintenance"),
            4.5f,
            6,
            950,
            "Mechanical expert with farming equipment experience"
        ));
        
        allWorkers.add(new Worker(
            "Lakshmi Devi",
            "Trincomalee, Sri Lanka",
            Arrays.asList("Planting", "Harvesting", "Fertilization"),
            4.8f,
            8,
            1000,
            "Senior farmer with extensive knowledge in multiple crops"
        ));
    }

    private void setFilter(String filter) {
        currentFilter = filter;
        updateFilterChips();
        filterWorkers();
    }

    private void updateFilterChips() {
        // Reset all chips
        allWorkersChip.setBackgroundResource(R.drawable.chip_background);
        allWorkersChip.setTextColor(Color.parseColor("#666666"));
        plantingChip.setBackgroundResource(R.drawable.chip_background);
        plantingChip.setTextColor(Color.parseColor("#666666"));
        harvestingChip.setBackgroundResource(R.drawable.chip_background);
        harvestingChip.setTextColor(Color.parseColor("#666666"));
        irrigationChip.setBackgroundResource(R.drawable.chip_background);
        irrigationChip.setTextColor(Color.parseColor("#666666"));
        
        // Set selected chip
        switch (currentFilter) {
            case "All":
                allWorkersChip.setBackgroundResource(R.drawable.chip_selected_background);
                allWorkersChip.setTextColor(Color.WHITE);
                break;
            case "Planting":
                plantingChip.setBackgroundResource(R.drawable.chip_selected_background);
                plantingChip.setTextColor(Color.WHITE);
                break;
            case "Harvesting":
                harvestingChip.setBackgroundResource(R.drawable.chip_selected_background);
                harvestingChip.setTextColor(Color.WHITE);
                break;
            case "Irrigation":
                irrigationChip.setBackgroundResource(R.drawable.chip_selected_background);
                irrigationChip.setTextColor(Color.WHITE);
                break;
        }
    }

    private void filterWorkers() {
        filteredWorkers.clear();
        String searchQuery = searchEditText.getText().toString().toLowerCase();
        
        for (Worker worker : allWorkers) {
            boolean matchesFilter = currentFilter.equals("All") || 
                                  worker.skills.contains(currentFilter);
            
            boolean matchesSearch = searchQuery.isEmpty() ||
                                  worker.name.toLowerCase().contains(searchQuery) ||
                                  worker.location.toLowerCase().contains(searchQuery) ||
                                  worker.description.toLowerCase().contains(searchQuery) ||
                                  worker.skills.stream().anyMatch(skill -> 
                                      skill.toLowerCase().contains(searchQuery));
            
            if (matchesFilter && matchesSearch) {
                filteredWorkers.add(worker);
            }
        }
        
        sortWorkers();
        adapter.notifyDataSetChanged();
        updateStats();
    }

    private void sortWorkers() {
        switch (currentSort) {
            case "Rating":
                filteredWorkers.sort((w1, w2) -> Float.compare(w2.rating, w1.rating));
                break;
            case "Experience":
                filteredWorkers.sort((w1, w2) -> Integer.compare(w2.experience, w1.experience));
                break;
            case "Rate":
                filteredWorkers.sort((w1, w2) -> Integer.compare(w1.hourlyRate, w2.hourlyRate));
                break;
            case "Name":
                filteredWorkers.sort((w1, w2) -> w1.name.compareTo(w2.name));
                break;
        }
    }

    private void updateStats() {
        availableWorkersText.setText(String.valueOf(filteredWorkers.size()));
        
        // Calculate average rating
        if (!filteredWorkers.isEmpty()) {
            float avgRating = 0;
            for (Worker worker : filteredWorkers) {
                avgRating += worker.rating;
            }
            avgRating /= filteredWorkers.size();
            avgRatingText.setText(String.format("%.1f", avgRating));
        } else {
            avgRatingText.setText("0.0");
        }
        
        // Simulate active jobs
        activeJobsText.setText(String.valueOf(new Random().nextInt(20) + 5));
    }

    private void showFilterDialog() {
        String[] filterOptions = {"All Skills", "Planting", "Harvesting", "Irrigation", "Fertilization", "Pest Control", "Equipment Maintenance"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter by Skills");
        builder.setItems(filterOptions, (dialog, which) -> {
            String selectedFilter = filterOptions[which];
            if (selectedFilter.equals("All Skills")) {
                setFilter("All");
            } else {
                setFilter(selectedFilter);
            }
        });
        builder.show();
    }

    private void showSortDialog() {
        String[] sortOptions = {"Rating", "Experience", "Rate (Low to High)", "Name"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort Workers");
        builder.setItems(sortOptions, (dialog, which) -> {
            switch (which) {
                case 0:
                    currentSort = "Rating";
                    break;
                case 1:
                    currentSort = "Experience";
                    break;
                case 2:
                    currentSort = "Rate";
                    break;
                case 3:
                    currentSort = "Name";
                    break;
            }
            sortButton.setText("Sort by: " + sortOptions[which]);
            sortWorkers();
            adapter.notifyDataSetChanged();
        });
        builder.show();
    }

    private void showPostJobDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Post a Job");
        builder.setMessage("Would you like to post a job to find workers?");
        
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Navigate to job posting activity or show job form
            Toast.makeText(this, "Job posting feature coming soon!", Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String workersJson = prefs.getString(KEY_WORKERS, null);
        
        if (workersJson != null) {
            try {
                JSONArray workersArray = new JSONArray(workersJson);
                allWorkers.clear();
                for (int i = 0; i < workersArray.length(); i++) {
                    allWorkers.add(Worker.fromJson(workersArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                // Use sample data if loading fails
            }
        }
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        try {
            JSONArray workersArray = new JSONArray();
            for (Worker worker : allWorkers) {
                workersArray.put(worker.toJson());
            }
            
            prefs.edit()
                .putString(KEY_WORKERS, workersArray.toString())
                .apply();
                
        } catch (JSONException e) {
            // Handle error
        }
    }

    // Worker Data Class
    public static class Worker {
        public String name;
        public String location;
        public List<String> skills;
        public float rating;
        public int experience;
        public int hourlyRate;
        public String description;

        public Worker(String name, String location, List<String> skills, float rating, int experience, int hourlyRate, String description) {
            this.name = name;
            this.location = location;
            this.skills = skills;
            this.rating = rating;
            this.experience = experience;
            this.hourlyRate = hourlyRate;
            this.description = description;
        }

        public JSONObject toJson() throws JSONException {
            JSONObject obj = new JSONObject();
            obj.put("name", name);
            obj.put("location", location);
            obj.put("skills", new JSONArray(skills));
            obj.put("rating", rating);
            obj.put("experience", experience);
            obj.put("hourlyRate", hourlyRate);
            obj.put("description", description);
            return obj;
        }

        public static Worker fromJson(JSONObject obj) throws JSONException {
            List<String> skills = new ArrayList<>();
            JSONArray skillsArray = obj.getJSONArray("skills");
            for (int i = 0; i < skillsArray.length(); i++) {
                skills.add(skillsArray.getString(i));
            }
            
            return new Worker(
                obj.getString("name"),
                obj.getString("location"),
                skills,
                (float) obj.getDouble("rating"),
                obj.getInt("experience"),
                obj.getInt("hourlyRate"),
                obj.getString("description")
            );
        }
    }

    // Worker Adapter
    private class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder> {
        private List<Worker> workers;

        public WorkerAdapter(List<Worker> workers) {
            this.workers = workers;
        }

        @Override
        public WorkerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_worker, parent, false);
            return new WorkerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(WorkerViewHolder holder, int position) {
            Worker worker = workers.get(position);
            
            holder.workerName.setText(worker.name);
            holder.workerLocation.setText(worker.location);
            holder.workerRating.setText(String.format("%.1f", worker.rating));
            holder.workerExperience.setText(worker.experience + " years exp");
            holder.workerRate.setText("Rs. " + worker.hourlyRate + "/hour");
            
            // Set skills text
            StringBuilder skillsText = new StringBuilder();
            for (int i = 0; i < worker.skills.size(); i++) {
                String skill = worker.skills.get(i);
                String emoji = getSkillEmoji(skill);
                skillsText.append(emoji).append(" ").append(skill);
                if (i < worker.skills.size() - 1) {
                    skillsText.append(" • ");
                }
            }
            holder.workerSkills.setText(skillsText.toString());
            
            // Set up button click listeners
            holder.hireButton.setOnClickListener(v -> showHireDialog(worker));
            holder.viewProfileButton.setOnClickListener(v -> showWorkerProfile(worker));
        }

        @Override
        public int getItemCount() {
            return workers.size();
        }

        private String getSkillEmoji(String skill) {
            switch (skill) {
                case "Planting": return "🌱";
                case "Harvesting": return "🌾";
                case "Irrigation": return "💧";
                case "Fertilization": return "🌿";
                case "Pest Control": return "🐛";
                case "Equipment Maintenance": return "🔧";
                default: return "👨‍🌾";
            }
        }

        class WorkerViewHolder extends RecyclerView.ViewHolder {
            TextView workerName, workerLocation, workerRating, workerExperience, workerRate, workerSkills;
            Button hireButton, viewProfileButton;

            public WorkerViewHolder(View itemView) {
                super(itemView);
                workerName = itemView.findViewById(R.id.workerName);
                workerLocation = itemView.findViewById(R.id.workerLocation);
                workerRating = itemView.findViewById(R.id.workerRating);
                workerExperience = itemView.findViewById(R.id.workerExperience);
                workerRate = itemView.findViewById(R.id.workerRate);
                workerSkills = itemView.findViewById(R.id.workerSkills);
                hireButton = itemView.findViewById(R.id.hireButton);
                viewProfileButton = itemView.findViewById(R.id.viewProfileButton);
            }
        }
    }

    private void showHireDialog(Worker worker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hire " + worker.name);
        builder.setMessage("Would you like to hire " + worker.name + " for your farming work?\n\nRate: Rs. " + worker.hourlyRate + "/hour\nRating: " + worker.rating + " stars");
        
        builder.setPositiveButton("Hire Now", (dialog, which) -> {
            Toast.makeText(this, "Hiring request sent to " + worker.name, Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showWorkerProfile(Worker worker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(worker.name + " - Profile");
        
        StringBuilder message = new StringBuilder();
        message.append("Location: ").append(worker.location).append("\n\n");
        message.append("Experience: ").append(worker.experience).append(" years\n\n");
        message.append("Rating: ").append(worker.rating).append(" stars\n\n");
        message.append("Hourly Rate: Rs. ").append(worker.hourlyRate).append("/hour\n\n");
        message.append("Skills: ");
        for (String skill : worker.skills) {
            message.append(skill).append(", ");
        }
        message.setLength(message.length() - 2); // Remove last comma
        message.append("\n\n");
        message.append("About: ").append(worker.description);
        
        builder.setMessage(message.toString());
        builder.setPositiveButton("Hire", (dialog, which) -> showHireDialog(worker));
        builder.setNegativeButton("Close", null);
        builder.show();
    }
}

