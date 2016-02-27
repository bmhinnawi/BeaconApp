package com.example.bmhinnawi.beaconapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.altbeacon.beacon.*;

import java.util.Collection;
import java.util.List;

// SET QuickBeacon to
// Power = -69

public class BeaconProximityActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    static TextView t;//=new TextView(this);

    static double theDist=0;
    static String theBeaconID="";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_proximity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //t=new TextView(this);
        TextView t=(TextView)findViewById(R.id.message);


        //The current device memID - for demo only
        final String[] theMemId = {""};


        final EditText enterMemId=(EditText)findViewById(R.id.memIdeT);
        final TextView currentMemId=(TextView)findViewById(R.id.regMemIdtV);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "ID: "+ theBeaconID +" = " + String.valueOf(theDist) + " meters", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //t.setText("ID: "+ theBeaconID +" = " + String.valueOf(theDist) + " meters");
                //INSERT MEM ID TO GLOBAL VARIABLE
                theMemId[0] = String.valueOf(enterMemId.getText());

                //ENTER THE MEMID FROM THE FIELD
                currentMemId.setText(theMemId[0]);
                //CLEAR FIELD
                enterMemId.setText("");

            }
        });

        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
           beaconManager.getBeaconParsers().add(new BeaconParser().
                   //           setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
                           setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24")); //iBeacon Detector   m:2-3=0215

        beaconManager.bind(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

 /*   void setTextbyBeaconAction()
    {
        Beacon
        List<BeaconParser> myBeacons=beaconManager.getBeaconParsers();
        myBeacons.get(10).fromScanData()
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beacon_proximity, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
 //       t=new TextView(this);
 //       t=(TextView)findViewById(R.id.message);

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.i(TAG, "The first beacon: "+ beacons.iterator().next().getId1().toString() +" I see is about " + beacons.iterator().next().getDistance() + " meters away.");
                    theBeaconID=beacons.iterator().next().getId1().toString();
                    theDist=beacons.iterator().next().getDistance();
  //                  t.setText("The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
                }
                else {
                    Log.i(TAG, "NO BEACON IN SIGHT.");
                    theBeaconID="NO BEACON";
                    theDist=-1;
  //                  t.append("No Beacon in sight");
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BeaconProximity Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.bmhinnawi.beaconapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BeaconProximity Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.bmhinnawi.beaconapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


