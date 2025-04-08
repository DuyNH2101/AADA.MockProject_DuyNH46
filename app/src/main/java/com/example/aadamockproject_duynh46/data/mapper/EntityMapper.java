package com.example.aadamockproject_duynh46.data.mapper;


import com.example.aadamockproject_duynh46.data.source.entities.MovieEntity;
import com.example.aadamockproject_duynh46.data.source.entities.ReminderEntity;
import com.example.aadamockproject_duynh46.domain.model.MovieModel;
import com.example.aadamockproject_duynh46.domain.model.ReminderModel;

public class EntityMapper {
    public static MovieEntity toMovieEntity(MovieModel movieModel){
        return new MovieEntity(movieModel.isAdult(),
                movieModel.getBackdrop_path(),
                movieModel.getId(),
                movieModel.getOverview(),
                movieModel.getPosterPath(),
                movieModel.getReleaseDate(),
                movieModel.getTitle(),
                movieModel.isVideo(),
                movieModel.getVoteAverage(),
                movieModel.isFav());
    }
    public static MovieModel toMovieModel(MovieEntity movieEntity){
        return new MovieModel(movieEntity.isAdult(),
                movieEntity.getBackdrop_path(),
                movieEntity.getId(),
                movieEntity.getOverview(),
                movieEntity.getPosterPath(),
                movieEntity.getReleaseDate(),
                movieEntity.getTitle(),
                movieEntity.isVideo(),
                movieEntity.getVoteAverage(),
                movieEntity.isFav());
    }

    public static ReminderEntity toReminderEntity(ReminderModel reminderModel){
        if(reminderModel == null){
            return null;
        }
        return new ReminderEntity(reminderModel.getId(),
                toMovieEntity(reminderModel.getMovie()),
                reminderModel.getTimeInMillis(),
                reminderModel.getTimeInString());
    }

    public static ReminderModel toReminderModel(ReminderEntity reminderEntity){
        if(reminderEntity == null){
            return null;
        }
        return new ReminderModel(reminderEntity.getId(),
                new MovieModel(reminderEntity.isAdult(),
                        reminderEntity.getBackdrop_path(),
                        reminderEntity.getMovieId(),
                        reminderEntity.getOverview(),
                        reminderEntity.getPosterPath(),
                        reminderEntity.getReleaseDate(),
                        reminderEntity.getTitle(),
                        reminderEntity.isVideo(),
                        reminderEntity.getVoteAverage(),
                        reminderEntity.isFav()),
                reminderEntity.getTimeInMillis(),
                reminderEntity.getTimeInString());
    }
}
