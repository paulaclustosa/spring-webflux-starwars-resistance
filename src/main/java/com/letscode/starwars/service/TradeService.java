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

  public Flux<RebelResponse> handleTrade(TradeInventoryItemsRequest request) {

    Integer tradePointsRebel1 = getTradePoints(request.getItemsRebelFirst());
    Integer tradePointsRebel2 = getTradePoints(request.getItemsRebelSecond());

    if (!validateTrade(tradePointsRebel1, tradePointsRebel2)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inventory's scores must be of same value.");
    }

    var a = validateInventoryItems(request.getRebelFirstId(), request.getItemsRebelFirst())
        .doOnSuccess(rebelFirstFromDb -> {
          if (rebelFirstFromDb.getIsTraitor()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rebel is traitor therefore can not trade items.");
          }
          executeTrade(rebelFirstFromDb, request.getItemsRebelFirst(), request.getItemsRebelSecond());
        }).flatMap(rebelUpdated -> repository.save(rebelUpdated).map(RebelMapper::toRebelResponse));

    var b = validateInventoryItems(request.getRebelSecondId(), request.getItemsRebelSecond())
        .doOnSuccess(rebelSecondFromDb ->
          executeTrade(rebelSecondFromDb, request.getItemsRebelSecond(), request.getItemsRebelFirst())
        ).flatMap(rebelUpdated -> repository.save(rebelUpdated).map(RebelMapper::toRebelResponse));

    return Flux.concat(a, b);
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

  private Mono<Rebel> validateInventoryItems(UUID rebelId, TradeInventoryItems tradeInventoryItems) {
    return repository.findByPublicId(rebelId)
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inventory items received doesn't match inventory items registered for the rebel.");
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
