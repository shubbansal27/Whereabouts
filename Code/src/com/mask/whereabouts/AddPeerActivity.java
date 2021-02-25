package com.mask.dell.whereabouts;


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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import com.mask.dell.whereabouts.common.*;

public class AddPeerActivity extends AppCompatActivity {

    private int searchOption = 0;
    private HashMap<Integer,String> posMap;
    private HashMap<String,String> nameMap,emailMap;
    private Context context;
    private FirebaseDatabase database=null;
    private ListView lv;
    private TextView textcountMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpeer);

        context = this;
        final TextView textSearchOption = (TextView)findViewById(R.id.searchType);
        textcountMatches = (TextView)findViewById(R.id.countMatches);
        final EditText editSearch = (EditText)findViewById(R.id.editSearch);
        Spinner spinner = (Spinner)findViewById(R.id.searchOptions);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textSearchOption.setText(parent.getItemAtPosition(position).toString());
                searchOption = position;
                editSearch.setText("");
                if(searchOption == 0) {
                    editSearch.setHint("8447503000");
                }
                else if(searchOption == 1) {
                    editSearch.setHint("sumitbansal291994@gmail.com");
                }
                else {
                    editSearch.setHint("Sumit");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        lv = (ListView)findViewById(R.id.listSearchResults);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMenuPressed(position);
            }
        });

        //create hashmap for email and name
        posMap = new HashMap<Integer,String>();
        nameMap = new HashMap<String,String>();
        emailMap = new HashMap<String,String>();

        Button btnSearch = (Button)findViewById(R.id.buttonSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPeer(editSearch.getText().toString());
            }
        });


    }

    public void onMenuPressed(int pos) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final String phoneNo = posMap.get(pos);
        alert.setTitle("Phone:  " + phoneNo);
        alert.setMessage("Name: " + nameMap.get(phoneNo) + "\n\n" + "Email: " + emailMap.get(phoneNo) + "\n");

        alert.setNegativeButton("Send Request", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                //create database connection
                if (database == null) database = FirebaseDatabase.getInstance();
                database.getReference().child("Peers").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        DataSnapshot ds = snapshot.child(Session.identity);
                        DataSnapshot dsPeer = null;
                        if(ds.exists()) dsPeer = ds.child(phoneNo);
                        if(dsPeer ==null || !dsPeer.exists()) {
                            DatabaseReference peerRequestRef = database.getReference("PeerRequest").child(phoneNo);
                            //current date-time
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                            String formattedDate = df.format(c.getTime());
                            peerRequestRef.child(Session.identity).setValue(formattedDate);
                            Toast.makeText(getApplicationContext(), "Request sent successfully !!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Already in your peer list!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        alert.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                });

        alert.setIcon(R.drawable.drama48);
        alert.show();

    }


    private void searchPeer(final String searchString) {

        textcountMatches.setText("Fetching data... please wait");
        lv.setAdapter(null);

        posMap.clear();
        //create database connection
        if(database == null) database = FirebaseDatabase.getInstance();
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                DataSnapshot usersRef = snapshot.child("UserTable");
                int count = 0;

                try {
                    ArrayList<String> peerList = new ArrayList<String>();
                    if (searchOption == 0) { //phone
                        DataSnapshot sdUser = usersRef.child(searchString.trim());
                        if (sdUser.exists()) {
                            String phoneNo = sdUser.getKey();
                            if (!phoneNo.equals(Session.identity)) {
                                String uname = (String) sdUser.child("Name").getValue();
                                String uemail = (String) sdUser.child("Email").getValue();
                                nameMap.put(phoneNo, uname);
                                emailMap.put(phoneNo, uemail);
                                posMap.put(posMap.size(), phoneNo);
                                peerList.add(uname);
                                count++;
                            }
                        }
                    } else if (searchOption == 1) { ///email
                        Iterator<DataSnapshot> itr = usersRef.getChildren().iterator();
                        while (itr.hasNext()) {
                            DataSnapshot sdUser = itr.next();
                            String dsEmail = ((String) sdUser.child("Email").getValue()).toLowerCase();
                            if (dsEmail.equals(searchString.trim().toLowerCase())) {
                                String phoneNo = sdUser.getKey();
                                if (!phoneNo.equals(Session.identity)) {
                                    String uname = (String) sdUser.child("Name").getValue();
                                    nameMap.put(phoneNo, uname);
                                    emailMap.put(phoneNo, dsEmail);
                                    posMap.put(posMap.size(), phoneNo);
                                    peerList.add(uname);
                                    count++;
                                    break;
                                }
                            }
                        }
                    } else { //name
                        Iterator<DataSnapshot> itr = usersRef.getChildren().iterator();
                        while (itr.hasNext()) {
                            DataSnapshot sdUser = itr.next();
                            String dsName = ((String) sdUser.child("Name").getValue()).toLowerCase();
                            if (dsName.startsWith(searchString.trim().toLowerCase())) {
                                String phoneNo = sdUser.getKey();
                                if (!phoneNo.equals(Session.identity)) {
                                    String uemail = (String) sdUser.child("Email").getValue();
                                    nameMap.put(phoneNo, dsName);
                                    emailMap.put(phoneNo, uemail);
                                    posMap.put(posMap.size(), phoneNo);
                                    peerList.add(dsName);
                                    count++;
                                }
                            }
                        }
                    }

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (context, android.R.layout.simple_list_item_1, peerList);

                    // DataBind ListView with items from ArrayAdapter
                    lv.setAdapter(arrayAdapter);


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Not found !!", Toast.LENGTH_SHORT).show();
                }

                textcountMatches.setText("Total Matches = " + count);
            }

            @Override
            public void onCancelled(DatabaseError e) {
                Toast.makeText(getApplicationContext(), "Not found !!", Toast.LENGTH_SHORT).show();
            }


        });

    }


}
