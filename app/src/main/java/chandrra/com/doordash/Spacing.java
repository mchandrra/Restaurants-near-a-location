package chandrra.com.doordash;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by smallipeddi on 12/12/16.
 */

public class Spacing extends RecyclerView.ItemDecoration {
    int space;
    public Spacing(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top =space;
//        if (parent.getChildLayoutPosition(view) > 0) {
        outRect.left = space;
        outRect.bottom = space;
        outRect.right = space;
        //} else {
        //outRect.bottom = space;
        //}
    }
}
