package ru.mirea.khrebtovsky.mireaproject;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SimpleWorker extends Worker {

    public SimpleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        System.out.println("Простая фоновая задача выполняется.");

        return Result.success();
    }
}
