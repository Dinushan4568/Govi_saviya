package com.s23010535.govisaviya;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// ============================================================================
// VIDEO MODEL CLASS
// ============================================================================
class Video {
    private String id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private String channelName;
    private String duration;
    private String category;
    private int viewCount;
    private String publishDate;

    public Video(String id, String title, String description, String thumbnailUrl, 
                 String videoUrl, String channelName, String duration, String category, 
                 int viewCount, String publishDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.channelName = channelName;
        this.duration = duration;
        this.category = category;
        this.viewCount = viewCount;
        this.publishDate = publishDate;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getVideoUrl() { return videoUrl; }
    public String getChannelName() { return channelName; }
    public String getDuration() { return duration; }
    public String getCategory() { return category; }
    public int getViewCount() { return viewCount; }
    public String getPublishDate() { return publishDate; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public void setChannelName(String channelName) { this.channelName = channelName; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setCategory(String category) { this.category = category; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }
    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }
}

// ============================================================================
// VIDEO ADAPTER CLASS
// ============================================================================
class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    
    private List<Video> videoList;
    private Context context;
    private OnVideoClickListener listener;

    public interface OnVideoClickListener {
        void onVideoClick(Video video);
    }

    public VideoAdapter(Context context, List<Video> videoList, OnVideoClickListener listener) {
        this.context = context;
        this.videoList = videoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void updateVideos(List<Video> newVideos) {
        this.videoList = newVideos;
        notifyDataSetChanged();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivThumbnail, btnMore;
        private TextView tvTitle, tvChannel, tvViews, tvPublishDate, tvDuration;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvChannel = itemView.findViewById(R.id.tvChannel);
            tvViews = itemView.findViewById(R.id.tvViews);
            tvPublishDate = itemView.findViewById(R.id.tvPublishDate);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            btnMore = itemView.findViewById(R.id.btnMore);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onVideoClick(videoList.get(position));
                }
            });

