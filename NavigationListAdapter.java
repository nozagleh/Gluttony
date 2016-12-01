package nozagleh.org.gluttony;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by arnarfreyr on 15.3.15.
 */
public class NavigationListAdapter extends ArrayAdapter<String> {

    String[] pics;
    String[] names;

    Context context;

    LayoutInflater inflater;

    public NavigationListAdapter(Activity context,String[] names, String[] pics) {
        super(context, R.layout.list,names);
        this.context = context;
        this.pics = pics;
        this.names = names;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View single_row = inflater.inflate(R.layout.navigation_list,null,true);


        TextView navText = (TextView) single_row.findViewById(R.id.nav_item_text_1);
        ImageView navImg = (ImageView) single_row.findViewById(R.id.nav_item_img_1);
        //ImageView mainImg = (ImageView) single_row.findViewById(R.id.nav_img);
        //mainImg.setClickable(false);
        //mainImg.setImageResource(R.drawable.logo);

        String fontpath = "fonts/Lora-Regular.ttf";
        Typeface font = Typeface.createFromAsset(context.getAssets(),fontpath);
        navText.setTypeface(font);

        navText.setText(names[position]);

        /*
        if (position == 0){
            mainImg.setVisibility(View.VISIBLE);
        }else{
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainImg.getLayoutParams();
            params.height = 0;
            mainImg.setLayoutParams(params);
            mainImg.setVisibility(View.INVISIBLE);
            mainImg.invalidate();
        }
        */

        if (pics[position].equals("add")){
            navImg.setImageResource(R.drawable.add);
        }else if(pics[position].equals("today")){
            navImg.setImageResource(R.drawable.today);
        }else if(pics[position].equals("all")){
            navImg.setImageResource(R.drawable.calendar);
        }else if(pics[position].equals("settings")){
            navImg.setImageResource(R.drawable.settings);
        }


        return single_row;
    }

    @Override
    public int getCount() {

        return names.length;
    }
}
