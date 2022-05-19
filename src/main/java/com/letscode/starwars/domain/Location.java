package com.letscode.starwars.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Location {

  private String galaxy;
  private double latitude;
  private double longitude;

}
