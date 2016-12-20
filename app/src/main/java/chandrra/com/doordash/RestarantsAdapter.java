package chandrra.com.doordash;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import chandrra.com.doordash.DataModel.Restaurants;

/**
 * Created by smallipeddi on 12/12/16.
 */

public class RestarantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Restaurants> restaurantsArrayList = null;
    Context context;
    static ArrayList<Restaurants> favouriteRestaurants = new ArrayList<>();
    //Constructor for RestarantsAdapter.
    //getting list of restaurants from MainActivity
    public RestarantsAdapter(Context context, ArrayList<Restaurants> restaurantsArrayList) {
        this.restaurantsArrayList = restaurantsArrayList;
        this.context = context;
    }

    /**
     * Holder for recyclerview
     */
    public class Holder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView image;
        ImageView heartImage;
        CardView cardView;
        public Holder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.restaurant_image);
            title = (TextView) itemView.findViewById(R.id.restaurant_name);
            heartImage = (ImageView) itemView.findViewById(R.id.imgbtn_favorite);
            cardView = (CardView) itemView.findViewById(R.id.cardview_restaurants);
        }
    }

    /**
     * Creating view for the recyclerview item
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_restaurants, parent, false);
        return new Holder(view);
    }

    /**
     * Binding data to the recyclerview item
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemCount() > 0) {
            final Restaurants current = restaurantsArrayList.get(position);
            if (restaurantsArrayList != null) {
                //((Holder) holder).image.setImageBitmap(null);
                ((Holder) holder).title.setText(current.getName());
                //Using Glide to display cover images of restaurants
                Glide.with(context).load(current.getCover_img_url()).into(((Holder) holder).image);
                //Checking if the current restaurant is added to favourite
                if (current.isFav()) {
                    ((Holder) holder).heartImage.setImageResource(R.drawable.mark_favourite_heart_red);
                } else {
                    ((Holder) holder).heartImage.setImageResource(R.drawable.remove_favourite_heart);
                }

                //Listener for adding and removing favourite items
                ((Holder) holder).heartImage.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (!current.isFav()) {
                            current.setFav(true);
                            favouriteRestaurants.add(current);
                            ((Holder) holder).heartImage.setImageResource(R.drawable.mark_favourite_heart_red);
                        } else {
                            ((Holder) holder).heartImage.setImageResource(R.drawable.remove_favourite_heart);
                            current.setFav(false);
                            favouriteRestaurants.remove(current);

                        }
                    }
                });
                //Listener for recyclerview item card clicks
                //Specific menu items and categories can be added when clicked on a specific card
                ((Holder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RestaurantMenuItems.class);
                        intent.putExtra("restaurant name", current.getName());
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (restaurantsArrayList != null) {
            //return 5;
            return restaurantsArrayList.size();
        }
        return 0;
    }

    public static ArrayList getFavourites() {

        return favouriteRestaurants;
    }
}
