package com.letscode.starwars.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.letscode.starwars.domain.Gender;
import com.letscode.starwars.domain.Inventory;
import com.letscode.starwars.domain.Location;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class RebelResponse {

  private String name;
  private Gender gender;
  private Location location;
  private Inventory inventory;
  private Boolean isTraitor;
  private Integer numberReportAsTraitor;
  @JsonProperty("public_id")
  private UUID publicId;

}
