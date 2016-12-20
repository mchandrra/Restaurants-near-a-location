package chandrra.com.doordash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import chandrra.com.doordash.DataModel.Restaurants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Restaurants> restaurants;
    ArrayList<Restaurants> favouriteRestaurants;
    private RecyclerView mRecyclerView;
    RestarantsAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;
    Context context = this;
    EditText search;
    //Root URl
    final static String rootURL = "https://api.doordash.com";
    //URL to get restaurant objects
    String restaurantsListURL = "/v2/restaurant/?lat=37.3622496&lng=-122.0212335";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        search = (EditText) findViewById( R.id.search);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);

        //Initiating async task to get json and parse json
        JSONAsyncTask jsonAsyncTask = new JSONAsyncTask(MainActivity.this, rootURL + restaurantsListURL, mRecyclerView);
        jsonAsyncTask.execute();

        try {
            restaurants = jsonAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Set recyclerview adapter
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new Spacing(20));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RestarantsAdapter(context, restaurants);
        mRecyclerView.setAdapter(adapter);

        addTextListener();
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * Implement actions for navigation drawer items selected
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Home
        if (id == R.id.nav_home) {
            // Handle the home action
            updateRecyclerView(restaurants);
            search.setText("");
            search.clearFocus();
            search.clearComposingText();
        } else if (id == R.id.nav_favourites) {
            //Handle Favourites item
            search.setText("");
            search.clearFocus();
            search.clearComposingText();
            ArrayList<Restaurants> favouriteRestaurants = RestarantsAdapter.favouriteRestaurants;
            updateRecyclerView(favouriteRestaurants);

        }

//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Method for just testing out things
     */
    public void test() {
        String restaurantDetailView = rootURL + "/v2/restaurant/42032";
        System.out.println("=======================================");
        System.out.println("restaurantDetailView" + getJSON(restaurantDetailView));
        System.out.println("=======================================");
    }

    /**
     * Async task for getting and parsing JSON
     */

    public class JSONAsyncTask extends AsyncTask<Void, Void, ArrayList> {
        private Context mContext;
        private String mUrl;
        RecyclerView recyclerView;
        ProgressDialog dialog;

        public JSONAsyncTask(Context context, String url, RecyclerView recyclerView) {
            mContext = context;
            mUrl = url;
            this.recyclerView = recyclerView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.show();
            dialog.setMessage("Loading ...");
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            String resultString = null;
            resultString = getJSON(mUrl);
            restaurants = parseRestaurantJSON(resultString);
            return restaurants;
        }

        @Override
        protected void onPostExecute(ArrayList aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }

    /**
     * JSON Methods
     */
    //Method to parse JSON data
    public ArrayList parseRestaurantJSON (String jsonString) {
        try {
            JSONArray json = new JSONArray(jsonString);
            restaurants = new ArrayList<>();

            for (int i = 0; i < json.length(); i++) {
                Restaurants items = new Restaurants();
                items.setId(json.getJSONObject(i).getInt("id"));
                items.setName(json.getJSONObject(i).getString("name"));
                items.setDescription(json.getJSONObject(i).getString("description"));
                items.setCover_img_url(json.getJSONObject(i).getString("cover_img_url"));
                items.setAddress(json.getJSONObject(i).getString("address"));

                JSONArray menusArray = json.getJSONObject(i).getJSONArray("menus");
                items.setMenuID(menusArray.getJSONObject(0).getInt("id"));
                items.setMenuName(menusArray.getJSONObject(0).getString("name"));
                restaurants.add(items);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    //Method to get JSON data
    public String getJSON(String url) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.connect();

            int status = c.getResponseCode();
            Log.d("status connection", String.valueOf(status));
//                switch (status) {
//                    case 200:
//                    case 201:

            BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            return sb.toString();

        } catch (Exception ex) {
            return ex.toString();
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    //disconnect error
                }
            }
        }
    }


    /**
     * Other Methods
     */
    //Method to hide keyboard
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //Update recyclerview data
    public void updateRecyclerView(ArrayList<Restaurants> restaurants) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new RestarantsAdapter(context, restaurants);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Method to filter restaurants and update recyclerview data
    public void addTextListener(){

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence searchQuery, int start, int before, int count) {

                searchQuery = searchQuery.toString().toLowerCase();

                ArrayList<Restaurants> filterRestaurants = new ArrayList<Restaurants>();
                if (restaurants != null) {
                    for (int i = 0; i < restaurants.size(); i++) {
                        final String restaurantName = restaurants.get(i).getName().toLowerCase();
                        if (restaurantName.contains(searchQuery)) {
                            filterRestaurants.add(restaurants.get(i));
                        }
                    }
                }
                updateRecyclerView(filterRestaurants);
            }
        });
    }
}
