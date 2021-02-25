package com.mask.dell.whereabouts.common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Users {

    FirebaseDatabase database;

    /* constructor */
    public Users() {
        database = FirebaseDatabase.getInstance();
    }


    /*
    * method: checkPhoneExistence()
    * desc:
    * args:
    * return-type:
    */

    public boolean checkPhoneExistence(String phone) {
        DatabaseReference users = database.getReference("UserTable").child(phone);
        users.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            snapshot.exists();

                        } catch (Exception e) {
                          //  txtStatus.setText(e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {

                    }


                });

        return false;
    }


}
