package com.thomit.winecellar.utils;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.thomit.winecellar.controllers.WineController;
import com.thomit.winecellar.models.Wine;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Relation(collectionRelation="wines")
public class WineResource extends ResourceSupport {
	
	private final Wine wine;
	
	public WineResource(Wine wine){
		String accountId = wine.getAccount().getAccountId();
		this.wine = wine;
		this.add(linkTo(WineController.class, accountId).withRel("wines"));
		this.add(linkTo(methodOn(WineController.class, accountId).readWine(wine.getId(), accountId)).withSelfRel());
	}
	
	public Wine getWine(){
		return this.wine;
	}

}