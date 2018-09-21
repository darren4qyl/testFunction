package b.com.myapplication.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import b.com.myapplication.R;

public class DeviceDetailControlActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_detail_control_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ddcl_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public String getToolbarName() {
        return getResources().getStringArray(R.array.view_tile_name)[0];
    }
}
