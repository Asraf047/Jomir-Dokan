package com.example.amanullah.myapplication50;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
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
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

public class MainActivity4 extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener,GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    LatLng centerPolygon,current=new LatLng(37.4, -122.1);
    Marker searchMarker;
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
    Button bleft, bright, bup, bdown, bclean, badd, buttonadd,lbclean,lbdetail,textadd2,bca,bun;
    ImageButton layer1, layer2, view1, view2, buttonLocate,bedit,ibutton,center;
    String locality;
    String email;
    String name;
    String phone;
    Marker firstMarker;
    Marker marker;
    Polygon polygon;
    Polygone polygone;
    Polygone2 polygone2;
    int index = 0;
    boolean addMarker=false;

    private FirebaseAuth auth;
    private FirebaseAuth mAuth;
    AlertDialog mAlertDialog;
    static Uri imageUri;
    static ImageView photo;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String TAG = MainActivity4.class.getSimpleName();

    DatabaseReference databaseReference, parentRef, pushRef;
    String olddata;
    String area;
    String callNumber="";
    String imgUri = "https://firebasestorage.googleapis.com/v0/b/fire2-6884c.appspot.com/o/profilepics%2Famanullahoasraf%40gmail.com.jpg?alt=media&token=05851273-f038-4f4b-b27f-87fbf41ead22\n";
    TextView t1, text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12, textadd1, search1;

    FlowingDrawer mDrawer;
    CardView search;
    TextView textView1, textView2;
    private Button button0, button1, button2, button3, button4, button5, buttoncall;
    CircleImageView userProfile;
    TextInputLayout tl1,tl2,tl3;
    TextInputEditText te1,te2,te3;
    FloatingActionButton f1,f2,f3,f4,f5,f6,f7,f8;
    LinearLayout lclean,ldetail,lcancel,lundo;
    NumberPicker numberPicker;
    String[] typePicker={"Select type","Residential","Industrial","Recreational","Commercial","Agricultural"};

