package com.example.amanullah.myapplication50;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity7 extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener {

    private static final String TAG = MainActivity7.class.getSimpleName();
    private RecyclerView recyclerView;
    Intent intentOnclick;
    private List<Contact> contactList;
    private List<SearchUser> contactList2;
    private ContactsAdapter mAdapter;
    private SearchView searchView;
    ArrayList<Marker> allmarkers = new ArrayList<Marker>();
    DatabaseReference databaseReference, parentRef, pushRef;
    ArrayList<LatLng> childLatLngs = new ArrayList<LatLng>();
    ArrayList<SearchUser> alluser = new ArrayList<SearchUser>();
    List<SearchUser> contactListFiltered = new ArrayList<SearchUser>();

    LatLng currents;
    Button bcancel,bsearch;
    TextView text1, text2,l1t,l2t;
    TextInputLayout tl1,tl2,tl3,tl4,tl5,tl6;
    TextInputEditText te1,te2,te3,te4,te5,te6;
    FloatingActionButton f1,f2,f3,f4,f5,f6,f7;
    NumberPicker numberPicker;
    ImageView l1,l2;
    String[] typePicker={"Select type","Residential","Industrial","Recreational","Commercial","Agricultural"};

    @BindView(R.id.bottom_sheet_option)
    NestedScrollView layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;
    String email="";
    String delKey="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        ButterKnife.bind(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.toolbar_title);
        getSupportActionBar().setTitle((Html.fromHtml(
                "<font color=\"#FFFFFF\">"
                        + getString(R.string.title_my_land)
                        + "</Sansation-Regular.ttf>"
        )));

        Intent i = getIntent();
        currents = i.getExtras().getParcelable("current");
        email = i.getStringExtra("email");
        Log.d("hi", "currents: " + currents);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        contactList2 = new ArrayList<>();

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        BottomSheetBehavior.from(layoutBottomSheet).setState(BottomSheetBehavior.STATE_HIDDEN);

        bcancel = (Button) findViewById(R.id.text2);
        bsearch = (Button) findViewById(R.id.button_search);
        text1 = (TextView) findViewById(R.id.text1);
        l1t = (TextView) findViewById(R.id.l1t);
        l2t = (TextView) findViewById(R.id.l2t);
        tl1 = (TextInputLayout) findViewById(R.id.price);
        tl2 = (TextInputLayout) findViewById(R.id.area);
        tl3 = (TextInputLayout) findViewById(R.id.distance);
        tl4 = (TextInputLayout) findViewById(R.id.price2);
        tl5 = (TextInputLayout) findViewById(R.id.area2);
        tl6 = (TextInputLayout) findViewById(R.id.distance2);
        te1 = (TextInputEditText) findViewById(R.id.price1);
        te2 = (TextInputEditText) findViewById(R.id.area1);
        te3 = (TextInputEditText) findViewById(R.id.distance1);
        te4 = (TextInputEditText) findViewById(R.id.price3);
        te5 = (TextInputEditText) findViewById(R.id.area3);
        te6 = (TextInputEditText) findViewById(R.id.distance3);
        l1 = (ImageView) findViewById(R.id.l1);
        l2 = (ImageView) findViewById(R.id.l2);

        bsearch.setVisibility(View.GONE);

        numberPicker = (NumberPicker) findViewById(R.id.numberPicker1);
        numberPicker.setMaxValue(typePicker.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(typePicker);
        numberPicker.setValue(0);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setWrapSelectorWheel(true);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Sansation-Regular.ttf");
        bcancel.setTypeface(tf);
        bsearch.setTypeface(tf);
        text1.setTypeface(tf);
        tl1.setTypeface(tf);
        tl2.setTypeface(tf);
        tl3.setTypeface(tf);
        tl4.setTypeface(tf);
        tl5.setTypeface(tf);
        tl6.setTypeface(tf);
        te1.setTypeface(tf);
        te2.setTypeface(tf);
        te3.setTypeface(tf);
        te4.setTypeface(tf);
        te5.setTypeface(tf);
        te6.setTypeface(tf);
        l1t.setTypeface(tf);
        l2t.setTypeface(tf);

        //Intent intent = getIntent();
        //Bundle args = intent.getBundleExtra("BUNDLE");
        //allmarkers = (ArrayList<Marker>) getIntent().getSerializableExtra("markers");
//        AllMarkers allMarkers=(AllMarkers) getIntent().getSerializableExtra("markers");

        bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delKey != null) {
                    databaseReference.child(delKey).removeValue();
                    Log.i("size : "," countSearchUsersPhone " + delKey);
                } else {
                    Log.i("size : ", delKey+" countSearchUsersPhone " + "error");
                }

                alluser.clear();
                contactList2.clear();
                contactListFiltered.clear();
//                mAdapter.notifyDataSetChanged();
                loadFirebaseUser();
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        bcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsearch.setVisibility(View.VISIBLE);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentOnclick);
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

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

        //initialize();
