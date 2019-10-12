package com.example.amanullah.myapplication50;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity3 extends FragmentActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng centerPolygon;
    ArrayList<Polygon> allpolygones = new ArrayList<Polygon>();
    ArrayList<Marker> allmarkers = new ArrayList<Marker>();
    ArrayList<Polygone> polygones = new ArrayList<Polygone>();
    ArrayList<Polygone2> polygones2 = new ArrayList<Polygone2>();
    ArrayList<Marker> markers = new ArrayList<Marker>();
    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
    ArrayList<LatLng> childLatLngs = new ArrayList<LatLng>();
    ArrayList<Circle> Circles = new ArrayList<Circle>();
    static final int points = 10;
    static final Double difference = 0.000002;
    Polygon shape;
    EditText name;
    Button bleft, bright, bup, bdown, bclean;
    ImageButton layer1, layer2, view1, view2;
    String locality;
    Marker firstMarker;
    Marker marker;
    Polygon polygon;
    Polygone polygone;
    Polygone2 polygone2;
    int index = 0;

    private FirebaseAuth auth;
    private FirebaseAuth mAuth;
    static Uri imageUri;
    static ImageView photo;

    DatabaseReference databaseReference, parentRef;
    String olddata;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        name = findViewById(R.id.name);
        InputMethodManager inputManager = (InputMethodManager) MainActivity3.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(name.getWindowToken(), 0);

        bleft = findViewById(R.id.left);
        bright = findViewById(R.id.right);
        bup = findViewById(R.id.up);
        bdown = findViewById(R.id.down);
        bclean = findViewById(R.id.clean);
        layer1 = findViewById(R.id.layer1);
        layer2 = findViewById(R.id.layer2);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        photo = (ImageView) findViewById(R.id.photo);
        //bclean.setBackgroundResource(R.drawable.wmc);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && shape == null) {
                    markerLeft();
                }
            }
        });
        bright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && shape == null) {
                    markerRight();
                }
            }
        });
        bup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && shape == null) {
                    markerUp();
                }
            }
        });
        bdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && shape == null) {
                    markerDown();
                }
            }
        });
        bclean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0) {
                    drawPolygon();
                    removeEverythings();
                    mMap.setMapType(mMap.MAP_TYPE_HYBRID);
                }
            }
        });

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(mMap.MAP_TYPE_HYBRID);
                view2.setVisibility(View.GONE);
                view1.setVisibility(View.VISIBLE);
            }
        });
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(mMap.MAP_TYPE_NORMAL);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
            }
        });


        layer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition INIT = new CameraPosition.Builder()
                        .target(new LatLng(37.4, -122.1))
                        .zoom(17.5F)
                        .bearing(0) // orientation
                        .tilt(0) // viewing angle
                        .build();
                mMap.setBuildingsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
                layer1.setVisibility(View.GONE);
                layer2.setVisibility(View.VISIBLE);
            }
        });
        layer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition INIT = new CameraPosition.Builder()
                        .target(new LatLng(37.4, -122.1))
                        .zoom(17.5F)
                        .bearing(300F) // orientation
                        .tilt(50F) // viewing angle
                        .build();
                mMap.setBuildingsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
                layer2.setVisibility(View.GONE);
                layer1.setVisibility(View.VISIBLE);
            }
        });


        auth = FirebaseAuth.getInstance();
        loadUserInformation();

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception ex) {

        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        parentRef = database.getReference();
        databaseReference = parentRef.child("Locations");
        databaseReference.keepSynced(true);

        //make();
        //databaseReference.removeValue();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (Polygon line : allpolygones) {
                    line.remove();
                }
                allpolygones.clear();
                for (Marker line : allmarkers) {
                    line.remove();
                }
                allmarkers.clear();
                for (DataSnapshot helpDataSnapshot1obj : dataSnapshot.getChildren()) {
                    olddata = "";
                    childLatLngs.clear();
                    olddata = helpDataSnapshot1obj.getKey();
                    //Log.i("getKey: ", olddata);
                    Toast.makeText(getApplicationContext(), "getKey: " + olddata, Toast.LENGTH_LONG).show();
                    for (DataSnapshot helpDataSnapshotChild : dataSnapshot.child(olddata).getChildren()) {
                        try {
                            PolygoneLatLng2 childLatLng = helpDataSnapshotChild.getValue(PolygoneLatLng2.class);
                            //Log.i("child of : ", helpDataSnapshotChild.getKey() + "  " + childLatLng.getLatitude());
                            Toast.makeText(getApplicationContext(), "child of " + helpDataSnapshotChild.getKey() + "\n", Toast.LENGTH_LONG).show();
                            childLatLngs.add(childLatLng.getLatLng());
                        } catch (Exception ex) {
                            Log.i("child of : ", " Exception ");
                            Toast.makeText(getApplicationContext(), "child of " + helpDataSnapshotChild.getKey() + "Exception", Toast.LENGTH_LONG).show();
                        }
                    }
//                    polygone2 = new Polygone2(olddata, childLatLngs);
                    //drawPolygonObjects2(olddata, childLatLngs);

                    polygones2.add(polygone2);
                }

                new Thread(new Runnable() {
                    public void run(){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadObjects();
                            }
                        });

                    }
                }).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Exception databaseError" + "\n", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void add(Polygone2 polygone2) {
        try {
            Log.i("child of : ", " add ");
            databaseReference.child(polygone2.getName()).setValue(polygone2.getAllLatLng());
            Toast.makeText(getApplicationContext(), "Success name: " + polygone.getName(), Toast.LENGTH_LONG).show();
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), "Exception occurs", Toast.LENGTH_LONG).show();
        }
    }

    private void loadUserInformation() {
        final FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String displayName = user.getDisplayName();
            Uri profileUri = user.getPhotoUrl();

            for (UserInfo userInfo : user.getProviderData()) {
                Log.i("display:", "userInfo ");
                if (displayName == null && userInfo.getDisplayName() != null) {
                    displayName = userInfo.getDisplayName();
                }
                if (profileUri == null && userInfo.getPhotoUrl() != null) {
                    profileUri = userInfo.getPhotoUrl();
                }
            }

            if (profileUri != null) {
                Glide.with(this)
                        .load(profileUri)
                        .into(photo);
                imageUri = profileUri;
                //Log.i("display:", "url "+user.getPhotoUrl().toString());
            }

            if (displayName != null) {
                Log.i("display:", "name " + displayName);
                name.setText(displayName);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MainActivity3.this.setMarker("local", latLng.latitude, latLng.longitude);
                }
            });
            //mMap.setOnPolygonClickListener(customPoly());

            //make();
            //calldrawPolygonObjects();
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
/*
                LatLng customMarkerLocationOne = new LatLng(24.823580, 67.025625);
                LatLng customMarkerLocationTwo = new LatLng(24.824580, 67.025625);
                LatLng customMarkerLocationThree = new LatLng(24.820211, 67.029465);


                mMap.addMarker(new MarkerOptions().position(customMarkerLocationOne).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MainActivity.this, R.drawable.my_dp, "Yasir Ameen"))));
                mMap.addMarker(new MarkerOptions().position(customMarkerLocationTwo).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MainActivity.this, R.drawable.janet, "Mary Jane"))));

                mMap.addMarker(new MarkerOptions().position(customMarkerLocationThree).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MainActivity.this, R.drawable.john, "Janet John"))));

                //LatLngBound will cover all your marker on Google Maps
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(customMarkerLocationOne); //Taking Point A (First LatLng)
                builder.include(customMarkerLocationThree); //Taking Point B (Second LatLng)
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                mMap.moveCamera(cu);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(7), 2000, null);

                LatLng coordinate = new LatLng(24.823580, 67.025625);
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 7);
                mMap.animateCamera(location);
                mMap.setMapType(mMap.MAP_TYPE_HYBRID);

                LatLng SYDNEY = new LatLng(-33.88,151.21);
                final LatLng MOUNTAIN_VIEW = new LatLng(37.4, -122.1);

// Move the camera instantly to Sydney with a zoom of 15.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 15));

// Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(MOUNTAIN_VIEW)      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                //LatLng centerMap = mMap.getCameraPosition().target;
                //Log.i("centerMap: ", centerMap.toString());
                CameraPosition INIT = new CameraPosition.Builder()
                                .target(new LatLng(25.400786777632458, 68.36694510653615))
                                .zoom( 17.5F )
                                .bearing( 300F) // orientation
                                .tilt( 50F) // viewing angle
                                .build();
                // use GooggleMap mMap to move camera into position
                mMap.setBuildingsEnabled(true);
                mMap.animateCamera( CameraUpdateFactory.newCameraPosition(INIT) );
*/
                CameraPosition INIT = new CameraPosition.Builder()
                        .target(new LatLng(37.4, -122.1))
                        .zoom(17.5F)
                        .bearing(0) // orientation
                        .tilt(0) // viewing angle
                        .build();
                mMap.setBuildingsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
                //CameraPosition currentposition=mMap.getCameraPosition();
                //Log.i("center"+currentposition.tilt,currentposition.bearing+"ends");
            }
        });


    }

    private void setMarker(String locality, double lat, double lng) {

        this.locality = locality;

        if (markers.size() >= points) {
            //removeEverythings();
        }

        createMarker(locality, lat, lng);

        if (markers.size() == 1) {
            firstMarker = markers.get(markers.size() - 1);
            Toast.makeText(this, "firstMarker.", Toast.LENGTH_LONG).show();
        }

        //if (markers.size() == points) {
        //drawPolygon();
        //Toast.makeText(this, "drawPolygon list of markers.", Toast.LENGTH_LONG).show();
        //}

        //checkMarker();

    }

    private void checkMarker() {
        if (markers.size() >= 3) {
            if (SphericalUtil.computeDistanceBetween(firstMarker.getPosition(), markers.get(markers.size() - 1).getPosition()) < 1) {
                drawPolygon();
                Toast.makeText(this, "drawPolygon 5.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void createMarker(String locality, double lat, double lng) {
        MarkerOptions options = new MarkerOptions()
                //.alpha(0.30f)
                .title(locality)
                //.draggable(true)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon))
                .position(new LatLng(lat, lng))
                .snippet(String.valueOf(new LatLng(lat, lng)));
        markers.add(mMap.addMarker(options));

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat, lng))
                .radius(1)
                .strokeColor(Color.rgb(0, 136, 255))
                .fillColor(Color.argb(20, 0, 136, 255)));
        Circles.add(circle);

        //checkMarker();
    }


    private void drawPolygon() {
        PolygonOptions options = new PolygonOptions()
                .fillColor(0x330000FF)
                .strokeColor(Color.RED)
                .strokeWidth(3);

        for (int i = 0; i < markers.size(); i++) {
            options.add(markers.get(i).getPosition());
            latLngs.add(markers.get(i).getPosition());
            //Log.i(""+markers.get(i).getPosition().latitude,""+markers.get(i).getPosition().longitude);
        }
        shape = mMap.addPolygon(options);

        centerPolygon = getPolygonCenterPoint(markers);
        latLngs.add(centerPolygon);
        //Log.i("center"+centerPolygon.latitude,centerPolygon.longitude+"ends");
        MarkerOptions optionCenter = new MarkerOptions()
                .alpha(1.0f)
                .infoWindowAnchor(.6f, 1.0f)
                .title(name.getText().toString())
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker2(MainActivity3.this, name.getText().toString())))
                .position(new LatLng(centerPolygon.latitude, centerPolygon.longitude))
                .snippet("This is my land.");
        markers.add(mMap.addMarker(optionCenter));

        polygone = new Polygone(options, centerPolygon, "First");
       // polygone2 = new Polygone2(name.getText().toString(), latLngs);
        //drawPolygonObjects(options,centerPolygon);
        add(polygone2);
    }


    private void removeEverythings() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();

        latLngs.clear();

        for (Circle circle : Circles) {
            circle.remove();
        }
        Circles.clear();

        if (shape != null) {
            shape.remove();
            shape = null;
        }
        Toast.makeText(this, "removeEverythings list of markers.", Toast.LENGTH_LONG).show();
    }


    private LatLng getPolygonCenterPoint(ArrayList<Marker> polygonPointsList) {
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < polygonPointsList.size(); i++) {
            LatLng position = polygonPointsList.get(i).getPosition();
            builder.include(position);
        }
        LatLngBounds bounds = builder.build();
        centerLatLng = bounds.getCenter();

        return centerLatLng;
    }


    private void markerLeft() {
        Marker selectedMarker = markers.get(markers.size() - 1);
        Circle selectedCircle = Circles.get(Circles.size() - 1);
        Double lat = selectedMarker.getPosition().latitude;
        Double lng = selectedMarker.getPosition().longitude - difference;
        selectedMarker.remove();
        selectedCircle.remove();
        markers.remove(markers.size() - 1);
        Circles.remove(Circles.size() - 1);
        createMarker(locality, lat, lng);
    }

    private void markerRight() {
        Marker selectedMarker = markers.get(markers.size() - 1);
        Circle selectedCircle = Circles.get(Circles.size() - 1);
        Double lat = selectedMarker.getPosition().latitude;
        Double lng = selectedMarker.getPosition().longitude + difference;
        selectedMarker.remove();
        selectedCircle.remove();
        markers.remove(markers.size() - 1);
        Circles.remove(Circles.size() - 1);
        createMarker(locality, lat, lng);
    }

    private void markerUp() {
        Marker selectedMarker = markers.get(markers.size() - 1);
        Circle selectedCircle = Circles.get(Circles.size() - 1);
        Double lat = selectedMarker.getPosition().latitude + difference;
        Double lng = selectedMarker.getPosition().longitude;
        selectedMarker.remove();
        selectedCircle.remove();
        markers.remove(markers.size() - 1);
        Circles.remove(Circles.size() - 1);
        createMarker(locality, lat, lng);
    }

    private void markerDown() {
        Marker selectedMarker = markers.get(markers.size() - 1);
        Circle selectedCircle = Circles.get(Circles.size() - 1);
        Double lat = selectedMarker.getPosition().latitude - difference;
        Double lng = selectedMarker.getPosition().longitude;
        selectedMarker.remove();
        selectedCircle.remove();
        markers.remove(markers.size() - 1);
        Circles.remove(Circles.size() - 1);
        createMarker(locality, lat, lng);
    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView) marker.findViewById(R.id.name);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }


    private void calldrawPolygonObjects() {
        for (int i = 0; i < polygones.size(); i++) {
            drawPolygonObjects(polygones.get(i).getPolygonOptions(), polygones.get(i).getCenter());
            Toast.makeText(this, "objects Print name = " + polygones.get(i).getName(), Toast.LENGTH_LONG).show();
        }
    }

    private void checkPolygonObjects() {
        for (int i = 0; i < polygones.size(); i++) {
            //list1.equals(list2)
            drawPolygonObjects(polygones.get(i).getPolygonOptions(), polygones.get(i).getCenter());
            Toast.makeText(this, "objects Print name = " + polygones.get(i).getName(), Toast.LENGTH_LONG).show();
        }
    }


    private void drawPolygonObjects(PolygonOptions options, LatLng centerPolygonObject) {

        mMap.addPolygon(options);

        centerPolygon = centerPolygonObject;
        //Log.i("center"+centerPolygon.latitude,centerPolygon.longitude+"ends");
        MarkerOptions optionCenter = new MarkerOptions()
                .alpha(1.0f)
                .infoWindowAnchor(.6f, 1.0f)
                .title(name.getText().toString())
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker2(MainActivity3.this, name.getText().toString())))
                .position(new LatLng(centerPolygon.latitude, centerPolygon.longitude))
                .snippet("This is my land.");
        mMap.addMarker(optionCenter);

    }

    private void drawPolygonObjects2(String name, ArrayList<LatLng> latLngs) {
        PolygonOptions childOption = new PolygonOptions()
                .fillColor(0x330000FF)
                .strokeColor(Color.RED)
                .strokeWidth(3);

        for (int i = 0; i < latLngs.size() - 1; i++) {
            childOption.add(latLngs.get(i));
            //Log.i(""+latLngs.get(i).latitude,""+latLngs.get(i).longitude);
        }
        polygon = mMap.addPolygon(childOption);
        allpolygones.add(polygon);
        Log.i("computeArea", ": " + SphericalUtil.computeArea(latLngs));

        centerPolygon = latLngs.get(latLngs.size() - 1);
        Log.i("center"+centerPolygon.latitude,centerPolygon.longitude+"ends");
        MarkerOptions optionCenter = new MarkerOptions()
                .alpha(1.0f)
                .infoWindowAnchor(.6f, 1.0f)
                .title(name)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker2(MainActivity3.this, name)))
                .position(new LatLng(centerPolygon.latitude, centerPolygon.longitude))
                .snippet("Area: " + SphericalUtil.computeArea(latLngs) + "m2");
        marker = mMap.addMarker(optionCenter);
        allmarkers.add(marker);
    }


    private void make() {
        ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(24.823149907637347, 67.02836103737353));
        latLngs.add(new LatLng(24.822625598884024, 67.02836573123932));
        latLngs.add(new LatLng(24.822526396862127, 67.02798184007406));
        latLngs.add(new LatLng(24.82226834951275, 67.02776592224836));
        latLngs.add(new LatLng(24.822039210319144, 67.02761705964804));
        latLngs.add(new LatLng(24.822209315020014, 67.02707726508378));
        latLngs.add(new LatLng(24.824176913418903, 67.02746585011482));

        LatLng centerPolygon = new LatLng(24.823129058341056, 67.02772149816155);
        makePolygonObject(latLngs, centerPolygon);

        databaseReference.child("P").setValue(latLngs);
    }

    private void makePolygonObject(ArrayList<LatLng> latLng, LatLng centerPolygon) {
        PolygonOptions options = new PolygonOptions()
                .fillColor(0x330000FF)
                .strokeColor(Color.RED)
                .strokeWidth(3);

        for (int i = 0; i < latLng.size(); i++) {
            options.add(latLng.get(i));
        }

        this.centerPolygon = centerPolygon;

        polygones.add(new Polygone(options, this.centerPolygon, "Zero"));
        Toast.makeText(this, "object created Zero.", Toast.LENGTH_LONG).show();
    }


    public Bitmap createCustomMarker2(Context context, String _name) {

        final View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout2, null);

        final ImageView markerImage = marker.findViewById(R.id.user_dp2);
        //markerImage.setImageURI(imageUri);
        //Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        try {
            URL urlimage = new URL(imageUri.toString());
            Bitmap bmp = BitmapFactory.decodeStream(urlimage.openConnection().getInputStream());
            markerImage.setImageBitmap(bmp);