//    @BindView(R.id.btn_bottom_sheet)
//    Button btnBottomSheet;

    @BindView(R.id.bottom_sheet)
    NestedScrollView layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;
    boolean permission=false;

    public static int REQUEST_STORAGE_PERMISSION = 124;

    private boolean checkStoragePermission() {
        return ActivityCompat.checkSelfPermission(MainActivity4.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity4.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_STORAGE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission=true;
//                Toast.makeText(MainActivity4.this, "permission= "+permission, Toast.LENGTH_LONG).show();
            } else {
                permission=false;
//                Toast.makeText(MainActivity4.this, "permission= "+permission, Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

//    LocationManager mLocationManager;
//    private final LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(final Location location) {
//            //your code here
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_map4);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        if (checkStoragePermission()) {
            permission=true;
            //Toast.makeText(MainActivity4.this, "permission= "+permission, Toast.LENGTH_LONG).show();
        } else {
            requestStoragePermission();
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

//        Window window=this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(0x00000000);
//        }

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

        //mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
//                LOCATION_REFRESH_DISTANCE, mLocationListener);

        //Toast.makeText(getApplicationContext(), "isNetworkAvailable: " + isNetworkAvailable() , Toast.LENGTH_LONG).show();

        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_DRAGGING_OPEN) {
                    Log.i("MainActivity2", "Drawer STATE_DRAGGING_OPEN");
                    if (isNetworkAvailable()) {
                        reload();
                    }
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity2", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });

        button0 = (Button) findViewById(R.id.b0);
        button1 = (Button) findViewById(R.id.b1);
        button2 = (Button) findViewById(R.id.b2);
        button3 = (Button) findViewById(R.id.b3);
        button4 = (Button) findViewById(R.id.b4);
        button5 = (Button) findViewById(R.id.b5);
        textadd2 = (Button) findViewById(R.id.text_add2);
        buttoncall = (Button) findViewById(R.id.button1);
        lbclean = (Button) findViewById(R.id.button_cl);
        lbdetail = (Button) findViewById(R.id.button_de);
        bca = (Button) findViewById(R.id.button_ca);
        bun = (Button) findViewById(R.id.button_un);
        buttonadd = (Button) findViewById(R.id.button_add);
        userProfile = (CircleImageView) findViewById(R.id.image_profile1);
        textView1 = (TextView) findViewById(R.id.textViewName);
        textView2 = (TextView) findViewById(R.id.textViewEmail);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);
        text6 = (TextView) findViewById(R.id.text6);
        text7 = (TextView) findViewById(R.id.text7);
        text8 = (TextView) findViewById(R.id.text8);
        text9 = (TextView) findViewById(R.id.text9);
        text10 = (TextView) findViewById(R.id.text10);
        text11 = (TextView) findViewById(R.id.text11);
        text12 = (TextView) findViewById(R.id.text12);
        textadd1 = (TextView) findViewById(R.id.text_add1);
        search1 = (TextView) findViewById(R.id.search1);
        tl1 = (TextInputLayout) findViewById(R.id.Price);
        tl2 = (TextInputLayout) findViewById(R.id.deail);
        te1 = (TextInputEditText) findViewById(R.id.Price1);
        te2 = (TextInputEditText) findViewById(R.id.deail1);
        f1 = (FloatingActionButton) findViewById(R.id.fab_locate);
        f2 = (FloatingActionButton) findViewById(R.id.fab_type1);
        f3 = (FloatingActionButton) findViewById(R.id.fab_type2);
        f4 = (FloatingActionButton) findViewById(R.id.left1);
        f5 = (FloatingActionButton) findViewById(R.id.right1);
        f6 = (FloatingActionButton) findViewById(R.id.up1);
        f7 = (FloatingActionButton) findViewById(R.id.down1);
        f8 = (FloatingActionButton) findViewById(R.id.fab_marker);
        lclean = (LinearLayout) findViewById(R.id.layout_button_clean);
        ldetail = (LinearLayout) findViewById(R.id.layout_button_detail);
        search = (CardView) findViewById(R.id.activityCatalogSearchContainer);
        lcancel = (LinearLayout) findViewById(R.id.layout_button_cancel);
        lundo = (LinearLayout) findViewById(R.id.layout_button_undo);

        //Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/montserrat.otf");
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-Regular.ttf");
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Sansation-Regular.ttf");
        button0.setTypeface(tf);
        button1.setTypeface(tf);
        button2.setTypeface(tf);
        button3.setTypeface(tf);
        button4.setTypeface(tf);
        button5.setTypeface(tf);
        buttoncall.setTypeface(tf);
        textView1.setTypeface(tf);
        textView2.setTypeface(tf);
        text1.setTypeface(tf);
        text2.setTypeface(tf);
        text3.setTypeface(tf);
        text4.setTypeface(tf);
        text5.setTypeface(tf);
        text6.setTypeface(tf);
        text7.setTypeface(tf);
        text8.setTypeface(tf);
        text9.setTypeface(tf);
        text10.setTypeface(tf);
        text11.setTypeface(tf);
        text12.setTypeface(tf);
        textadd1.setTypeface(tf);
        textadd2.setTypeface(tf);
        buttonadd.setTypeface(tf);
        te1.setTypeface(tf);
        te2.setTypeface(tf);
        tl1.setTypeface(tf);
        tl2.setTypeface(tf);
        lbclean.setTypeface(tf);
        lbdetail.setTypeface(tf);
        search1.setTypeface(tf2);
        bca.setTypeface(tf);
        bun.setTypeface(tf2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        search1.setSelected(true);
        search1 .setMovementMethod(new ScrollingMovementMethod());

        edit_type = findViewById(R.id.name);
        edit_price = findViewById(R.id.price);
        //InputMethodManager inputManager = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        //inputManager.hideSoftInputFromWindow(edit_type.getWindowToken(), 0);

        bleft = findViewById(R.id.left);
        bright = findViewById(R.id.right);
        bup = findViewById(R.id.up);
        bdown = findViewById(R.id.down);
        bclean = findViewById(R.id.clean);
        badd = findViewById(R.id.add);
        layer1 = findViewById(R.id.layer1);
        layer2 = findViewById(R.id.layer2);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        buttonLocate = findViewById(R.id.locate);
        bedit = findViewById(R.id.edit1);
        ibutton = findViewById(R.id.search_image);
        center = findViewById(R.id.center);
        photo = (ImageView) findViewById(R.id.photo);
        //bclean.setBackgroundResource(R.drawable.wmc);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        f4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && markers != null) {
                    markerLeft();
                }
            }
        });
        f5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && markers != null) {
                    markerRight();
                }
            }
        });
        f6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && markers != null) {
                    markerUp();
                }
            }
        });
        f7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && markers != null) {
                    markerDown();
                }
            }
        });
        lclean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0) {
                    removeEverythings();
                }
            }
        });
        lbclean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0) {
                    removeEverythings();
                }
            }
        });
        ibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openMenu();
            }
        });
        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0) {
                    drawPolygon();
                    removeEverythings();

                    f4.setVisibility(View.GONE);
                    f5.setVisibility(View.GONE);
                    f6.setVisibility(View.GONE);
                    f7.setVisibility(View.GONE);
                    f8.setVisibility(View.GONE);
                    center.setVisibility(View.GONE);
                    lclean.setVisibility(View.GONE);
                    ldetail.setVisibility(View.GONE);
                    lcancel.setVisibility(View.GONE);
                    lundo.setVisibility(View.GONE);

                    te1.setVisibility(View.GONE);
                    te2.setVisibility(View.GONE);
                    te1.setText("");
                    te2.setText("");
                    numberPicker.setValue(0);

                    search.setVisibility(View.VISIBLE);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    addMarker=false;
                }
            }
        });

        textadd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0) {
                    removeEverythings();
                }
                    f4.setVisibility(View.GONE);
                    f5.setVisibility(View.GONE);
                    f6.setVisibility(View.GONE);
                    f7.setVisibility(View.GONE);
                    f8.setVisibility(View.GONE);
                    center.setVisibility(View.GONE);
                    lclean.setVisibility(View.GONE);
                    ldetail.setVisibility(View.GONE);
                    lcancel.setVisibility(View.GONE);
                    lundo.setVisibility(View.GONE);

                    te1.setVisibility(View.GONE);
                    te2.setVisibility(View.GONE);
                    te1.setText("");
                    te2.setText("");
                    numberPicker.setValue(0);

                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    search.setVisibility(View.VISIBLE);
                    addMarker=false;

            }
        });
        bca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0) {
                    removeEverythings();
                }
                f4.setVisibility(View.GONE);
                f5.setVisibility(View.GONE);
                f6.setVisibility(View.GONE);
                f7.setVisibility(View.GONE);
                f8.setVisibility(View.GONE);
                center.setVisibility(View.GONE);
                lclean.setVisibility(View.GONE);
                ldetail.setVisibility(View.GONE);
                lcancel.setVisibility(View.GONE);
                lundo.setVisibility(View.GONE);

                te1.setVisibility(View.GONE);
                te2.setVisibility(View.GONE);
                te1.setText("");
                te2.setText("");
                numberPicker.setValue(0);

                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                search.setVisibility(View.VISIBLE);
                addMarker=false;
            }
        });
        lcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0) {
                    removeEverythings();
                }
                f4.setVisibility(View.GONE);
                f5.setVisibility(View.GONE);
                f6.setVisibility(View.GONE);
                f7.setVisibility(View.GONE);
                f8.setVisibility(View.GONE);
                center.setVisibility(View.GONE);
                lclean.setVisibility(View.GONE);
                ldetail.setVisibility(View.GONE);
                lcancel.setVisibility(View.GONE);
                lundo.setVisibility(View.GONE);

                te1.setVisibility(View.GONE);
                te2.setVisibility(View.GONE);
                te1.setText("");
                te2.setText("");
                numberPicker.setValue(0);

                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                search.setVisibility(View.VISIBLE);
                addMarker=false;
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button3.getText().equals("Sign out")) {
                    signOut();
                } else {
                    startActivity(new Intent(MainActivity4.this, LoginActivity.class));
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button3.getText().equals("Sign out")) {
                    //startActivity(new Intent(MainActivity4.this, MainActivity7.class));
                    Intent intent = new Intent(MainActivity4.this, MainActivity7.class);
                    intent.putExtra("current",current);
                    intent.putExtra("email",email);
                    startActivity(intent);
                } else {
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(MainActivity4.this, HelpActivity.class));
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
//                Bundle args = new Bundle();
//                args.putSerializable("ARRAYLIST",(Serializable)allmarkers);
//                intent.putExtra("BUNDLE",args);
//                intent.putParcelableArrayListExtra("myArrayListKey", allmarkers);
//                intent.putParcelableArrayListExtra("Object_list", (ArrayList<? extends Parcelable>) allmarkers);
//                intent.putExtra("markers", (Serializable) allmarkerObject);
//                startActivity(intent);

                Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
                intent.putExtra("current",current);
                startActivity(intent);

                //startActivity(new Intent(MainActivity4.this, MainActivity5.class));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity4.this, MainActivity2.class));
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f4.setVisibility(View.VISIBLE);
                f5.setVisibility(View.VISIBLE);
                f6.setVisibility(View.VISIBLE);
                f7.setVisibility(View.VISIBLE);
                f8.setVisibility(View.VISIBLE);
                center.setVisibility(View.VISIBLE);
                lclean.setVisibility(View.VISIBLE);
                ldetail.setVisibility(View.VISIBLE);
                lcancel.setVisibility(View.VISIBLE);
                lundo.setVisibility(View.VISIBLE);
