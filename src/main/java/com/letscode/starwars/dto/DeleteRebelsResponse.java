package com.letscode.starwars.dto;

import lombok.Getter;

@Getter
public class DeleteRebelsResponse {

  Boolean success;

  public DeleteRebelsResponse() {
    this.success = true;
  }

}
