package sundeep.yahoo.com.gridsearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import sundeep.yahoo.com.gridsearch.R;
import sundeep.yahoo.com.gridsearch.models.Settings;


public class SettingsActivity extends Activity {
    private Settings settings;
    private Spinner imageType, colorFilter, imageSize, siteFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.colorFilter = (Spinner) findViewById(R.id.sp_colorFilter);
        this.imageType = (Spinner) findViewById(R.id.sp_imageType);
        this.imageSize = (Spinner) findViewById(R.id.sp_imageSize);
        this.siteFilter = (Spinner) findViewById(R.id.sp_siteFilter);

        settings = (Settings) getIntent().getExtras().getSerializable("settings");
        if(settings.colorFilter != null) {
            this.setSpinnerToValue(this.colorFilter, settings.colorFilter);
        }

        if(settings.imageType != null) {
            this.setSpinnerToValue(this.imageType, settings.imageType);
        }

        if(settings.imageSize != null) {
            this.setSpinnerToValue(this.imageSize, settings.imageSize);
        }

        if(settings.siteFilter != null) {
            this.setSpinnerToValue(this.siteFilter, settings.siteFilter);
        }
        
    }

    public void onSubmit(View view){
        //probably can loop this

        settings.setValues(this.imageType.getSelectedItem().toString(),
                this.colorFilter.getSelectedItem().toString(),
                this.imageSize.getSelectedItem().toString(),
                this.siteFilter.getSelectedItem().toString());

        Intent data = new Intent();
        data.putExtra("settings", settings);
        setResult(RESULT_OK, data);
        finish();


    }

    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