//                te1.setVisibility(View.VISIBLE);
//                te2.setVisibility(View.VISIBLE);

                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                text7.setVisibility(View.GONE);
                text8.setVisibility(View.GONE);
                text9.setVisibility(View.GONE);
                text10.setVisibility(View.GONE);
                text11.setVisibility(View.GONE);
                text12.setVisibility(View.GONE);
                buttoncall.setVisibility(View.GONE);

                textadd1.setVisibility(View.VISIBLE);
                textadd2.setVisibility(View.VISIBLE);
                buttonadd.setVisibility(View.VISIBLE);
                tl1.setVisibility(View.VISIBLE);
                tl2.setVisibility(View.VISIBLE);
                te1.setVisibility(View.VISIBLE);
                te2.setVisibility(View.VISIBLE);
                numberPicker.setVisibility(View.VISIBLE);

                BottomSheetBehavior.from(layoutBottomSheet).setPeekHeight(140);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mDrawer.closeMenu();

                search.setVisibility(View.GONE);
                addMarker=true;
            }
        });

        ldetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                text7.setVisibility(View.GONE);
                text8.setVisibility(View.GONE);
                text9.setVisibility(View.GONE);
                text10.setVisibility(View.GONE);
                text11.setVisibility(View.GONE);
                text12.setVisibility(View.GONE);
                buttoncall.setVisibility(View.GONE);

                textadd1.setVisibility(View.VISIBLE);
                textadd2.setVisibility(View.VISIBLE);
                buttonadd.setVisibility(View.VISIBLE);
                tl1.setVisibility(View.VISIBLE);
                tl2.setVisibility(View.VISIBLE);
                te1.setVisibility(View.VISIBLE);
                te2.setVisibility(View.VISIBLE);
                numberPicker.setVisibility(View.VISIBLE);

                BottomSheetBehavior.from(layoutBottomSheet).setPeekHeight(140);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        lbdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                text7.setVisibility(View.GONE);
                text8.setVisibility(View.GONE);
                text9.setVisibility(View.GONE);
                text10.setVisibility(View.GONE);
                text11.setVisibility(View.GONE);
                text12.setVisibility(View.GONE);
                buttoncall.setVisibility(View.GONE);

                textadd1.setVisibility(View.VISIBLE);
                textadd2.setVisibility(View.VISIBLE);
                buttonadd.setVisibility(View.VISIBLE);
                tl1.setVisibility(View.VISIBLE);
                tl2.setVisibility(View.VISIBLE);
                te1.setVisibility(View.VISIBLE);
                te2.setVisibility(View.VISIBLE);
                numberPicker.setVisibility(View.VISIBLE);

                BottomSheetBehavior.from(layoutBottomSheet).setPeekHeight(140);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        bun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0) {
                    Marker selectedMarker = markers.get(markers.size() - 1);
//                    Double lat = selectedMarker.getPosition().latitude - difference;
//                    Double lng = selectedMarker.getPosition().longitude;
                    selectedMarker.remove();
                    markers.remove(markers.size() - 1);
//                    createMarker(locality, lat, lng);
                }
            }
        });
        lundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0) {
                    Marker selectedMarker = markers.get(markers.size() - 1);
//                    Double lat = selectedMarker.getPosition().latitude - difference;
//                    Double lng = selectedMarker.getPosition().longitude;
                    selectedMarker.remove();
                    markers.remove(markers.size() - 1);
//                    createMarker(locality, lat, lng);
                }
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

        f8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng centerLatLang = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                if(addMarker) {
                    MainActivity4.this.setMarker("local", centerLatLang.latitude, centerLatLang.longitude);
                    Toast.makeText(getApplicationContext(), "New marker added", Toast.LENGTH_LONG).show();
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity4.this, MainActivity2.class));
                callPlaceSearchIntent();
            }
        });

        buttoncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });

        //if(isNetworkAvailable()) {
        mAuth = FirebaseAuth.getInstance();
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
            loadFirebaseUser();
        } catch (Exception ex) {
            Log.i("Exception : ", " databaseReference " + ex);
        }

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        BottomSheetBehavior.from(layoutBottomSheet).setState(BottomSheetBehavior.STATE_HIDDEN);

