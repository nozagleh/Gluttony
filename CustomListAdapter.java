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
 * Created by arnarfreyr on 11.3.15.
 */
public class CustomListAdapter extends ArrayAdapter<String> {
    String[] times;
    String[] names;
    String[] comments;
    String[] types;

    Boolean all;

    Context context;

    LayoutInflater inflater;

    public CustomListAdapter(Activity context, String[] times, String[] names, String[] comments,String[] types,Boolean all) {
        super(context, R.layout.list,names);
        this.context = context;
        this.times = times;
        this.names = names;
        this.comments = comments;
        this.types = types;
        this.all = all;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View single_row = inflater.inflate(R.layout.list,null,true);
        //single_row.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));

        TextView time = (TextView) single_row.findViewById(R.id.txtLTime);
        TextView name = (TextView) single_row.findViewById(R.id.txtLName);
        TextView comment = (TextView) single_row.findViewById(R.id.txtLDesc);
        ImageView type = (ImageView) single_row.findViewById(R.id.nav_img);
        TextView newDate = (TextView) single_row.findViewById(R.id.textView4);
        newDate.setVisibility(View.GONE);

        String fontpath = "fonts/Roboto/Roboto-Italic.ttf";
        Typeface font = Typeface.createFromAsset(context.getAssets(),fontpath);
        time.setTypeface(font);
        name.setTypeface(font);
        comment.setTypeface(font);
        newDate.setTypeface(font);

        int location = Arrays.asList(times).indexOf(position);

        if(names.length > 0){
            if(all){
                String[] currtime = currtime = names[position].split(" - ");
                name.setText(currtime[1]);
                time.setText(currtime[0]);
            }else{
                name.setText(names[position]);
                time.setText(times[position]);
            }

            if(position > 0 && all){

                if(!times[position].equals(times[position - 1])){
                    newDate.setVisibility(View.VISIBLE);
                    newDate.setText(times[position]);
                }else{
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newDate.getLayoutParams();
                    params.height = 0;
                    newDate.setLayoutParams(params);
                    newDate.setVisibility(View.GONE);
                    newDate.invalidate();
                }
            }else if(position == 0 && all){
                newDate.setVisibility(View.VISIBLE);
                newDate.setText(times[position]);
            }else if(position == 0 && !all){
                newDate.setVisibility(View.VISIBLE);
                newDate.setText("Today");
            }

            if (!comments[position].equals("No description")){
                comment.setText(comments[position]);
            }





            if (types[position].equals("Food")){
                type.setImageResource(R.drawable.food);
            }else if(types[position].equals("Activity")){
                type.setImageResource(R.drawable.activity);
            }
        }

        return single_row;
    }

    @Override
    public int getCount() {

        return names.length;
    }
}