/*
            Picasso.get().load(imageUri).transform(new CircleTransform()).into(markerImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {//Use your "bitmap" here

                                    //Bitmap innerBitmap = ((BitmapDrawable) marker.getDrawable()).getBitmap();
                                    Picasso.get().load(imageUri).transform(new CircleTransform()).into(markerImage);
                                }
                            }, 100);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
*/
            //Picasso.get().load(imageUri).transform(new CircleTransform()).into(markerImage);
            Log.i("display:", "url " + "e.()");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("display:", "url " + "e.printStackTrace()");
        }
/*
        if (imageUri != null) {
            Glide.with(context)
                    .load(imageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(photo);
            Log.i("display:", "url "+imageUri.toString());
        }
*/
        TextView txt_name = (TextView) marker.findViewById(R.id.name);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }


    private void loadMarkerIcon(final Marker marker) {
        Glide.with(this)
                .asBitmap()
                .load("http://www.myiconfinder.com/uploads/iconsets/256-256-a5485b563efc4511e0cd8bd04ad0fe9e.png")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
                        marker.setIcon(icon);
                    }
                });
    }


    private void loadObjects() {
        Log.i("computeArea", ": " + polygones2.size()+"");
        for (int i = 0; i < polygones2.size(); i++) {
            Log.i("computeArea", ": " + polygones2.get(i).getName()+"");
            drawPolygonObjects2(polygones2.get(i).getName(), polygones2.get(i).getAllLatLng());
        }
    }

    private void drawPolygonObjects3(String name, ArrayList<LatLng> latLngs) {
        PolygonOptions childOption = new PolygonOptions()
                .fillColor(0x330000FF)
                .strokeColor(Color.RED)
                .strokeWidth(3);

        for (int i = 0; i < latLngs.size() - 1; i++) {
            childOption.add(latLngs.get(i));
            //Log.i(""+latLngs.get(i).latitude,""+latLngs.get(i).longitude);
        }
        polygon = mMap.addPolygon(childOption);
        allpolygones.add(polygon);
        Log.i("computeArea", ": " + SphericalUtil.computeArea(latLngs));

        centerPolygon = latLngs.get(latLngs.size() - 1);
        //Log.i("center"+centerPolygon.latitude,centerPolygon.longitude+"ends");

        new PositionUpdate1().execute(centerPolygon,imageUri.toString());
    }


    public class PositionUpdate1 extends AsyncTask<Object, Void, MarkerOptions> {

        @Override
        protected MarkerOptions doInBackground(Object... params) {
            LatLng center = (LatLng)params[0];
            String imgUri = (String) params[1];
            Bitmap bmp = null;
            URL urlimage = null;
            try {
                urlimage = new URL(imgUri);
                bmp = BitmapFactory.decodeStream(urlimage.openConnection().getInputStream());
                Log.i("center","doInBackground try"+imgUri);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new MarkerOptions()
                    .alpha(1.0f)
                    .infoWindowAnchor(.6f, 1.0f)
                    .title("title")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker3(MainActivity3.this,bmp, "name")))
                    .position(new LatLng(center.latitude, center.longitude));
                    //.snippet("Area: " + SphericalUtil.computeArea(latLngs) + "m2");
        }

        @Override
        protected void onPostExecute(MarkerOptions optionCenter) {
            //map.addMarker(new MarkerOptions().position(loc));
            //markerImage.setImageBitmap(bmp);
            marker = mMap.addMarker(optionCenter);
            allmarkers.add(marker);
            Log.i("center","onPostExecute");
        }


        public Bitmap createCustomMarker3(Context context, Bitmap bmp, String _name) {

            final View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout2, null);

            final ImageView markerImage = marker.findViewById(R.id.user_dp2);
            //markerImage.setImageURI(imageUri);
            //Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            markerImage.setImageBitmap(bmp);

            TextView txt_name = (TextView) marker.findViewById(R.id.name);
            txt_name.setText(_name);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
            marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
            marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
            marker.buildDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            marker.draw(canvas);

            return bitmap;
        }
    }

}