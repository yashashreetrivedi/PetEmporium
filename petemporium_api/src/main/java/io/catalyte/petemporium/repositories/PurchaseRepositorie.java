package io.catalyte.petemporium.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


import io.catalyte.petemporium.domain.Purchases;



public interface PurchaseRepositorie extends MongoRepository<Purchases, String>{
	public List<Purchases> findByDate(String date);

}
