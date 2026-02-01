package com.px.jfmbackend.configuration;

import com.px.jfmbackend.service.MovieService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class FreshValStartupRunner implements ApplicationRunner {

  private final MovieService movieService;

  public FreshValStartupRunner(MovieService movieService) {
    this.movieService = movieService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    movieService.updateAllFreshVals();
  }
}
