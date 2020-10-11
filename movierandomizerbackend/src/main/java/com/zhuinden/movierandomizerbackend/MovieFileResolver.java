package com.zhuinden.movierandomizerbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MovieFileResolver {
    private static final String MOVIES_FILE_NAME = "movies.xlsm";

    @Autowired
    private Environment environment;

    public String getMovieFilePath() {
        String filename = environment.getProperty("filename");
        String filepath = environment.getProperty("path");

        String actualPath = MOVIES_FILE_NAME;
        if(filename != null) {
            actualPath = filename;
        }
        if(filepath != null) { // path takes precedence over filename
            actualPath = filepath;
        }
        return actualPath;
    }
}
