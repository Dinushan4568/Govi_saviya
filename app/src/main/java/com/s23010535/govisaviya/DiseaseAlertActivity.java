package com.s23010535.govisaviya;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiseaseAlertActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "DiseaseAlertActivity";
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;

    private EditText searchEditText;
    private ImageView iconSearch;
    private Button buttonLiveLocation, buttonDisease, buttonFoodShops, buttonPesticideShops;
    private LinearLayout bottomSheetLayout;
    private TextView locationName, locationWeather, locationKeyFeatures;
    private final List<Marker> allMarkers = new ArrayList<>();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private CardView locationListPopup;
    private ListView locationListView;
    private TextView popupTitle;
    private List<MapLocation> filteredLocations = new ArrayList<>();
    private String currentFilter = "";
    private int currentLocationIndex = -1;

    // Sample data for agricultural shops and disease areas
    private final List<MapLocation> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_alert);

        initializeSampleData();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        searchEditText = findViewById(R.id.searchEditText);
        iconSearch = findViewById(R.id.iconSearch);
        buttonLiveLocation = findViewById(R.id.buttonLiveLocation);
        buttonDisease = findViewById(R.id.buttonDisease);
        buttonFoodShops = findViewById(R.id.buttonFoodShops);
        buttonPesticideShops = findViewById(R.id.buttonPesticideShops);
        bottomSheetLayout = findViewById(R.id.bottomSheetLayout);
        locationName = findViewById(R.id.locationName);
        locationWeather = findViewById(R.id.locationWeather);
        locationKeyFeatures = findViewById(R.id.locationKeyFeatures);

        locationListPopup = findViewById(R.id.locationListPopup);
        locationListView = findViewById(R.id.locationListView);
        popupTitle = findViewById(R.id.popupTitle);

        setupSearchFunctionality();

        buttonLiveLocation.setOnClickListener(v -> {
            locationListPopup.setVisibility(View.GONE);
            moveToCurrentLocation();
        });
        buttonDisease.setOnClickListener(v -> handleFilterClick("Disease Alert"));
        buttonFoodShops.setOnClickListener(v -> handleFilterClick("Agricultural Shop"));
        buttonPesticideShops.setOnClickListener(v -> handleFilterClick("Pesticide Shop"));


        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Error initializing map", Toast.LENGTH_SHORT).show();
        }

        requestLocationPermission();
    }

    private void initializeSampleData() {
        // Food Buying Shops (previously Agricultural Shop)
        locations.add(new MapLocation("Green Agro Store", "Agricultural Shop", "Fresh vegetables and organic products.", 6.9271, 79.8612, BitmapDescriptorFactory.HUE_GREEN));
        locations.add(new MapLocation("Farmers Supply Co.", "Agricultural Shop", "Wholesale grains and supplies.", 7.2906, 80.6337, BitmapDescriptorFactory.HUE_GREEN));
        locations.add(new MapLocation("Agri Solutions", "Agricultural Shop", "Your one-stop shop for farm needs.", 6.0535, 80.2210, BitmapDescriptorFactory.HUE_GREEN));
        locations.add(new MapLocation("Organic Farm Store", "Agricultural Shop", "Certified organic produce.", 9.6615, 80.0255, BitmapDescriptorFactory.HUE_GREEN));

        // Pesticide Shops (previously Equipment Store)
        locations.add(new MapLocation("Agri Equipments", "Pesticide Shop", "Wide range of pesticides and fertilizers.", 6.8484, 79.9267, BitmapDescriptorFactory.HUE_ORANGE));
        locations.add(new MapLocation("Farm Tools Center", "Pesticide Shop", "Eco-friendly pest solutions.", 7.4863, 80.3623, BitmapDescriptorFactory.HUE_ORANGE));
        locations.add(new MapLocation("Tractor Parts Lanka", "Pesticide Shop", "Heavy-duty pesticides for large farms.", 8.3114, 80.4037, BitmapDescriptorFactory.HUE_ORANGE));

        // Disease Alerts
        locations.add(new MapLocation("Leaf Blight Alert", "Disease Alert", "High-risk area for Leaf Blight. Take precautions.", 8.3, 80.4, BitmapDescriptorFactory.HUE_RED));
        locations.add(new MapLocation("Rice Blast Disease", "Disease Alert", "Medium-risk area. Monitor crops closely.", 7.9403, 81.0188, BitmapDescriptorFactory.HUE_RED));
        locations.add(new MapLocation("Coconut Wilt", "Disease Alert", "Low-risk area, but stay vigilant.", 8.0362, 79.8283, BitmapDescriptorFactory.HUE_YELLOW));
        locations.add(new MapLocation("Tea Leaf Disease", "Disease Alert", "Medium risk for tea plantations.", 6.9497, 80.7891, BitmapDescriptorFactory.HUE_RED));
    }

    private void setupSearchFunctionality() {
        iconSearch.setOnClickListener(v -> {
            String searchQuery = searchEditText.getText().toString();
            if (!searchQuery.isEmpty()) {
                searchLocation(searchQuery);
            } else {
                Toast.makeText(this, "Please enter a location to search", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchLocation(String locationName) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                Toast.makeText(this, "Showing " + address.getFeatureName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location not found: " + locationName, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Geocoding failed", e);
            Toast.makeText(this, "Error searching for location", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFilterClick(String filterType) {
        bottomSheetLayout.setVisibility(View.GONE);

        if (!currentFilter.equals(filterType)) {
            currentFilter = filterType;
            currentLocationIndex = -1;
            filteredLocations.clear();
            for (MapLocation loc : locations) {
                if (loc.type.equals(filterType)) {
                    filteredLocations.add(loc);
                }
            }
            displayFilteredLocations();
            updateLocationListPopup();

        } else {
            // Cycle through locations
            if (!filteredLocations.isEmpty()) {
                currentLocationIndex = (currentLocationIndex + 1) % filteredLocations.size();
                MapLocation nextLocation = filteredLocations.get(currentLocationIndex);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(nextLocation.latitude, nextLocation.longitude), 15));

                // Find and show info window for the corresponding marker
                for (Marker marker : allMarkers) {
                    if (marker.getTag() != null && marker.getTag().equals(nextLocation)) {
                        marker.showInfoWindow();
                        showDetails(marker);
                        break;
                    }
                }
            }
        }
    }

    private void displayFilteredLocations() {
        clearMarkers();
        for (MapLocation loc : filteredLocations) {
            addMarker(loc);
        }

        if (!filteredLocations.isEmpty()) {
            // Zoom to the first location in the list
            MapLocation firstLocation = filteredLocations.get(0);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstLocation.latitude, firstLocation.longitude), 12));
        } else {
            Toast.makeText(this, "No locations found for this filter.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLocationListPopup() {
        if (filteredLocations.isEmpty()) {
            locationListPopup.setVisibility(View.GONE);
            return;
        }

        popupTitle.setText(currentFilter);
        List<String> locationNames = new ArrayList<>();
        for (MapLocation loc : filteredLocations) {
            locationNames.add(loc.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationNames);
        locationListView.setAdapter(adapter);

        locationListView.setOnItemClickListener((parent, view, position, id) -> {
            currentLocationIndex = position;
            MapLocation selectedLocation = filteredLocations.get(position);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selectedLocation.latitude, selectedLocation.longitude), 15));
            for (Marker marker : allMarkers) {
                if (marker.getTag() != null && marker.getTag().equals(selectedLocation)) {
                    showDetails(marker);
                    break;
                }
            }
        });

        locationListPopup.setVisibility(View.VISIBLE);
    }

    private void showAllLocations() {
        clearMarkers();
        for (MapLocation loc : locations) {
            addMarker(loc);
        }
        if (currentLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 10));
        } else if (!allMarkers.isEmpty()) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(allMarkers.get(0).getPosition(), 7));
        }
        locationListPopup.setVisibility(View.GONE);
    }

    private void moveToCurrentLocation() {
        if (currentLocation != null) {
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
            Toast.makeText(this, "Showing your location", Toast.LENGTH_SHORT).show();
            locationListPopup.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "Current location not available. Please enable location services.", Toast.LENGTH_SHORT).show();
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation();
        }

        showAllLocations(); // Show all markers initially
    }

    private void fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                currentLocation = location;
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                // Add a blue dot for current location
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            } else {
                Log.w(TAG, "Last known location is null.");
                Toast.makeText(this, "Could not get current location.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission is required to show your position on the map.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void clearMarkers() {
        for (Marker marker : allMarkers) {
            marker.remove();
        }
        allMarkers.clear();
        // Keep the user's location marker if it exists
        if (mMap != null) {
           mMap.clear(); // This will clear all markers including the user's location, so we need to re-add it
            if(currentLocation != null) {
                 mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
        }
    }

    private void addMarker(MapLocation location) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.latitude, location.longitude))
                .title(location.name)
                .icon(BitmapDescriptorFactory.defaultMarker(location.color)));
        if (marker != null) {
            marker.setTag(location);
            allMarkers.add(marker);
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        // Don't show details for the user's location marker
        if ("Your Location".equals(marker.getTitle())) {
             bottomSheetLayout.setVisibility(View.GONE);
            return true; // Consume the event
        }
        showDetails(marker);
        return false;
    }

    private void showDetails(Marker marker) {
        MapLocation location = (MapLocation) marker.getTag();
        if (location != null) {
            bottomSheetLayout.setVisibility(View.VISIBLE);
            locationName.setText(location.name);
            locationKeyFeatures.setText("Key Features: " + location.keyFeatures);
            fetchWeatherForLocation(location.latitude, location.longitude);
        }
    }

    private void fetchWeatherForLocation(double lat, double lon) {
        // TODO: Implement Weather API call here
        // For now, setting placeholder text
        locationWeather.setText("Weather: Loading...");

        // Example of how you might call your existing WeatherApi
        // This is a placeholder and assumes WeatherApi has a method like this
        /*
        WeatherApi.getWeather(lat, lon, new WeatherApi.WeatherCallback() {
            @Override
            public void onSuccess(WeatherResponse response) {
                locationWeather.setText("Weather: " + response.getTemperature() + "°C, " + response.getDescription());
            }

            @Override
            public void onFailure(Exception e) {
                locationWeather.setText("Weather: Could not load weather data.");
            }
        });
        */
    }


    private static class MapLocation {
        String name;
        String type;
        String keyFeatures;
        double latitude;
        double longitude;
        float color;

        MapLocation(String name, String type, String keyFeatures, double latitude, double longitude, float color) {
            this.name = name;
            this.type = type;
            this.keyFeatures = keyFeatures;
            this.latitude = latitude;
            this.longitude = longitude;
            this.color = color;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            MapLocation that = (MapLocation) obj;
            return Double.compare(that.latitude, latitude) == 0 &&
                    Double.compare(that.longitude, longitude) == 0 &&
                    name.equals(that.name);
        }
    }
}
