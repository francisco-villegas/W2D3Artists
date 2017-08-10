package com.example.francisco.w2d3artists;

/**
 * Created by FRANCISCO on 07/08/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class ActivityContactList extends Fragment implements View.OnClickListener, OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button btn1;

    private static final String TAG = "MA";
    ListView list;
    String[] itemname;
    String[] id;
    String[] phone;

    byte[][] imgid ;

    Button btnAdd;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ActivityContactList fragmentContactList;

    private OnFragmentInteractionListener mListener;

    public ActivityContactList() {
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
    public static ActivityContactList newInstance(String param1, String param2) {
        ActivityContactList fragment = new ActivityContactList();
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
        return inflater.inflate(R.layout.activity_contact_list, container, false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        void onFragmentInteraction(String string);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        Log.d(TAG, "onClick: contactlist");

        list = (ListView) view.findViewById(R.id.list);
        list.setOnItemClickListener(this);


        NotificationItemChanged();


        btnAdd.setOnClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mListener.onFragmentInteraction(id[i]);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: contactlist");
        mListener.onFragmentInteraction("");
    }

    public void NotificationItemChanged(){
        Log.d(TAG, "NotificationItemChanged: ");
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());


        try {
            ArrayList<MyContact> contacts = databaseHelper.getContacts("-1");

            itemname = new String[contacts.size()];
            imgid = new byte[contacts.size()][];
            id = new String[contacts.size()];
            phone = new String[contacts.size()];
            for (int i = 0; i < contacts.size(); i++) {
                itemname[i] = contacts.get(i).getName() + " " + contacts.get(i).getLast_name();
                imgid[i] = contacts.get(i).getBitmap();
                id[i] = "" + contacts.get(i).getID();
                phone[i] = contacts.get(i).getPhone();
            }

            CustomListAdapter adapter = new CustomListAdapter(getActivity(), itemname, imgid, id, phone);
            list.setAdapter(adapter);

        }catch(Exception ex){}
    }

}