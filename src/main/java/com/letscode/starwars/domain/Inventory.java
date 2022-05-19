package com.letscode.starwars.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Inventory {

  private Integer weapon;
  private Integer ammo;
  private Integer water;
  private Integer food;
  private Integer score;

  @Builder
  public Inventory(Integer weapon, Integer ammo, Integer water, Integer food) {
    this.weapon = weapon;
    this.ammo = ammo;
    this.water = water;
    this.food = food;
    this.score = weapon * 4 + ammo * 3 + water * 2 + food;
  }

  public void updateScore() {
    this.score = weapon * 4 + ammo * 3 + water * 2 + food;
  }

}
