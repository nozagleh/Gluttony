package nozagleh.org.gluttony;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arnarfreyr on 11.3.15.
 */
public class menu0_Fragment extends Fragment {
    //Initialize View
    View rootview;

    TextView txtTime;
    TextView txtName;
    TextView txtDesc;
    ImageView imgMain;

    DBConnector db;

    Food food;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the root view
        rootview = inflater.inflate(R.layout.menu0_layout,container,false);

        //Initialize DB connection
        db = new DBConnector(getActivity());
        txtTime = (TextView) rootview.findViewById(R.id.txtLTime);
        txtName = (TextView) rootview.findViewById(R.id.txtLName);
        txtDesc = (TextView) rootview.findViewById(R.id.txtLDesc);
        imgMain = (ImageView) rootview.findViewById(R.id.imgLMain);

        //Set the font for the elements
        String fontpath = "fonts/PoiretOne-Regular.ttf";
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),fontpath);
        txtName.setTypeface(font);
        txtDesc.setTypeface(font);
        txtTime.setTypeface(font);

        getAllFood();

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllFood();
    }

    public void getAllFood(){
        List<Food> listOfFood = db.getAllfood();
        Food food;

        int s = listOfFood.size();

        if (s > 0){
            food = listOfFood.get(0);
            txtName.setText(food.get_name());

            String[] m_t = food.get_time().split(" ");

            String[] m = m_t[0].split("-");

            String day = m[2];
            String month = m[1];
            String year = m[0];

            String[] t = m_t[1].split(":");
            String h = t[0];
            String min = t[1];

            txtTime.setText(h + ":" + min + "\n" +day + "." + month + "." + year);
            txtDesc.setText(food.get_comment());

            if(food.get_type() == 0){
                imgMain.setImageResource(R.drawable.food);
            }else{
                imgMain.setImageResource(R.drawable.activity);
            }
        }else{
            txtTime.setVisibility(View.INVISIBLE);
            txtDesc.setVisibility(View.INVISIBLE);
            txtName.setVisibility(View.INVISIBLE);
        }


    }

}
