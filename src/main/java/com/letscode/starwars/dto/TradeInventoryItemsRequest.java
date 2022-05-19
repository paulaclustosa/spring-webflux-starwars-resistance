package com.letscode.starwars.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TradeInventoryItemsRequest {

  UUID rebelFirstId;
  TradeInventoryItems itemsRebelFirst;
  UUID rebelSecondId;
  TradeInventoryItems itemsRebelSecond;

}
