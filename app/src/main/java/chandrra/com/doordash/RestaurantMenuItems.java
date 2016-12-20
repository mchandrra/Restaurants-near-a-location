package chandrra.com.doordash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class RestaurantMenuItems extends AppCompatActivity {
    SeekBar seekBar;
    TextView quantity, itemName, price;
    String item_name = "Any Dish";
    int price_per_item = 12, no_of_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Enabling back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle bundle = getIntent().getExtras();
        toolbar.setTitle(bundle.getString("restaurant name"));

        itemName = (TextView) findViewById(R.id.item_name);
        price = (TextView) findViewById(R.id.price);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        quantity = (TextView) findViewById(R.id.no_of_items);

        itemName.setText(item_name);
        price.setText("$ " + price_per_item);

        //seekbar for selecting quantity
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                quantity.setText("" + i);
                no_of_items = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        this.finish(); //Finish activity when back button is clicked
        return true;
    }

    //Implement onClick checkOut method
    public void checkOut(View view) {
        Intent intent = new Intent(this, CheckoutOrder.class);
        intent.putExtra("item_name", item_name);
        intent.putExtra("no_of_items", no_of_items);
        intent.putExtra("price", price_per_item);
        this.startActivity(intent);
    }


}

