package com.letscode.starwars.dto;

import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
public class TradeInventoryItems {

  @Positive
  private Integer weapon;
  @Positive
  private Integer ammo;
  @Positive
  private Integer water;
  @Positive
  private Integer food;

}
