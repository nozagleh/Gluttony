package nozagleh.org.gluttony;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.Console;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arnarfreyr on 11.3.15.
 */
public class menu2_Fragment extends Fragment {
    View rootview;

    ListView listFood;

    List<String> foodNames;
    List<Food> listOfFood = new ArrayList<Food>();

    List<String> names;
    List<String> times;
    List<String> comments;
    List<String> types;

    String[] s_names;
    String[] s_times;
    String[] s_comments;
    String[] s_types;

    CustomListAdapter c_adapter;

    Food food;


    DBConnector db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu2_layout,container,false);

        //creates db instance
        db = new DBConnector(getActivity());
        getAllCurrentDateFood();

        c_adapter  = new CustomListAdapter(getActivity(),s_times,s_names,s_comments,s_types,false);

        //sets the list of food
        listFood = (ListView) rootview.findViewById(R.id.listFood);
        listFood.setAdapter(c_adapter);


        listFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),String.valueOf(listOfFood.get(i).get_id()),Toast.LENGTH_SHORT).show();
            }
        });

        //populates the list view
        //getAllFood();


        listFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });

        listFood.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Food food = listOfFood.get(i);

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setIcon(R.drawable.delete);
                alert.setTitle(getString(R.string.alert_change_header));
                alert.setMessage(getString(R.string.alert_change_text));

                //Add Edit button to the dialog
                alert.setPositiveButton(getString(R.string.btn_change), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int f) {
                        //final int id = listOfFood.get(i).get_id();
                        final int id_2 = food.get_id();

                        Boolean edit = true;

                        Intent intent = new Intent(getActivity(), MainActivity.class);

                        intent.putExtra("id",id_2);
                        intent.putExtra("editflag",edit);


                        startActivity(intent);
                    }
                });

                alert.setNegativeButton(getString(R.string.btn_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.deleteFood(food);

                        new CountDownTimer(1000,500){
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            final Dialog dialog = alert.show();
                            @Override
                            public void onTick(long l) {

                                dialog.setContentView(R.layout.insert_message);
                                dialog.setTitle(getString(R.string.txt_success));
                                dialog.setCancelable(false);
                                // there are a lot of settings, for dialog, check them all out!
                                final ImageView imgDone = (ImageView) dialog.findViewById(R.id.imgDone);
                                imgDone.setImageResource(R.drawable.delete_big);


                                // now that the dialog is set up, it's time to show it
                                dialog.show();

                            }

                            @Override
                            public void onFinish() {
                                dialog.dismiss();
                            }
                        }.start();

                        getAllCurrentDateFood();
                        c_adapter = new CustomListAdapter(getActivity(),s_times,s_names,s_comments,s_types,false);
                        listFood.setAdapter(c_adapter);

                 }
                });

                alert.show();

                return true;
            }
        });

        return rootview;
    }

    public void getAllFood(){
        listOfFood = db.getAllfood();
        Food food = new Food();
        //foodNames = new ArrayList<String>();
        List<Map<String, String>> foodNames = new ArrayList<Map<String, String>>();


        for (int i = 0; i < listOfFood.size(); i++){
            Map<String,String> foods = new HashMap<String, String>(2);
            food = listOfFood.get(i);

            foods.put("name",food.get_name());
            foods.put("comment",food.get_comment());


            //foodNames.add(food.get_name());
            //foodTime.add(food.get_time());

            foodNames.add(foods);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), foodNames,
                android.R.layout.simple_list_item_2,
                new String[] {"name", "comment"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});

        listFood.setAdapter(adapter);

    }

    public void getAllCurrentDateFood(){
        DateFormat df = new SimpleDateFormat("yyyy-M-d");
        Date date = new Date();

        listOfFood = db.getTodayfood(df.format(date));
        Food food;
        names = new ArrayList<String>();
        times = new ArrayList<String>();
        comments = new ArrayList<String>();
        types = new ArrayList<String>();

        for (int i = 0; i < listOfFood.size(); i++){
            food = listOfFood.get(i);
            names.add(food.get_name());
            String[] time = food.get_time().split(" ");
            times.add(time[1]);
            comments.add(food.get_comment());

            if (food.get_type() == 0){
                types.add("Food");
            }else{
                types.add("Activity");
            }
        }
        s_names = names.toArray(new String[names.size()]);
        s_times = times.toArray(new String[times.size()]);
        s_comments = comments.toArray(new String[comments.size()]);
        s_types = types.toArray(new String[types.size()]);

    }
}
