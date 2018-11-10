package app.patuhmobile.module.Upload;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codemybrainsout.placesearch.PlaceSearchDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import app.patuhmobile.App;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthService;
import app.patuhmobile.auth.events.SuccessEvent;
import app.patuhmobile.datahelper.HelperBridge;
import app.patuhmobile.model.ResponeApiModel;
import app.patuhmobile.module.adapter.CustomPagerAdapter;
import app.patuhmobile.utils.NetworkUtils;
import app.patuhmobile.utils.PermissionUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends Fragment implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtil.PermissionResultCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;


    ImageView headerImg;
    TextView btnLokasi, btnUnggah;
    EditText etJudul, etCerita;
    AuthService authService;
    App mApp;
    Uri uri;
    String filePath;
    private File finalFile = null;
    public final int REQUEST_CAMERA = 100;
    public final int SELECT_FILE = 101;

    EventBus mBus = EventBus.getDefault();

    PermissionUtil permissionUtils;
    ArrayList<String> permissions=new ArrayList<>();



    private ProgressDialog dialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private double lat = 0.0;
    private double lng = 0.0 ;
    private ViewPager mViewpager;
    private CustomPagerAdapter mAdapterViewpager;

    private OnFragmentInteractionListener mListener;

    public  UploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = App.get(getActivity());
        authService = new AuthService(mApp,mApp.getAuthInfo(), mApp.getEventBus());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dialog = new ProgressDialog(getActivity());

    }

    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //mBus = mApp.getEventBus();
        mBus.register(this);
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_upload, container, false);
        return mView;
    }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        headerImg = (ImageView) view.findViewById(R.id.image_content);
        btnLokasi = (TextView) view.findViewById(R.id.btn_lokasi);
        btnUnggah = (TextView) view.findViewById(R.id.btn_unggah);
        etJudul = (EditText) view.findViewById(R.id.etJudul);
        etCerita = (EditText) view.findViewById(R.id.etCerita);
        setPermissions();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        headerImg.setOnClickListener(view1 -> selectImage());
        btnUnggah.setOnClickListener(view1 -> postData());
        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlaceSearchDialog placeSearchDialog = new PlaceSearchDialog.Builder(getActivity())
                        .setHeaderImage(R.drawable.patuh)
                        .setHintText("Ketik nama lokasi")
                        .setHintTextColor(R.color.colorGray)
                        .setNegativeText("BATAL")
                        .setNegativeTextColor(R.color.colorGray)
                        .setPositiveText("CARI")
                        .setPositiveTextColor(R.color.palletepurplesoft)
                        .setLocationNameListener(new PlaceSearchDialog.LocationNameListener() {
                            @Override
                            public void locationName(String locationName) {
                                btnLokasi.setText(locationName);
                            }
                        })
                        .build();
                placeSearchDialog.show();

            }
        });
        /*mViewpager = (ViewPager) mView.findViewById(R.id.viewpager_upload);
        mAdapterViewpager = new CustomPagerAdapter(getActivity());
        mViewpager.setAdapter(mAdapterViewpager);*/
    }


    public void postData() {
        dialog.setMessage("Mengirimkan data, mohon menunggu..");
        dialog.setCancelable(true);
        dialog.show();
        if (NetworkUtils.isOnline(getActivity())){
            authService.unggahCerita("0", "Lakalantas",
                    etJudul.getText().toString(), etCerita.getText().toString(), String.valueOf(HelperBridge.latitude),
                    String.valueOf(HelperBridge.longitude),
                    authService.getAuthInfo().getUserId(),btnLokasi.getText().toString(), finalFile, new AuthService.DataCallback() {
                @Override
                public void onSuccess(ResponeApiModel responeRegisterModel) {
                    if (dialog.isShowing()) dialog.dismiss();
                    Toast.makeText(getActivity(), "Berhasil", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                   // Toast.makeText(getActivity(), "SUKSES", Toast.LENGTH_SHORT).show();
                    Log.e("APIUNGGAH", "SUKSES");
                }

                @Override
                public void onFail(Throwable e) {
                    if (dialog.isShowing()) dialog.dismiss();
                    Toast.makeText(getActivity(), "GAGAL, mohon coba lagi", Toast.LENGTH_SHORT).show();
                    Log.e("APIUNGGAH", "GAGAL");
                }
            });

        } else {
            if (dialog.isShowing()) dialog.dismiss();
            Toast.makeText(getActivity(), "Tidak ada internet", Toast.LENGTH_SHORT).show();
          //  view.hideProgress();
            //view.showToast("Tidak ada Internet");
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBus.unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        try {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lng = mLastLocation.getLongitude();
                HelperBridge.latitude = lat;
                HelperBridge.longitude = lng;
                //Toast.makeText(getActivity(), lat + ", " + lng, Toast.LENGTH_LONG).show();
            }
        } catch (SecurityException e) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void selectImage() {
        final CharSequence[] items = { "Pilih dari Kamera", "Pilih dari Galeri Foto",
                "Batal" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    private void setPermissions() {
        permissionUtils=new PermissionUtil(getActivity());
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.CAMERA);
    }



    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Fragment frag = this;
        frag.startActivityForResult(intent, REQUEST_CAMERA);

        //Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePicture, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Fragment frag = this;
        frag.startActivityForResult(pickPhoto , SELECT_FILE);//one can be replaced with any action c
        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);*/
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data, resultCode);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";

        // ExternalStorageProvider
       if (DocumentsContract.isDocumentUri(context, uri)) {
            // MediaProvider
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String[] ids = wholeID.split(":");
            String id;
            String type;
            if (ids.length > 1) {
                id = ids[1];
                type = ids[0];
            } else {
                id = ids[0];
                type = ids[0];
            }

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{id};
            final String column = "_data";
            final String[] projection = {column};
            Cursor cursor = context.getContentResolver().query(contentUri,
                    projection, selection, selectionArgs, null);

            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex(column);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
            return filePath;
        } else {
            String[] proj = {MediaStore.Audio.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                if (cursor.moveToFirst())
                    filePath = cursor.getString(column_index);
                cursor.close();
            }

            return filePath;
        }
    }

    private void onSelectFromGalleryResult(Intent data, int resultCode) {

        uri = data.getData();
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        headerImg.setImageBitmap(bm);
        filePath = getRealPathFromURI(uri);
        finalFile = new File(filePath);
        //uri = data.getData();
        //permissionUtils.check_permission(permissions,"Explain here why the app needs permissions",1);
        /*uri = data.getData();
        String path = uri.getPath(); // "/mnt/sdcard/FileName.mp3"
        finalFile = new File(path);*/
        //finalFile = new File(String.valueOf(bm.compress(Bitmap.CompressFormat.PNG, 90, outStream)));
        //permissionUtils.check_permission(permissions,"Explain here why the app needs permissions",1);
    }



    private void onCaptureImageResult(Intent data) {

        Bitmap photo = (Bitmap) data.getExtras().get("data");
        headerImg.setImageBitmap(photo);
        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        uri = getImageUri(getActivity(), photo);
        filePath = getRealPathFromURI(uri);
        finalFile = new File(filePath);


        /*Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".PNG");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        headerImg.setImageBitmap(thumbnail);
        uri = data.getData();
        filePath = getRealPathFromURI(uri);
        finalFile = new File(filePath);*/


        /*uri = data.getData();
        permissionUtils.check_permission(permissions,"Explain here why the app needs permissions",1);*/
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
            cursor = getActivity().getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;

        /*if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }*/
    }

    @Override
    public void PermissionGranted(int request_code) {
        filePath = getRealPathFromURI_API19(getActivity(), uri);
        finalFile = new File(filePath);
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {

    }

    @Override
    public void PermissionDenied(int request_code) {

    }

    @Override
    public void NeverAskAgain(int request_code) {

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void checkPermission();
    }



    @Subscribe
    public void onSuccesEvent(SuccessEvent event){
        if (event.getRequestCode() == SELECT_FILE) {
            onSelectFromGalleryResult(event.getMdata(), event.getResultCode());
        } else if (event.getRequestCode() == REQUEST_CAMERA) {
            onCaptureImageResult(event.getMdata());
        }
    }


}
