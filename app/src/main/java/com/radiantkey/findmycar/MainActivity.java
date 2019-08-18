package com.radiantkey.findmycar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private Fragment curFragment;

    private Fragment record_fragment, map_fragment;

    private BottomNavigationView navView;

    public ItemContainer getFocusedItem() {
        return focusedItem;
    }

    public void setFocusedItem(ItemContainer focusedItem) {
        this.focusedItem = focusedItem;
    }

    private ItemContainer focusedItem = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
//                    startActivity(new Intent(MainActivity.this, MyLocationDemoActivity.class));
                    fragment = record_fragment;
                    break;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
//                    startActivity(new Intent(MainActivity.this, MarkerCloseInfoWindowOnRetapDemoActivity.class));
                    fragment = map_fragment;
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        record_fragment = new FragmentRecord();
        map_fragment = new MarkerCloseInfoWindowOnRetapDemoActivity();
        loadFragment(record_fragment);
    }

    public Fragment getCurFragment(){
        return curFragment;
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            curFragment = fragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    public void remoteFragmentChange(ItemContainer item){
        navView.setSelectedItemId(R.id.navigation_dashboard);
        focusedItem = item;
        mTextMessage.setText(R.string.title_dashboard);
        loadFragment(map_fragment);
    }
}
