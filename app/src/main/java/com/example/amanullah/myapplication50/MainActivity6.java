package com.example.amanullah.myapplication50;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

public class MainActivity6 extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener,GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

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
    EditText edit_type,edit_price;
    Button bleft, bright, bup, bdown, bclean, badd;
    ImageButton layer1, layer2, view1, view2, buttonLocate,isearch;
    String locality;
    String email;
    String name;
    Marker firstMarker;
    Marker marker;
    Polygon polygon;
    Polygone polygone;
    Polygone2 polygone2;
    int index = 0;

    private FirebaseAuth auth;
    private FirebaseAuth mAuth;
    AlertDialog mAlertDialog;
    static Uri imageUri;
    static ImageView photo;

    DatabaseReference databaseReference, parentRef, pushRef;
    String olddata;
    String area;
    FloatingActionButton f1,f2,f3,f4,f5,f6,f7;
    String imgUri = "https://firebasestorage.googleapis.com/v0/b/fire2-6884c.appspot.com/o/profilepics%2Famanullahoasraf%40gmail.com.jpg?alt=media&token=05851273-f038-4f4b-b27f-87fbf41ead22\n";
    TextView tsearch, text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12;

    TextView textView1, textView2;
    private Button button0, button1, button2, button3, button4, button5, buttoncall;
    CircleImageView userProfile;
    CardView cSearch;

    ArrayList<String> list;
    LatLng onclicks,currents;

//    @BindView(R.id.btn_bottom_sheet)
//    Button btnBottomSheet;

    @BindView(R.id.bottom_sheet)
    NestedScrollView layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;
    boolean permission=false;

    public static int REQUEST_STORAGE_PERMISSION = 124;

    private boolean checkStoragePermission() {
        return ActivityCompat.checkSelfPermission(MainActivity6.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity6.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_STORAGE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission=true;
                //Toast.makeText(MainActivity6.this, "permission= "+permission, Toast.LENGTH_LONG).show();
            } else {
                permission=false;
                //Toast.makeText(MainActivity6.this, "permission= "+permission, Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map6);
        ButterKnife.bind(this);

        if (checkStoragePermission()) {
            permission=true;
            //Toast.makeText(MainActivity6.this, "permission= "+permission, Toast.LENGTH_LONG).show();
        } else {
            requestStoragePermission();
        }

        Intent i = getIntent();
        list = i.getStringArrayListExtra("key");
        onclicks = i.getExtras().getParcelable("Latlng");
        currents = i.getExtras().getParcelable("currents");

        //Toast.makeText(getApplicationContext(), "isNetworkAvailable: " + isNetworkAvailable() , Toast.LENGTH_LONG).show();


        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text7 = (TextView) findViewById(R.id.text7);
        text8 = (TextView) findViewById(R.id.text8);
        text9 = (TextView) findViewById(R.id.text9);
        text10 = (TextView) findViewById(R.id.text10);
        text11 = (TextView) findViewById(R.id.text11);
        text12 = (TextView) findViewById(R.id.text12);
        tsearch=(TextView) findViewById(R.id.search1);
        buttoncall = (Button) findViewById(R.id.button1);
        layer1 = findViewById(R.id.layer1);
        layer2 = findViewById(R.id.layer2);
        isearch=findViewById(R.id.search_image);
        f1 = (FloatingActionButton) findViewById(R.id.fab_locate);
        f2 = (FloatingActionButton) findViewById(R.id.fab_type1);
        f3 = (FloatingActionButton) findViewById(R.id.fab_type2);
        cSearch = (CardView) findViewById(R.id.activityCatalogSearchContainer);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0x50000000);  // transparent
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            window.addFlags(flags);
        }
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        //Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/montserrat.otf");
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Regular.ttf");
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Sansation-Regular.ttf");
        buttoncall.setTypeface(tf);
        text1.setTypeface(tf);
        text2.setTypeface(tf);
        text7.setTypeface(tf);
        text8.setTypeface(tf);
        text9.setTypeface(tf);
        text10.setTypeface(tf);
        text11.setTypeface(tf);
        text12.setTypeface(tf);
        tsearch.setTypeface(tf2);

        isearch.setBackgroundResource(R.drawable.ic_arrow_back_black_24dp);
        setMargins(isearch,35,0,0,0);
        isearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(mMap.MAP_TYPE_HYBRID);
                f3.setVisibility(View.GONE);
                f2.setVisibility(View.VISIBLE);
            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(mMap.MAP_TYPE_NORMAL);
                f2.setVisibility(View.GONE);
                f3.setVisibility(View.VISIBLE);
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

        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //locateMe();
                requestLocationUpdates();
            }
        });

        //if(isNetworkAvailable()) {
        //mAuth = FirebaseAuth.getInstance();
        //}
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception ex) {
            Log.i("Exception : ", " FirebaseDatabase.getInstance() " + ex);
        }

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            parentRef = database.getReference();
            databaseReference = parentRef.child("Locations");
            databaseReference.keepSynced(true);
            for (String user : list) {
                loadFirebaseUser(user);
                Log.i("size : ", " countSearchUser:: "+user);
            }
        } catch (Exception ex) {
            Log.i("Exception : ", " databaseReference " + ex);
        }


        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        BottomSheetBehavior.from(layoutBottomSheet).setState(BottomSheetBehavior.STATE_HIDDEN);

