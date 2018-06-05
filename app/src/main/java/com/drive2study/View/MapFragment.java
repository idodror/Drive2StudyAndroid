package com.drive2study.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.drive2study.AppActivity;
import com.drive2study.Model.DriveRide;
import com.drive2study.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    public interface MapFragmentDelegate {
        void onMarkerTap(DriveRide dr);
    }

    public MapFragmentDelegate delegate;

    final double MAP_DEFAULT_LATITUDE = 32.087912;
    final double MAP_DEFUALT_LONGITUDE = 34.790725;
    final int MAP_DEFUALT_ZOOM = 12;

    GoogleMap myGoogleMap;
    MapView mapView;
    Map<Marker, DriveRide> mapMarkersToDr;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapMarkersToDr = new HashMap<>();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());
        myGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(MAP_DEFAULT_LATITUDE, MAP_DEFUALT_LONGITUDE)).zoom(MAP_DEFUALT_ZOOM).bearing(0).build();

        myGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        myGoogleMap.setOnMarkerClickListener(marker -> {
            DriveRide dr = mapMarkersToDr.get(marker);
            if (delegate != null && dr != null)
                delegate.onMarkerTap(dr);
            return true;
        });

        AppActivity.dataModel = ViewModelProviders.of(this).get(DriveRideListViewModel.class);
        AppActivity.dataModel.getData().observe(this, driveRideList -> {
            if(driveRideList != null)
                myGoogleMap.clear();
                addAllMapMarkers(driveRideList);
        });
    }

    private void addAllMapMarkers(List<DriveRide> driveRideList) {
        for (final DriveRide dr : driveRideList) {
            if (dr.getCoordinates() != null) {
                MarkerOptions markerOptions = new MarkerOptions().position(dr.getCoordinates()).icon(bitmapDescriptorFromVector(getActivity(), dr.getType())).title(dr.getUserName());

                Marker marker = myGoogleMap.addMarker(markerOptions);
                mapMarkersToDr.put(marker, dr);
                Log.d("TAG", "Marker added");
            }
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, String driverOrRider) {
        @DrawableRes int vectorDrawableResourceId = driverOrRider.equals(DriveRide.DRIVER) ? R.drawable.ic_map_car : R.drawable.ic_map_student;
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        if (background != null) {
            background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        }
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        if (vectorDrawable != null) {
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        }
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        if (context instanceof MapFragmentDelegate) {
            delegate = (MapFragmentDelegate) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_add).setVisible(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        delegate = null;
    }
}