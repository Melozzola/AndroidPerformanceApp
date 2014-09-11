package silvano.riz.perfevaluation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class Home extends Activity implements View.OnClickListener{

    @InjectView(R.id.selectionTest)
    Spinner selectionTest;

    @InjectView(R.id.btnRun)
    Button btnRun;

    @InjectView(R.id.btnClear)
    Button btnClear;

    @InjectView(R.id.tvLogs)
    TextView tvLogs;

    Tests.TestsId selectedTestId = Tests.TestsId.TEST_HELLO_WORLD_STRING;
    TestRunner testRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.inject(this);

        btnRun.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        ArrayAdapter selectionTestAdapter = new ArrayAdapter<Tests.TestsId>(this, android.R.layout.simple_spinner_item, Tests.TestsId.values());
        selectionTestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectionTest.setAdapter(selectionTestAdapter);
        selectionTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTestId = (Tests.TestsId)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not implemented
            }
        });

        testRunner = TestRunner.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRun){
            switchButtonsState();

            testRunner.run(selectedTestId, new TestOnSeparateThreadCallback() {
                @Override
                public void finish(TestRunner.Stats stats) {
                    printStats(stats);
                    switchButtonsState();
                }
            });
        }else if (v == btnClear){
            tvLogs.setText("");
        }
    }

    private void printStats(TestRunner.Stats stats){
        tvLogs.setText("");
        tvLogs.append(stats.getLog());
        tvLogs.append("\n");
        tvLogs.append("Time taken: " + Utils.getElapsedTimeInMilliseconds(stats.start, stats.end) + " (ms)");
    }

    private synchronized void switchButtonsState(){
        btnClear.setEnabled(!btnClear.isEnabled());
        btnRun.setEnabled(!btnRun.isEnabled());
    }
}