//        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_HIDDEN:{
//                          sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        //btnBottomSheet.setText("Close Sheet");
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        //btnBottomSheet.setText("Expand Sheet");
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //if(isNetworkAvailable()) {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        //}
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private void requestLocationUpdates() {
        mMap.setMapType(mMap.MAP_TYPE_NORMAL);
        f2.setVisibility(View.GONE);
        f3.setVisibility(View.VISIBLE);
        tsearch.setText("Where to?");
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Log.d("hi", "location update checkSelfPermission" + PackageManager.PERMISSION_GRANTED);
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d("hi", "location update onLocationResult" + location);
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                        LatLng coordinate = new LatLng(lat, lng);
                        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 17.5f);
                        mMap.animateCamera(yourLocation);
                    }
                }
            }, null);
        }
    }


    public void loadFirebaseUser(String user) {
        databaseReference.child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("onChildAdded", "addChildEventListener");
                String usersEmail = null;
                String usersName = null;
                String usersArea = null;
                String usersPrice = null;
                String usersType = null;
                String usersAddress = null;
                String usersDetail = null;
                olddata = "";
                childLatLngs.clear();
                olddata = dataSnapshot.getKey();
                long num = dataSnapshot.getChildrenCount();
                int index = 0;
                for (DataSnapshot helpDataSnapshotChild : dataSnapshot.getChildren()) {
                    if (++index == num) {
                        usersEmail = helpDataSnapshotChild.child("email").getValue(String.class);
                        usersName = helpDataSnapshotChild.child("name").getValue(String.class);
                        usersArea = helpDataSnapshotChild.child("area").getValue(String.class);
                        usersPrice = helpDataSnapshotChild.child("price").getValue(String.class);
                        usersType = helpDataSnapshotChild.child("type").getValue(String.class);
                        usersAddress = helpDataSnapshotChild.child("address").getValue(String.class);
                        usersDetail = helpDataSnapshotChild.child("details").getValue(String.class);
                        Log.i("getKey: ", index + "getChildrenCount1: " + num + usersEmail + usersName);
                        break;
                    }
                    Log.i("getKey: ", index + "getChildrenCount2: " + num);
                    try {
                        PolygoneLatLng2 childLatLng = helpDataSnapshotChild.getValue(PolygoneLatLng2.class);
                        childLatLngs.add(childLatLng.getLatLng());
                    } catch (Exception ex) {
                        Log.i("child of : ", " Exception ");
                        Toast.makeText(getApplicationContext(), "child of " + helpDataSnapshotChild.getKey() + "Exception", Toast.LENGTH_LONG).show();
                    }
                }
                countUser.add(olddata);

                User user = new User();
                user.setKey(olddata);
                alluser.add(user);

                //tsearch.setText(usersAddress);
                Log.i("size : ", " countUser " + (alluser.size() - 1));
                drawPolygonObjects3(usersName, usersEmail, childLatLngs, alluser.size() - 1, usersArea, usersPrice, usersAddress, usersDetail, usersType);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCalcnelle", databaseError.toString() + "addChildEventListener");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);

        if (mMap != null) {

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    //MainActivity6.this.setMarker("local", latLng.latitude, latLng.longitude);
                }
            });
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                CameraPosition INIT = new CameraPosition.Builder()
                        .target(onclicks)
                        .zoom(17.5F)
                        .bearing(0) // orientation
                        .tilt(0) // viewing angle
                        .build();
                mMap.setBuildingsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));


                MarkerOptions options = new MarkerOptions()
                        //.alpha(0.30f)
                        //.title(place.getName().toString())
                        //.draggable(true)
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.m1))
                        .position(currents);
                        //.snippet(place.getAddress().toString());
                Marker m=mMap.addMarker(options);
                m.setTag("no");

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(MainActivity6.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(currents.latitude, currents.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String address = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                tsearch.setText(city);

//                tsearch.setText(m.getTitle());
                //mMap.setMapType(mMap.MAP_TYPE_HYBRID);
                //CameraPosition currentposition=mMap.getCameraPosition();
                //Log.i("center"+currentposition.tilt,currentposition.bearing+"ends");
            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        //mMap.setOnMyLocationButtonClickListener(this);
        //mMap.setOnMyLocationClickListener(this);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    private void drawPolygonObjects3(final String uname, final String usersEmail, ArrayList<LatLng> latLngs, final int countUsers, String usersArea, String usersPrice, String usersAddress, String usersDetail, String usersType) {
        PolygonOptions childOption = new PolygonOptions()
                .fillColor(0x330000FF)
                .strokeColor(0x330000FF)
                //.strokeColor(Color.RED)
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
            alluser.set(countUsers, u);
            final LatLng centerPolygon = latLngs.get(latLngs.size() - 1);
            Log.i("computeArea",(countUsers)+ "countUser: " + u.getPolyCount());

            final String snippetAll=usersEmail+"||"+usersArea+"||"+usersPrice+"||"+usersAddress+"||"+usersDetail+"||"+usersType;

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
                    //.snippet("Area: " + new DecimalFormat("##.###").format(parseDouble(area) ) + " sm");
                    .snippet(snippetAll);
            marker = mMap.addMarker(optionCenter);
            marker.setTag("user");
            allmarkers.add(marker);
            mMap.setOnInfoWindowClickListener(MainActivity6.this);
            mMap.setOnMarkerClickListener(MainActivity6.this);

            u.setMarkerCount(allmarkers.size()-1);
            alluser.set(countUsers, u);
            Log.i("marker",(countUsers)+ "countUser: " + u.getPolyCount()+"  "+u.getMarkerCount());

            StorageReference profileImageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fire2-6884c.appspot.com/profilepics/"+usersEmail+".jpg");
            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String userImgUri = String.valueOf(uri);
                    Log.i("getKeysRef ", usersEmail+"\n"+userImgUri);

                    //new PositionUpdate1().execute(centerPolygon, userImgUri, uname, area, countUsers);
                    new PositionUpdate1().executeOnExecutor(THREAD_POOL_EXECUTOR, centerPolygon, userImgUri, uname, area, countUsers);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("getKeysRef ", usersEmail+"\n"+"Exception profileImageRef"+exception);
                }
            });

            //new PositionUpdate1().execute(centerPolygon, imgUri, name,area);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //new PositionUpdate2().execute(marker.getPosition());
        //toggleBottomSheet();
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        text1.setText(marker.getTitle());
        //text3.setText(getCompleteAddressString(marker.getPosition().latitude,marker.getPosition().longitude));
