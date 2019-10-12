package com.example.amanullah.myapplication50;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends FragmentActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng centerPolygon;
    ArrayList<Polygon> allpolygones = new ArrayList<Polygon>();
    ArrayList<Marker> allmarkers = new ArrayList<Marker>();
    ArrayList<Polygone> polygones = new ArrayList<Polygone>();
    ArrayList<Polygone2> polygones2 = new ArrayList<Polygone2>();
    ArrayList<User> alluser = new ArrayList<User>();
    ArrayList<Marker> markers = new ArrayList<Marker>();
    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
    ArrayList<LatLng> childLatLngs = new ArrayList<LatLng>();
    ArrayList<Circle> Circles = new ArrayList<Circle>();
    ArrayList<String> countUser = new ArrayList<String>();
    static final int points = 10;
    static final Double difference = 0.000002;
    Polygon shape;
    EditText edit_name;
    Button bleft, bright, bup, bdown, bclean;
    ImageButton layer1, layer2, view1, view2;
    String locality;
    String email;
    String name;
    String usersEmail;
    String usersName;
    Marker firstMarker;
    Marker marker;
    Polygon polygon;
    Polygone polygone;
    Polygone2 polygone2;
    int index = 0;

    private FirebaseAuth auth;
    static Uri imageUri;
    static ImageView photo;

    DatabaseReference databaseReference, parentRef, pushRef;
    String olddata;
    String area;
    String imgUri="https://firebasestorage.googleapis.com/v0/b/fire2-6884c.appspot.com/o/profilepics%2Famanullahoasraf%40gmail.com.jpg?alt=media&token=05851273-f038-4f4b-b27f-87fbf41ead22\n";
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        edit_name = findViewById(R.id.name);
        //InputMethodManager inputManager = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        //inputManager.hideSoftInputFromWindow(edit_name.getWindowToken(), 0);

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
                if (markers.size() != 0 && markers != null) {
                    markerLeft();
                }
            }
        });
        bright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && markers != null) {
                    markerRight();
                }
            }
        });
        bup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && markers != null) {
                    markerUp();
                }
            }
        });
        bdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && markers != null) {
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
                    //mMap.setMapType(mMap.MAP_TYPE_HYBRID);
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
                LatLng centerLatLang = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                CameraPosition INIT = new CameraPosition.Builder()
                        .target(centerLatLang)
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
                LatLng centerLatLang = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                CameraPosition INIT = new CameraPosition.Builder()
                        .target(centerLatLang)
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


        try {
            //auth = FirebaseAuth.getInstance();
            //loadUserInformation();
        } catch (Exception ex) {

        }

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception ex) {

        }

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            parentRef = database.getReference();
            databaseReference = parentRef.child("Locations");
            databaseReference.keepSynced(true);
            loadFirebaseUser();
        } catch (Exception ex) {

        }

/*
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fire2-6884c.appspot.com/profilepics/"+email+".jpg");
        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.i("getKeysRef ", uri+"");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
*/
        //make();
        //databaseReference.removeValue();
