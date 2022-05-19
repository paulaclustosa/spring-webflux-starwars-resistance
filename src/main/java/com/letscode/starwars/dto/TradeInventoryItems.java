package com.letscode.starwars.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class TradeInventoryItems {

  @NotNull
  private Integer weapon;
  @NotNull
  private Integer ammo;
  @NotNull
  private Integer water;
  @NotNull
  private Integer food;

}
