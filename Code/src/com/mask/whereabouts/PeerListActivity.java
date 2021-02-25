package com.mask.dell.whereabouts;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.mask.dell.whereabouts.common.*;

public class PeerListActivity  extends AppCompatActivity {

    private HashMap<Integer,String> posMap;
    private HashMap<String,String> nameMap,emailMap;
    private Context context;
    private FirebaseDatabase database = null;
    private TextView textCountPeer;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peerlist);

        context = this;
        textCountPeer = (TextView)findViewById(R.id.countPeer);
        lv = (ListView)findViewById(R.id.peer_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMenuPressed(position);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onMenuLongPressed(position);
                return false;
            }
        });

        //create hashmap for email and name
        posMap = new HashMap<Integer,String>();
        nameMap = new HashMap<String,String>();
        emailMap = new HashMap<String,String>();

        //fetch peer list
        fetchList();

    }


    public void onMenuPressed(int pos) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        String phoneNo = posMap.get(pos);
        alert.setTitle("Phone:  " + phoneNo);
        alert.setMessage("Name: " + nameMap.get(phoneNo) + "\n\n" + "Email: " + emailMap.get(phoneNo) + "\n");

        alert.setNegativeButton("Chat", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               //
            }
        });

        alert.setNeutralButton("Drop Message", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Your action here
            }
        });

       /* alert.setPositiveButton("Track Me",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        */

        alert.setIcon(R.drawable.drama48);
        alert.show();

    }

    public void onMenuLongPressed(int pos) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final String phoneNo = posMap.get(pos);
        alert.setTitle("Remove Peer");
        alert.setMessage("Phone:  " + phoneNo + "\n" + "Name: " + nameMap.get(phoneNo) + "\n" + "Email: " + emailMap.get(phoneNo) + "\n");

        alert.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                database.getReference().child("Peers").child(Session.identity).child(phoneNo).setValue(null);
                database.getReference().child("Peers").child(phoneNo).child(Session.identity).setValue(null);
                fetchList();
            }
        });

        alert.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        alert.setIcon(R.drawable.remove48);
        alert.show();
    }

    private void fetchList() {

        textCountPeer.setText("Fetching data..please wait");
        posMap.clear();
        lv.setAdapter(null);

        //create database connection
        if(database==null) database = FirebaseDatabase.getInstance();
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                DataSnapshot usersRef = snapshot.child("UserTable");
                DataSnapshot peersRef = snapshot.child("Peers").child(Session.identity);
                int count = 0;

                try {
                    if (peersRef.exists()) {
                        ArrayList<String> peerList = new ArrayList<String>();

                        Iterator<DataSnapshot> itr = peersRef.getChildren().iterator();
                        while (itr.hasNext()) {
                            DataSnapshot sdPeer = itr.next();
                            String phoneNo = sdPeer.getKey();

                            //fetch username from userTable
                            DataSnapshot sdUser = usersRef.child(phoneNo);
                            if(sdUser.exists()) {
                                String uname = (String)sdUser.child("Name").getValue();
                                String uemail = (String)sdUser.child("Email").getValue();
                                nameMap.put(phoneNo,uname);
                                emailMap.put(phoneNo,uemail);
                                posMap.put(posMap.size(),phoneNo);
                                peerList.add(uname);
                                count++;
                            }
                        }

                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                                (context, android.R.layout.simple_list_item_1, peerList);

                        // DataBind ListView with items from ArrayAdapter
                        lv.setAdapter(arrayAdapter);

                    } else {
                        //txtStatus.setText("Phone doesn't exist !!");
                    }
                } catch (Exception e) {
                    //txtStatus.setText(e.getMessage());
                }

                textCountPeer.setText("Count = " + count);
            }

            @Override
            public void onCancelled(DatabaseError e) {
                //txtStatus.setText(e.getMessage());
            }


        });
    }

}
