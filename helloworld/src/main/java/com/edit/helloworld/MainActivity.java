package com.edit.helloworld;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jaredrummler.android.shell.CommandResult;
import com.jaredrummler.android.shell.Shell;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        CommandResult result = Shell.SH.run("ls /data/");
//        Log.i("darren", "onCreate: "+result.isSuccessful()+":"+result.getStderr()+":"+result.toString());
//        Log.i("darren", result.toAllString());
//        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.forceStopPackage("wzl Test");

        Button btn = findViewById(R.id.test_fd);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.test_fd:
                while(true){
                    try{
                        FileInputStream fin = new FileInputStream("/data/local/xxxx");
                        Os.dup(fin.getFD());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (ErrnoException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
