package com.technocrats.bloodlife;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity {

    public void navigateToMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    public void navigateToLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    public void replaceFragment(int frameFL, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameFL, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public static void changeFragment(FragmentActivity activity, Fragment fragment, boolean isFragmentReplace, boolean addToBackStack, int container) {
        try {
            if (isFragmentReplace && addToBackStack) {
                activity
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(container, fragment, fragment.getClass().getName())
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
            } else if (!isFragmentReplace && addToBackStack) {
                activity
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(container, fragment, fragment.getClass().getName())
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
            } else if (isFragmentReplace) {
                activity
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(container, fragment, fragment.getClass().getName())
                        .commit();
            } else {
                activity
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(container, fragment, fragment.getClass().getName())
                        .commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
