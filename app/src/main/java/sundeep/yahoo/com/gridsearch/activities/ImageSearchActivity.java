package sundeep.yahoo.com.gridsearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import sundeep.yahoo.com.gridsearch.R;
import sundeep.yahoo.com.gridsearch.adapters.ImageResultsAdapter;
import sundeep.yahoo.com.gridsearch.listeners.EndlessScrollListener;
import sundeep.yahoo.com.gridsearch.models.ImageResult;
import sundeep.yahoo.com.gridsearch.models.Settings;


public class ImageSearchActivity extends Activity {
    private static final int SETTINGS_TAP_RETURN = 1;
    private EditText searchTerms;
    private Settings settings;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter imageResultsAdapter;
    private GridView gvResults;
    private String query;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setupViews();
        this.settings = new Settings();
        imageResults = new ArrayList<ImageResult>();
        page = 0;
        imageResultsAdapter = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(imageResultsAdapter);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                page = p;
                doAsync(page);
            }
        });

    }

    private void setupViews() {
        searchTerms = (EditText) findViewById(R.id.et_query);
        gvResults = (GridView) findViewById(R.id.gv_results);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(ImageSearchActivity.this, FullScreenImageActivity.class);
                ImageResult result = imageResults.get(position);
                i.putExtra("url", result.fullUrl);
                startActivity(i);
            }
        });
    }

    //go to the search screen
    public void onSettingsTap(MenuItem menu){

        Intent i = new Intent(this, SettingsActivity.class);
        i.putExtra("settings", this.settings);
        startActivityForResult(i, SETTINGS_TAP_RETURN);

    }

    //go to the search screen
    public void onSearchTap(View v) {
        if (query != searchTerms.getText().toString()) {
            //setting page to 0 will clear things as well
            page = 0;
            query = searchTerms.getText().toString();
        }

       doAsync(page);
    }

    public void doAsync(Integer p) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = buildUrlString(p);
        RequestHandle requestHandle = client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    //only clear on new query
                    if (page == 0) {
                        imageResults.clear();
                    }
                    imageResultsAdapter.addAll(ImageResult.fromJSONArray(imageResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private String buildUrlString(Integer start) {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
        url += "&q=" + searchTerms.getText().toString();
        url += "&rsz=8";
        if (start != null) {
            url += "&start="+start;
        }
        if(this.settings != null){
            if (this.settings.siteFilter != "") {
                try {
                    url += "&as_sitesearch=" + URLEncoder.encode(this.settings.siteFilter, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.settings.colorFilter != "") {
                url += "&imgcolor=" + this.settings.colorFilter;
            }
            if (this.settings.imageSize != "") {
                url += "&imgsz=" + this.settings.imageSize;
            }
            if (this.settings.imageType != "") {
                url += "&imgtype=" + this.settings.imageType;
            }

        }

        return url;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == SETTINGS_TAP_RETURN) {

            if (resultCode == RESULT_OK) {
                //results go here

                this.settings = (Settings) data.getExtras().getSerializable("settings");
            }

        }

    }
    



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_search, menu);
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
