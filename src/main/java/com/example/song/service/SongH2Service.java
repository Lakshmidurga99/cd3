
package com.example.song.service;

import com.example.song.model.SongRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.example.song.model.Song;
import com.example.song.repository.SongRepository;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class SongH2Service implements SongRepository {
    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Song> getSongs() {
        List<Song> songList = db.query("select * from PLAYLIST ", new SongRowMapper());
        ArrayList<Song> songs = new ArrayList<>(songList);
        return songs;
    }

    @Override
    public Song getSongById(int SongId) {
        try {
            Song song = db.queryForObject("select * from PLAYLIST  where songId=?", new SongRowMapper(), songId);
            return song;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Song addSong(Song songId) {
        db.update("insert into PLAYLIST (songName, lyricist,singer,musicDirector) values(?,?,?,?)", song.getSongName(),
                song.getLyricist(), song.getSinger(), song.getMusicDirector());
        Song savedSong = db.queryForObject(
                "select * from PLAYLIST  where songName=? and lyricist=? and singer=? and musicDirector=?",
                new SongRowMapper(), song.getSongName(), song.getLyricist(), song.getSinger(), song.getMusicDirector());
        return savedSong;
    }

    @Override
    public Song updateSong(int songId, Song song) {
        if (song.getSongName() != null) {
            db.update("update PLAYLIST  set songName=? where songId=?", song.getSongName(),
                    songId);
        }
        if (song.getLyricist() != null) {
            db.update("update PLAYLIST  set lyricist=? where songId=?", song.getLyricist(), songId);
        }
        if (song.getSinger() != null) {
            db.update("update PLAYLIST  set singer=? where songId=?", song.getSinger(), songId);
        }
        if (song.getMusicDirector() != null) {
            db.update("update PLAYLIST  set musicDirector=? where songId=?", song.getMusicDirector(), songId);
        }
        return getSongById(songId);
    }

    @Override
    public void deleteSong(int songId) {
        db.update("delete from PLAYLIST  where songId=?", songId);
    }

}