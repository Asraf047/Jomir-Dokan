package com.example.amanullah.myapplication50;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends AppCompatActivity {

    private Button btnChangeEmail, btnChangePassword, btnSendResetEmail, btnRemoveUser, changeEmail, changePassword, sendEmail, remove, update, next, signOut;

    private EditText oldEmail, newEmail, password, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    DatabaseReference databaseReference, parentRef, pushRef;

    private static final int CHOOSE_IMAGE = 101;
    File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");
    TextView textView;
    ImageView imageView;
    EditText editText;
    Uri uriProfileImage;
    Uri uriProfileImage2;
    String profileImageUrl;
    String email;
    FirebaseAuth mAuth;
    boolean permission=false;
    boolean permission2=false;
    AlertDialog mAlertDialog;
    CardView cp,bcp;

    TextInputLayout tl1,tl2,tl3;
    TextInputEditText te1,te2,te3;
    CircleImageView profile;
    private KeyListener listener;


    public static int REQUEST_STORAGE_PERMISSION = 122;

    private boolean checkStoragePermission() {
        return ActivityCompat.checkSelfPermission(MainActivity2.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity2.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_STORAGE_PERMISSION);
    }

    public static int REQUEST_STORAGE_PERMISSION2 = 123;

    private boolean checkStoragePermission2() {
        return ActivityCompat.checkSelfPermission(MainActivity2.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission2() {
        ActivityCompat.requestPermissions(MainActivity2.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_STORAGE_PERMISSION2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission=true;
                //Toast.makeText(MainActivity2.this, "permission= "+permission, Toast.LENGTH_LONG).show();
            } else {
                permission=false;
                //Toast.makeText(MainActivity2.this, "permission= "+permission, Toast.LENGTH_LONG).show();
                finish();
            }
        }
        if (requestCode == REQUEST_STORAGE_PERMISSION2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission2=true;
                //Toast.makeText(MainActivity2.this, "permission2= "+permission2, Toast.LENGTH_LONG).show();
            } else {
                permission2=false;
                //Toast.makeText(MainActivity2.this, "permission2= "+permission2, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitle("Edit Account");
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //change stutus ber color..
        Window window=this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent));
        }

        if (checkStoragePermission()) {
            permission=true;
            //Toast.makeText(MainActivity2.this, "permission= "+permission, Toast.LENGTH_LONG).show();
        } else {
            requestStoragePermission();
        }

        if (checkStoragePermission2()) {
            permission=true;
            //Toast.makeText(MainActivity2.this, "permission= "+permission, Toast.LENGTH_LONG).show();
        } else {
            requestStoragePermission2();
        }

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            parentRef = database.getReference();
            databaseReference = parentRef.child("Locations");
            databaseReference.keepSynced(true);
        } catch (Exception ex) {
            Log.i("Exception : ", " databaseReference " + ex);
        }

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity2.this, LoginActivity.class));
                    finish();
                }
            }
        };

        btnChangeEmail = (Button) findViewById(R.id.change_email_button);
        btnChangePassword = (Button) findViewById(R.id.change_password_button);
        btnSendResetEmail = (Button) findViewById(R.id.sending_pass_reset_button);
        btnRemoveUser = (Button) findViewById(R.id.remove_user_button);
        changeEmail = (Button) findViewById(R.id.changeEmail);
        changePassword = (Button) findViewById(R.id.changePass);
        sendEmail = (Button) findViewById(R.id.send);
        remove = (Button) findViewById(R.id.remove);
        update = (Button) findViewById(R.id.upload);
        next = (Button) findViewById(R.id.next);
        signOut = (Button) findViewById(R.id.sign_out);

        oldEmail = (EditText) findViewById(R.id.old_email);
        newEmail = (EditText) findViewById(R.id.new_email);
        password = (EditText) findViewById(R.id.password);
        newPassword = (EditText) findViewById(R.id.newPassword);
        editText = (EditText) findViewById(R.id.name);

        tl1 = (TextInputLayout) findViewById(R.id.name_til);
        tl2 = (TextInputLayout) findViewById(R.id.email_til);
        tl3 = (TextInputLayout) findViewById(R.id.phone_til);
        te1 = (TextInputEditText) findViewById(R.id.name1);
        te2 = (TextInputEditText) findViewById(R.id.email1);
        te3 = (TextInputEditText) findViewById(R.id.phone1);

        imageView = (ImageView) findViewById(R.id.Image_profile);
        profile = (CircleImageView) findViewById(R.id.Image_profile2);
        cp=(CardView) findViewById(R.id.card_changePass);
        bcp=(CardView) findViewById(R.id.card_change_password_button);

        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        cp.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }


        listener = editText.getKeyListener();
        //tl1.setEnabled(false);
        //editText.setKeyListener(null);
