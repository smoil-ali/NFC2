package com.techyasoft.nfc2.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.techyasoft.nfc2.Interfaces.InputTextListener;
import com.techyasoft.nfc2.R;
import com.techyasoft.nfc2.databinding.ItemInputText2Binding;


public class InputText extends DialogFragment {

    final String TAG = InputText.class.getSimpleName();
    EditText input_text;
    TextView button;
    InputTextListener listener;
    ItemInputText2Binding binding;

    public InputText() {
    }

    public void setListener(InputTextListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_input_text2,container,false);


        binding.inputText.setHintTextColor(ContextCompat.getColor(getContext(),R.color.white));
        binding.done.setOnClickListener(v -> {
            dismiss();
            listener.onInputText(binding.inputText.getText().toString());
        });

        binding.cancel.setOnClickListener(v -> {
            listener.onInputText(binding.inputText.getText().toString());
            dismiss();
        });
        return binding.getRoot();
    }

}
