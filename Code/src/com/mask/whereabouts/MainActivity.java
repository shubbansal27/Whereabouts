package mask.bits.whereabouts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    //private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseDatabase database;
    private SQLiteDatabase db;

    private Button msg_snd;
    private EditText msg;
    private ArrayList chatHistory;
    private ChatAdapter adapter;
    private ListView messagesContainer;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String source = "9582853277";
    //9582853277
    protected void onCreate(Bundle savedInstanceState){


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        /*
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
   +         startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
        */


        msg_snd = (Button)findViewById(R.id.chatSendButton);

        msg_snd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEntry();
            }
        });
        database = FirebaseDatabase.getInstance();
        db=openOrCreateDatabase("message", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS messages (ID TEXT, MSG TEXT, SENDER TEXT, RECEIVER TEXT);");

        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        adapter = new ChatAdapter(MainActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);
        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        display();

        database.getReference().child("Messages").child(Session.identity).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //System.out.println("+++++++++++++++++++++++++++++");
                DataSnapshot childSource = dataSnapshot.child(source);
                if(childSource.exists()) {
                    //System.out.println("asdfgh");
                    Iterator<DataSnapshot> itr = childSource.getChildren().iterator();
                    while (itr.hasNext()) {
                        ChatMessage m = new ChatMessage();
                        DataSnapshot msgNode = itr.next();

                        if(Session.lastReadVal < Long.parseLong(msgNode.getKey())){
                            db.execSQL("INSERT INTO messages VALUES('" + msgNode.getKey() + "', '" + msgNode.getValue().toString() + "', '" + source +"', 'null')");
                            Session.lastReadVal = Long.parseLong(msgNode.getKey());
                            m.setId(Long.parseLong(msgNode.getKey()));
                            m.setMessage(msgNode.getValue().toString());
                            m.setMe(false);
                            m.setUserId(Long.parseLong(source));
                            displayMessage(m);

                            //delete from firebase
                            msgNode.getRef().setValue(null);

                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void createEntry() {
        msg = (EditText)findViewById(R.id.messageEdit);
        String msgtxt = msg.getText().toString();
        if (TextUtils.isEmpty(msgtxt)) {
            return;
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = Objects.toString(timestamp.getTime(), null);
        database.getReference().child("Messages").child(source).child(Session.identity).child(time).setValue(msgtxt);
        System.out.println("INSERT INTO messages VALUES('" + time + "', '" + msgtxt + "', 'null'" + ", '" + source + "')");
        db.execSQL("INSERT INTO messages VALUES('" + time + "', '" + msgtxt + "', 'null'" + ", '" + source + "')");
        Session.lastReadVal = Long.parseLong(time);
        ChatMessage m = new ChatMessage();
        m.setMe(true);
        m.setMessage(msgtxt);
        m.setUserId(Long.parseLong(source));
        m.setId(Long.parseLong(time));
        msg.setText("");
        displayMessage(m);
    }

    private void displayMessage(ChatMessage message) {
        System.out.println(message.getMessage());
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void display(){
        Cursor result = db.rawQuery("SELECT * FROM messages ORDER BY ID", null);
        chatHistory = new ArrayList<ChatMessage>();

        result.moveToFirst();
        while (result.isAfterLast() == false) {
            ChatMessage msg = new ChatMessage();
            msg.setId(Long.parseLong(result.getString(0)));
            Session.lastReadVal = Long.parseLong(result.getString(0));
            msg.setMessage(result.getString(1));
            msg.setUserId(Long.parseLong(source));
            if(result.getString(3).contains("null"))
                msg.setMe(false);
            else
                msg.setMe(true);

            chatHistory.add(msg);
            result.moveToNext();
        }

        for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = (ChatMessage) chatHistory.get(i);
            displayMessage(message);
        }
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }
}
