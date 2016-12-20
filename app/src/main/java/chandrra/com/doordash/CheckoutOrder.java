package chandrra.com.doordash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * CheckoutOrder class
 * This will show the items added to cart (Order Details) and Card details can be added
 * and place the order.
 */
public class CheckoutOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Enabling back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Order Details");

        Bundle bundle = getIntent().getExtras();

        String orderItemName = bundle.getString("item_name");
        int no = bundle.getInt("no_of_items");
        int price = bundle.getInt("price");
        //Calculate total price
        int totalOrderPrice = no * price;

        TextView itemName, totalPrice;
        itemName = (TextView) findViewById(R.id.order_item_name);
        totalPrice = (TextView) findViewById(R.id.total_price);
        itemName.setText(orderItemName + "\t \t $ " + price);
        totalPrice.setText("Total Price \t \t : (" + price + "*" + no + ") \t " + "$ " + totalOrderPrice);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        this.finish(); //Finish activity when back button is clicked
        return true;
    }
    public void checkOut(View view) {
    }
}
