
package com.s23010535.govisaviya;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import java.util.*;
import java.util.List;

public class PesticidesActivity extends Activity {
    
    // UI Components
    private ImageButton btnBack, btnSearch, btnCloseSearch;
    private Button btnPestIdentification, btnEmergency;
    private EditText etSearch;
    private LinearLayout searchContainer;
    private Chip chipAll, chipInsecticides, chipFungicides, chipHerbicides, chipOrganic;
    private RecyclerView rvPesticides;
    
    // Data
    private List<Pesticide> pesticides;
    private PesticideAdapter adapter;
    private String currentCategory = "All";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_pesticides);
            
            initializeViews();
            initializeData();
            setupClickListeners();
            setupSearch();
            setupRecyclerView();
            addSamplePesticides();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading pesticides page: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void initializeViews() {
        try {
            btnBack = findViewById(R.id.btnBack);
            btnSearch = findViewById(R.id.btnSearch);
            btnCloseSearch = findViewById(R.id.btnCloseSearch);
            btnPestIdentification = findViewById(R.id.btnPestIdentification);
            btnEmergency = findViewById(R.id.btnEmergency);
            etSearch = findViewById(R.id.etSearch);
            searchContainer = findViewById(R.id.searchContainer);
            chipAll = findViewById(R.id.chipAll);
            chipInsecticides = findViewById(R.id.chipInsecticides);
            chipFungicides = findViewById(R.id.chipFungicides);
            chipHerbicides = findViewById(R.id.chipHerbicides);
            chipOrganic = findViewById(R.id.chipOrganic);
            rvPesticides = findViewById(R.id.rvPesticides);
            
            // Verify critical views are not null
            if (rvPesticides == null || etSearch == null || searchContainer == null) {
                throw new RuntimeException("Critical views not found in layout");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize views: " + e.getMessage(), e);
        }
    }
    
    private void initializeData() {
        pesticides = new ArrayList<>();
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSearch.setOnClickListener(v -> toggleSearch());
        btnCloseSearch.setOnClickListener(v -> toggleSearch());
        btnPestIdentification.setOnClickListener(v -> showPestIdentificationDialog());
        btnEmergency.setOnClickListener(v -> callEmergency());
        
        // Category chip listeners
        chipAll.setOnClickListener(v -> filterByCategory("All"));
        chipInsecticides.setOnClickListener(v -> filterByCategory("Insecticides"));
        chipFungicides.setOnClickListener(v -> filterByCategory("Fungicides"));
        chipHerbicides.setOnClickListener(v -> filterByCategory("Herbicides"));
        chipOrganic.setOnClickListener(v -> filterByCategory("Organic"));
    }
    
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                filterPesticides();
            }
        });
    }
    
    private void setupRecyclerView() {
        try {
            adapter = new PesticideAdapter(pesticides);
            rvPesticides.setLayoutManager(new LinearLayoutManager(this));
            rvPesticides.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error setting up pesticides list", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void addSamplePesticides() {
        pesticides.add(new Pesticide(
            "Malathion 50EC",
            "Broad-spectrum insecticide for controlling various pests in vegetables and fruits",
            "Insecticides",
            "Rs. 1,200",
            "#4CAF50"
        ));
        
        pesticides.add(new Pesticide(
            "Copper Oxychloride",
            "Fungicide for controlling fungal diseases in crops",
            "Fungicides",
            "Rs. 800",
            "#2196F3"
        ));
        
        pesticides.add(new Pesticide(
            "Glyphosate 41%",
            "Systemic herbicide for weed control in agricultural fields",
            "Herbicides",
            "Rs. 1,500",
            "#FF9800"
        ));
        
        pesticides.add(new Pesticide(
            "Neem Oil",
            "Natural organic pesticide derived from neem tree seeds",
            "Organic",
            "Rs. 600",
            "#8BC34A"
        ));
        
        pesticides.add(new Pesticide(
            "Deltamethrin 2.8%",
            "Synthetic pyrethroid insecticide for pest control",
            "Insecticides",
            "Rs. 950",
            "#4CAF50"
        ));
        
        pesticides.add(new Pesticide(
            "Mancozeb 75%",
            "Protective fungicide for disease prevention in crops",
            "Fungicides",
            "Rs. 1,100",
            "#2196F3"
        ));
        
        adapter.notifyDataSetChanged();
    }
    
    private void toggleSearch() {
        if (searchContainer.getVisibility() == View.VISIBLE) {
            searchContainer.setVisibility(View.GONE);
            etSearch.setText("");
            filterPesticides();
        } else {
            searchContainer.setVisibility(View.VISIBLE);
            etSearch.requestFocus();
        }
    }
    
    private void filterByCategory(String category) {
        currentCategory = category;
        updateFilterChips();
        filterPesticides();
    }
    
    private void updateFilterChips() {
        // Reset all chips
        chipAll.setChecked(false);
        chipInsecticides.setChecked(false);
        chipFungicides.setChecked(false);
        chipHerbicides.setChecked(false);
        chipOrganic.setChecked(false);
        
        // Check selected chip
        switch (currentCategory) {
            case "All":
                chipAll.setChecked(true);
                break;
            case "Insecticides":
                chipInsecticides.setChecked(true);
                break;
            case "Fungicides":
                chipFungicides.setChecked(true);
                break;
            case "Herbicides":
                chipHerbicides.setChecked(true);
                break;
            case "Organic":
                chipOrganic.setChecked(true);
                break;
        }
    }
    
    private void filterPesticides() {
        String searchQuery = etSearch.getText().toString().toLowerCase();
        List<Pesticide> filteredList = new ArrayList<>();
        
        for (Pesticide pesticide : pesticides) {
            boolean matchesCategory = currentCategory.equals("All") || 
                                    pesticide.type.equals(currentCategory);
            boolean matchesSearch = pesticide.name.toLowerCase().contains(searchQuery) ||
                                  pesticide.description.toLowerCase().contains(searchQuery);
            
            if (matchesCategory && matchesSearch) {
                filteredList.add(pesticide);
            }
        }
        
        adapter.updatePesticides(filteredList);
    }
    
    private void showPestIdentificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pest Identification Guide");
        
        String[] commonPests = {
            "Aphids - Small, soft-bodied insects that suck plant sap",
            "Spider Mites - Tiny arachnids that cause stippling on leaves",
            "Whiteflies - Small white insects that fly when disturbed",
            "Thrips - Slender insects that feed on plant tissue",
            "Mealybugs - Soft-bodied insects covered with white wax",
            "Scale Insects - Immobile insects that appear as bumps on plants",
            "Leaf Miners - Larvae that tunnel through leaves",
            "Cutworms - Caterpillars that cut young plants at soil level"
        };
        
        builder.setItems(commonPests, (dialog, which) -> {
            String selectedPest = commonPests[which];
            showPestDetails(selectedPest);
        });
        
        builder.setPositiveButton("Close", null);
        builder.show();
    }
    
    private void showPestDetails(String pestInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pest Details");
        builder.setMessage(pestInfo + "\n\nFor specific identification and treatment recommendations, please consult with a local agricultural expert.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    
    private void callEmergency() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Emergency Contact");
        builder.setMessage("Poison Control Center: 1-800-222-1222\n\nAgricultural Extension: +94 11 269 0000\n\nEmergency Services: 119");
        builder.setPositiveButton("Call Poison Control", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:1-800-222-1222"));
            startActivity(intent);
        });
        builder.setNegativeButton("Close", null);
        builder.show();
    }
    
    // Pesticide Data Class
    public static class Pesticide {
        public String name;
        public String description;
        public String type;
        public String price;
        public String colorCode;
        
        public Pesticide(String name, String description, String type, String price, String colorCode) {
            this.name = name;
            this.description = description;
            this.type = type;
            this.price = price;
            this.colorCode = colorCode;
        }
    }
    
    // Pesticide Adapter
    private class PesticideAdapter extends RecyclerView.Adapter<PesticideAdapter.PesticideViewHolder> {
        private List<Pesticide> pesticides;
        
        public PesticideAdapter(List<Pesticide> pesticides) {
            this.pesticides = pesticides;
        }
        
        public void updatePesticides(List<Pesticide> newPesticides) {
            this.pesticides = newPesticides;
            notifyDataSetChanged();
        }
        
        @Override
        public PesticideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_pesticide, parent, false);
            return new PesticideViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(PesticideViewHolder holder, int position) {
            Pesticide pesticide = pesticides.get(position);
            
            holder.tvPesticideName.setText(pesticide.name);
            holder.tvDescription.setText(pesticide.description);
            holder.tvType.setText(pesticide.type);
            holder.tvPrice.setText(pesticide.price);
            
            // Set category indicator color
            try {
                int color = android.graphics.Color.parseColor(pesticide.colorCode);
                holder.categoryIndicator.setBackgroundColor(color);
            } catch (Exception e) {
                holder.categoryIndicator.setBackgroundColor(android.graphics.Color.GRAY);
            }
            
            holder.btnViewDetails.setOnClickListener(v -> showPesticideDetails(pesticide));
        }
        
        @Override
        public int getItemCount() {
            return pesticides.size();
        }
        
        class PesticideViewHolder extends RecyclerView.ViewHolder {
            View categoryIndicator;
            TextView tvPesticideName, tvDescription, tvType, tvPrice;
            Button btnViewDetails;
            
            public PesticideViewHolder(View itemView) {
                super(itemView);
                categoryIndicator = itemView.findViewById(R.id.categoryIndicator);
                tvPesticideName = itemView.findViewById(R.id.tvPesticideName);
                tvDescription = itemView.findViewById(R.id.tvDescription);
                tvType = itemView.findViewById(R.id.tvType);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            }
        }
    }
    
    private void showPesticideDetails(Pesticide pesticide) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(pesticide.name);
        
        String details = "Type: " + pesticide.type + "\n\n" +
                        "Description: " + pesticide.description + "\n\n" +
                        "Price: " + pesticide.price + "\n\n" +
                        "Safety Guidelines:\n" +
                        "• Always read the label before use\n" +
                        "• Wear protective equipment\n" +
                        "• Store in secure locations\n" +
                        "• Follow recommended application rates\n" +
                        "• Keep away from children and pets";
        
        builder.setMessage(details);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}

