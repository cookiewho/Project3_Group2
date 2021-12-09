package com.example.rocketcorner;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;

//This class is for entering currency amount while keeping the right format as the user enters values
public class NumberTextWatcherTV implements TextWatcher {
    private final DecimalFormat df;
    private final TextView tv;

    public NumberTextWatcherTV(TextView textView) {
        df = new DecimalFormat("#,##0.00");
        this.tv = textView;
    }

    @Override
    public void afterTextChanged(Editable s) {
        tv.removeTextChangedListener(this);
        //After all the text editing, if there is a string to validate - format it
        if (s != null && !s.toString().isEmpty()) {
            try {
                //Take the input string and remove all formatting characters
                if (df == null){
                    tv.setText("$0.00");
                    tv.addTextChangedListener(this);
                    return;
                }
                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "").replace("$","").replace(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()), "");
                //Pass the altered string to a number
                Number n = df.parse(v);
                //Get the decimal point correct again
                n = n.doubleValue() / 100.0;
                //Reformat the text with currency symbols, grouping places etc.
                tv.setText(df.format(n));
                //Add the Dollar symbol ($)
                tv.setText("$".concat(tv.getText().toString()));
                //Move the editing cursor back to the right place
                tv.setText(tv.getText().length());

            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
        } else //if the input field is empty
        {
            tv.setText("$0.00");
        }

        tv.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }
}
