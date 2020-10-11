package com.zhuinden.movierandomizerbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Initializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    MovieFileResolver movieFileResolver;

    @PostConstruct
    public void init() {
        LOGGER.info("The file is resolved to [" + movieFileResolver.getMovieFilePath() + "]");
    }
}
