package jullendgatc.punyatemenv4;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by jullendgatc on 10/30/17.
 */

public class gridAdapter extends BaseAdapter {

    private Context mContext;

    // Declare image pada array
    public Integer[] deretGambar = {
            R.drawable.bwt_grid, R.drawable.bwt_grid,
            R.drawable.bwt_grid, R.drawable.bwt_grid,
            R.drawable.bwt_grid, R.drawable.bwt_grid,
            R.drawable.bwt_grid, R.drawable.bwt_grid,
            R.drawable.bwt_grid, R.drawable.bwt_grid,
            R.drawable.bwt_grid, R.drawable.bwt_grid,
            R.drawable.bwt_grid, R.drawable.bwt_grid
    };
    // Constructor
    public gridAdapter(Context c){
        mContext = c;
    }
    @Override
    public int getCount() {
        return deretGambar.length;
    }

    @Override
    public Object getItem(int position) {
        return deretGambar[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView bu = new ImageView(mContext);
        bu.setImageResource(deretGambar[position]);
        bu.setScaleType(ImageView.ScaleType.CENTER_CROP);
        bu.setLayoutParams(new GridView.LayoutParams(250, 250));
        return bu;
    }
}