//        SearchUser user=new SearchUser();
//        user.setKey("aaaaaaaaa");
//        user.setName("ssssss");
//        user.setEmail("ffffffff");
//        //user.setCent(centerpoly.getLatLng());
//        alluser.add(user);
//        contactList2.add(user);

//        new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground( final Void ... params ) {
//
//                    return null;
//                }
//                @Override
//                protected void onPostExecute( final Void result ) {
//
//
//
//                }
//            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

        mAdapter = new ContactsAdapter(MainActivity7.this, contactList2, MainActivity7.this,currents);

        // white background notification bar
        //whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 20));
        recyclerView.setAdapter(mAdapter);

        //change stutus ber color..
        Window window=this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent));
        }
    }

//    private void initialize() {
//        for (int i = 0; i < 4; i++) {
//            Contact item = new Contact();
//            item.setName(title[i] + "");
//            item.setPhone(dis[i] + "");
//            item.setImage(img2[i]);
//            contactList.add(item);
//        }
//    }

    public void loadFirebaseUser() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildAdded", "addChildEventListener" + s);
                String olddata = "";
                String usersEmail= null;
                String usersName = null;
                String usersArea= null;
                String usersPrice = null;
                String usersType = null;
                String usersAddress= null;
                PolygoneLatLng2 centerpoly = null;
                olddata = dataSnapshot.getKey();
                long num = dataSnapshot.getChildrenCount();
                int index=0;
                for (DataSnapshot helpDataSnapshotChild : dataSnapshot.getChildren()) {
                    if(++index == num) {
                        usersEmail=helpDataSnapshotChild.child("email").getValue(String.class);
                        usersName=helpDataSnapshotChild.child("name").getValue(String.class);
                        usersArea = helpDataSnapshotChild.child("area").getValue(String.class);
                        usersPrice = helpDataSnapshotChild.child("price").getValue(String.class);
                        usersType = helpDataSnapshotChild.child("type").getValue(String.class);
                        usersAddress = helpDataSnapshotChild.child("address").getValue(String.class);
                        Log.i("getKey: ", index+"getChildrenCount1: "+num+usersEmail+usersName);
                        break;
                    }
                    Log.i("getKey: ", index+"getChildrenCount2: "+num);
                    try {
                        centerpoly = helpDataSnapshotChild.getValue(PolygoneLatLng2.class);
                        //childLatLngs.add(childLatLng.getLatLng());
                    } catch (Exception ex) {
                        Log.i("child of : ", " Exception ");
                        Toast.makeText(getApplicationContext(), "child of " + helpDataSnapshotChild.getKey() + "Exception", Toast.LENGTH_LONG).show();
                    }
                }
                final SearchUser user=new SearchUser();
                user.setKey(olddata);
                user.setName(usersName);
                user.setEmail(usersEmail);
                user.setArea(usersArea);
                user.setPrice(usersPrice);
                user.setType(usersType);
                user.setAddress(usersAddress);
                user.setCent(centerpoly.getLatLng());

                double dis= SphericalUtil.computeDistanceBetween(centerpoly.getLatLng(), currents);
                if (usersEmail != null && usersEmail.equals(email)) {
                //if(dis<=15000) {
                    alluser.add(user);
                    contactList2.add(user);
                    contactListFiltered.add(user);
                    mAdapter.notifyDataSetChanged();
//                  MainActivity7.this.notify();

                    Log.i("size : ", user.getName() + " countSearchUsers " + (contactList2.size() - 1) + user.getAddress());
                }
                //drawPolygonObjects3(usersName, usersEmail, childLatLngs, alluser.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("onChildChanged", "addChildEventListener" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("onChildRemoved", "addChildEventListener");
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }
//        if (id == R.id.action_filter) {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
////            final Context context = this;
////            Intent intent = new Intent(context, DetailActivity.class);
////            intent.putExtra("roll", "47");
//            //startActivity(intent);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(SearchUser contact) {
        ArrayList<String> mylist = new ArrayList<String>();
        List<SearchUser> all=mAdapter.getFilterValue();
        for (SearchUser user : all) {
            mylist.add(user.getKey());
            Log.i("size : ", user.getName()+" countSearchUser: "+user.getKey());
        }

        intentOnclick = new Intent(MainActivity7.this, MainActivity6for7.class);
        intentOnclick.putStringArrayListExtra("key", mylist);
        intentOnclick.putExtra("Latlng", contact.getCent());
        intentOnclick.putExtra("currents", currents);
        //startActivity(intentOnclick);

        delKey=contact.getKey();

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bsearch.setVisibility(View.GONE);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //final Context context = this;
        //Intent intent = new Intent(context, DetailActivity.class);
        //intent.putExtra("roll", SearchUser.getPhone());
        //startActivity(intent);
        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getAddress(), Toast.LENGTH_LONG).show();
    }

    public void onSearch() {
        //List<SearchUser> contactListFiltered = new ArrayList<SearchUser>();
        //contactListFiltered.addAll(contactList2);
        if (te1.getText().toString().equals("") && te2.getText().toString().equals("") && te3.getText().toString().equals("")
                && te4.getText().toString().equals("") && te5.getText().toString().equals("") && te6.getText().toString().equals("")
                && typePicker[numberPicker.getValue()].equals("Select type")) {
            contactList2.clear();
            contactList2.addAll(contactListFiltered);
            mAdapter.notifyDataSetChanged();
        } else {
            List<SearchUser> filteredList = new ArrayList<>();
//            for (SearchUser row : contactListFiltered) {
//                Log.i("getKeys: ", row.getAddress()+" = rowAddress: "+row.getKey());
//                if(!te2.getText().toString().equals("")) {
//                    if (Double.parseDouble(row.getArea()) <= Double.parseDouble(te2.getText().toString())) {
//                        filteredList.add(row);
//                    }
//                } else {
//                    //filteredList.addAll(contactListFiltered);
//                }
//            }
            if( !te2.getText().toString().equals("") || !te5.getText().toString().equals("") ) {
                if(te2.getText().toString().equals("")){
                    for (SearchUser row : contactListFiltered) {
                        Log.i("getKeys: ", row.getArea()+" = rowAddress: "+row.getKey());
                        if ((Double.parseDouble(row.getArea()) <= Double.parseDouble(te5.getText().toString()))) {
                            filteredList.add(row);
                        }
                    }
                } else if(te5.getText().toString().equals("")){
                    for (SearchUser row : contactListFiltered) {
                        Log.i("getKeys: ", row.getArea()+" = rowAddress: "+row.getKey());
                        if ((Double.parseDouble(row.getArea()) >= Double.parseDouble(te2.getText().toString()))) {
                            filteredList.add(row);
                        }
                    }
                } else {
                    for (SearchUser row : contactListFiltered) {
                        Log.i("getKeys: ", row.getArea() + " = rowAddress: " + row.getKey());
                        if ((Double.parseDouble(row.getArea()) <= Double.parseDouble(te5.getText().toString())) &&
                                (Double.parseDouble(row.getArea()) >= Double.parseDouble(te2.getText().toString()))) {
                            filteredList.add(row);
                        }
                    }
                }
            } else {
                filteredList.addAll(contactListFiltered);
            }

            List<SearchUser> filteredList2 = new ArrayList<>();
            if(!te1.getText().toString().equals("") || !te4.getText().toString().equals("")) {
                if(te1.getText().toString().equals("")) {
                    for (SearchUser row : filteredList) {
                        Log.i("getKeys: ", row.getPrice()+" = getPrice: "+row.getKey());
                        if (Double.parseDouble(row.getPrice()) <= Double.parseDouble(te4.getText().toString())) {
                            filteredList2.add(row);
                        }
                    }
                }else if(te4.getText().toString().equals("")) {
                    for (SearchUser row : filteredList) {
                        Log.i("getKeys: ", row.getPrice()+" = getPrice: "+row.getKey());
                        if (Double.parseDouble(row.getPrice()) >= Double.parseDouble(te1.getText().toString())) {
                            filteredList2.add(row);
                        }
                    }
                }else {
                    for (SearchUser row : filteredList) {
                        Log.i("getKeys: ", row.getPrice() + " = getPrice: " + row.getKey());
                        if ((Double.parseDouble(row.getPrice()) >= Double.parseDouble(te1.getText().toString())) &&
                                (Double.parseDouble(row.getPrice()) <= Double.parseDouble(te4.getText().toString())) ) {
                            filteredList2.add(row);
                        }
                    }
                }
            } else {
                filteredList2.addAll(filteredList);
            }

            List<SearchUser> filteredList3 = new ArrayList<>();
            if(!te3.getText().toString().equals("") || !te6.getText().toString().equals("")) {
                if(te3.getText().toString().equals("")) {
                    for (SearchUser row : filteredList2) {
                        Log.i("getKeys: ", row.getDistance()+" = getDistance: "+row.getKey());
                        if (Double.parseDouble(row.getDistance()) <= Double.parseDouble(te6.getText().toString())) {
                            filteredList3.add(row);
                        }
                    }
                }else if(te6.getText().toString().equals("")) {
                    for (SearchUser row : filteredList2) {
                        Log.i("getKeys: ", row.getDistance()+" = getDistance: "+row.getKey());
                        if (Double.parseDouble(row.getDistance()) >= Double.parseDouble(te3.getText().toString())) {
                            filteredList3.add(row);
                        }
                    }
                }else {
                    for (SearchUser row : filteredList2) {
                        Log.i("getKeys: ", row.getDistance() + " = getDistance: " + row.getKey());
                        if ((Double.parseDouble(row.getDistance()) >= Double.parseDouble(te3.getText().toString())) &&
                                (Double.parseDouble(row.getDistance()) <= Double.parseDouble(te6.getText().toString()))) {
                            filteredList3.add(row);
                        }
                    }
                }
            } else {
                filteredList3.addAll(filteredList2);
            }

            List<SearchUser> filteredList4 = new ArrayList<>();
            if(!typePicker[numberPicker.getValue()].equals("Select type")) {
                for (SearchUser row : filteredList3) {
                    Log.i("getKeys: ", row.getType()+" = getType: "+row.getKey());
                    if (row.getType().equals(typePicker[numberPicker.getValue()]) ) {
                        filteredList4.add(row);
                    }
                }
            } else {
                filteredList4.addAll(filteredList3);
            }

            contactList2.clear();
//            for (SearchUser row : filteredList) {
//                Log.i("getKeys: ", row.getAddress()+" = rowAddress:= "+row.getKey());
//                contactList2.add(row);
//                mAdapter.notifyDataSetChanged();
//            }
            contactList2.addAll(filteredList4);
            mAdapter.notifyDataSetChanged();
            //contactList2 = filteredList;
        }

        //return contactListFiltered;
    }
}