//        te1.setKeyListener(null);
        te2.setKeyListener(null);

        loadUserInformation();

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.VISIBLE);
                cp.setVisibility(View.GONE);
                sendEmail.setVisibility(View.GONE);
                remove.setVisibility(View.GONE);
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newEmail.getText().toString().trim().equals("")) {
                    user.updateEmail(newEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity2.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                        signOut();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity2.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else if (newEmail.getText().toString().trim().equals("")) {
                    newEmail.setError("Enter email");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.VISIBLE);
                changeEmail.setVisibility(View.GONE);
                cp.setVisibility(View.VISIBLE);
                sendEmail.setVisibility(View.GONE);
                remove.setVisibility(View.GONE);
                bcp.setVisibility(View.GONE);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too short, enter minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity2.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(MainActivity2.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnSendResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.GONE);
                cp.setVisibility(View.GONE);
                sendEmail.setVisibility(View.VISIBLE);
                remove.setVisibility(View.GONE);
            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (!oldEmail.getText().toString().trim().equals("")) {
                    auth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity2.this, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity2.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else {
                    oldEmail.setError("Enter email");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity2.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity2.this, SignupActivity.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity2.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showImageChooser();
                show(1,2);
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


    private void loadUserInformation() {
        final FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String displayName = user.getDisplayName();
            Uri profileUri = user.getPhotoUrl();
            email = user.getEmail();
            te2.setText(user.getEmail());

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
                        .into(profile);
                //Log.i("display:", "url "+user.getPhotoUrl().toString());
            }

            if (displayName != null) {
                Log.i("display:", "name "+displayName);
                String[] separated = displayName.split("\\|\\|");
                te1.setText(separated[0]);
                try {
                    te3.setText(separated[1]);
                } catch (Exception e){
                    te3.setKeyListener(listener);
                    te3.setError("Phone Number Required");
                    te3.requestFocus();
                    Toast.makeText(MainActivity2.this, "Please add phone number", Toast.LENGTH_SHORT).show();
                }
                //te2.setText(user.getEmail());
            } else {
                te1.setKeyListener(listener);
                te3.setKeyListener(listener);
            }
        }
    }


    private void saveUserInformation() {

        final String displayName = te1.getText().toString();
        final String displayPhone= te3.getText().toString();
        final String display= displayName+"||"+displayPhone;

        if (displayName.isEmpty()) {
            te1.setError("Name required");
            te1.requestFocus();
            return;
        }
        if (displayPhone.isEmpty()) {
            te3.setError("Phone Number required");
            te3.requestFocus();
            return;
        }
        //uploadImageToFirebaseStorage();

        final FirebaseUser user = auth.getCurrentUser();
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + email + ".jpg");

        if (uriProfileImage2 != null) {
            progressBar.setVisibility(View.VISIBLE);
            UploadTask uploadTask = profileImageRef.putFile(uriProfileImage2);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return profileImageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        profileImageUrl =task.getResult().toString();

                        if (user != null && profileImageUrl != null) {
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(display)
                                    .setPhotoUri(Uri.parse(profileImageUrl))
                                    .build();

                            user.updateProfile(profile)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(MainActivity2.this, "Profile Photo, Name Updated", Toast.LENGTH_SHORT).show();
                                            }
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    } else {
                        Log.i("display:", "error"+profileImageUrl);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } else if(user != null) {
            Log.i("display:", "error, No profile image change.");
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setDisplayName(display)
                        .build();

                user.updateProfile(profile)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity2.this, "Profile Name Updated", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });

            updateFirebase();

        } else {
            Log.i("display:", "error, No name, profile image change.");
            Toast.makeText(MainActivity2.this, "Profile Not Updated", Toast.LENGTH_SHORT).show();
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            String uriSting = (direct.getAbsolutePath() + "/" +"first.jpg");
            Log.d(AddActivity.class.getSimpleName(), "Failed to RESULT_OK intent data, result code is " + requestCode);
            if (requestCode == 1) {
                //Uri selected=data.getData();
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //imageView.setImageURI(selected);
                profile.setImageBitmap(photo);

                createDirectoryAndSaveFile(photo,"first.jpg");
                uriSting = (direct.getAbsolutePath() + "/" +"first.jpg");
            }
            if (requestCode == 2) {
                Uri selected=data.getData();
                profile.setImageURI(selected);

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selected, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //file path of captured image
                String filePath = cursor.getString(columnIndex);
                //file path of captured image
                File f = new File(filePath);
                String filename= f.getName();
                cursor.close();

                Savefile("second",filePath);
                uriSting = (direct.getAbsolutePath() + "/" +"second.jpg");
            }

//            File file = new File(picturePath);
//                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
//                Log.d("Picture file_size2", file_size+"");

            String picturePath="/sdcard/DirName/first.jpg";
            //String uriSting = (direct.getAbsolutePath() + "/" +"first.jpg");
            uriProfileImage2=Uri.fromFile(new File(compressImage(uriSting)));

        } else {
            //Toast.makeText(getApplicationContext(), R.string.msg_failed_to_get_intent_data, Toast.LENGTH_LONG).show();
            Log.d(AddActivity.class.getSimpleName(), "Failed to get intent data, result code is " + resultCode);
        }
    }

    public void Savefile(String name, String path) {
        //File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");
        File file = new File(new File("/sdcard/DirName/"), name+".jpg");

        if(!direct.exists()) {
            direct.mkdir();
        }

        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileChannel src = new FileInputStream(path).getChannel();
                FileChannel dst = new FileOutputStream(file).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        //File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/DirName/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/DirName/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        Log.i("this",data.getExtras().get("data")+ "uriProfileImage: " + data.getData());
//
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            uriProfileImage = data.getData();
//            Log.i("this", "uriProfileImage: " + uriProfileImage);
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
//                profile.setImageBitmap(bitmap);
//
//                String picturePath = null;
//                if(Build.VERSION.SDK_INT >= 19) {
//                    Log.i("this", "uriProfileImage: " + uriProfileImage);
//                    picturePath = getRealPathFromURI_API19(MainActivity2.this, uriProfileImage);
//                    Log.d("Picture Path", picturePath);
//                }
//                else if(Build.VERSION.SDK_INT >= 11) {
//                    Log.i("this", "uriProfileImage: " + uriProfileImage);
//                    picturePath = getRealPathFromURI_API11to18(MainActivity2.this, uriProfileImage);
//                    Log.d("Picture Path", picturePath);
//                }
//                else if(Build.VERSION.SDK_INT < 11) {
//                    Log.i("this", "uriProfileImage: " + uriProfileImage);
//                    picturePath = getRealPathFromURI_BelowAPI11(MainActivity2.this, uriProfileImage);
//                    Log.d("Picture Path", picturePath);
//                }
//                //File f = new File(uriProfileImage.getPath());
//                //int file_size1 = Integer.parseInt(String.valueOf(f.length()/1024));
//                //Log.d("Picture file_size1", file_size1+"");
//
//                File file = new File(picturePath);
//                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
//                Log.d("Picture file_size2", file_size+"");
//
//                uriProfileImage2=Uri.fromFile(new File(compressImage(picturePath)));
//
//                //compressImage(picturePath);
//                //uploadImageToFirebaseStorage();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API19(Context context, Uri uri){
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = { MediaStore.Images.Media.DATA };

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{ id }, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }


        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
            String[] proj = { MediaStore.Images.Media.DATA };
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    contentUri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if(cursor != null){
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }

        public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }


    public String compressImage(String imageUri) {

        //String filePath = getRealPathFromURI(imageUri);
        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            Log.d("this", "fileCreate: " );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Log.d("this", "filename: " + filename);
        //Toast.makeText(MainActivity2.this, "filename= "+filename, Toast.LENGTH_LONG).show();
        File file = new File(filename);
        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        Log.d("Picture file_size3", file_size+"");

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Jomir Dokan/Images");
        if (!file.exists()) {
            file.mkdirs();
            Log.d("this", "getFilename: " );
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }






    private void uploadImageToFirebaseStorage() {
        final StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            /*
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            profileImageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            Toast.makeText(MainActivity2.this,"upload: "+profileImageUrl, Toast.LENGTH_LONG).show();
                            Log.i("display:", ""+profileImageUrl);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
 */

            //final StorageReference ref = storageRef.child("images/mountains.jpg");
            UploadTask uploadTask = profileImageRef.putFile(uriProfileImage);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return profileImageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //Uri downloadUri = task.getResult();
                        progressBar.setVisibility(View.GONE);
                        profileImageUrl =task.getResult().toString();
                        //Toast.makeText(MainActivity2.this,"upload: "+profileImageUrl, Toast.LENGTH_LONG).show();
                        Log.i("display:", ""+profileImageUrl);
                    } else {
                        // Handle failures
                        Log.i("display:", "error"+profileImageUrl);
                    }
                }
            });
        }
    }


    public void show(final int i, final int i1) {
        final AlertDialog.Builder d = new AlertDialog.Builder(MainActivity2.this);
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




    public void updateFirebase() {
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

                index=index-1;
                Log.i("size : ", user.getName() + " index= " + index + olddata);
                if (usersEmail != null && usersEmail.equals(email)) {
                    databaseReference.child(olddata).child(String.valueOf(index)).child("phone").setValue(te3.getText().toString());
                    Log.i("size : ", user.getName() + " countSearchUsersPhone " + te3.getText().toString() + user.getAddress());
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

}