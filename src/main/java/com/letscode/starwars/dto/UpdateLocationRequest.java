package com.letscode.starwars.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateLocationRequest {

  private UUID publicId;
  private String galaxy;
  private double latitude;
  private double longitude;

}
