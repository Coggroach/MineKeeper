package com.coggroach.minekeeper.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.coggroach.minekeeper.R;

public class MenuActivity extends Activity {
    Intent intent = new Intent(this, GameActivity.class);


    public void onButtonClick(View v){

        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void onButtonClick2(View w){

        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
