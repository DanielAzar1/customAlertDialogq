package com.example.customalertdialogq;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.lang.Math.pow;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

/**
 * @author Daniel Azar
 * @version 1.0
 * @since 2025-02-12
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    AlertDialog.Builder adb;
    LinearLayout mydialog;
    EditText eTx1, eTq;
    Switch sw1;
    TextView indexTV, sumTV, numTV, multTV, tv1, tv2, tv3, tv4;
    ListView lv1;
    Double firstNum, diff;
    boolean seriesChoice;
    ArrayList<Double> series;

    /**
     * @param savedInstanceState The saved instance state of the activity.
     * The function builds the main activity.
     *
     * @return void
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        lv1 = findViewById(R.id.lv);
        indexTV = findViewById(R.id.indexTV);
        sumTV = findViewById(R.id.sumTV);
        numTV = findViewById(R.id.numTV);
        multTV = findViewById(R.id.multTV);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        lv1.setOnItemClickListener(this);
        lv1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        tv1.setVisibility(INVISIBLE);
        tv2.setVisibility(INVISIBLE);
        tv3.setVisibility(INVISIBLE);
        tv4.setVisibility(INVISIBLE);
    }

    /**
     * @param view The button which is pressed
     * Function creates a custom alert dialog which gets the first term and the difference of a series.
     *
     * @return void
     */
    public void getInput(View view)
    {
        mydialog = (LinearLayout) getLayoutInflater().inflate(R.layout.my_dialog, null);
        eTx1 = mydialog.findViewById(R.id.eTx1);
        eTq = mydialog.findViewById(R.id.eTd);
        sw1 = mydialog.findViewById(R.id.swType);

        adb = new AlertDialog.Builder(this);

        adb.setView(mydialog);
        mydialog.setBackgroundColor(Color.WHITE);
        adb.setTitle("Series maker");
        adb.setMessage("Enter values for the first 20 terms of the series");
        adb.setPositiveButton("OK", myListener);
        adb.setNegativeButton("BACK", myListener);
        adb.setCancelable(false);
        adb.show();

    }

    DialogInterface.OnClickListener myListener  = new DialogInterface.OnClickListener() {
        /**
         * @param dialog The dialog which is created
         * @param which button to get input
         * Function validates the input written by the user and creates the series.
         *
         * @return void
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE)
            {
                String x1 = eTx1.getText().toString();
                String qD = eTq.getText().toString();
                seriesChoice = sw1.isChecked();
                if (checkInput(x1, qD, seriesChoice))
                {
                    firstNum = Double.parseDouble(x1);
                    diff = Double.parseDouble(qD);

                    generateSeriesLV();

                    tv1.setVisibility(VISIBLE);
                    tv2.setVisibility(VISIBLE);
                    tv3.setVisibility(VISIBLE);
                    tv4.setVisibility(VISIBLE);
                    dialog.dismiss();
                }
                else Toast.makeText(MainActivity.this, "Wrong inputs!", Toast.LENGTH_SHORT).show();

            }
            if (which == DialogInterface.BUTTON_NEGATIVE)
            {
                dialog.dismiss();
            }
        }
    };

    /**
     * @param x1 The first term
     * @param d The difference
     * @param series The type of series
     * Function validates the data inputted by the user.
     *
     * @return True if the data is valid, false otherwise.
     */
    public boolean checkInput(String x1, String d, boolean series)
    {
        if (d.equals("") || x1.equals("")) return false;
        else if ((d.equals("0") || x1.equals("0")) && series ) return false;
        else if (d.equals("0") && !series) return false;
        else if (!series && x1.equals("0")) return false;
        else if (x1.equals("-") || d.equals("-") || x1.equals("+") || d.equals("+") || x1.charAt(0) == '.' || d.charAt(0) == '.') return false;

        String incorrectPattern = "-.";
        if (x1.contains(incorrectPattern) || d.contains(incorrectPattern)) return false;
        incorrectPattern = "+.";
        if (x1.contains(incorrectPattern) || d.contains(incorrectPattern)) return false;
        return true;
    }

    /**
     * Function generates the series based on user input
     * @return void
     */
    public void generateSeriesLV() {
        series = new ArrayList<>(); // Use ArrayList<Long>
        numTV.setText(String.valueOf(firstNum));
        multTV.setText(String.valueOf(diff));

        if (seriesChoice) // Geometric series
        {
            for (int i = 0; i < 20; i++)
            {
                series.add(firstNum);
                firstNum *= diff;
            }
        }
        else // Mathmatical series
        {
            for (int i = 0; i < 20; i++)
            {
                series.add(firstNum);
                firstNum += diff;
            }
        }
        ArrayAdapter<Double> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, series); // Use ArrayAdapter<Long>
        lv1.setAdapter(adapter);

    }


    /**
     * @param adapterView The adapter view which is clicked
     * @param view The Item which was clicked
     * @param index The index of the clicked item
     * @param l The long value
     * Function sets the index and the sum of the series.
     * @return void
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
        indexTV.setText(String.valueOf(index + 1));

        double sum = 0;
        for (int i = 0; i <= index; i++)
        {
            sum += series.get(i);
        }

        sumTV.setText(String.valueOf(sum));
    }

    /**
     * @param menu The menu which is created
     * @return True if the menu is created, false otherwise
     * Function creates the menu
     */
    public boolean  onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param item The item which is selected
     * @return True if the item is selected, false otherwise
     * Function launches the credits activity
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent myIntent = new Intent(this, Credits.class);
        startActivity(myIntent);
        return true;
    }
}