package io.catalyte.petemporium.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


import io.catalyte.petemporium.domain.Pet;



public interface PetRepositorie  extends MongoRepository<Pet, String>{
	public List<Pet>findByName(String name);

}
