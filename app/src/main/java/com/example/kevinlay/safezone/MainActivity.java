package com.example.kevinlay.safezone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Button report;
    Button call;
    Button find;

    Button map;
    TextView t1;

    boolean imp = false;

    boolean imp2 = false;
    TextView t12;
    ArrayList<SafeZoneInfo> s2Info = new ArrayList<SafeZoneInfo>();
    SafeZoneInfo fis;
    Button go;
    int it = 0;
    


    LocationManager locationManager;
    double longitudeNetwork, latitudeNetwork;
    TextView longitudeValueNetwork, latitudeValueNetwork;

    public static Context myAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAppContext = getApplicationContext();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setContentView(R.layout.activity_main);
        toggleNetworkUpdates();
        toggleNetworkUpdates();
        toggleNetworkUpdates();
        //toggleNetworkUpdates();

        t12 = (TextView) findViewById(R.id.latitudeValueNetwork);

        call =(Button) findViewById(R.id.button2);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:8583817760"));
                    startActivity(intent);


            }
        });

        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        report = (Button) findViewById(R.id.button1);
        //t1 = (TextView) findViewById(R.id.textView);
        //t12 = (TextView) findViewById(R.id.textView4);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggleNetworkUpdates();

                //startReport();
                if(imp){
                    t12.setTextColor(Color.RED);
                    t12.setText("READY");
                    startReport();
                    report.setText("Turn safety On");
                    imp = false;
                    imp2 = true;
                    Log.d("Test", "onClick: "+ s2Info);

                }else{
                    toggleNetworkUpdates();
                    t12.setTextColor(Color.BLACK);
                    t12.setText("Safety On");
                    report.setText("Turn safety Off");
                    imp = true;
                    imp2 = false;
                    Log.d("Test", "onClick: "+ s2Info);

                }


            }
        });

        map = (Button) findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.sfsu.edu/news/2010/spring/images/30b.jpg";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        find = (Button) findViewById(R.id.loc);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imp2) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" + latitudeNetwork + "," +
                                    longitudeNetwork + "&daddr=" +
                                    s2Info.get(it).lat + "," +
                                    s2Info.get(it).lng));
                    startActivity(intent);
                    //timp2 = true;
                    //imp2 = false;

                }else{
                    showe();

                    //imp2 = true;
                }
                //startFind();
                //fis = getClosest(s2Info);
                //t12.setText(fis.name);
            }
        });

        //longitudeValueNetwork = (TextView) findViewById(R.id.longitudeValueNetwork);
        //latitudeValueNetwork = (TextView) findViewById(R.id.latitudeValueNetwork);
        /*
        go = (Button) findViewById(R.id.button2);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleNetworkUpdates();
                //toggleNetworkUpdates(go);

                //startReport();
                //startFind();
                //fis = getClosest(s2Info);
                //t12.setText(fis.name);



            }
        });
        */
    }

    public void showe(){
        Toast.makeText(this,"Turn off Safety",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        toggleNetworkUpdates();
        toggleNetworkUpdates();
        toggleNetworkUpdates();
    }

    @Override
    protected void onStart() {
        super.onStart();
        toggleNetworkUpdates();
        toggleNetworkUpdates();
        toggleNetworkUpdates();
    }

    public SafeZoneInfo getClosest (ArrayList<SafeZoneInfo> info){
        double sum = 0;

        int indexToSave = 0;

        double lowestValue = 200;

        double tLat = latitudeNetwork;
        double tLng = longitudeNetwork;


        if(tLat < 1){
            tLat = tLat * -1;
        }

        if(tLng < 1){
            tLng = tLng * -1;
        }

        for(int i =0; i < info.size();i++){
            double fLat = Double.parseDouble(info.get(i).lat);
            if(fLat < 1){
                fLat = fLat * -1;
            }
            double fLng = Double.parseDouble(info.get(i).lng);
            if(fLng < 1){
                fLng = fLng * -1;
            }
            double sLat = tLat - fLat;
            double sLng = tLng -  fLng;

            sum = sLat + sLng;

            if(sum > lowestValue ){
                sum = lowestValue;
                indexToSave = i;
            }
        }
        it = indexToSave;
        return info.get(indexToSave);
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void startReport() {
        //Intent intent = new Intent(this,Report.class);
        //startActivity(intent);
        GetCord g1 = new GetCord(this);

        g1.execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                latitudeNetwork+","+
                longitudeNetwork+
                "&radius=2000&type=hospital&key=AIzaSyC5WjIsxwWhJr7DV8Cyyc-3wPIp7dxwLqk");

    }

    public void startFind() {
        Intent intent = new Intent(this, FindSafeZone.class);
        startActivity(intent);
    }

    public void toggleNetworkUpdates() {
        //if (!checkLocation())
        //    return;
        //Button button = (Button) view;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            Toast.makeText(this,"Did not work",Toast.LENGTH_LONG).show();
        }
        //locationManager.removeUpdates(locationListenerNetwork);
        //button.setText("Nope");


        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 60 * 10, 10, locationListenerNetwork);
        Log.d("Test", "onClic212k: "+ "still empty ");
        //Toast.makeText(this, "Network provider started running", Toast.LENGTH_LONG).show();
        //button.setText("Yes");

    }
    public final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();
            // Send the Location here
            // Website is down so comment out until website is back up
            // www.kevinlay.000webhostapp.com/phpPostScript
            /*
            try {
                    ArrayList<String> postParameters = new ArrayList<>;
                    postParameters.add(longitudeNetwork);
                    postParameters.add(latitudeNetwork);
                    CustomHttpClient.executeHttpPost("http://kevinlay.000webhostapp.com/phpPostScript", postParameters);
                } catch (Exception e) {
                }
             */
            Log.d("Test", "onClic212k: "+ longitudeNetwork);
            imp = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //longitudeValueNetwork.setText(longitudeNetwork + "");
                    //latitudeValueNetwork.setText(latitudeNetwork + "");
                    //Toast.makeText(MainActivity.this, "Network Provider update", Toast.LENGTH_SHORT).show();
                    //imp = true;
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}