//        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_HIDDEN:{
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

        numberPicker = (NumberPicker) findViewById(R.id.numberPicker2);
        numberPicker.setMaxValue(typePicker.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(typePicker);
        numberPicker.setValue(0);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setWrapSelectorWheel(true);
    }

    private void callPlaceSearchIntent() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            Log.i(TAG, "Place Exception:" + e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if(searchMarker != null) {
                    searchMarker.remove();
                }
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place:" + place.getLatLng());
                Log.i(TAG, "Place:" + place.getLocale());
                Log.i(TAG, "Place:" + place.getName());
                Log.i(TAG, "Place:" + place.getWebsiteUri());

                current=place.getLatLng();
                search1.setText(place.getAddress());
                CameraUpdate selectLocation = CameraUpdateFactory.newLatLngZoom( place.getLatLng(), 15.5f);
                mMap.animateCamera(selectLocation);
                Log.d("hi", "current: " + current);

                MarkerOptions options = new MarkerOptions()
                        //.alpha(0.30f)
                        .title(place.getName().toString())
                        .draggable(true)
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.m1))
                        .position(place.getLatLng())
                        .snippet(place.getAddress().toString());
                searchMarker = mMap.addMarker(options);
                searchMarker.setTag("search");
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }
        }
    }

    /**
     * manually opening / closing bottom sheet on button click
     */

    //@OnClick(R.id.btn_bottom_sheet)
