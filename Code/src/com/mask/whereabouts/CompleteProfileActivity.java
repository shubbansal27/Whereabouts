package mask.bits.whereabouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompleteProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        //Bitmap bmp = BitmapFactory.decodeStream(Session.dp.);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button btnSubmit = (Button)findViewById(R.id.sc3_submit);

        btnSubmit.setOnClickListener(this);

        Glide.with(this).load(Session.dp.toString()).into(imageView);

        ((TextView)findViewById(R.id.sc3_email)).setText(Session.email);
        ((TextView)findViewById(R.id.sc3_username)).setText(Session.name);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sc3_submit:
                createEntry();
                break;
            default:
                return;
        }
    }

    public void createEntry(){
        Session.identity = ((EditText) findViewById(R.id.sc3_mobile)).getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        try {
            database.getReference().child("UserTable").child(Session.identity).child("Email").setValue(Session.email);
            database.getReference().child("UserTable").child(Session.identity).child("Name").setValue(Session.name);
            database.getReference().child("UserTable").child(Session.identity).child("PhotoURL").setValue(Session.dp.toString());
            database.getReference().child("Messages").child(Session.identity).child("dummy").setValue("dummy msg");
            //FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("Messages").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot childSource = dataSnapshot.child(Session.identity);
                    if(childSource.exists()) {
                        startActivity(new Intent(CompleteProfileActivity.this, MainActivity.class));
                        finish();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }
        catch (Exception e){

        }
    }
}
