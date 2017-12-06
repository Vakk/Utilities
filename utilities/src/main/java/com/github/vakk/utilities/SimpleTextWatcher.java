package com.github.vakk.utilities;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Valery Kotsulym on 12/6/17.
 */

public abstract class SimpleTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        textChanged(s.toString());
    }

    public abstract void textChanged(String newText);
}