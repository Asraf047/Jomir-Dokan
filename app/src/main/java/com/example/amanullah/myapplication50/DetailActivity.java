package com.example.amanullah.myapplication50;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AMANULLAH on 07-Feb-18.
 */

public class DetailActivity extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener {

    Context context=this;
    String rolls;
    ImageView imageView;
    ImageView imageView2;
    TextView tx1,tx2,tx3,tx4,tx5,tx6,tx7,tx8;
    CircleImageView circleImageView;
    int[] img2 = {
            R.drawable.i1,
            R.drawable.i2,
            R.drawable.i3,
            R.drawable.i4
    };
    String[] title = {
            "M.Asifur Rahman",
            "Angkur Mondal",
            "Sohana Akter",
            "S.M. Mohaiminul Islam Rafi"
    };

    String[] dis = {
            "001",
            "002",
            "003",
            "004"
    };

    String[] fb = {
            "https://www.facebook.com/MD.asifur.rahman.anik",
            "https://www.facebook.com/iAngkur/about?lst=100002989436677%3A100012410827171%3A1511406900",
            "https://www.facebook.com/profile.php?id=100009393594800",
            "https://www.facebook.com/mohaimin66"
    };

    String[] phone = {
            "01516111574",
            "01934487492",
            "01722535557",
            "01792628063"
    };
    String[] home = {
            "Jessore",
            "Dhaka",
            "Nilphamari",
            "Sirajgang"
    };
    String[] email = {

    };
    String[] blood = {

    };
    String[] birth = {

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // toolbar fancy stuff
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.toolbar_title);
/*
        getSupportActionBar().setTitle((Html.fromHtml(
                "<font color=\"#FFFFFF\">"
                        + getString(R.string.toolbar_title)
                //+ "</font>"
        )));
*/

//        circleImageView= (CircleImageView) findViewById(R.id.img1);
//        imageView= (ImageView) findViewById(R.id.background);
//        imageView2= (ImageView) findViewById(R.id.background2);
//        tx1= (TextView) findViewById(R.id.textView1);
//        tx2= (TextView) findViewById(R.id.textView2);
//        tx3= (TextView) findViewById(R.id.text_phone);
//        tx4= (TextView) findViewById(R.id.text_fb);
//        tx5= (TextView) findViewById(R.id.text_work);
//        tx6= (TextView) findViewById(R.id.text_email);
//        tx7= (TextView) findViewById(R.id.text_blood);
//        tx8= (TextView) findViewById(R.id.text_b);
        Intent intent = getIntent();
        rolls = intent.getStringExtra("roll");
        //Toast.makeText(getApplicationContext(), "Selected: " + rolls, Toast.LENGTH_LONG).show();
        circleImageView.setImageResource(img2[Integer.parseInt(rolls)-1]);
        imageView.setImageResource(img2[Integer.parseInt(rolls)-1]);
        imageView2.setImageResource(img2[Integer.parseInt(rolls)-1]);
        imageView2.setRotationX(180);
        tx1.setText(title[Integer.parseInt(rolls)-1]);
        tx2.setText("1507"+dis[Integer.parseInt(rolls)-1]);
        tx3.setText(phone[Integer.parseInt(rolls)-1]);
        tx4.setText(fb[Integer.parseInt(rolls)-1]);
        tx4.setMovementMethod(new ScrollingMovementMethod());
        tx5.setText(home[Integer.parseInt(rolls)-1]);
        //tx6.setText(email[Integer.parseInt(rolls)-1]);
        //tx7.setText(blood[Integer.parseInt(rolls)-1]);
        //tx8.setText(birth[Integer.parseInt(rolls)-1]);
        //circleImageView.setImageResource(R.drawable.i3);
        if(rolls.equals("47")){
            tx6.setText("amanullahoasraf@gmail.com");
            tx7.setText("A+");
        }
    }

    @Override
    public void onContactSelected(SearchUser contact) {
        //imgss=contact.getPhone();
        circleImageView.setImageResource(R.drawable.i2);
        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();

    }

    public void call1(View view) {
        String uri = "tel:"+tx3.getText().toString();
        //Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        try {
            //startActivity(callIntent);
            startActivity(dialIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DetailActivity.this, "CALL faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void msg1(View view) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        String num = tx3.getText().toString();
        //String sms = editText5.getText().toString();
        smsIntent.putExtra("address"  , num);
        //smsIntent.putExtra("sms_body"  , sms);
        try {
            startActivity(smsIntent);
            finish();
            //Toast.makeText(DetailActivity.this, "msg :"+num, Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DetailActivity.this, "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void email(View view) {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{tx6.getText().toString()});
        //emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello");
        //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Add Message here");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DetailActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void map1(View view) {
        //Uri mapuri=Uri.parse("geo:0,0?q=" + Uri.encode("khulna"));
        //Intent mapintent=new Intent(Intent.ACTION_VIEW, mapuri);
        //mapintent.setPackage(com.google.android.apps.maps);
        String uri="http://maps.google.co.in/maps?q="+home[Integer.parseInt(rolls)-1]+", bangladesh";
        Intent map=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        try {
            startActivity(map);
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(DetailActivity.this, "map not open", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(DetailActivity.this, "home", Toast.LENGTH_SHORT).show();
    }

    public void direction(View view) {
        Toast.makeText(DetailActivity.this, "direction", Toast.LENGTH_SHORT).show();
    }

    public void facebook(View view) {
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fb[Integer.parseInt(rolls)-1]));
        //Toast.makeText(DetailActivity.this,fb[Integer.parseInt(rolls)-1], Toast.LENGTH_SHORT).show();
        PackageManager pack=context.getPackageManager();
        try {
            //startActivity(browserIntent);
            int version=pack.getPackageInfo("com.facebook.katana",0).versionCode;
            boolean activate=pack.getApplicationInfo("com.facebook.katana",0).enabled;
            if(activate){
                if(version>=3002850){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href="+fb[Integer.parseInt(rolls)-1]));
                    startActivity(browserIntent);
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+fb[Integer.parseInt(rolls)-1]));
                    startActivity(browserIntent);
                }
            }else {

                Toast.makeText(DetailActivity.this, "facebook not activate", Toast.LENGTH_SHORT).show();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(DetailActivity.this, "facebook not open", Toast.LENGTH_SHORT).show();
        }
    }

    public void messenger(View view) {

        //Intent intent=new Intent();
        //intent.setAction(Intent.ACTION_SEND);
        //intent.putExtra(Intent.EXTRA_TEXT,"hi");
        //intent.setType("text/plain");
        //intent.setPackage("com.facebook.orca");
        try {
            //startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(DetailActivity.this, "mesenger not open.", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(DetailActivity.this, "messenger", Toast.LENGTH_SHORT).show();
    }
}
