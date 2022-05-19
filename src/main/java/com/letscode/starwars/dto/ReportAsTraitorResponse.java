package com.letscode.starwars.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class ReportAsTraitorResponse {

  private String name;
  private Boolean isTraitor;
  private Integer numberReportAsTraitor;
  @JsonProperty("public_id")
  private UUID publicId;

}
