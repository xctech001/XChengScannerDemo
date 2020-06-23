package com.xcheng.scanner.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xcheng.scanner.demo.utils.Log;

public class MainActivity
        extends Activity
        implements AdapterView.OnItemClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        ListView menu = this.findViewById(R.id.menu);
        menu.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Log.info("you tap %d", i);

        // i == 0 is single scan
        if (i == 0)
            this.startActivity(new Intent(this, SingleScannerActivity.class));
        else if (i == 1)
            this.startActivity(new Intent(this, ContinuousScannerActivity.class));
        else if (i == 2)
            this.startActivity(new Intent(this, PreviewScannerActivity.class));
    }
}
