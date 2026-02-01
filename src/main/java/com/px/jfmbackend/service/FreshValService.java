package com.px.jfmbackend.service;

import com.px.jfmbackend.entity.MovieFileEntity;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FreshValService {

  @Value("${FRESH_GROWTH:2}")
  private int FRESH_GROWTH;

  @Value("${MAX_DAILY_GROWTH:10}")
  private int MAX_DAILY_GROWTH;

  @Value("${FRESH_MAX:100}")
  private int FRESH_MAX;

  public void applyAgingIfNeeded(MovieFileEntity movie) {
    Instant now = Instant.now();
    Instant lastUpdated = movie.getFreshValUpdatedAt();

    // In case there is a null update time
    if (lastUpdated == null) {
      movie.setFreshValUpdatedAt(now);
      return;
    }

    // Fresh Value is increased in daily base
    long days =
        Math.abs(
            ChronoUnit.DAYS.between(
                lastUpdated.truncatedTo(ChronoUnit.DAYS), now.truncatedTo(ChronoUnit.DAYS)));

    // No need to update the freshVal
    if (days <= 0) {
      return;
    }

    // calculate growth value
    // There is a limit on each time a movie's fresh value growth
    int growth = (int) Math.min(days * FRESH_GROWTH, MAX_DAILY_GROWTH);
    int newFreshVal = Math.min(movie.getFreshVal() + growth, FRESH_MAX);

    movie.setFreshVal(newFreshVal);
    movie.setFreshValUpdatedAt(now);
  }
}
