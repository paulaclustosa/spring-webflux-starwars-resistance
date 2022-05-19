package com.letscode.starwars.service;

import com.letscode.starwars.domain.*;
import com.letscode.starwars.dto.*;
import com.letscode.starwars.repository.RebelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Service
public class TradeService {

  private final RebelRepository repository;

  @Autowired
  public TradeService(RebelRepository repository) {
    this.repository = repository;
  }

  public Flux<RebelResponse> handleTrade(String rebelFirstId, String rebelSecondId, TradeInventoryItemsRequest itemsToBeTrade) {
    Integer tradePointsRebelFirst = getTradePoints(itemsToBeTrade.getItemsToBeTradeFromFirstRebel());
    Integer tradePointsRebelSecond = getTradePoints(itemsToBeTrade.getItemsToBeTradeFromSecondRebel());

    if (!validateTrade(tradePointsRebelFirst, tradePointsRebelSecond)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inventory's scores must be of same value.");
    }

    Mono<RebelResponse> rebelFirstAfterTrade = validateInventoryItems(rebelFirstId, itemsToBeTrade.getItemsToBeTradeFromFirstRebel())
        .doOnSuccess(rebelFirstFromDb -> {
          if (rebelFirstFromDb.getIsTraitor()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rebel is traitor therefore cannot trade items.");
          }
          executeTrade(rebelFirstFromDb, itemsToBeTrade.getItemsToBeTradeFromFirstRebel(), itemsToBeTrade.getItemsToBeTradeFromSecondRebel());
        }).flatMap(rebelUpdated -> repository.save(rebelUpdated).map(RebelMapper::toRebelResponse));

    Mono<RebelResponse> rebelSecondAfterTrade = validateInventoryItems(rebelSecondId, itemsToBeTrade.getItemsToBeTradeFromSecondRebel())
        .doOnSuccess(rebelSecondFromDb ->
          executeTrade(rebelSecondFromDb, itemsToBeTrade.getItemsToBeTradeFromSecondRebel(), itemsToBeTrade.getItemsToBeTradeFromFirstRebel())
        ).flatMap(rebelUpdated -> repository.save(rebelUpdated).map(RebelMapper::toRebelResponse));

    return Flux.concat(rebelFirstAfterTrade, rebelSecondAfterTrade);
  }

  private Integer getTradePoints(TradeInventoryItems tradeInventoryItems) {
    return tradeInventoryItems.getWeapon() * 4 +
        tradeInventoryItems.getAmmo() * 3 +
        tradeInventoryItems.getWater() * 2 +
        tradeInventoryItems.getFood();
  }

  private Boolean validateTrade(Integer tradePointsRebelFirst, Integer tradePointsRebelSecond) {
    return Objects.equals(tradePointsRebelFirst, tradePointsRebelSecond);
  }

  private Mono<Rebel> validateInventoryItems(String rebelId, TradeInventoryItems tradeInventoryItems) {
    UUID uuidId = UUID.fromString(rebelId);
    return repository.findByPublicId(uuidId)
        .doOnSuccess(rebelFromDb -> {
          Integer weaponFromDb = rebelFromDb.getInventory().getWeapon();
          Integer ammoFromDb = rebelFromDb.getInventory().getAmmo();
          Integer waterFromDb = rebelFromDb.getInventory().getWater();
          Integer foodFromDb = rebelFromDb.getInventory().getFood();

          Integer weaponFromRequest = tradeInventoryItems.getWeapon();
          Integer ammoFromRequest = tradeInventoryItems.getAmmo();
          Integer waterFromRequest = tradeInventoryItems.getWater();
          Integer foodFromRequest = tradeInventoryItems.getFood();

          if (weaponFromRequest > weaponFromDb || ammoFromRequest > ammoFromDb || waterFromRequest > waterFromDb || foodFromRequest > foodFromDb)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rebel doesn't have enough items in its inventory for trade.");
        });
  }

  private Mono<Rebel> executeTrade(Rebel rebelRef, TradeInventoryItems tradeInventoryItemsRebelRef, TradeInventoryItems tradeInventoryItemsOtherRebel) {
    Integer weaponUpdated = rebelRef.getInventory().getWeapon() - tradeInventoryItemsRebelRef.getWeapon() + tradeInventoryItemsOtherRebel.getWeapon();
    Integer ammoUpdated = rebelRef.getInventory().getAmmo() - tradeInventoryItemsRebelRef.getAmmo() + tradeInventoryItemsOtherRebel.getAmmo();
    Integer waterUpdated = rebelRef.getInventory().getWater() - tradeInventoryItemsRebelRef.getWater() + tradeInventoryItemsOtherRebel.getWater();
    Integer foodUpdated = rebelRef.getInventory().getFood() - tradeInventoryItemsRebelRef.getFood() + tradeInventoryItemsOtherRebel.getFood();

    rebelRef.setInventory(Inventory.builder()
        .weapon(weaponUpdated)
        .ammo(ammoUpdated)
        .water(waterUpdated)
        .food(foodUpdated)
        .build());

    return Mono.just(rebelRef);
  }

}
