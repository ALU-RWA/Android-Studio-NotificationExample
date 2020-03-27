package com.example.notificationexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.os.Bundle;
import android.widget.TextView;

public class RemoteReceiver extends AppCompatActivity {
    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_receiver);
        txtView = findViewById(R.id.textView2);

        Bundle remoteReply = RemoteInput.getResultsFromIntent(getIntent());
        if(remoteReply!=null)
        {
            String message = remoteReply.getCharSequence(MainActivity.txtReply).toString();
            txtView.setText(message);
        }
        NotificationManager NM= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NM.cancel(MainActivity.NOTIFICATION_ID);
    }
}
