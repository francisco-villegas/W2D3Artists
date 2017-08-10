package com.example.francisco.w2d3artists;

/**
 * Created by FRANCISCO on 07/08/2017.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements ActivityContactList.OnFragmentInteractionListener, ActivityContactInfo.OnFragmentInteractionListener {

    ActivityContactList fragmentContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        fragmentContactList = new ActivityContactList();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.fragment_contact_list, fragmentContactList, fragmentContactList.getClass().getName());
        ft.addToBackStack(fragmentContactList.getClass().getName());
        ft.commit();


    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        try {
            if (Integer.parseInt(string) > 0) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ActivityContactInfo fragmentContactInfo = ActivityContactInfo.newInstance(string,"");
                ft.replace(R.id.flFrag1, fragmentContactInfo, fragmentContactInfo.getClass().getName());
                ft.addToBackStack(fragmentContactInfo.getClass().getName());
                ft.commit();
            }
        }catch (Exception ex){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ActivityContactInfo fragmentContactInfo = new ActivityContactInfo();
            ft.replace(R.id.flFrag1, fragmentContactInfo, fragmentContactInfo.getClass().getName());
            ft.addToBackStack(fragmentContactInfo.getClass().getName());
            ft.commit();
        }
    }

    @Override
    public void onFragmentInteraction(int integer) {
        fragmentContactList.NotificationItemChanged();
    }

}