/*
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
                    //Toast.makeText(getApplicationContext(), "getKey: " + olddata, Toast.LENGTH_LONG).show();
                    for (DataSnapshot helpDataSnapshotChild : dataSnapshot.child(olddata).getChildren()) {
                        try {
                            if(helpDataSnapshotChild.getKey().equals("img")){
                                //imgUri=helpDataSnapshotChild.getValue(String.class);
                                Log.i("child of : ", helpDataSnapshotChild.getKey() + " called: "+imgUri);
                            } else if(helpDataSnapshotChild.getKey().equals("email")){
                                usersEmail=helpDataSnapshotChild.getValue(String.class);
                                Log.i("child of : ", helpDataSnapshotChild.getKey() + " called: "+usersEmail);
                            } else if(helpDataSnapshotChild.getKey().equals("name")){
                                usersName=helpDataSnapshotChild.getValue(String.class);
                                Log.i("child of : ", helpDataSnapshotChild.getKey() + " called: "+usersName);
                            } else {
                                PolygoneLatLng2 childLatLng = helpDataSnapshotChild.getValue(PolygoneLatLng2.class);
                                //Log.i("child of : ", helpDataSnapshotChild.getKey() + "  " + childLatLng.getLatitude());
                                //Toast.makeText(getApplicationContext(), "child of " + helpDataSnapshotChild.getKey() + "\n", Toast.LENGTH_LONG).show();
                                childLatLngs.add(childLatLng.getLatLng());
                            }
                        } catch (Exception ex) {
                            Log.i("child of : ", " Exception ");
                            Toast.makeText(getApplicationContext(), "child of " + helpDataSnapshotChild.getKey() + "Exception", Toast.LENGTH_LONG).show();
                        }
                    }
                    polygone2 = new Polygone2(olddata, childLatLngs);
                    drawPolygonObjects3(usersName, usersEmail, childLatLngs, imgUri);
                    //polygones2.add(polygone2);
                }
                mMap.setMapType(mMap.MAP_TYPE_HYBRID);
                //loadObjects();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Exception databaseError" + "\n", Toast.LENGTH_LONG).show();
            }
        });
*/



        //mMap.setMapType(mMap.MAP_TYPE_HYBRID);


    }

    public void loadFirebaseUser() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildAdded", "addChildEventListener" + s);
                olddata = "";
                childLatLngs.clear();
                olddata = dataSnapshot.getKey();
                long num = dataSnapshot.getChildrenCount();
                int index=0;
                for (DataSnapshot helpDataSnapshotChild : dataSnapshot.getChildren()) {
                    if(++index == num) {
                        usersEmail=helpDataSnapshotChild.child("email").getValue(String.class);
                        usersName=helpDataSnapshotChild.child("name").getValue(String.class);
                        Log.i("getKey: ", index+"getChildrenCount1: "+num+usersEmail+usersName);
                        break;
                    }
                    Log.i("getKey: ", index+"getChildrenCount2: "+num);
                    try {
                        PolygoneLatLng2 childLatLng = helpDataSnapshotChild.getValue(PolygoneLatLng2.class);
                        childLatLngs.add(childLatLng.getLatLng());
                    } catch (Exception ex) {
                        Log.i("child of : ", " Exception ");
                        Toast.makeText(getApplicationContext(), "child of " + helpDataSnapshotChild.getKey() + "Exception", Toast.LENGTH_LONG).show();
                    }
                }
                countUser.add(olddata);

                User user=new User();
                user.setKey(olddata);
                alluser.add(user);

                Log.i("size : ", " countUser "+(alluser.size()-1));
                drawPolygonObjects3(usersName, usersEmail, childLatLngs, alluser.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildChanged", "addChildEventListener" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("onChildRemoved", "addChildEventListener");

                olddata = "";
                olddata = dataSnapshot.getKey();
                int cp=0;
                int cm=0;

                for (User user : alluser) {
                    if(user.getKey().equals(dataSnapshot.getKey())){
                        cp=user.getPolyCount();
                        cm=user.getMarkerCount();
                        break;
                    }
                }
                allpolygones.get(cp).remove();
                allpolygones.set(cp,null);
                allmarkers.get(cm).remove();
                allmarkers.set(cm,null);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildMoved", "addChildEventListener"+s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCalcnelle", databaseError.toString()+"addChildEventListener");
            }
        });
    }


    public void add(Polygone2 polygone2) {
        try {
            pushRef = databaseReference.push();
            pushRef.keepSynced(true);
            //Log.i("child of : ", " add ");
            //String child=email+":"+System.currentTimeMillis();
//            pushRef.setValue(polygone2.getAllLatLng());
//            pushRef.child("email").setValue(email);
//            pushRef.child("name").setValue(name);
            //databaseReference.child(child).child("img").setValue(imageUri.toString());
            Toast.makeText(getApplicationContext(), "Successfully add: " + name , Toast.LENGTH_LONG).show();

//            HashMap< ArrayList<LatLng> , String > taskMap = new HashMap< ArrayList<LatLng> , String>();
//            taskMap.put(polygone2.getAllLatLng(), email);
//            pushRef.setValue(taskMap);
//            taskMap.clear();

            HashMap< String , String > infoMap = new HashMap< String , String>();
            infoMap.put("email",email);
            infoMap.put("name",name);
//            ArrayList<HashMap< String , String >> info=new ArrayList<HashMap< String , String >>();
//            info.addAll(infoMap);

            ArrayList finalList = new ArrayList();
            finalList.addAll(polygone2.getAllLatLng());
            finalList.add(infoMap);
            pushRef.setValue(finalList);

        } catch (Exception exception) {
            Log.i("child of : ", " add "+exception);
            Toast.makeText(getApplicationContext(), "Add Exception occurs"+exception, Toast.LENGTH_LONG).show();
        }
    }

    private void loadUserInformation() {
        final FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String displayName = user.getDisplayName();
            Uri profileUri = user.getPhotoUrl();
            email = user.getEmail();

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
                Log.i("display:", "imageUrl "+user.getPhotoUrl().toString());
            }

            if (displayName != null) {
                edit_name.setText(displayName);
                name = displayName;
                Log.i("display:", "name " + displayName);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);

        if (mMap != null) {

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MainActivity.this.setMarker("local", latLng.latitude, latLng.longitude);
                }
            });
            //mMap.setOnPolygonClickListener(customPoly());

            //make();
            //calldrawPolygonObjects();
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                CameraPosition INIT = new CameraPosition.Builder()
                        .target(new LatLng(37.4, -122.1))
                        .zoom(17.5F)
                        .bearing(0) // orientation
                        .tilt(0) // viewing angle
                        .build();
                mMap.setBuildingsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
                //mMap.setMapType(mMap.MAP_TYPE_HYBRID);
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
        area=SphericalUtil.computeArea(latLngs)+"";
        Log.i("computeArea", ": " + area);

        latLngs.add(centerPolygon);
        //new PositionUpdate1().execute(centerPolygon, imageUri.toString(), edit_name.getText().toString() ,area);

        //polygone = new Polygone(options, centerPolygon, "First");
