package org.fsftn.fsconf15;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;


public class NotificationsActivity extends ActionBarActivity {

    ListView notificationsView;
    String TAG = "fsconf_notif";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        notificationsView = (ListView) findViewById(R.id.notificationsListView);
        try {
            SharedPreferences sp = this.getSharedPreferences("org.fsftn.sc15", Context.MODE_PRIVATE);
            String notifications = sp.getString("notifications", "");
            JSONArray notificationsArray = new JSONArray(notifications);
            JSONObject[] nArray = new JSONObject[notificationsArray.length()];
            //for(int i = notificationsArray.length() - 1; i >= 0; i--) {
            for(int i = 0; i < notificationsArray.length(); i++) {
                nArray[i] = new JSONObject(notificationsArray.getString(notificationsArray.length()-i-1));
                //Log.i(TAG, notificationsArray.getString(i));
            }
            ListAdapter notificationAdapter = new NotificationsAdapter(this, nArray);
            notificationsView.setAdapter(notificationAdapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class NotificationsAdapter extends ArrayAdapter<JSONObject> {
        public NotificationsAdapter(Context context, JSONObject[] notifications) {
            super(context, R.layout.notifications_rowlayout,notifications);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater li = LayoutInflater.from(getContext());

            View nView = li.inflate(R.layout.notifications_rowlayout, parent, false);

            String nTitle=null,nContent=null,nTimestamp="";
            int nType = -1;
            try {
                nTitle = getItem(position).getString("title");
                nContent = getItem(position).getString("content");
                nTimestamp = getItem(position).getString("timestamp");
                nType = Integer.parseInt(getItem(position).getString("type"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            TextView nTitleView = (TextView) nView.findViewById(R.id.title);
            TextView nContentView = (TextView) nView.findViewById(R.id.content);
            TextView nTimestampView = (TextView) nView.findViewById(R.id.timestamp);

            nTitleView.setText(nTitle);


            if(nTimestamp.length() > 20)
                nTimestampView.setText("  " + nTimestamp.substring(0,nTimestamp.length() - 5));

            nContentView.setText(nContent);

            ImageView thumbnailView = (ImageView) nView.findViewById(R.id.thumbnail);
            switch(nType){

                case 0:
                    thumbnailView.setImageResource(R.drawable.notif);
                    break;

                case 1:
                    thumbnailView.setImageResource(R.drawable.event);
                    break;

                default:
                    thumbnailView.setImageResource(R.drawable.ques);


            }

            return nView;
        }
    }

}
