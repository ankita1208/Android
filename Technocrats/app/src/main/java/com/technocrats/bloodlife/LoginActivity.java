package com.technocrats.bloodlife;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.technocrats.bloodlife.R;
import com.technocrats.bloodlife.databinding.ActivityLoginBinding;


public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        replaceFragment(R.id.frameFL, new LoginFragment());
    }
}