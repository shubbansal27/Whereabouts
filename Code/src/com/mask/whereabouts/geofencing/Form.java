package com.google.android.gms.location.sample.geofencing;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.sample.geofencing.utils.ApplicationUtils;
import com.google.android.gms.location.sample.geofencing.utils.DateSelector;
import com.google.android.gms.location.sample.geofencing.utils.TimeSelector;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

//import com.example.instaspot.di.MainApplication;

//import javax.inject.Inject;
//import javax.inject.Provider;


/**
 * Created by venkat on 11/7/2016.
 */

public class Form extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {
    EditText stTime,stDate,edDate,edTime,desc,title;
    Button ok,mapPicker;
    Intent intent;
    TextView locDetails,radius;
    int PLACE_PICKER_REQUEST=1;
    SeekBar seekBar;
    int ACCESS_FINE_LOCATION_ID = 100 ;

    String latLong;

    /*@Inject
    Provider<BackGroundTask> backGroundTask;

    @Inject
    ReminderDAO reminderDAO;

    @Inject
    DbOperations dbOperations;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Inside On Create:Form Activity");
        //MainApplication.component().inject(this);
        System.out.println("Inside On Create After Injection:Form Activity");
        setContentView(R.layout.form);



        desc=(EditText) findViewById(R.id.desc);
        title=(EditText) findViewById(R.id.title);
        stDate=(EditText) findViewById(R.id.dateST);
        stTime=(EditText) findViewById(R.id.timeSt);
       // edDate=(EditText) findViewById(R.id.dateET);
        edTime=(EditText) findViewById(R.id.timeEt);

        //seekBar = (SeekBar) findViewById(R.id.seekBar2) ;

        locDetails=(TextView) findViewById(R.id.locDet);
        //radius=(TextView) findViewById(R.id.radius);

        ok=(Button) findViewById(R.id.saveB) ;
        mapPicker=(Button) findViewById(R.id.selectLocB) ;
        stDate.setKeyListener(null);
        stTime.setKeyListener(null);
//        edDate.setKeyListener(null);
        edTime.setKeyListener(null);

        ok.setOnClickListener(this);
        mapPicker.setOnClickListener(this);

      //  seekBar.setOnSeekBarChangeListener(this);

        DateSelector stDatepicker= new DateSelector(this,R.id.dateST);
        TimeSelector stTimeSelector = new TimeSelector(this,R.id.timeSt);
        //DateSelector edDatepicker= new DateSelector(this,R.id.dateET);
        TimeSelector edTimePicker= new TimeSelector(this,R.id.timeEt);



    }
    public void onClick(View v){
        if(v.getId()==R.id.saveB){

            String peername = title.getText().toString();
            String message = desc.getText().toString();
            String stdate = stDate.getText().toString();
            String sttime = stTime.getText().toString();
            //String eddate = edDate.getText().toString();
            String edtime = edTime.getText().toString();
            //String loc = locDetails.getText().toString();
            String loc="28.7852,88.9088";
            System.out.println(loc);
            System.out.println("button clicked"+peername);
//            int progress= seekBar.getProgress();
            if(peername==null || peername.equals("") || message==null || message.equals("") || stdate==null || stdate.equals("") ||  sttime==null || sttime.equals("") || edtime==null || edtime.equals("") || loc==null || loc.equals("")){
                Toast.makeText(this,"Please enter all the details..",Toast.LENGTH_LONG).show();
            }
            else {
                //ReminderLocation rl = new ReminderLocation();
                ApplicationUtils.title=peername;
                ApplicationUtils.desc=message;
                ApplicationUtils.stdate=stdate;
               // ApplicationUtils.eddate=eddate;
                ApplicationUtils.sttime=sttime;
                ApplicationUtils.edtime=edtime;
                //ApplicationUtils.radius=progress;
                int type = 0;


                Calendar cal = ApplicationUtils.getCalender(stdate, sttime);


                //intent = new Intent(this, AlarmBroadcastReceiver.class);

                String reminder = "" + this.desc.getText();
                Toast.makeText(this, "ALARM STARTED......." + reminder + " " + cal.get(Calendar.MONTH), Toast.LENGTH_LONG).show();
               // AlarmManager am1 = (AlarmManager) getSystemService(ALARM_SERVICE);


                System.out.println("Inside On Click:Form Activity");
                System.out.println("Inside On Click:Form Activity BackGroundTask Is:-");

                //bg.execute("addInfo",title1,description,stdate,eddate,sttime,edtime,loc,latlong,progress+"",type+"");
               // DbOperations dbOperations = new DbOperations(this);
                //ReminderDAO reminderDAO = new ReminderDAOImpl();
                //BackGroundTask bg =  new BackGroundTask(this,dbOperations,reminderDAO);
             //   long id=reminderDAO.addInformation(dbOperations,title1,description,stdate,eddate,sttime,edtime,latLong,loc,""+progress,""+type);

              //  intent.putExtra("id",""+id);

//starting firebase sending code
                String phone="9582853277";

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference drpmsg = database.getReference("DropMessages");
               // final DatabaseReference dropmsg = drpmsg.child(peername);
                drpmsg.child(peername).child(phone).child("Text").setValue(message);
                drpmsg.child(peername).child(phone).child("Location").setValue(loc);
                drpmsg.child(peername).child(phone).child("EndTime").setValue(edtime);
                drpmsg.child(peername).child(phone).child("StartTime").setValue(sttime);
                drpmsg.child(peername).child(phone).child("date").setValue(stdate);
              //  drpmsg.child(peername).child("Text").setValue(message);



               /* PendingIntent pi1 = PendingIntent.getBroadcast(this.getApplicationContext(), 1234578, intent, 0);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
                sharedPreferences.edit().putLong("stTime", cal.getTimeInMillis()).apply();
                System.out.println("ALARM STARTED1" + cal.getTimeInMillis());
                //BackGroundTask backGroundTask = new BackGroundTask(this);*/
                System.out.println(loc);

               // System.out.println("id:-"+id);
                //am1.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi1);

                finish();

                 //intent.addFlags(1);
            }

        }
        else if(v.getId()==R.id.selectLocB){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_FINE_LOCATION_ID);
                }
            }
            try {


                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }


    }
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        float value = (float)Math.round(progress/100) / (float)10;
        radius.setText ("Radius: "+value+"km");
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this,data);
                locDetails.setText(place.getAddress());

                ApplicationUtils.latlng=place.getLatLng();

                Toast.makeText(this, ""+place.getLatLng(), Toast.LENGTH_LONG).show();
                latLong = place.getLatLng().toString();
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==ACCESS_FINE_LOCATION_ID){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "No Permission", Toast.LENGTH_SHORT).show();
            }
        }
        // Forward results to EasyPermissions
        // EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        //EasyPermissions.requestPermissions()
    }

    //////////
    /*public void onDestroy() {
        intent.putExtra("State","Destroy");
        super.onDestroy();


    }*/
    ///////////////

}

