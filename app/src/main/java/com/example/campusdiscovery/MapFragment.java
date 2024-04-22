package com.example.campusdiscovery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.GEMFFileArchive;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileFilesystemProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.modules.NetworkAvailabliltyCheck;
import org.osmdroid.tileprovider.modules.TileWriter;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MapFragment extends Fragment {


    MapView mMapView;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView =  inflater.inflate(R.layout.fragment_map, container, false);
        org.osmdroid.config.Configuration.getInstance().setUserAgentValue(getContext().getPackageName());
        mMapView = (MapView) mainView.findViewById(R.id.mapView);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        GeoPoint point = new GeoPoint(33.775618,-84.396285);
        mMapView.getController().setCenter(point);
        mMapView.getController().setZoom(16.0);
        Random r = new Random();
        for (Event curr : FilterFragment.getFilteredEventList()) {
            GeoPoint coordinates = Database.getDB().getLocationList().get(curr.getLocation());
            coordinates.setLatitude(coordinates.getLatitude() + r.nextDouble() * 0.00001 -  r.nextDouble() * 0.00001);
            coordinates.setLongitude(coordinates.getLongitude() +  r.nextDouble() * 0.00001 -  r.nextDouble() * 0.00001);
            Marker marker = new Marker(mMapView);
            marker.setPosition(coordinates);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    StringBuilder eventInfo = new StringBuilder();
                    eventInfo.append("Title: " + curr.getTitle() + "\n");
                    eventInfo.append("Time: " + curr.getTime() + "\n");
                    eventInfo.append("Host: " + curr.getHost().getName() + "\n");
                    eventInfo.append("Location: " + curr.getLocation() + "\n");
                    eventInfo.append("Description: " + curr.getDescription() + "\n");
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Event Info")
                            .setMessage(eventInfo.toString())
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                    return false;
                }
            });
            mMapView.getOverlays().add(marker);
        }
        return mainView;
    }
}