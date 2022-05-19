package com.letscode.starwars.dto;

import lombok.Getter;

@Getter
public class DeleteRebelsResponse {

  Boolean success;

  public DeleteRebelsResponse(Boolean wasSuccessfulyDeleted ) {
    this.success = wasSuccessfulyDeleted;
  }

}
