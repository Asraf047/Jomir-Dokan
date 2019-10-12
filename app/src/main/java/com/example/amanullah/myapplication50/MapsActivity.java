package com.example.amanullah.myapplication50;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends FragmentActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng centerPolygon;
    ArrayList<Polygone> polygones = new ArrayList<Polygone>();
    ArrayList<Marker> markers = new ArrayList<Marker>();
    ArrayList<Circle> Circles = new ArrayList<Circle>();
    static final int points = 10;
    static final Double difference = 0.000002;
    Polygon shape;
    Button bleft,bright,bup,bdown,bclean;
    String locality;
    Marker firstMarker;
    Polygone polygone;
    int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        bleft=findViewById(R.id.left);
        bright=findViewById(R.id.right);
        bup=findViewById(R.id.up);
        bdown=findViewById(R.id.down);
        bclean=findViewById(R.id.clean);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && shape ==null) {
                    markerLeft();
                }
            }
        });
        bright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && shape ==null) {
                    markerRight();
                }
            }
        });
        bup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && shape ==null) {
                    markerUp();
                }
            }
        });
        bdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markers.size() != 0 && shape ==null) {
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
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MapsActivity.this.setMarker("local", latLng.latitude, latLng.longitude);
                }
            });
            //mMap.setOnPolygonClickListener(customPoly());

            make();
            calldrawPolygonObjects();
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                LatLng customMarkerLocationOne = new LatLng(24.823580, 67.025625);
                LatLng customMarkerLocationTwo = new LatLng(24.824580, 67.025625);
                LatLng customMarkerLocationThree = new LatLng(24.820211, 67.029465);


                mMap.addMarker(new MarkerOptions().position(customMarkerLocationOne).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapsActivity.this, R.drawable.my_dp, "Yasir Ameen"))));
                mMap.addMarker(new MarkerOptions().position(customMarkerLocationTwo).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapsActivity.this, R.drawable.janet, "Mary Jane"))));

                mMap.addMarker(new MarkerOptions().position(customMarkerLocationThree).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(MapsActivity.this, R.drawable.john, "Janet John"))));

                //LatLngBound will cover all your marker on Google Maps
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(customMarkerLocationOne); //Taking Point A (First LatLng)
                builder.include(customMarkerLocationThree); //Taking Point B (Second LatLng)
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                mMap.moveCamera(cu);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(7), 2000, null);
            }
        });


    }

    private void setMarker(String locality, double lat, double lng) {

        this.locality=locality;

        if (markers.size() >= points) {
            //removeEverythings();
        }

        createMarker(locality,lat,lng);

        if (markers.size() == 1) {
            firstMarker=markers.get(markers.size()-1);
            Toast.makeText(this, "firstMarker.", Toast.LENGTH_LONG).show();
        }

        //if (markers.size() == points) {
        //drawPolygon();
        //Toast.makeText(this, "drawPolygon list of markers.", Toast.LENGTH_LONG).show();
        //}

        //checkMarker();

    }

    private void checkMarker(){
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
            Log.i(""+markers.get(i).getPosition().latitude,""+markers.get(i).getPosition().longitude);
        }
        shape = mMap.addPolygon(options);

        centerPolygon = getPolygonCenterPoint(markers);
        Log.i("center"+centerPolygon.latitude,centerPolygon.longitude+"ends");
        MarkerOptions optionCenter = new MarkerOptions()
                .alpha(1.0f)
                .infoWindowAnchor(.6f,1.0f)
                .title("Janet John")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MapsActivity.this, R.drawable.janet, "Janet John")))
                .position(new LatLng(centerPolygon.latitude, centerPolygon.longitude))
                .snippet("This is my land.");
        markers.add(mMap.addMarker(optionCenter));

        polygone = new Polygone(options, centerPolygon, "First");
        drawPolygonObjects(options,centerPolygon);
    }


    private void removeEverythings() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();

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


    private LatLng getPolygonCenterPoint(ArrayList<Marker> polygonPointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++)
        {
            LatLng position = polygonPointsList.get(i).getPosition();
            builder.include(position);
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }


    private void markerLeft() {
        Marker selectedMarker = markers.get(markers.size()-1);
        Circle selectedCircle = Circles.get(Circles.size()-1);
        Double lat=selectedMarker.getPosition().latitude;
        Double lng=selectedMarker.getPosition().longitude - difference;
        selectedMarker.remove();
        selectedCircle.remove();
        markers.remove(markers.size() - 1);
        Circles.remove(Circles.size() - 1);
        createMarker(locality,lat,lng);
    }

    private void markerRight() {
        Marker selectedMarker = markers.get(markers.size()-1);
        Circle selectedCircle = Circles.get(Circles.size()-1);
        Double lat=selectedMarker.getPosition().latitude;
        Double lng=selectedMarker.getPosition().longitude + difference;
        selectedMarker.remove();
        selectedCircle.remove();
        markers.remove(markers.size() - 1);
        Circles.remove(Circles.size() - 1);
        createMarker(locality,lat,lng);
    }

    private void markerUp() {
        Marker selectedMarker = markers.get(markers.size()-1);
        Circle selectedCircle = Circles.get(Circles.size()-1);
        Double lat=selectedMarker.getPosition().latitude + difference;
        Double lng=selectedMarker.getPosition().longitude;
        selectedMarker.remove();
        selectedCircle.remove();
        markers.remove(markers.size() - 1);
        Circles.remove(Circles.size() - 1);
        createMarker(locality,lat,lng);
    }

    private void markerDown() {
        Marker selectedMarker = markers.get(markers.size()-1);
        Circle selectedCircle = Circles.get(Circles.size()-1);
        Double lat=selectedMarker.getPosition().latitude - difference;
        Double lng=selectedMarker.getPosition().longitude;
        selectedMarker.remove();
        selectedCircle.remove();
        markers.remove(markers.size() - 1);
        Circles.remove(Circles.size() - 1);
        createMarker(locality,lat,lng);
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


    /*
        public GoogleMap.OnPolygonClickListener customPoly(){
            return new GoogleMap.OnPolygonClickListener() {
                @Override
                public void onPolygonClick(Polygon polygon) {

                    Marker mar = mMap.addMarker(new MarkerOptions()
                            .alpha(0.0f)
                            .infoWindowAnchor(.6f,1.0f)
                            .position(new LatLng(centerPolygon.latitude, centerPolygon.longitude))
                            .title("Janet John")
                            .snippet("This is my land.")
                    );
                    mar.showInfoWindow();

                }
            };
        }
        */
    /*
    LatLng pos = new LatLng(-48.59, -2.30);
    GroundOverlayOptions iroad = new GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromBitmap(textAsBitmap("Ineseno Road",100,0xffffffff)))
            .positionFromBounds(ir)
            .zIndex(3)
            .position(pos,8600f);
    iroad.zIndex(4);
    iroad.anchor(a,b);
    iroad.bearing(0);
    iroad.visible(true);
    iroad.transparency(0);
    mMap.addGroundOverlay(iroad);

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setStrokeWidth(5);
        paint.setShadowLayer(5, 0, 0, 0xff000000);
        paint.setTextAlign(Paint.Align.LEFT);

        int width = (int) (paint.measureText(text) + 0.5f); // round
        float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative
        int height = (int) (baseline + paint.descent() + 0.5f);

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);

        Drawable drawable2 = new BitmapDrawable(getResources(), image);
        Drawable drawable1 = getResources().getDrawable(R.drawable.black_box);

        Drawable[] layers = new Drawable[2];
        layers[0] = drawable1;
        layers[1] = drawable2;

        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setLayerInset(1, 0, 0, 0, 0);

        Bitmap bitmap = Bitmap.createBitmap((width + 5), (height + 5), Bitmap.Config.ARGB_8888);

        Canvas can = new Canvas(bitmap);
        layerDrawable.setBounds(0, 0, width, height);
        layerDrawable.draw(can);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(this.getResources(), bitmap);

        bitmapDrawable.setBounds(0, 0, (width + 5), (height + 5));

        return bitmapDrawable.getBitmap().copy( Bitmap.Config.ARGB_8888, true);
    }


    public float distance (float lat_a, float lng_a, float lat_b, float lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }
*/
    private void make() {
        ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(24.823149907637347, 67.02836103737353));
        latLngs.add(new LatLng(24.822625598884024, 67.02836573123932));
        latLngs.add(new LatLng(24.822526396862127, 67.02798184007406));
        latLngs.add(new LatLng(24.82226834951275, 67.02776592224836));
        latLngs.add(new LatLng(24.822039210319144, 67.02761705964804));
        latLngs.add(new LatLng(24.822209315020014, 67.02707726508378));
        latLngs.add(new LatLng(24.824176913418903, 67.02746585011482));

        LatLng centerPolygon=new LatLng(24.823129058341056, 67.02772149816155);
        makePolygonObject(latLngs,centerPolygon);
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



    private void calldrawPolygonObjects() {
        for (int i = 0; i < polygones.size(); i++) {
            drawPolygonObjects(polygones.get(i).getPolygonOptions(),polygones.get(i).getCenter());
            Toast.makeText(this, "objects Print name = " + polygones.get(i).getName(), Toast.LENGTH_LONG).show();
        }
    }

    private void drawPolygonObjects(PolygonOptions options,LatLng centerPolygonObject) {

        mMap.addPolygon(options);

        centerPolygon = centerPolygonObject;
        Log.i("center"+centerPolygon.latitude,centerPolygon.longitude+"ends");
        MarkerOptions optionCenter = new MarkerOptions()
                .alpha(1.0f)
                .infoWindowAnchor(.6f,1.0f)
                .title("Janet John")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(MapsActivity.this, R.drawable.janet, "Janet John")))
                .position(new LatLng(centerPolygon.latitude, centerPolygon.longitude))
                .snippet("This is my land.");
        mMap.addMarker(optionCenter);

    }

}