//        polygone2 = new Polygone2(edit_name.getText().toString(), latLngs);
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
        //Toast.makeText(this, "removeEverythings list of markers.", Toast.LENGTH_LONG).show();
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
                .title(edit_name.getText().toString())
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker2(MainActivity.this, edit_name.getText().toString())))
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
                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker2(MainActivity.this, name)))
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
            //Picasso.get().load(imageUri).transform(new CircleTransform()).into(markerImage);
            Log.i("display:", "url " + "e.()");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("display:", "url " + "e.printStackTrace()");
        }

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

    private void loadObjects() {
        Log.i("computeArea", ": " + polygones2.size()+"");
        for (int i = 0; i < polygones2.size(); i++) {
            Log.i("computeArea", ": " + polygones2.get(i).getName()+"");
            drawPolygonObjects2(polygones2.get(i).getName(), polygones2.get(i).getAllLatLng());
        }
    }

    private void drawPolygonObjects3(final String uname, final String usersEmail, ArrayList<LatLng> latLngs, final int countUsers) {
        PolygonOptions childOption = new PolygonOptions()
                .fillColor(0x330000FF)
                .strokeColor(Color.RED)
                .strokeWidth(3);

        for (int i = 0; i < latLngs.size() - 1; i++) {
            childOption.add(latLngs.get(i));
            //Log.i(""+latLngs.get(i).latitude,""+latLngs.get(i).longitude);
        }
        if(latLngs.size()!=0) {
            polygon = mMap.addPolygon(childOption);
            allpolygones.add(polygon);
            //allpolygones.set(countUsers,polygon);

            User u= alluser.get(countUsers);
            u.setPolyCount(allpolygones.size()-1);
            //alluser.set(countUsers, u);
            final LatLng centerPolygon = latLngs.get(latLngs.size() - 1);
            Log.i("computeArea",(countUsers)+ "countUser: " + u.getPolyCount());

            //Log.i("center"+centerPolygon.latitude,centerPolygon.longitude+"ends");
            final String area=SphericalUtil.computeArea(latLngs)+"";
            Log.i("computeArea", ": " + area);

            int height = 150;
            int width = 150;
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.av2);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            //marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            MarkerOptions optionCenter = new MarkerOptions()
                    .alpha(1.0f)
                    //.infoWindowAnchor(.6f, 1.0f)
                    .title(uname)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .position(new LatLng(centerPolygon.latitude, centerPolygon.longitude))
                    .snippet("Area: " + area + "ms");
            marker = mMap.addMarker(optionCenter);
            allmarkers.add(marker);
            u.setMarkerCount(allmarkers.size()-1);
            alluser.set(countUsers, u);
            Log.i("marker",(countUsers)+ "countUser: " + u.getPolyCount()+"  "+u.getMarkerCount());

            StorageReference profileImageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fire2-6884c.appspot.com/profilepics/"+usersEmail+".jpg");
            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String userImgUri = String.valueOf(uri);
                    Log.i("getKeysRef ", usersEmail+"\n"+userImgUri);

                    new PositionUpdate1().execute(centerPolygon, userImgUri, uname, area, countUsers);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("getKeysRef ", usersEmail+"\n"+"Exception profileImageRef");
                }
            });

            //new PositionUpdate1().execute(centerPolygon, imgUri, name,area);
        }
    }

    public class Users
    {
        public Bitmap markerOptions;
        public int userCount;
    }

    public class PositionUpdate1 extends AsyncTask<Object, Void, Users> {
        @Override
        protected Users doInBackground(Object... params) {
            LatLng center = (LatLng)params[0];
            String imgUri = (String) params[1];
            String username = (String) params[2];
            String area = (String) params[3];
            int userCounts = (int) params[4];
            Bitmap bmp = null;
            URL urlimage = null;

            Log.i("getKeysRef ", "uri="+imgUri);
            try {
                urlimage = new URL(imgUri);
                Log.i("getKeysRefs ", urlimage+""+center);
                bmp = BitmapFactory.decodeStream(urlimage.openConnection().getInputStream());
                //Log.i("center","doInBackground try"+imgUri);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            MarkerOptions m=new MarkerOptions()
//                    .alpha(1.0f)
//                    //.infoWindowAnchor(.6f, 1.0f)
//                    .title(username)
//                    .draggable(true)
//                    .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker3(MainActivity.this, bmp, username)))
//                    .position(new LatLng(center.latitude, center.longitude))
//                    .snippet("Area: " + area + "ms");
            Users u=new Users();
            u.markerOptions=bmp;
            u.userCount=userCounts;
            return  u;
        }

        @Override
        protected void onPostExecute(Users u) {
            //map.addMarker(new MarkerOptions().position(loc));
            //markerImage.setImageBitmap(bmp);
            //marker = mMap.addMarker(u.markerOptions);
            //allmarkers.add(marker);

//            User us= alluser.get(u.userCount);
//            us.setMarkerCount(allmarkers.size()-1);
//            alluser.set(u.userCount, us);
            allmarkers.get(u.userCount).setIcon(BitmapDescriptorFactory.fromBitmap(u.markerOptions));
            //Log.i("marker",(u.userCount)+ "countUser: " + us.getPolyCount()+"  "+us.getMarkerCount());
            //allmarkers.set(u.userCount-1, (marker));
            //Log.i("center","onPostExecute");
            //Log.i("size","marker: "+allmarkers.size());
        }


        public Bitmap createCustomMarker3(Context context, Bitmap bmp, String _name) {

            final View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

            final CircleImageView markerImage = marker.findViewById(R.id.user_dp);
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