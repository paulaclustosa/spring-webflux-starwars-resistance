package com.letscode.starwars.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Rebel {

  @Id
  private String id;
  private String name;
  private Gender gender;
  private Location location;
  private Inventory inventory;
  private Boolean isTraitor;
  private Integer numberReportAsTraitor;
  private UUID publicId;

  @Builder
  public Rebel(String name, Gender gender, Location location, Inventory inventory) {
    this.name = name;
    this.gender = gender;
    this.location = location;
    this.inventory = inventory;
    this.isTraitor = false;
    this.numberReportAsTraitor = 0;
    this.publicId = UUID.randomUUID();
  }

  public void updateNumberReportAsTraitor() {
    this.numberReportAsTraitor = this.numberReportAsTraitor + 1;
  }

  public void updateIsTraitor () {
    this.isTraitor = numberReportAsTraitor > 2;
  }

}
