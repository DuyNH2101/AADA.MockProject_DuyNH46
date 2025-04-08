package com.example.aadamockproject_duynh46.framework.worker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.hilt.work.HiltWorker;
import androidx.work.ListenableWorker;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.data.source.entities.ReminderEntity;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.repository.ReminderRepository;
import com.example.aadamockproject_duynh46.presentation.main.MainActivity;

import java.util.Calendar;
import java.util.UUID;

import javax.inject.Inject;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@HiltWorker
public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "1001";

    private final Context mContext;
    private final ReminderRepository repository;
    private final int movieId;

    @AssistedInject
    public NotificationWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters workerParams,
            ReminderRepository repository
    ) {
        super(context, workerParams);
        this.mContext = context;
        this.repository = repository;
        this.movieId = workerParams.getInputData().getInt("MOVIE_ID", -1);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("WORKER", "doWork");
        pushNotification(mContext);
        return Result.success();
    }

    private void pushNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        ReminderEntity reminderEntity = repository.getReminderByMovieId(movieId);
        if (reminderEntity == null) return;

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("MOVIE", new MovieModel(
                reminderEntity.isAdult(),
                reminderEntity.getBackdrop_path(),
                reminderEntity.getMovieId(),
                reminderEntity.getOverview(),
                reminderEntity.getPosterPath(),
                reminderEntity.getReleaseDate(),
                reminderEntity.getTitle(),
                reminderEntity.isVideo(),
                reminderEntity.getVoteAverage(),
                reminderEntity.isFav()
        ));

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,
                "Reminder Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        notificationManager.createNotificationChannel(notificationChannel);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(reminderEntity.getTitle())
                .setContentText("Rate: " + reminderEntity.getVoteAverage() + "/10")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(movieId, notification);

        repository.deleteByMovieId(movieId);
        WorkManager.getInstance(context).cancelWorkById(getId());
    }
}
