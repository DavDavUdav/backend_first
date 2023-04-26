package factory.first.may.backend.repositories

import factory.first.may.backend.models.Sells
import org.springframework.data.repository.CrudRepository


interface SellsRepository extends CrudRepository<Sells, Long> {}