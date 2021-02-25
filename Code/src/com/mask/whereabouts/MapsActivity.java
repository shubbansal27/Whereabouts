package com.mask.dell.whereabouts;


import android.app.Application;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.LocationListener;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.BubbleIconFactory;
import com.google.maps.android.ui.IconGenerator;
import com.mask.dell.whereabouts.common.Session;
import com.google.android.gms.maps.*;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class MapsActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Timer mBackGroundTimer;
    private static Context context;
    final LatLng bits = new LatLng(28.363419, 75.587119);
    public MapsActivity(Context c){
        //super("myservice");

        this.mBackGroundTimer=new Timer();
        context=c;
    }

    FirebaseDatabase locDb=FirebaseDatabase.getInstance();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap= googleMap;
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.addMarker(new MarkerOptions().position(bits).title("BITS, Pilani"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bits, 16));
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        fetchList();

        long startTime=1000;
        long repeatTime=10000;
        new Timer().schedule(new TimerTask() {
            public void run() {
                try {
                    //mMap.clear();
                    //mMap.addMarker(new MarkerOptions().position(bits).title("BITS, Pilani"));
                    fetchList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, startTime, repeatTime);
        //fetchList();
    }

    private void fetchList() {
        if(locDb==null) locDb=FirebaseDatabase.getInstance();
        locDb.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot peersRef = dataSnapshot.child("Peers").child(Session.identity);
                try {
                    if (peersRef.exists()) {
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(bits).title("BITS, Pilani"));

                        for (DataSnapshot dataSnapshot1 : peersRef.getChildren()) {
                            final String peerNo = dataSnapshot1.getKey();
                            String peerLoc = (String) dataSnapshot.child("Location").child(peerNo).getValue();
                            //System.out.println("----"+peerLoc+"*******");
                            String[] latlong = peerLoc.split(",");
                            double platitude = Double.parseDouble(latlong[0]);
                            double plongitude = Double.parseDouble(latlong[1]);
                            LatLng peerLatLng = new LatLng(platitude, plongitude);
                            //******** marker title
                            Marker m1 = mMap.addMarker(new MarkerOptions()
                                    .position(peerLatLng)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).title(peerNo)
                            );
                            m1.showInfoWindow();
/**
                            TextView text = new TextView(context);
                            text.setText(peerNo);
                            IconGenerator generator = new IconGenerator(context);
                           // generator.setBackground(context.getDrawable(R.drawable.bubble_mask));
                           // generator.setContentView(text);
                            Bitmap icon = generator.makeIcon(peerNo);
                            MarkerOptions tp = new MarkerOptions().position(peerLatLng).icon(BitmapDescriptorFactory.fromBitmap(icon));
                            mMap.addMarker(tp);
**/
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    if(!marker.getTitle().equals("BITS, Pilani")) {
                                        Intent in1 = new Intent(context, ChatActivity.class).putExtra("pNo", marker.getTitle());
                                        in1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(in1);
                                    }
                                        return false;
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
