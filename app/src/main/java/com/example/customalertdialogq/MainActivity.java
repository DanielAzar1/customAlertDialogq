package com.example.customalertdialogq;

import static java.lang.Math.pow;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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
    TextView indexTV, sumTV;
    ListView lv1;
    Double firstNum, diff;
    boolean seriesChoice;
    ArrayList<Double> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        lv1 = findViewById(R.id.lv);
        indexTV = findViewById(R.id.indexTV);
        sumTV = findViewById(R.id.sumTV);

        lv1.setOnItemClickListener(this);
        lv1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

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

    public void generateSeriesLV() {
        series = new ArrayList<>(); // Use ArrayList<Long>
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
}