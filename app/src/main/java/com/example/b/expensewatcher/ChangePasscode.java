package com.example.b.expensewatcher;

/*
TODO:
Create fragments:
Case 1: No Password Exists
Fragment 1: Enter New Passcode
Fragment 2: Confirm New Passcode

Case 2: Passcode Exists
Fragment 1: Enter Current Passcode
Fragment 2: Enter New Passcode
Fragment 3: Confirm New Passcode
 */
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.widget.Toast;
import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.example.b.expensewatcher.Utilities.DatabaseHelper;

public class ChangePasscode extends AppCompatActivity {

    String correct_passcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passcode);

        ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.change_passcode);
        }

        DatabaseHelper db = new DatabaseHelper(ChangePasscode.this);
        correct_passcode = db.readpasscode(db);

        final PinEntryEditText currentpinEntry = (PinEntryEditText) findViewById(R.id.current_pin_entry);
        final PinEntryEditText newpinEntry = (PinEntryEditText) findViewById(R.id.new_pin_entry);
        final KeyListener newPinListener = newpinEntry.getKeyListener();
        //disable the newpinEntry
        newpinEntry.setKeyListener(null);


        if (currentpinEntry != null) {
            currentpinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(final CharSequence str) {
                    if (str.toString().equals(correct_passcode)) {

                        //enable the newpinEntry
                        newpinEntry.setKeyListener(newPinListener);
                        newpinEntry.focus();

                        if(newpinEntry != null) {
                            newpinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                                @Override
                                public void onPinEntered(CharSequence newstr) {

                                    DatabaseHelper db = new DatabaseHelper(ChangePasscode.this);
                                    String oldPasstext = str.toString();
                                    String newPasstext = newstr.toString();
                                    Boolean value = db.changepasscode(db, oldPasstext, newPasstext);
                                    if(value){
                                        Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                                        NavUtils.navigateUpFromSameTask(ChangePasscode.this);
                                        //Close Screen and Return to Configure
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(),R.string.error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } else {
                        Toast.makeText(ChangePasscode.this, R.string.error, Toast.LENGTH_SHORT).show();
                        currentpinEntry.setText(null);
                    }
                }
            });
        }

    }
}