            btnMore.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    showMoreOptions(videoList.get(position), v);
                }
            });
        }

        public void bind(Video video) {
            tvTitle.setText(video.getTitle());
            tvChannel.setText(video.getChannelName());
            tvDuration.setText(video.getDuration());
            tvPublishDate.setText(video.getPublishDate());
            
            // Format view count
            String viewText = formatViewCount(video.getViewCount());
            tvViews.setText(viewText + " views");
        }

        private String formatViewCount(int viewCount) {
            if (viewCount >= 1000000) {
                return String.format("%.1fM", viewCount / 1000000.0);
            } else if (viewCount >= 1000) {
                return String.format("%.1fK", viewCount / 1000.0);
            } else {
                return String.valueOf(viewCount);
            }
        }

        private void showMoreOptions(Video video, View anchorView) {
            // Create a simple popup menu or dialog
            Toast.makeText(context, "More options for: " + video.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}

// ============================================================================
// MAIN VIDEO TUTORIALS ACTIVITY
// ============================================================================
public class VideoTutorialsActivity extends AppCompatActivity implements VideoAdapter.OnVideoClickListener {

    private EditText etSearch;
    private ImageView btnBack, btnSearch, btnFilter;
    private ChipGroup chipGroupCategories;
    private CardView cardFeaturedVideo, cardChannelInfo;
    private TextView tvFeaturedTitle, tvFeaturedChannel;
    private RecyclerView rvVideos;
    
    private VideoAdapter videoAdapter;
    private List<Video> allVideos;
    private List<Video> filteredVideos;
    private Video featuredVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_tutorials);
        
        initializeViews();
        setupClickListeners();
        loadVideos();
        setupRecyclerView();
        setupSearch();
        setupCategoryFilter();
    }

    private void initializeViews() {
        etSearch = findViewById(R.id.etSearch);
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        btnFilter = findViewById(R.id.btnFilter);
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        cardFeaturedVideo = findViewById(R.id.cardFeaturedVideo);
        cardChannelInfo = findViewById(R.id.cardChannelInfo);
        tvFeaturedTitle = findViewById(R.id.tvFeaturedTitle);
        tvFeaturedChannel = findViewById(R.id.tvFeaturedChannel);
        rvVideos = findViewById(R.id.rvVideos);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnSearch.setOnClickListener(v -> {
            etSearch.requestFocus();
            // Show keyboard
        });
        
        btnFilter.setOnClickListener(v -> {
            Toast.makeText(this, "Filter options coming soon!", Toast.LENGTH_SHORT).show();
        });
        
        cardFeaturedVideo.setOnClickListener(v -> {
            if (featuredVideo != null) {
                openVideo(featuredVideo);
            }
        });
        
        cardChannelInfo.setOnClickListener(v -> {
            openSmartAgriChannel();
        });
    }

    private void openSmartAgriChannel() {
        try {
            // Open SmartAgri YouTube channel
            Intent intent = new Intent(Intent.ACTION_VIEW, 
                Uri.parse("https://www.youtube.com/@smartagri8644"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open SmartAgri channel", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadVideos() {
        allVideos = new ArrayList<>();
        // Featured video
        featuredVideo = new Video(
            "featured_1",
            "එළවලු වගාවට පණුවෝ",
            "ඔයාගේ එළවලු වාගාවේ පණුවෝ ඉන්නවද ? මෙන්න විසදුම - ඉල් මැස්සාගේ හානීය",
            "https://img.youtube.com/vi/9F7paHFzpcc/hqdefault.jpg",
            "https://www.youtube.com/watch?v=9F7paHFzpcc",
            "SmartAgri",
            "12:45",
            "Pest Control",
            89000,
            "3 days ago"
        );
        allVideos.add(new Video("1", "බෙල්පෙපර් වගාව", "බෙල් පෙපර් වගාව ගැන විස්තරයක්", "https://img.youtube.com/vi/jWvtY8k534A/hqdefault.jpg", "https://www.youtube.com/watch?v=jWvtY8k534A", "SmartAgri", "10:20", "Organic Farming", 75000, "1 week ago"));
        allVideos.add(new Video("2", "පිපිඤ්ඤා වගාව", "පිපිඤ්ඤා වගාවේ හොදම ටික", "https://img.youtube.com/vi/7d_Fd7o_NE0/hqdefault.jpg", "https://www.youtube.com/watch?v=7d_Fd7o_NE0", "SmartAgri", "14:30", "Irrigation", 62000, "2 weeks ago"));
        allVideos.add(new Video("3", "කුකුළු වගාව", "කුකුළු වගාව සාර්ථකව කරන්නේ කෙසේද", "https://img.youtube.com/vi/1O4hF1p5z6A/hqdefault.jpg", "https://www.youtube.com/watch?v=1O4hF1p5z6A", "SmartAgri", "11:10", "Livestock", 54000, "5 days ago"));
        allVideos.add(new Video("4", "කෘමිනාශක භාවිතය", "ආරක්ෂිත කෘමිනාශක භාවිතය", "https://img.youtube.com/vi/2Fh8p1f5z6B/hqdefault.jpg", "https://www.youtube.com/watch?v=2Fh8p1f5z6B", "SmartAgri", "13:00", "Pesticides", 48000, "1 week ago"));
        allVideos.add(new Video("5", "පොහොර සකස් කිරීම", "පොහොර සකස් කිරීමේ ක්‍රම", "https://img.youtube.com/vi/3Gh9q2g6z7C/hqdefault.jpg", "https://www.youtube.com/watch?v=3Gh9q2g6z7C", "SmartAgri", "15:40", "Fertilizer", 39000, "3 weeks ago"));
        allVideos.add(new Video("6", "කෘෂිකර්ම උපදෙස්", "විශේෂ කෘෂිකර්ම උපදෙස්", "https://img.youtube.com/vi/4Jk0r3h7z8D/hqdefault.jpg", "https://www.youtube.com/watch?v=4Jk0r3h7z8D", "SmartAgri", "16:20", "Expert Advice", 41000, "2 weeks ago"));
        allVideos.add(new Video("7", "ජල කළමනාකරණය", "ජල කළමනාකරණය සඳහා උපදෙස්", "https://img.youtube.com/vi/5Kl1s4i8z9E/hqdefault.jpg", "https://www.youtube.com/watch?v=5Kl1s4i8z9E", "SmartAgri", "12:50", "Water Management", 37000, "1 week ago"));
        allVideos.add(new Video("8", "අස්වැන්න රැකීම", "අස්වැන්න රැකීමේ ක්‍රම", "https://img.youtube.com/vi/6Lm2t5j9z0F/hqdefault.jpg", "https://www.youtube.com/watch?v=6Lm2t5j9z0F", "SmartAgri", "10:30", "Harvesting", 32000, "4 days ago"));
        allVideos.add(new Video("9", "ඉඩම් සකස් කිරීම", "ඉඩම් සකස් කිරීමේ වැදගත්කම", "https://img.youtube.com/vi/7Mn3u6k0z1G/hqdefault.jpg", "https://www.youtube.com/watch?v=7Mn3u6k0z1G", "SmartAgri", "13:10", "Soil Preparation", 28000, "2 weeks ago"));
        allVideos.add(new Video("10", "පළතුරු වගාව", "පළතුරු වගාවේ නව ක්‍රම", "https://img.youtube.com/vi/8No4v7l1z2H/hqdefault.jpg", "https://www.youtube.com/watch?v=8No4v7l1z2H", "SmartAgri", "11:55", "Fruit Farming", 25000, "3 weeks ago"));
        filteredVideos = new ArrayList<>(allVideos);
        tvFeaturedTitle.setText(featuredVideo.getTitle());
        tvFeaturedChannel.setText(featuredVideo.getChannelName());
    }

    private void setupRecyclerView() {
        videoAdapter = new VideoAdapter(this, filteredVideos, this);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));
        rvVideos.setAdapter(videoAdapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterVideos();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupCategoryFilter() {
        chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            filterVideos();
        });
    }

    private void filterVideos() {
        String searchQuery = etSearch.getText().toString().toLowerCase();
        String selectedCategory = getSelectedCategory();
        
        filteredVideos = allVideos.stream()
            .filter(video -> {
                boolean matchesSearch = searchQuery.isEmpty() || 
                    video.getTitle().toLowerCase().contains(searchQuery) ||
                    video.getDescription().toLowerCase().contains(searchQuery);
                
                boolean matchesCategory = selectedCategory.equals("All") || 
                    video.getCategory().equals(selectedCategory);
                
                return matchesSearch && matchesCategory;
            })
            .collect(Collectors.toList());
        
        videoAdapter.updateVideos(filteredVideos);
    }

    private String getSelectedCategory() {
        int checkedChipId = chipGroupCategories.getCheckedChipId();
        if (checkedChipId != View.NO_ID) {
            Chip chip = findViewById(checkedChipId);
            return chip.getText().toString();
        }
        return "All";
    }

    @Override
    public void onVideoClick(Video video) {
        openVideo(video);
    }

    private void openVideo(Video video) {
        try {
            // Open YouTube app or browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getVideoUrl()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open video", Toast.LENGTH_SHORT).show();
        }
    }
}

