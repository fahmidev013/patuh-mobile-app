package app.patuhmobile.module;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.codemybrainsout.placesearch.PlaceSearchDialog;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jota.autocompletelocation.AutoCompleteLocation;

import app.patuhmobile.R;


public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback, AutoCompleteLocation.AutoCompleteLocationListener {


    GoogleMap mMap;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        AutoCompleteTextView autoCompleteLocation =
                (AutoCompleteTextView) findViewById(R.id.autocomplete_location);
        //autoCompleteLocation.setAutoCompleteTextListener(this);
        autoCompleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceSearchDialog placeSearchDialog = new PlaceSearchDialog.Builder(MapActivity.this)
                        .setLocationNameListener(new PlaceSearchDialog.LocationNameListener() {
                            @Override
                            public void locationName(String locationName) {
                                //set textview or edittext
                            }
                        })
                        .build();
                placeSearchDialog.show();
            }
        });
    }

    @Override public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng madrid = new LatLng(40.4167754, -3.7037902);
        mMap.addMarker(new MarkerOptions().position(madrid).title("Marker in Madrid"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 16));
    }

    @Override public void onTextClear() {
        mMap.clear();
    }

    @Override public void onItemSelected(Place selectedPlace) {
        addMapMarker(selectedPlace.getLatLng());
    }

    private void addMapMarker(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }
}
