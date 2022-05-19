package com.letscode.starwars.dto;

import com.letscode.starwars.domain.Gender;
import com.letscode.starwars.domain.Inventory;
import com.letscode.starwars.domain.Location;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CreateRebelRequest {

  @NotBlank
  private String name;
  private Gender gender;
  @NotNull
  private Location location;
  @NotNull
  private Inventory inventory;

}