//    public void toggleBottomSheet() {
//        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            //btnBottomSheet.setText("Close sheet");
//        } else {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            //btnBottomSheet.setText("Expand sheet");
//        }
//    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //if(isNetworkAvailable()) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        //}
    }

    boolean myloc;
    private void requestLocationUpdates() {
        myloc=true;
        mMap.setMapType(mMap.MAP_TYPE_NORMAL);
        f2.setVisibility(View.GONE);
        f3.setVisibility(View.VISIBLE);
        search1.setText("Where to?");
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        //request.setExpirationDuration(10000);
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
                        if(myloc) {
                            mMap.animateCamera(yourLocation);
                            current=new LatLng(lat,lng);
                            myloc=false;
                            Log.d("hi", "current: " + current);
                        }
                    }
                }
            }, null);
        }
    }

//    public void locateMe() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//            Criteria criteria = new Criteria();
//            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//            String provider = locationManager.getBestProvider(criteria, false);
//            Location location = locationManager.getLastKnownLocation(provider);
//            double lat = location.getLatitude();
//            double lng = location.getLongitude();
//            LatLng coordinate = new LatLng(lat, lng);
//            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 13);
//            mMap.animateCamera(yourLocation);
//
//        } else {
//            final AlertDialog alertDialogGPS = new AlertDialog.Builder(this).create();
//            alertDialogGPS.setTitle("Info");
//            alertDialogGPS.setMessage("Looks like you have not given GPS permissions. Please give GPS permissions and return back to the app.");
//            alertDialogGPS.setIcon(android.R.drawable.ic_dialog_alert);
//            alertDialogGPS.setButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intentSettings = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
//                    startActivity(intentSettings);
//                    alertDialogGPS.dismiss();
//                }
//            });
//            alertDialogGPS.show();
//        }
//    }

    public void loadFirebaseUser() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildAdded", "addChildEventListener" + s);
                String usersEmail= null;
                String usersName = null;
                String usersArea= null;
                String usersPrice = null;
                String usersType = null;
                String usersAddress= null;
                String usersDetail = null;
                String usersPhone = null;
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
                        usersPhone = helpDataSnapshotChild.child("phone").getValue(String.class);
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

                Log.i("size : ", " countUser " + (alluser.size() - 1));
                drawPolygonObjects3(usersName, usersEmail, childLatLngs, alluser.size() - 1,usersArea, usersPrice,usersAddress, usersDetail,usersType,usersPhone);
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
                int cp = 0;
                int cm = 0;

                for (User user : alluser) {
                    if (user.getKey().equals(dataSnapshot.getKey())) {
                        cp = user.getPolyCount();
                        cm = user.getMarkerCount();
                        break;
                    }
                }
                allpolygones.get(cp).remove();
                allpolygones.set(cp, null);
                allmarkers.get(cm).remove();
                allmarkers.set(cm, null);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildMoved", "addChildEventListener" + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("onCalcnelle", databaseError.toString() + "addChildEventListener");
            }
        });
    }


    public void add(Polygone2 polygone2) {
        try {
            String detail="Spacious dual level Mediterranean style home in coveted Greenbrae Hills overlooks Bon Air shopping center. Comfortable yet stylish floor plan offers open living area, relaxing fireplace, formal dining and brightly renovated eat-in kitchen. Wood floors, stairs and pano windows lead to majestic master suite w/full bath, large closet + Mt Tam view deck. Two adjoining bedrooms + full bath and access to terraced yard complete this Best of Marin home!";
            if(!email.equals("") && !name.equals("")) {
                pushRef = databaseReference.push();
                //pushRef.keepSynced(true);

                HashMap<String, String> infoMap = new HashMap<String, String>();
                infoMap.put("email", email);
                infoMap.put("name", name);
                infoMap.put("price", polygone2.getPrice());
                infoMap.put("area",  polygone2.getArea());
                infoMap.put("type",  polygone2.getName());
                infoMap.put("address", polygone2.getAddress());
                //infoMap.put("details", polygone2.getDetails());
                infoMap.put("details", detail);
                infoMap.put("phone", phone);

                ArrayList finalList = new ArrayList();
                finalList.addAll(polygone2.getAllLatLng());
                finalList.add(infoMap);
                pushRef.setValue(finalList);

                Toast.makeText(getApplicationContext(), "Successfully add: " + name, Toast.LENGTH_LONG).show();
            } else {
                Log.i("child of : ", "Not Login ");
                Toast.makeText(getApplicationContext(), "Please, Login to Add Land" , Toast.LENGTH_LONG).show();
            }
        } catch (Exception exception) {
            Log.i("child of : ", " add " + exception);
            Toast.makeText(getApplicationContext(), "Add Exception occurs" + exception, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMapType(mMap.MAP_TYPE_HYBRID);

        if (mMap != null) {

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    if(addMarker) {
                        MainActivity4.this.setMarker("local", latLng.latitude, latLng.longitude);
                    }
                }
            });
        }

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                //Log.e("mapDrag", "DragStart : " + marker.getPosition());
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                //Log.e("mapDrag", "Drag : " + marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                current=marker.getPosition();
                Log.e("mapDrag", "DragEnd : " + marker.getPosition());
                current=searchMarker.getPosition();
                Log.e("mapDrag", "DragEnd current: " + current);
            }
        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                requestLocationUpdates();
