package app.patuhmobile.module;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import app.patuhmobile.App;
import app.patuhmobile.AppConfig;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.auth.AuthService;
import app.patuhmobile.datahelper.HelperBridge;
import app.patuhmobile.model.ResponeApiModel;
import app.patuhmobile.model.User;
import app.patuhmobile.model.UserProfile;
import app.patuhmobile.module.Home.HomeActivity;
import app.patuhmobile.module.Login.LoginActivity;
import app.patuhmobile.utils.ImagePicker;
import app.patuhmobile.utils.PermissionUtil;

public class DaftarActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {

    private EditText etNama, etTgLahir, etUserid, etLokasi, etHP, etEmail, etPasswd;
    private CheckBox checkBox;
    private TextView tvTitle;
    private String filePath;
    private AuthService authService;
    private ImageView btnImage, btnBack;
    private String fileImg = "";
    private File finalFile = null;


    public static final int PICK_IMAGE = 100;
    public static final int PICK_CAMERA = 99;

    Service service;


    private ProgressDialog dialog;

    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    private Uri uri;

    private App mApp;
    private AuthInfo session;

    PermissionUtil permissionUtils;
    ArrayList<String> permissions=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(this);
        setContentView(R.layout.layout_pro_daftar);
        mApp = App.get(this);
        session = mApp.getAuthInfo();
        authService = new AuthService(mApp, session, new EventBus());
        btnImage = (ImageView) findViewById(R.id.img_profile);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        etNama = (EditText) findViewById(R.id.etNama);
        etUserid = (EditText) findViewById(R.id.etUserid);
        etTgLahir = (EditText) findViewById(R.id.etTgLahir);
        etLokasi = (EditText) findViewById(R.id.etLokasi);
        etHP = (EditText) findViewById(R.id.etHP);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPasswd = (EditText) findViewById(R.id.etPasswd);
        tvTitle = (TextView) findViewById(R.id.tv_daftar);
        tvTitle.setText(HelperBridge.isClickedforUpdate ? "SUNTING PROFILE" : "DAFTAR");
        Button btnSimpan = (Button) findViewById(R.id.btn_simpan);
        btnSimpan.setText(HelperBridge.isClickedforUpdate ? "SUNTING" : "DAFTAR");
        LinearLayout vSK = (LinearLayout) findViewById(R.id.ll_sk);
        Button btnBatal = (Button) findViewById(R.id.btn_batal);
        checkBox = (CheckBox)findViewById(R.id.cbSK);
       // btnImage.setOnClickListener(view -> onPickImage());
        btnImage.setOnClickListener(view -> selectImage());
        etUserid.setVisibility(HelperBridge.isClickedforUpdate ? View.GONE : View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DaftarActivity.this, HelperBridge.isClickedforUpdate ? HomeActivity.class : LoginActivity.class));
                if (HelperBridge.isClickedforUpdate) {
                    HelperBridge.showProfilefromHome = true;
                } else {
                    HelperBridge.showProfilefromHome = false;
                }
                finish();
            }
        });
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DaftarActivity.this, HelperBridge.isClickedforUpdate ? HomeActivity.class : LoginActivity.class));
                if (HelperBridge.isClickedforUpdate) {
                    HelperBridge.showProfilefromHome = true;
                } else {
                    HelperBridge.showProfilefromHome = false;
                }
                finish();
            }
        });
        vSK.setVisibility(HelperBridge.isClickedDaftarFromProfile ? View.GONE : View.VISIBLE);
        if (checkBox.getVisibility() == View.VISIBLE) {
            btnSimpan.setOnClickListener(this);
        } else {
            btnSimpan.setOnClickListener(view -> simpanData());
        }
        dateFormatter = new SimpleDateFormat("ddMMyyyy", Locale.US);
        //setDateTimeField();
        etHP.addTextChangedListener(new TextWatcher() {
            int prevL = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevL = etHP.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if ((prevL < length) && (length == 4)) {
                    editable.append("-");
                }
            }
        });
        etTgLahir.addTextChangedListener(new TextWatcher() {
            int prevL = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevL = etTgLahir.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if ((prevL < length) && (length == 2 || length == 5)) {
                    editable.append("-");
                }
            }
        });
        if (session.getUsername() != null){
            Glide.with(this)
                    .load(AppConfig.API_BASE_URL + "UserProfile/GetProfilePic?UserId=" + session.getUserId())
                    .animate(R.anim.abc_fade_in)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(btnImage);

            try {
                finalFile = new File(session.getPicprofile());
            } catch (Exception e) {}
            etNama.setText(session.getUsername());
            etTgLahir.setText(session.getDob());
            etLokasi.setText(session.getLokasi());
            etHP.setText(session.getHp());
            etEmail.setText(session.getMail());
            etUserid.setText(session.getUserId());
        }
        //filePath = getRealPathFromURIPath(uri, DaftarActivity.this);
        //finalFile = new File(filePath);
    }


    private void selectImage() {
        final CharSequence[] items = { "Pilih dari Kamera", "Pilih dari Galeri Foto",
                "Batal" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Pilih dari Kamera")) {
                    HelperBridge.userChoosenTask="Pilih dari Kamera";

                    cameraIntent();

                    //mListener.checkPermission();
                } else if (items[item].equals("Pilih dari Galeri Foto")) {
                    HelperBridge.userChoosenTask="Pilih dari Galeri Foto";
                    galleryIntent();

                    //mListener.checkPermission();
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_CAMERA);

        //Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePicture, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , PICK_IMAGE);//one can be replaced with any action c
        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);*/
    }




    public void onPickImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_IMAGE)
                onSelectFromGalleryResult(data, resultCode);
            else if (requestCode == PICK_CAMERA){
                onCaptureImageResult(data);
            }
        }

        /*super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
            btnImage.setImageBitmap(bitmap);
            uri = data.getData();
            permissionUtils.check_permission(permissions,"Explain here why the app needs permissions",1);
        }*/
    }


    private void onSelectFromGalleryResult(Intent data, int resultCode) {

        uri = data.getData();
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), uri);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        btnImage.setImageBitmap(bm);
        filePath = getRealPathFromURI(uri);
        finalFile = new File(filePath);

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = this.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    private void onCaptureImageResult(Intent data) {

        Bitmap photo = (Bitmap) data.getExtras().get("data");
        btnImage.setImageBitmap(photo);
        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        uri = getImageUri(this, photo);
        filePath = getRealPathFromURI(uri);
        finalFile = new File(filePath);
    }


    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }



    private void simpanData(){
        if (finalFile != null){
            dialog.setMessage("Mengirim data, mohon menunggu..");
            dialog.setCancelable(false);
            dialog.show();
            authService.register(etUserid.getText().toString().equals("") ? session.getUserId() : etUserid.getText().toString() , etPasswd.getText().toString(), etNama.getText().toString(), etEmail.getText().toString(),
                    etHP.getText().toString(), etLokasi.getText().toString(), etTgLahir.getText().toString(), finalFile, new AuthService.SignupCallback() {
                        @Override
                        public void onSuccess(ResponeApiModel responBody) {
                            UserProfile up = new UserProfile(etUserid.getText().toString().equals("") ? session.getUserId() : etUserid.getText().toString(), etTgLahir.getText().toString(), etLokasi.getText().toString()
                                    , etHP.getText().toString(), etEmail.getText().toString(), 0.0);
                            User user = new User(etUserid.getText().toString().equals("") ? session.getUserId() : etUserid.getText().toString(), etNama.getText().toString()
                                    , session.getLoginType(), up, filePath, 0.0);
                            session.setPicprofile(filePath);
                            session.updateFromLoginResponse(user);
                            Toast.makeText(DaftarActivity.this, responBody.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DaftarActivity.this, HelperBridge.isClickedforUpdate ? HomeActivity.class : LoginActivity.class));

                            if (HelperBridge.isClickedforUpdate) {
                                HelperBridge.showProfilefromHome = true;
                            } else {
                                HelperBridge.showProfilefromHome = false;
                            }
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            finish();
                        }

                        @Override
                        public void onFail(Throwable e) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toast.makeText(DaftarActivity.this, e + "", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Gambar harus dilengkapi, klik pada foto untuk upload gambar", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    private void setDateTimeField() {
        etTgLahir.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etTgLahir.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.etTgLahir:
                toDatePickerDialog.show();
                break;
            case R.id.btn_simpan:
                if (HelperBridge.isClickedDaftarFromProfile){
                    if(!TextUtils.isEmpty(etPasswd.getText())){
                        simpanData();
                    } else {
                        Toast.makeText(this, "Lengkapi Password terbaru anda terlebih dahulu!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(checkBox.isChecked() && !TextUtils.isEmpty(etPasswd.getText())){
                        simpanData();
                    } else {
                        Toast.makeText(this, "Anda harus menceklist Syarat dan Ketentuan dan mengisi password terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Subscribe
    void onEvent(){

    }

}
