package nozagleh.org.gluttony;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by arnarfreyr on 11.3.15.
 */
public class menu4_Fragment extends Fragment {
    View rootview;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Configuration configuration;

    final static String LA_DEFAULT = "en";

    TextView txtCounter;

    DBConnector db;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu4_layout,container,false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();

        db = new DBConnector(getActivity());

        final Button btnClear = (Button) rootview.findViewById(R.id.btnClear);
        Button btnLa = (Button) rootview.findViewById(R.id.btnLa);
        final Button btnBackup = (Button) rootview.findViewById(R.id.btnBackup);
        final Button btnImport = (Button) rootview.findViewById(R.id.btnImport);

        txtCounter = (TextView) rootview.findViewById(R.id.txtCounter);
        txtCounter.setText(getString(R.string.nr_of_items) + " " + db.getCount());

        //Set the font for the elements
        String fontpath = "fonts/Roboto/Roboto-Regular.ttf";
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),fontpath);
        btnClear.setTypeface(font);
        btnLa.setTypeface(font);
        btnBackup.setTypeface(font);
        btnImport.setTypeface(font);
        txtCounter.setTypeface(font);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                try {

                    alert.setIcon(R.drawable.delete);
                    alert.setTitle(getString(R.string.alert_delete_all_header));
                    alert.setMessage(getString(R.string.alert_delete_all_text));
                    alert.setNegativeButton(getString(R.string.btn_delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db = new DBConnector(getActivity());
                            db.deleteAll();

                            try {
                                //db.importDatabase();
                                new CountDownTimer(1000, 500) {
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

                            } catch (Exception e) {

                            }
                            txtCounter.setText(getString(R.string.nr_of_items) + " " + db.getCount());
                        }
                    });
                    alert.setNeutralButton(R.string.btn_quit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alert.show();
                }catch (Exception e){
                    alert.setIcon(R.drawable.error);
                    alert.setTitle(getString(R.string.alert_error_delete));
                    alert.setMessage(getString(R.string.alert_error_text));
                    alert.setNegativeButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.show();
                }
            }

        });



        btnLa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.language_message);
                dialog.setTitle(getString(R.string.la_settings_btn));
                dialog.setCancelable(true);
                // there are a lot of settings, for dialog, check them all out!
                // set up radiobutton
                final RadioButton rd_en = (RadioButton) dialog.findViewById(R.id.la_en);
                final RadioButton rd_is = (RadioButton) dialog.findViewById(R.id.la_is);
                final RadioButton rd_es = (RadioButton) dialog.findViewById(R.id.la_es);
                final RadioButton rd_fi = (RadioButton) dialog.findViewById(R.id.la_fi);

                String la_set = sharedPreferences.getString("la","en");
                if (la_set.equals("en")){
                    rd_en.setChecked(true);
                }else if(la_set.equals("is")){
                    rd_is.setChecked(true);
                }else if(la_set.equals("es")){
                    rd_es.setChecked(true);
                }else if(la_set.equals("fi")){
                    rd_fi.setChecked(true);
                }


                // now that the dialog is set up, it's time to show it
                dialog.show();

                rd_en.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rd_en.setChecked(true);
                        editor.putString("la","en");
                        configuration = getResources().getConfiguration();
                        configuration.locale = new Locale("en");
                    }
                });

                rd_is.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rd_is.setChecked(true);
                        editor.putString("la", "is");
                        configuration = getResources().getConfiguration();
                        configuration.locale = new Locale("is");
                    }
                });

                rd_es.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rd_es.setChecked(true);
                        editor.putString("la", "es");
                        configuration = getResources().getConfiguration();
                        configuration.locale = new Locale("es");
                    }
                });

                rd_fi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rd_fi.setChecked(true);
                        editor.putString("la", "fi");
                        configuration = getResources().getConfiguration();
                        configuration.locale = new Locale("fi");
                    }
                });

                Button btnLand = (Button) dialog.findViewById(R.id.btnLang);

                btnLand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.commit();
                        Log.d("Lang: ", sharedPreferences.getString("la","en"));
                        getActivity().getBaseContext().getResources().updateConfiguration(configuration,
                                getActivity().getBaseContext().getResources().getDisplayMetrics());
                        dialog.dismiss();

                        Intent mStartActivity = new Intent(getActivity(), MainActivity.class);
                        int mPendingIntentId = 123456;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    }
                });

            }
        });

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    db.dumpDatabase();
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
                            imgDone.setImageResource(R.drawable.exp);


                            // now that the dialog is set up, it's time to show it
                            dialog.show();

                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                        }
                    }.start();
                }catch (Exception e){
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
                            imgDone.setImageResource(R.drawable.excl);


                            // now that the dialog is set up, it's time to show it
                            dialog.show();

                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                        }
                    }.start();
                }

            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    db.importDatabase();
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
                            imgDone.setImageResource(R.drawable.imp);


                            // now that the dialog is set up, it's time to show it
                            dialog.show();

                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                        }
                    }.start();
                }catch (Exception e){
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
                            imgDone.setImageResource(R.drawable.excl);


                            // now that the dialog is set up, it's time to show it
                            dialog.show();

                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                        }
                    }.start();
                }
                txtCounter.setText(getString(R.string.nr_of_items) + " " + db.getCount());
            }
        });

        return rootview;
    }
}