//                CameraPosition INIT = new CameraPosition.Builder()
//                        .target(current)
//                        //.target(new LatLng(37.4, -122.1))
//                        .zoom(17.5F)
//                        .bearing(0) // orientation
//                        .tilt(0) // viewing angle
//                        .build();
                mMap.setBuildingsEnabled(true);
                //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
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
    private void setMarker(String locality, double lat, double lng) {

        this.locality = locality;

        if (markers.size() >= points) {
            //removeEverythings();
        }

        createMarker(locality, lat, lng);

        if (markers.size() == 1) {
            firstMarker = markers.get(markers.size() - 1);
            //Toast.makeText(this, "firstMarker.", Toast.LENGTH_LONG).show();
        }
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
                .title(String.valueOf(new LatLng(lat, lng)))
                //.draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pinc2))
                .position(new LatLng(lat, lng));
                //.snippet(String.valueOf(new LatLng(lat, lng)));
        Marker m1=mMap.addMarker(options);
        m1.setTag("no");
        markers.add(m1);

//        Circle circle = mMap.addCircle(new CircleOptions()
//                .center(new LatLng(lat, lng))
//                .radius(1)
//                .strokeColor(Color.rgb(0, 136, 255))
//                .fillColor(Color.argb(20, 0, 136, 255)));
//        Circles.add(circle);
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

        final String area=SphericalUtil.computeArea(latLngs)+"";
        Log.i("computeArea", ": " + area);
        double areasft=SphericalUtil.computeArea(latLngs)*10.7639;
        String areaft=new DecimalFormat("##.##").format(areasft)+"";

        latLngs.add(centerPolygon);
        //new PositionUpdate1().execute(centerPolygon, imageUri.toString(), te1.getText().toString() ,area);

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(centerPolygon.latitude, centerPolygon.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);

        //polygone = new Polygone(options, centerPolygon, "First");
        if(!te1.getText().toString().equals("") &&
                !te2.getText().toString().equals("") &&
                !typePicker[numberPicker.getValue()].equals("Select type")) {
            polygone2 = new Polygone2(
                    typePicker[numberPicker.getValue()],
                    te1.getText().toString(),
                    areaft,
                    address,
                    te2.getText().toString(),
                    latLngs);
            //drawPolygonObjects(options,centerPolygon);
            add(polygone2);
        } else {
            Log.i("child of : ", "Not Login ");
            Toast.makeText(getApplicationContext(), "Please, type price and type of Land" , Toast.LENGTH_LONG).show();
        }
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
//        Circle selectedCircle = Circles.get(Circles.size() - 1);
        Double lat = selectedMarker.getPosition().latitude;
        Double lng = selectedMarker.getPosition().longitude - difference;
        selectedMarker.remove();
//        selectedCircle.remove();
        markers.remove(markers.size() - 1);
//        Circles.remove(Circles.size() - 1);
        createMarker(locality, lat, lng);
    }

    private void markerRight() {
        Marker selectedMarker = markers.get(markers.size() - 1);
//        Circle selectedCircle = Circles.get(Circles.size() - 1);
        Double lat = selectedMarker.getPosition().latitude;
        Double lng = selectedMarker.getPosition().longitude + difference;
        selectedMarker.remove();
//        selectedCircle.remove();
        markers.remove(markers.size() - 1);
//        Circles.remove(Circles.size() - 1);
        createMarker(locality, lat, lng);
    }

    private void markerUp() {
        Marker selectedMarker = markers.get(markers.size() - 1);
//        Circle selectedCircle = Circles.get(Circles.size() - 1);
        Double lat = selectedMarker.getPosition().latitude + difference;
        Double lng = selectedMarker.getPosition().longitude;
        selectedMarker.remove();
//        selectedCircle.remove();
        markers.remove(markers.size() - 1);
//        Circles.remove(Circles.size() - 1);
        createMarker(locality, lat, lng);
    }

    private void markerDown() {
        Marker selectedMarker = markers.get(markers.size() - 1);
//        Circle selectedCircle = Circles.get(Circles.size() - 1);
        Double lat = selectedMarker.getPosition().latitude - difference;
        Double lng = selectedMarker.getPosition().longitude;
        selectedMarker.remove();
//        selectedCircle.remove();
        markers.remove(markers.size() - 1);
//        Circles.remove(Circles.size() - 1);
        createMarker(locality, lat, lng);
    }

    private void drawPolygonObjects3(final String uname, final String usersEmail, ArrayList<LatLng> latLngs, final int countUsers, String usersArea, String usersPrice, String usersAddress, String usersDetail, String usersType, String usersPhone) {
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

            final String snippetAll=usersEmail+"||"+usersArea+"||"+usersPrice+"||"+usersAddress+"||"+usersDetail+"||"+usersType+"||"+usersPhone;

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
            mMap.setOnInfoWindowClickListener(MainActivity4.this);
            mMap.setOnMarkerClickListener(MainActivity4.this);

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

            textadd1.setVisibility(View.GONE);
            textadd2.setVisibility(View.GONE);
            buttonadd.setVisibility(View.GONE);
            tl1.setVisibility(View.GONE);
            tl2.setVisibility(View.GONE);
            te1.setVisibility(View.GONE);
            te2.setVisibility(View.GONE);
            numberPicker.setVisibility(View.GONE);

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
            callNumber = separated[6];
            Log.d("hi", "getSnippet= " + marker.getSnippet());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        current=marker.getPosition();
        Log.d("hi", "current= " + current);

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        current=marker.getPosition();
        Log.d("hi", "current= " + current);

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        current=marker.getPosition();
        Log.d("hi", "current= " + current);
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
//            button3.animate()
//                    .translationY(button3.getHeight())
//                    .alpha(0.0f)
//                    .setDuration(300)
//                    .setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            button3.setVisibility(View.GONE);
//                        }
//                    });
//            Animation in = AnimationUtils.loadAnimation(MainActivity4.this, android.R.anim.fade_in);
//            button3.startAnimation(in);
//            button3.setVisibility(View.VISIBLE);
            allmarkers.get(u.userCount).setIcon(BitmapDescriptorFactory.fromBitmap(createCustomMarker3(MainActivity4.this, u.bmp, "")));
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
//        final AlertDialog.Builder d = new AlertDialog.Builder(MainActivity4.this);
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


    public class UserData
    {
        public String address;
        public double area;
        public String state;
        public String knownName;
    }

    public class PositionUpdate2 extends AsyncTask<Object, Void, UserData> {
        @Override
        protected UserData doInBackground(Object... params) {
            LatLng latLng = (LatLng)params[0];
            double area=(double)params[1];

            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(MainActivity4.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            UserData userData=new UserData();
            userData.address=address;
            userData.area=area;
            return  userData;
        }

        @Override
        protected void onPostExecute(UserData u) {
            text8.setText(u.address);
            //text9.setText(new DecimalFormat("##.##").format(u.area)+" Squre Feet");
            double price=u.area*30;
            text9.setText(""+new DecimalFormat("##.##").format(price)+" USD");
//            text5.setText(u.state);
//            text6.setText(u.knownName);
        }

    }


    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }


    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            button0.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            //mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            String displayName = user.getDisplayName();
            String displayEmail = user.getEmail();
            Uri profileUri = user.getPhotoUrl();

            for (UserInfo userInfo : user.getProviderData()) {
                Log.i("display:", "userInfo ");
                if (displayName == null && userInfo.getDisplayName() != null) {
                    displayName = userInfo.getDisplayName();
                }
                if (displayEmail == null && userInfo.getEmail() != null) {
                    displayEmail = userInfo.getEmail();
                }
                if (profileUri == null && userInfo.getPhotoUrl() != null) {
                    profileUri = userInfo.getPhotoUrl();
                }
            }

            if (profileUri != null) {
                try{
                    Glide.with(this)
                            .load(profileUri)
                            .into(userProfile);
                    //imageUri = profileUri;
                    //Log.i("display:", "url "+user.getPhotoUrl().toString());
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "Exception: " + ex , Toast.LENGTH_LONG).show();
                }

            }
            if (displayName != null) {
                Log.i("display:", "name " + displayName);
                String[] separated = displayName.split("\\|\\|");
                name=separated[0];
                phone=separated[1];
                textView1.setText(separated[0]);
                //textView1.setText(displayName);
            }
            if (displayName != null) {
                Log.i("display:", "name " + displayEmail);
                email=displayEmail;
                textView2.setText(displayEmail);
            }

            button3.setText(R.string.signed_out);
//            textView1.setText(user.getDisplayName());
//            textView2.setText(user.getEmail());

//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground( final Void ... params ) {
//                    // something you know that will take a few seconds
//                    Bitmap bmp = null;
//                    URL urlimage = null;
//
//                    Log.i("getKeysRef ", "uri="+imgUri);
//                    try {
//                        urlimage = new URL(imgUri);
//                        bmp = BitmapFactory.decodeStream(urlimage.openConnection().getInputStream());
//                        //Log.i("center","doInBackground try"+imgUri);
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute( final Void result ) {
//                    // continue what you are doing...
//
//                }
//            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

//            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
//            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
//            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);
//
//            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
        } else {
            name="";
            email="";
            button3.setText(R.string.signed_in);
            button0.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            textView1.setText("Username");
            textView2.setText("sample@gmail.com");
            userProfile.setImageResource(R.drawable.avatar2);

//            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
//            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void reload() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void call() {
        String uri = "tel:"+callNumber;
//        String uri = "tel:"+t2.getText().toString();
        //Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        try {
            //startActivity(callIntent);
            startActivity(dialIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity4.this, "CALL faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}