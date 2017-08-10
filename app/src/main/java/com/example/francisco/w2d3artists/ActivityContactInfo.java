package com.example.francisco.w2d3artists;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class ActivityContactInfo extends Fragment implements View.OnClickListener {
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    private static final String TAG = "Main";
    private WebView webView;
    ImageButton img;
    Button btnSave, btnDel;
    EditText etname, etlastname, etphone, etemail, company, address;
    Bitmap bitmap;
    String filepath = "";
    String source = "";
    DatabaseHelper databaseHelper;
    String id = "-1";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button btn1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ActivityContactInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityContactInfo newInstance(String param1, String param2) {
        ActivityContactInfo fragment = new ActivityContactInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_contact_info, container, false);
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

        // TODO: Update argument type and name
        void onFragmentInteraction(int integer);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        etname = (EditText) view.findViewById(R.id.etname);
        etlastname = (EditText) view.findViewById(R.id.etlastname);
        etphone = (EditText) view.findViewById(R.id.etphone);
        etemail = (EditText) view.findViewById(R.id.etemail);
        company = (EditText) view.findViewById(R.id.company);
        address = (EditText) view.findViewById(R.id.address);

        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnDel = (Button) view.findViewById(R.id.btnDel);
        img = (ImageButton) view.findViewById(R.id.img);

        btnSave.setOnClickListener(this);
        btnDel.setOnClickListener(this);

        img.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(getActivity());

        try {
            id = mParam1;
            if(Integer.parseInt(id)>=0){
                btnSave.setText("Edit");
                btnSave.setBackgroundColor(Color.parseColor("#F0AD4E"));
            }
            if(!id.equals("")) {
                ArrayList<MyContact> contacts = databaseHelper.getContacts(id);
                if (contacts.size() > 0) {
                    etname.setText(contacts.get(0).getName());
                    etlastname.setText(contacts.get(0).getLast_name());
                    etphone.setText(contacts.get(0).getPhone());
                    etemail.setText(contacts.get(0).getEmail());
                    company.setText(contacts.get(0).getCompany());
                    address.setText(contacts.get(0).getAddress());
                    byte[] b = contacts.get(0).getBitmap();
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    img.setImageBitmap(bitmap);
                }
            }
        }catch(Exception ex){}

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(bitmap!=null) {
            outState.putString("img", "uploaded");
            outState.putString("source", source);
            outState.putString("filepath", filepath);
        }
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            if (savedInstanceState.getString("img").equals("uploaded")) {
                filepath = savedInstanceState.getString("filepath");
                File file = new File(filepath);
                source = savedInstanceState.getString("source");
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //Landscape do some stuff

                    if(!savedInstanceState.getString("source").equals("landscape")){
                        bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 768, 1024);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        img.setImageBitmap(bitmap2);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    else {
                        bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1024, 768);
                        img.setImageBitmap(bitmap);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                } else {
                    //portrait

                    //rotates img
                    if(savedInstanceState.getString("source").equals("portrait")) {
                        bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 768, 1024);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        img.setImageBitmap(bitmap2);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    else {
                        bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1024 , 768);
                        img.setImageBitmap(bitmap);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                }
            }
        }catch(Exception ex){}
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
            //Get our saved file into a bitmap object:

            File file = new File(Environment.getExternalStorageDirectory() + File.separator +
                    "image.jpg");
            filepath = file.getAbsolutePath();
            Log.d(TAG, "onActivityResult: "+file.getAbsolutePath());

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                //Landscape do some stuff
                source = "landscape";
                bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 768, 1024);
                img.setImageBitmap(bitmap);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            else{
                //portrait
                bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1024 , 768);
                //rotates img
                source = "portrait";
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                img.setImageBitmap(bitmap2);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
            }



        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSave:
                if(etname.getText().toString().equals("") || etlastname.getText().toString().equals("") || etphone.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Name, last name, and phone are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    byte[] b = null;
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        b = stream.toByteArray();
                    }
                    MyContact contact = new MyContact(-1, etname.getText().toString(), etlastname.getText().toString(),
                            etphone.getText().toString(), etemail.getText().toString(), company.getText().toString(), address.getText().toString(), b);
                    Log.d(TAG, "onClick: " + contact.getName() + " " + contact.getPhone());
                    try {
                        if (Integer.parseInt(id) >= 0) {
                            databaseHelper.uploadNewContact(contact, id);
                            Toast.makeText(getActivity(), "Element updated successfully", Toast.LENGTH_SHORT).show();
                            mListener.onFragmentInteraction(0);
                        } else {
                            databaseHelper.saveNewContact(contact);
                            Toast.makeText(getActivity(), "Element created successfully", Toast.LENGTH_SHORT).show();
                            mListener.onFragmentInteraction(1);
                        }
                    } catch (Exception ex) {
                        databaseHelper.saveNewContact(contact);
                        Toast.makeText(getActivity(), "Element created successfully", Toast.LENGTH_SHORT).show();
                        mListener.onFragmentInteraction(1);
                    }
                }

                break;
            case R.id.btnDel:
                try {
                    if (Integer.parseInt(id) >= 0) {
                        databaseHelper.DeleteContact(id);
                        bitmap = null;
                        etname.setText("");
                        etlastname.setText("");
                        etphone.setText("");
                        etemail.setText("");
                        company.setText("");
                        address.setText("");
                        img.setImageResource(R.drawable.ic_person);
                        Toast.makeText(getActivity(), "Element deleted successfully", Toast.LENGTH_SHORT).show();
                        mListener.onFragmentInteraction(-1);
                    }
                }catch(Exception ex){}

                break;
            case R.id.img:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
                break;
        }
    }

}
