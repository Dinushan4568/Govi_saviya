package com.s23010535.govisaviya;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class FarmCalendarActivity extends Activity {
    
    // UI Components
    private ImageButton backButton, addTaskButton;
    private ImageButton previousMonthButton, nextMonthButton;
    private TextView monthYearText;
    private GridLayout calendarGrid;
    private TextView weatherLocation, weatherDescription, weatherHumidity;
    private ImageView weatherIcon;
    private Button refreshWeatherButton;
    private LinearLayout todayTasksContainer, upcomingEventsContainer;
    private TextView taskCountText, noTasksText, noEventsText;
    private Button plantingButton, harvestButton;

    // Calendar Data
    private Calendar currentDate;
    private Calendar selectedDate;
    private int currentMonth;
    private int currentYear;

    // Task and Event Data
    private List<FarmTask> tasks;
    private List<FarmEvent> events;

    // Weather Data
    private String currentWeather = "Sunny, 28°C";
    private String currentLocation = "Current Location";
    private String weatherDetails = "Humidity: 65% • Wind: 12 km/h";

    private static final String PREFS_NAME = "farm_calendar_prefs";
    private static final String KEY_TASKS = "tasks";
    private static final String KEY_EVENTS = "events";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_calendar);
        
        initializeViews();
        initializeData();
        setupClickListeners();
        loadData();
        setupCalendar();
        updateWeatherDisplay();
        updateTasksDisplay();
        updateEventsDisplay();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        addTaskButton = findViewById(R.id.addTaskButton);
        previousMonthButton = findViewById(R.id.previousMonthButton);
        nextMonthButton = findViewById(R.id.nextMonthButton);
        monthYearText = findViewById(R.id.monthYearText);
        calendarGrid = findViewById(R.id.calendarGrid);
        weatherLocation = findViewById(R.id.weatherLocation);
        weatherDescription = findViewById(R.id.weatherDescription);
        weatherHumidity = findViewById(R.id.weatherHumidity);
        weatherIcon = findViewById(R.id.weatherIcon);
        refreshWeatherButton = findViewById(R.id.refreshWeatherButton);
        todayTasksContainer = findViewById(R.id.todayTasksContainer);
        upcomingEventsContainer = findViewById(R.id.upcomingEventsContainer);
        taskCountText = findViewById(R.id.taskCountText);
        noTasksText = findViewById(R.id.noTasksText);
        noEventsText = findViewById(R.id.noEventsText);
        plantingButton = findViewById(R.id.plantingButton);
        harvestButton = findViewById(R.id.harvestButton);
    }

    private void initializeData() {
        currentDate = Calendar.getInstance();
        selectedDate = Calendar.getInstance();
        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);
        
        tasks = new ArrayList<>();
        events = new ArrayList<>();
        
        // Add sample data
        addSampleData();
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        addTaskButton.setOnClickListener(v -> showAddTaskDialog());
        previousMonthButton.setOnClickListener(v -> previousMonth());
        nextMonthButton.setOnClickListener(v -> nextMonth());
        refreshWeatherButton.setOnClickListener(v -> refreshWeather());
        plantingButton.setOnClickListener(v -> showPlantingDialog());
        harvestButton.setOnClickListener(v -> showHarvestDialog());
    }

    private void setupCalendar() {
        updateMonthYearText();
        populateCalendar();
    }

    private void updateMonthYearText() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearText.setText(sdf.format(currentDate.getTime()));
    }

    private void populateCalendar() {
        calendarGrid.removeAllViews();
        
        Calendar firstDayOfMonth = Calendar.getInstance();
        firstDayOfMonth.set(currentYear, currentMonth, 1);
        
        int firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1; // 0 = Sunday
        int daysInMonth = firstDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // Add empty cells for days before the first day of the month
        for (int i = 0; i < firstDayOfWeek; i++) {
            addCalendarDay("", false, false);
        }
        
        // Add days of the month
        for (int day = 1; day <= daysInMonth; day++) {
            boolean isToday = isToday(day);
            boolean hasTask = hasTaskOnDate(day);
            addCalendarDay(String.valueOf(day), isToday, hasTask);
        }
    }

    private void addCalendarDay(String dayText, boolean isToday, boolean hasTask) {
        TextView dayView = new TextView(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(4, 4, 4, 4);
        
        dayView.setLayoutParams(params);
        dayView.setText(dayText);
        dayView.setTextSize(14);
        dayView.setGravity(Gravity.CENTER);
        dayView.setPadding(8, 16, 8, 16);
        
        // Set background and text color
        if (isToday) {
            dayView.setBackgroundResource(R.drawable.circle_background);
            dayView.setTextColor(Color.WHITE);
        } else if (hasTask) {
            dayView.setBackgroundResource(R.drawable.task_indicator_background);
            dayView.setTextColor(Color.WHITE);
        } else {
            dayView.setBackgroundResource(R.drawable.calendar_day_background);
            dayView.setTextColor(Color.BLACK);
        }
        
        if (!dayText.isEmpty()) {
            dayView.setOnClickListener(v -> onDaySelected(Integer.parseInt(dayText)));
        }
        
        calendarGrid.addView(dayView);
    }

    private boolean isToday(int day) {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.DAY_OF_MONTH) == day && 
               today.get(Calendar.MONTH) == currentMonth && 
               today.get(Calendar.YEAR) == currentYear;
    }

    private boolean hasTaskOnDate(int day) {
        Calendar checkDate = Calendar.getInstance();
        checkDate.set(currentYear, currentMonth, day);
        
        for (FarmTask task : tasks) {
            if (isSameDay(task.date, checkDate)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSameDay(Calendar date1, Calendar date2) {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
               date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
               date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);
    }

    private void onDaySelected(int day) {
        selectedDate.set(currentYear, currentMonth, day);
        showDayDetailsDialog(day);
    }

    private void previousMonth() {
        currentDate.add(Calendar.MONTH, -1);
        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);
        updateMonthYearText();
        populateCalendar();
    }

    private void nextMonth() {
        currentDate.add(Calendar.MONTH, 1);
        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);
        updateMonthYearText();
        populateCalendar();
    }

    private void refreshWeather() {
        // Simulate weather refresh
        String[] weatherConditions = {
            "Sunny, 28°C",
            "Partly Cloudy, 25°C", 
            "Rainy, 22°C",
            "Cloudy, 24°C"
        };
        currentWeather = weatherConditions[new Random().nextInt(weatherConditions.length)];
        updateWeatherDisplay();
        Toast.makeText(this, "Weather updated", Toast.LENGTH_SHORT).show();
    }

    private void updateWeatherDisplay() {
        weatherLocation.setText(currentLocation);
        weatherDescription.setText(currentWeather);
        weatherHumidity.setText(weatherDetails);
    }

    private void updateTasksDisplay() {
        todayTasksContainer.removeAllViews();
        
        List<FarmTask> todayTasks = getTasksForToday();
        taskCountText.setText(String.valueOf(todayTasks.size()));
        
        if (todayTasks.isEmpty()) {
            noTasksText.setVisibility(View.VISIBLE);
        } else {
            noTasksText.setVisibility(View.GONE);
            for (FarmTask task : todayTasks) {
                addTaskView(task);
            }
        }
    }

    private List<FarmTask> getTasksForToday() {
        List<FarmTask> todayTasks = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        
        for (FarmTask task : tasks) {
            if (isSameDay(task.date, today)) {
                todayTasks.add(task);
            }
        }
        return todayTasks;
    }

    private void addTaskView(FarmTask task) {
        LinearLayout taskLayout = new LinearLayout(this);
        taskLayout.setOrientation(LinearLayout.HORIZONTAL);
        taskLayout.setPadding(0, 8, 0, 8);
        taskLayout.setGravity(Gravity.CENTER_VERTICAL);
        
        // Task indicator
        View indicator = new View(this);
        LinearLayout.LayoutParams indicatorParams = new LinearLayout.LayoutParams(4, 40);
        indicatorParams.setMargins(0, 0, 12, 0);
        indicator.setLayoutParams(indicatorParams);
        indicator.setBackgroundColor(getTaskColor(task.type));
        
        // Task text
        TextView taskText = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        taskText.setLayoutParams(textParams);
        taskText.setText(task.title);
        taskText.setTextSize(14);
        taskText.setTextColor(Color.BLACK);
        
        // Time text
        TextView timeText = new TextView(this);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        timeText.setLayoutParams(timeParams);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeText.setText(sdf.format(task.date.getTime()));
        timeText.setTextSize(12);
        timeText.setTextColor(Color.GRAY);
        
        taskLayout.addView(indicator);
        taskLayout.addView(taskText);
        taskLayout.addView(timeText);
        
        todayTasksContainer.addView(taskLayout);
    }

    private int getTaskColor(String taskType) {
        switch (taskType) {
            case "Planting": return Color.GREEN;
            case "Harvest": return Color.parseColor("#FF9800");
            case "Irrigation": return Color.BLUE;
            case "Fertilization": return Color.parseColor("#8BC34A");
            default: return Color.GRAY;
        }
    }

    private void updateEventsDisplay() {
        upcomingEventsContainer.removeAllViews();
        
        List<FarmEvent> upcomingEvents = getUpcomingEvents();
        
        if (upcomingEvents.isEmpty()) {
            noEventsText.setVisibility(View.VISIBLE);
        } else {
            noEventsText.setVisibility(View.GONE);
            for (FarmEvent event : upcomingEvents) {
                addEventView(event);
            }
        }
    }

    private List<FarmEvent> getUpcomingEvents() {
        List<FarmEvent> upcomingEvents = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        
        for (FarmEvent event : events) {
            if (event.date.after(today)) {
                upcomingEvents.add(event);
            }
        }
        
        // Sort by date
        upcomingEvents.sort((e1, e2) -> e1.date.compareTo(e2.date));
        
        // Return only next 5 events
        return upcomingEvents.subList(0, Math.min(5, upcomingEvents.size()));
    }

    private void addEventView(FarmEvent event) {
        LinearLayout eventLayout = new LinearLayout(this);
        eventLayout.setOrientation(LinearLayout.HORIZONTAL);
        eventLayout.setPadding(0, 8, 0, 8);
        eventLayout.setGravity(Gravity.CENTER_VERTICAL);
        
        // Event icon
        TextView eventIcon = new TextView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconParams.setMargins(0, 0, 12, 0);
        eventIcon.setLayoutParams(iconParams);
        eventIcon.setText(getEventIcon(event.type));
        eventIcon.setTextSize(20);
        
        // Event details
        LinearLayout detailsLayout = new LinearLayout(this);
        detailsLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        detailsLayout.setLayoutParams(detailsParams);
        
        TextView eventTitle = new TextView(this);
        eventTitle.setText(event.title);
        eventTitle.setTextSize(14);
        eventTitle.setTextColor(Color.BLACK);
        eventTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        
        TextView eventDate = new TextView(this);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        eventDate.setText(sdf.format(event.date.getTime()));
        eventDate.setTextSize(12);
        eventDate.setTextColor(Color.GRAY);
        
        detailsLayout.addView(eventTitle);
        detailsLayout.addView(eventDate);
        
        eventLayout.addView(eventIcon);
        eventLayout.addView(detailsLayout);
        
        upcomingEventsContainer.addView(eventLayout);
    }

    private String getEventIcon(String eventType) {
        switch (eventType) {
            case "Market": return "🛒";
            case "Weather Alert": return "⚠️";
            case "Equipment Maintenance": return "🔧";
            case "Crop Rotation": return "🔄";
            default: return "📅";
        }
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");
        
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        builder.setView(dialogView);
        
        EditText taskTitleInput = dialogView.findViewById(R.id.taskTitleInput);
        Spinner taskTypeSpinner = dialogView.findViewById(R.id.taskTypeSpinner);
        EditText taskDateInput = dialogView.findViewById(R.id.taskDateInput);
        EditText taskTimeInput = dialogView.findViewById(R.id.taskTimeInput);
        
        // Setup task type spinner
        String[] taskTypes = {"Planting", "Harvest", "Irrigation", "Fertilization", "Pest Control", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, taskTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskTypeSpinner.setAdapter(adapter);
        
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = taskTitleInput.getText().toString();
            String type = taskTypeSpinner.getSelectedItem().toString();
            
            if (!title.isEmpty()) {
                FarmTask newTask = new FarmTask(title, type, selectedDate);
                tasks.add(newTask);
                saveData();
                updateTasksDisplay();
                populateCalendar();
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            }
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDayDetailsDialog(int day) {
        Calendar selectedDay = Calendar.getInstance();
        selectedDay.set(currentYear, currentMonth, day);
        
        List<FarmTask> dayTasks = new ArrayList<>();
        for (FarmTask task : tasks) {
            if (isSameDay(task.date, selectedDay)) {
                dayTasks.add(task);
            }
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(selectedDay.getTime()));
        
        if (dayTasks.isEmpty()) {
            builder.setMessage("No tasks scheduled for this day.");
        } else {
            StringBuilder message = new StringBuilder("Tasks for this day:\n\n");
            for (FarmTask task : dayTasks) {
                message.append("• ").append(task.title).append(" (").append(task.type).append(")\n");
            }
            builder.setMessage(message.toString());
        }
        
        builder.setPositiveButton("Add Task", (dialog, which) -> showAddTaskDialog());
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void showPlantingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quick Planting Task");
        builder.setMessage("Add a quick planting task for today?");
        
        builder.setPositiveButton("Yes", (dialog, which) -> {
            FarmTask plantingTask = new FarmTask("Planting Activity", "Planting", Calendar.getInstance());
            tasks.add(plantingTask);
            saveData();
            updateTasksDisplay();
            populateCalendar();
            Toast.makeText(this, "Planting task added", Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void showHarvestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quick Harvest Task");
        builder.setMessage("Add a quick harvest task for today?");
        
        builder.setPositiveButton("Yes", (dialog, which) -> {
            FarmTask harvestTask = new FarmTask("Harvest Activity", "Harvest", Calendar.getInstance());
            tasks.add(harvestTask);
            saveData();
            updateTasksDisplay();
            populateCalendar();
            Toast.makeText(this, "Harvest task added", Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void addSampleData() {
        // Add sample tasks
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        
        tasks.add(new FarmTask("Plant Tomatoes", "Planting", tomorrow));
        tasks.add(new FarmTask("Water Corn Field", "Irrigation", Calendar.getInstance()));
        tasks.add(new FarmTask("Harvest Wheat", "Harvest", Calendar.getInstance()));
        
        // Add sample events
        Calendar nextWeek = Calendar.getInstance();
        nextWeek.add(Calendar.DAY_OF_MONTH, 7);
        
        events.add(new FarmEvent("Local Market Day", "Market", nextWeek));
        events.add(new FarmEvent("Equipment Maintenance", "Equipment Maintenance", nextWeek));
    }

    private void loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String tasksJson = prefs.getString(KEY_TASKS, null);
        String eventsJson = prefs.getString(KEY_EVENTS, null);
        
        if (tasksJson != null) {
            try {
                JSONArray tasksArray = new JSONArray(tasksJson);
                tasks.clear();
                for (int i = 0; i < tasksArray.length(); i++) {
                    tasks.add(FarmTask.fromJson(tasksArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                // Use sample data if loading fails
            }
        }
        
        if (eventsJson != null) {
            try {
                JSONArray eventsArray = new JSONArray(eventsJson);
                events.clear();
                for (int i = 0; i < eventsArray.length(); i++) {
                    events.add(FarmEvent.fromJson(eventsArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                // Use sample data if loading fails
            }
        }
    }

    private void saveData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        try {
            JSONArray tasksArray = new JSONArray();
            for (FarmTask task : tasks) {
                tasksArray.put(task.toJson());
            }
            
            JSONArray eventsArray = new JSONArray();
            for (FarmEvent event : events) {
                eventsArray.put(event.toJson());
            }
            
            prefs.edit()
                .putString(KEY_TASKS, tasksArray.toString())
                .putString(KEY_EVENTS, eventsArray.toString())
                .apply();
                
        } catch (JSONException e) {
            // Handle error
        }
    }

    // Data Classes
    public static class FarmTask {
        public String title;
        public String type;
        public Calendar date;

        public FarmTask(String title, String type, Calendar date) {
            this.title = title;
            this.type = type;
            this.date = date;
        }

        public JSONObject toJson() throws JSONException {
            JSONObject obj = new JSONObject();
            obj.put("title", title);
            obj.put("type", type);
            obj.put("date", date.getTimeInMillis());
            return obj;
        }

        public static FarmTask fromJson(JSONObject obj) throws JSONException {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(obj.getLong("date"));
            return new FarmTask(
                obj.getString("title"),
                obj.getString("type"),
                date
            );
        }
    }

    public static class FarmEvent {
        public String title;
        public String type;
        public Calendar date;

        public FarmEvent(String title, String type, Calendar date) {
            this.title = title;
            this.type = type;
            this.date = date;
        }

        public JSONObject toJson() throws JSONException {
            JSONObject obj = new JSONObject();
            obj.put("title", title);
            obj.put("type", type);
            obj.put("date", date.getTimeInMillis());
            return obj;
        }

        public static FarmEvent fromJson(JSONObject obj) throws JSONException {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(obj.getLong("date"));
            return new FarmEvent(
                obj.getString("title"),
                obj.getString("type"),
                date
            );
        }
    }
}

