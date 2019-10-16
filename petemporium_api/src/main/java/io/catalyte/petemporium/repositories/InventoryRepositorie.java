package io.catalyte.petemporium.repositories;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import io.catalyte.petemporium.domain.Inventory;



public interface InventoryRepositorie extends MongoRepository<Inventory, String>{
	public List<Inventory> findByType(String type);
}
