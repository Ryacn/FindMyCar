package com.radiantkey.findmycar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentRecord extends Fragment implements RecordDialog.RecordDialogListener {

    private ListView eventView;
    private CustomAdapter adapter;
    private List<ItemContainer> mList;

    private FloatingActionButton addButton;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Switch aSwitch;

    DatabaseHelper mdb;
    PowerConnectionReceiver receiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        eventView = (ListView) view.findViewById((R.id.TimeRecorder));
        addButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        aSwitch = (Switch) view.findViewById(R.id.switch1);
        if(receiver == null)
            receiver = new PowerConnectionReceiver();
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences settings = getActivity().getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Switch", b);
                editor.commit();
                if(b) {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
                    getActivity().registerReceiver(receiver, intentFilter);
                }else{
                    getActivity().unregisterReceiver(receiver);
                }
            }
        });
        SharedPreferences settings = getActivity().getSharedPreferences("UserInfo", 0);
        aSwitch.setChecked(settings.getBoolean("Switch", false));
        setupRecord(view.getContext());
    }

    private void setupRecord(final Context context){
        mdb = new DatabaseHelper(this.getContext());

        mList = new ArrayList<>();
        //set list int id, String name, String cat, boolean switchState
        loadAll();

        adapter = new CustomAdapter(context, mList);
        eventView.setAdapter(adapter);

        eventView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                long viewId = view.getId();
//                if(viewId == R.id.onSwitch){
//                    Toast.makeText(context, "switch", Toast.LENGTH_SHORT).show();
//                }else {
                    //maybe add alertdialog to change names?
                Toast.makeText(context, "clicked name = " + ((TextView) view.findViewById(R.id.name)).getText(), Toast.LENGTH_SHORT).show();
                ItemContainer tempItem = mList.get(i);
                ItemContainer focusedItem = new ItemContainer(tempItem.getId(),tempItem.getName(),tempItem.getNote(),tempItem.getTime(),tempItem.getLat(),tempItem.getLng(),tempItem.getAlt());
                ((MainActivity) getActivity()).remoteFragmentChange(focusedItem);
//                }
            }
        });
        eventView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //deleting data
                deleteData(mList.get(i).getId(), i);
                return true;
            }
        });
    }

    public void openDialog(){
        RecordDialog dialog = new RecordDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "example");
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void loadAll(){
        Cursor res = mdb.getAllData();
        if(res.getCount() == 0){
            //Toast.makeText(this, "no data", Toast.LENGTH_LONG).show();
            showMessage("Error", "No data found");
            return;
        }else{
//            Record database column: ID, NAME TEXT, NOTE TEXT, TIME INTEGER, LAT DOUBLE, LNG DOUBLE, ALT DOUBLE
//            StringBuffer buffer = new StringBuffer();
            while(res.moveToNext()){
//                buffer.append("Id: " + res.getString(0) + "\nName: " + res.getString(1) + "\nCategory: " + res.getString(2) + "\nTime: " + res.getInt(0) + "\n");
                //Toast.makeText(this, res.toString(), Toast.LENGTH_LONG).show();
//                showMessage("data", buffer.toString());
                mList.add(new ItemContainer(res.getLong(0), res.getString(1), res.getString(2), res.getLong(3), res.getDouble(4), res.getDouble(5), res.getDouble(6)));
            }
        }
    }

    public void addData(String name, String cat){
        long time = System.currentTimeMillis();
        long isSuccessful = -1;
        double longitude = -1, latitude = -1, altitude = -1;
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);

        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            altitude = location.getAltitude();
            isSuccessful = mdb.insertData(name, cat, time, latitude, longitude, altitude);
//            Log.w("long", " " + longitude);
//            Log.w("lant", " " + latitude);
        }
        if(isSuccessful >= 0){
            mList.add(new ItemContainer(isSuccessful, name, cat, time, latitude, longitude, altitude));
            adapter.notifyDataSetChanged();
            Toast.makeText(this.getContext(), "data inserted", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.getContext(), "data not inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteData(long id, int i){
        if(mdb.deleteData(id) > 0) {
            mList.remove(i);
            adapter.notifyDataSetChanged();
            Toast.makeText(this.getContext(), "data deleted", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.getContext(), "data not deleted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void applyTexts(String name, String cat) {
        addData(name, cat);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecord(getContext());
    }

    public static boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
    }
}
