package com.letscode.starwars.dto;

import com.letscode.starwars.domain.Rebel;

public class RebelMapper {

  private RebelMapper() {}

  public static Rebel toRebel(CreateRebelRequest createRebelRequest) {
    return Rebel.builder()
        .name(createRebelRequest.getName())
        .gender(createRebelRequest.getGender())
        .location(createRebelRequest.getLocation())
        .inventory(createRebelRequest.getInventory())
        .build();
  }

  public static RebelResponse toRebelResponse(Rebel rebel) {
    return RebelResponse.builder()
        .name(rebel.getName())
        .gender(rebel.getGender())
        .location(rebel.getLocation())
        .inventory(rebel.getInventory())
        .isTraitor(rebel.getIsTraitor())
        .numberReportAsTraitor(rebel.getNumberReportAsTraitor())
        .publicId(rebel.getPublicId())
        .build();
  }

  public static ReportAsTraitorResponse toReportAsTraitorResponse(Rebel rebel) {
    return ReportAsTraitorResponse.builder()
        .name(rebel.getName())
        .isTraitor(rebel.getIsTraitor())
        .numberReportAsTraitor(rebel.getNumberReportAsTraitor())
        .publicId(rebel.getPublicId())
        .build();
  }

}
