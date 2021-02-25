package com.google.android.gms.location.sample.geofencing.utils;

import android.content.Context;
import android.media.AudioManager;
import android.widget.Toast;

/**
 * Created by venkat on 10/17/2016.
 */

public class SoundProfile {
    private AudioManager audioManager;
    private Context context;
    public SoundProfile(Context context){
        this.context=context;
        audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }


    public void setRinger() {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Toast.makeText(context,"Ringer Mode",Toast.LENGTH_LONG).show();
    }

    public void setSilent() {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        Toast.makeText(context,"Silent Mode",Toast.LENGTH_LONG).show();
    }

    public void setVibrate() {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        Toast.makeText(context,"Vibration Mode",Toast.LENGTH_LONG).show();
    }

}
