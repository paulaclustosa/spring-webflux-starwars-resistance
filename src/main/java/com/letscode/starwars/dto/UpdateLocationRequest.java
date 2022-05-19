package com.letscode.starwars.dto;

import lombok.Getter;

@Getter
public class UpdateLocationRequest {

  private String galaxy;
  private double latitude;
  private double longitude;

}
