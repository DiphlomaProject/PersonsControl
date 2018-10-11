package com.example.artem.personscontrol;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.artem.personscontrol.DataClasses.Data_Singleton;
import com.example.artem.personscontrol.SupportLibrary.Network_connections;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public static ProfileFragment sharedInstance() { return new ProfileFragment(); }

    View view;
    EditText name;
    EditText email;
    EditText position;
    EditText phone;
    EditText city;
    EditText street;
    EditText country;
    String pathToNewImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);//Make sure you have this line of code.

        name = view.findViewById(R.id.nameEditText);
        phone = view.findViewById(R.id.phoneEditText);
        position = view.findViewById(R.id.positionEditText);
        email = view.findViewById(R.id.emailEditText);
        city = view.findViewById(R.id.cityEditText);
        street = view.findViewById(R.id.streetEditText);
        country = view.findViewById(R.id.countryEditText);

        SetOldInfo();

        //return inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    // Подключение меню к активности (декларативное или программное)
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_profile, menu);
        //return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_user_profile_save:
                // User chose the "Settings" item, show the app settings UI...
                boolean isPhone =  isValidPhoneNumber(phone.getText().toString());
                if(name.getText().length() <= 0 || !isPhone || email.getText().length() <= 0 || city.getText().length() <= 0
                        || street.getText().length() <= 0 || country.getText().length() <= 0){
                    if(isPhone)
                        Toast.makeText(getContext(), "The first you must fill all fields.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                HashMap<String, String> userInfo = new HashMap<>();
                userInfo.put("id", Data_Singleton.getInstance().currentUser.id);
                userInfo.put("token", Data_Singleton.getInstance().currentUser.token);
                userInfo.put("DisplayName", name.getText().toString());
                userInfo.put("phone", phone.getText().toString());
                userInfo.put("Address", street.getText().toString());
                userInfo.put("Country", country.getText().toString());
                userInfo.put("City", city.getText().toString());

                ((BaseActivity)getContext()).showProgressDialog();
                Network_connections network_connections = new Network_connections();
                network_connections.UpdateProfile(getContext(), userInfo);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            //if(phoneNumber.length() > 9 && phoneNumber.length() < 14)
            //return Patterns.PHONE.matcher(phoneNumber).matches();
            String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{12}$";
            CharSequence inputString = phoneNumber;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputString);

            if(!matcher.matches()) {
                Toast.makeText(getContext(), "Write correctly number (+xxx xxx-xx-xx)", Toast.LENGTH_LONG).show();
                return false;
            }
            else
                return true;
        }
        return false;
    }

    public  int isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return 1; // все разрешения есть
            } else {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}, Data_Singleton.RESULT_STORAGE_PERMISSIONS) ; // show ask about permission
                return -1; // permission создан в ручную
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return 0; // permission создан в програмно обработка ответов на програмные permission в onRequestPermissionsResult
        }
    }

    void TakePhoto(){
        if(isStoragePermissionGranted() == 1) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            //intent.putExtra("browseCoa", itemToBrowse);
            //Intent chooser = Intent.createChooser(intent, "Select a File to Upload");
            try {
                //startActivityForResult(chooser, FILE_SELECT_CODE);
                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), Data_Singleton.RESULT_SET_USER_PHOTO);
            } catch (Exception ex) {
                System.out.println("browseClick :" + ex);//android.content.ActivityNotFoundException ex
            }//try catch
        }//if isStoragePermissionGranted
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Data_Singleton.RESULT_STORAGE_PERMISSIONS)
            for(int i : grantResults) // проверяем получили ли мы все нужные разрешения если нет то выйти
                if(i == -1) {
                    String appName = getContext().getApplicationInfo().loadLabel(getContext().getPackageManager()).toString();
                    Toast.makeText(sharedInstance().getContext(), "You can allow storage permission in Settings. (Settings -> Apps & notifications -> " + appName + ")", Toast.LENGTH_LONG).show();
                    return;
                }

        //Toast.makeText(getContext(), "onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)", Toast.LENGTH_LONG).show();
        //StartApp(); // запустить функцию с обработкой данных
    }

    //get photo

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // In fragment class callback
        // Если активность завершилась без ошибок
        if (resultCode == RESULT_OK) {
            if(requestCode  == Data_Singleton.RESULT_SET_USER_PHOTO) {
                try {
                    Uri uri = data.getData();

                    String fullPath = getPath(getActivity(), uri);
                    String mimeType = getActivity().getContentResolver().getType(uri);
                    //Toast.makeText(getActivity(), fullPath, Toast.LENGTH_LONG ).show();
                    pathToNewImage = fullPath;
                    File imgFile = new  File(fullPath);

                    if(imgFile.exists()){

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        ImageView myImage = (ImageView)view.findViewById(R.id.userImageProfile);

                        myImage.setImageBitmap(myBitmap);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//if requestCode
        }//if resultCode
    }

    void SetOldInfo() {
        name.setText(Data_Singleton.getInstance().currentUser.displayName);
        phone.setText(Data_Singleton.getInstance().currentUser.phone);
        position.setText("Employee");
        email.setText(Data_Singleton.getInstance().currentUser.email);
        city.setText(Data_Singleton.getInstance().currentUser.city);
        street.setText(Data_Singleton.getInstance().currentUser.address);
        country.setText(Data_Singleton.getInstance().currentUser.country);


        ((ImageView) view.findViewById(R.id.userImageProfile)).setImageBitmap(null);


        if (Data_Singleton.getInstance().currentUser.image != null) {
            Bitmap bmp = Data_Singleton.getInstance().currentUser.image;
            //preferences_singleton.setUserImage(bmp);

            //((ImageView)findViewById(R.id.imageUser)).setImageBitmap(bmp);
            Resources mResources = getResources();
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                    mResources,
                    bmp
            );

            // Set the RoundedBitmapDrawable corners radius
            roundedBitmapDrawable.setCornerRadius(50.0f);
            roundedBitmapDrawable.setAntiAlias(true);
            // Set the ImageView image as drawable object
            ((ImageView) view.findViewById(R.id.userImageProfile)).setImageDrawable(roundedBitmapDrawable);
            //((ImageView) view.findViewById(R.id.userImageProfile)).setImageBitmap(preferences_singleton.userBitmapPhoto);
        }
    }

}
