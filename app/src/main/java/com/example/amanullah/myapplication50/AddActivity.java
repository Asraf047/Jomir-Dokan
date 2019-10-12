package com.example.amanullah.myapplication50;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddActivity extends AppCompatActivity {


    private EditText subjectEditText;
    private EditText descEditText;
    private EditText phone_text;
    private EditText mobile_text;
    private EditText email_text;
    private EditText work_text;
    private EditText home_text;
    private EditText nid_text;
    private EditText roll_text;
    AlertDialog mAlertDialog;

    CollapsingToolbarLayout collapsingToolbar;
    ImageView imageView;
    ImageView imageView11;
    Bitmap image_bitmap;
    public Button add_photo,button_next,cancel;
    private static final int PICK_FROM_GALLERY = 1;

    FirebaseStorage storage;
    StorageReference storageReference;
    String olddata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit1);

        imageView= (ImageView) findViewById(R.id.Image_profile);
        imageView11= (ImageView) findViewById(R.id.header_img2);
        image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wmc);
        imageView11.setImageBitmap(image_bitmap);

        subjectEditText = (EditText) findViewById(R.id.editText_name1e);
        descEditText = (EditText) findViewById(R.id.editText_name1e2);
        phone_text = (EditText) findViewById(R.id.editText_phonee);
        mobile_text = (EditText) findViewById(R.id.editText_mobilee);
        email_text = (EditText) findViewById(R.id.editText_emaile);
        work_text = (EditText) findViewById(R.id.editText_addresse2);
        home_text = (EditText) findViewById(R.id.editText_addresse1);
        nid_text = (EditText) findViewById(R.id.editText_fbe);
        //descEditText = (EditText) findViewById(R.id.editText_name1e2);

        button_next = (Button) findViewById(R.id.button_next);
        cancel = (Button) findViewById(R.id.button_cancel);
        add_photo = (Button) findViewById(R.id.button_photo);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(1,2);
            }
        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "click button", Toast.LENGTH_LONG).show();
                show(3, 4);
                //imageView11.setRotation(90);
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, MainActivity.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //callFirebase();
    }

 /*   private void callFirebase() {
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception ex){

        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //make();
        //databaseReference.removeValue();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot helpDataSnapshot1obj : dataSnapshot.getChildren()) {
                    olddata="";
                    olddata=helpDataSnapshot1obj.getKey();
                    //Log.i("getKey: ", olddata);
                    Toast.makeText(getApplicationContext(), "getKey: " + olddata , Toast.LENGTH_LONG).show();
                    for (DataSnapshot helpDataSnapshotChild : dataSnapshot.child(olddata).getChildren()) {
                        try {
                            PolygoneLatLng2 childLatLng = helpDataSnapshotChild.getValue(PolygoneLatLng2.class);
                            //Log.i("child of : ", helpDataSnapshotChild.getKey() + "  " + childLatLng.getLatitude());
                            Toast.makeText(getApplicationContext(), "child of " + helpDataSnapshotChild.getKey() + "\n", Toast.LENGTH_LONG).show();
                            childLatLngs.add(childLatLng.getLatLng());
                        } catch (Exception ex) {
                            Log.i("child of : ", " Exception " );
                            Toast.makeText(getApplicationContext(), "child of " + helpDataSnapshotChild.getKey() + "Exception", Toast.LENGTH_LONG).show();
                        }
                    }
                    polygone2 = new Polygone2(olddata, childLatLngs);
                    drawPolygonObjects2(olddata, childLatLngs);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Exception databaseError" + "\n", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void add(Polygone2 polygone2) {
        try {
            databaseReference.child(polygone2.getName()).setValue(polygone2.getAllLatLng());
            Toast.makeText(getApplicationContext(), "Success name: "+polygone.getName(), Toast.LENGTH_LONG).show();
        }
        catch (Exception exception){
            Toast.makeText(getApplicationContext(), "Exception occurs", Toast.LENGTH_LONG).show();
        }
    }
*/


    /*
        //Show manu option icon...
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_scrolling_edit, menu);
            return true;
        }

        //Manu item(Home button,Setting,...)action setting..
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                NavUtils.navigateUpTo(this, new Intent(this, ScrollingActivity.class));
                return true;
            }
            if (id == R.id.action_settings) {
                return true;
            }
            if (id == R.id.action_right) {

                final String name = subjectEditText.getText().toString();
                final String desc = descEditText.getText().toString();
                final String phone = phone_text.getText().toString();
                final String mobile = mobile_text.getText().toString();
                final String email = email_text.getText().toString();
                final String home = home_text.getText().toString();
                final String work = work_text.getText().toString();
                final String fb = nid_text.getText().toString();
                final String roll = roll_text.getText().toString();
                //final String desc = descEditText.getText().toString();

                Intent main = new Intent(AddActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);

                //startActivity(new Intent(ScrollingActivity.this, Edit1.class));
                return true;
            }
            if (id == R.id.action_delete) {
                //startActivity(new Intent(ScrollingActivity.this, Edit1.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            Log.d(AddActivity.class.getSimpleName(), "Failed to RESULT_OK intent data, result code is " + requestCode);
            if (requestCode == 1) {
                //Uri selected=data.getData();
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //imageView.setImageURI(selected);
                imageView.setImageBitmap(photo);
            }
            if (requestCode == 2) {
                Uri selected=data.getData();
                imageView.setImageURI(selected);
            }
            if (requestCode == 3) {
                //Uri selected=data.getData();
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //imageView.setImageURI(selected);
                imageView11.setImageBitmap(photo);
            }
            if (requestCode == 4) {
                Log.d(AddActivity.class.getSimpleName(), "Failed to requestCode intent data, result code is " + resultCode);
                Uri selected=data.getData();
                imageView11.setImageURI(selected);
            }
        } else {
            //Toast.makeText(getApplicationContext(), R.string.msg_failed_to_get_intent_data, Toast.LENGTH_LONG).show();
            Log.d(AddActivity.class.getSimpleName(), "Failed to get intent data, result code is " + resultCode);
        }
    }

    public void next(){
        final String name = subjectEditText.getText().toString();
        final String desc = descEditText.getText().toString();
        final String phone = phone_text.getText().toString();
        final String mobile = mobile_text.getText().toString();
        final String email = email_text.getText().toString();
        final String home = home_text.getText().toString();
        final String work = work_text.getText().toString();
        final String fb = nid_text.getText().toString();
        final String roll = roll_text.getText().toString();
        //final String desc = descEditText.getText().toString();

        Intent main = new Intent(AddActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(main);

        //startActivity(new Intent(ScrollingActivity.this, Edit1.class));
    }

    public void show(final int i, final int i1) {
        final AlertDialog.Builder d = new AlertDialog.Builder(AddActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        d.setTitle("Choose");
        d.setView(dialogView);

        ImageView l1=dialogView.findViewById(R.id.l1);
        ImageView l2=dialogView.findViewById(R.id.l2);


        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickCamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(pickCamera,i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
                }
                mAlertDialog.dismiss();
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickGallery=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickGallery.putExtra("crop", "true");
                pickGallery.putExtra("aspectX", 0);
                pickGallery.putExtra("aspectY", 0);
                pickGallery.putExtra("outputX", 300);
                pickGallery.putExtra("outputY", 300);
                try {
                    startActivityForResult(pickGallery,i1);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
                }
                mAlertDialog.dismiss();
            }
        });

        mAlertDialog = d.create();
        mAlertDialog.show();
    }

}