package sg.edu.rp.c346.id19020844.mybmicalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText editWeight, editHeight;
    TextView tvDate, tvBMI, tvStatus;
    Button btnCalc, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editWeight = findViewById(R.id.weight);
        editHeight = findViewById(R.id.height);
        tvDate = findViewById(R.id.calcDate);
        tvBMI = findViewById(R.id.calcBMI);
        btnCalc = findViewById(R.id.calc);
        btnReset = findViewById(R.id.reset);
        tvStatus = findViewById(R.id.status);

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(editWeight.getText().toString());
                float height = Float.parseFloat(editHeight.getText().toString());
                float bmi = weight / (height * height);

                // Create a Calendar object with current date and time
                Calendar now = Calendar.getInstance();
                String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                        now.get((Calendar.MONTH)+1) + "/" + now.get(Calendar.YEAR) + " " +
                        now.get(Calendar.HOUR_OF_DAY) + ":" +
                        now.get(Calendar.MINUTE);
                tvDate.setText(datetime);
                tvBMI.setText(String.format("%.3f", bmi));
                editWeight.setText("");
                editHeight.setText("");

                if (bmi == 0.0) {
                    tvStatus.setText("");
                }
                else if (bmi < 18.5) {
                    tvStatus.setText("You are underweight");
                }
                else if (bmi >= 18.5 && bmi <= 24.9) {
                    tvStatus.setText("Your BMI is normal");
                }
                else if (bmi >= 25 && bmi <= 29.9) {
                    tvStatus.setText("You are overweight");
                }
                else if (bmi >= 30) {
                    tvStatus.setText("You are obese");
                }
                else {
                    tvStatus.setText("Error in calculation");
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWeight.setText("");
                editHeight.setText("");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor prefEdit = prefs.edit();
                prefEdit.clear();
                prefEdit.commit();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Step 1: Get User input and store as variable
        String dateTimeString = tvDate.getText().toString();
        float bmi = Float.parseFloat(tvBMI.getText().toString());

        // Step 2: Obtain instance of SharedPreferences & SharedPreferences Editor
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor prefEdit = prefs.edit();

        // Step 3: Add key-value pair
        prefEdit.putString("dateTime", dateTimeString);
        prefEdit.putFloat("bmi", bmi);

        // Step 4: Call commit()
        prefEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Step 1: Obtain instance of SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Step 2: Retrieve saved data from SharedPreferences object
        String dateTimeString = prefs.getString("dateTime", "");
        float bmi = prefs.getFloat("bmi", 0);

        // Step 3: Update UI elements with value
        tvDate.setText(dateTimeString);
        tvBMI.setText(bmi + "");
    }
}