//        text3.setText(city);
//        text3.setText(state);
//        text3.setText(country);
        //Toast.makeText(getApplicationContext(), "child of " + marker.getTitle() + "  onInfoWindowClick", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getTag().equals("user")) {
            text1.setVisibility(View.VISIBLE);
            text2.setVisibility(View.VISIBLE);
            text7.setVisibility(View.VISIBLE);
            text8.setVisibility(View.VISIBLE);
            text9.setVisibility(View.VISIBLE);
            text10.setVisibility(View.VISIBLE);
            text11.setVisibility(View.VISIBLE);
            text12.setVisibility(View.VISIBLE);
            buttoncall.setVisibility(View.VISIBLE);

            BottomSheetBehavior.from(layoutBottomSheet).setPeekHeight(430);
            //double area= (double) marker.getTag();
            //area= area*100;
            //new PositionUpdate2().executeOnExecutor(THREAD_POOL_EXECUTOR, marker.getPosition(),area);
            //Proces.setThreadPriority(THREAD_PRIORITY_BACKGROUND + THREAD_PRIORITY_MORE_FAVORABLE);
            String currentString = marker.getSnippet();
            String[] separated = currentString.split("\\|\\|");
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            text1.setText(marker.getTitle());
            text2.setText((separated[1] + " Sq Ft"));
            text9.setText((separated[2] + " USD"));
            text8.setText(separated[3]);
            text12.setText(separated[4]);
            Log.d("hi", "getSnippet= " + marker.getSnippet());
            return true;
        } else {
            return false;
        }
    }




    public class Users
    {
        public Bitmap bmp;
        public int userCount;
    }

    public class PositionUpdate1 extends AsyncTask<Object, Void, Users> {
        @Override
        protected Users doInBackground(Object... params) {
            LatLng center = (LatLng)params[0];
            String imgUri = (String) params[1];
//            String username = (String) params[2];
//            String area = (String) params[3];
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

            Users u=new Users();
            u.bmp=bmp;
            u.userCount=userCounts;
            return  u;
        }

        @Override
        protected void onPostExecute(Users u) {
            allmarkers.get(u.userCount).setIcon(BitmapDescriptorFactory.fromBitmap(createCustomMarker3(MainActivity6.this, u.bmp, "")));
            Log.i("marker",(u.userCount)+ "countUser: " );
            //Log.i("size","marker: "+allmarkers.size());
        }


        public Bitmap createCustomMarker3(Context context, Bitmap bmp, String _name) {

            final View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout4, null);

            final CircleImageView markerImage = marker.findViewById(R.id.user_dp);
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




//    public void show() {
//        final AlertDialog.Builder d = new AlertDialog.Builder(MainActivity6.this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog1, null);
////        d.setTitle("Add New Member");
////        d.setMessage("(Every field must be filled)");
//        d.setView(dialogView);
//        mAlertDialog = d.create();
//        //mAlertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        //mAlertDialog.getWindow().setGravity(Gravity.BOTTOM);
//        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowManager.LayoutParams wmlp = mAlertDialog.getWindow().getAttributes();
//        wmlp.gravity = Gravity.TOP ;
//        wmlp.x = -100;   //x position
//        wmlp.y = 00;   //y position
////        ViewGroup.LayoutParams params = mAlertDialog.getWindow().getAttributes();
////        params.width = WindowManager.LayoutParams.MATCH_PARENT;
////        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
////        mAlertDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
//        mAlertDialog.show();
//    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}