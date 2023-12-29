package com.example.meditationtimer;

import android.content.Context;
import android.os.CountDownTimer;

public class MeditationTimerModel {

    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = 60 * ONE_SECOND;

    private CountDownTimer countDownTimer;
    private OnTimerTickListener onTimerTickListener;

    public MeditationTimerModel(Context context) {
        // Constructor initialization if needed
    }

    public interface OnTimerTickListener {
        void onTick(int remainingTimeInSeconds);

        void onFinish();
    }

    public void setOnTimerTickListener(OnTimerTickListener listener) {
        this.onTimerTickListener = listener;
    }

    public void startMeditationTimer(int durationInMinutes) {
        long durationInMillis = durationInMinutes * ONE_MINUTE;

        countDownTimer = new CountDownTimer(durationInMillis, ONE_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                int remainingTimeInSeconds = (int) (millisUntilFinished / ONE_SECOND);
                if (onTimerTickListener != null) {
                    onTimerTickListener.onTick(remainingTimeInSeconds);
                }
            }

            @Override
            public void onFinish() {
                if (onTimerTickListener != null) {
                    onTimerTickListener.onFinish();
                }
            }
        };

        countDownTimer.start();
    }

    public void stopMeditationTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
