package factory.first.may.backend.repositories

import factory.first.may.backend.models.Sell
import org.springframework.data.repository.CrudRepository

interface SellRepository extends CrudRepository<Sell, Long> {

    Sell findByIdSell(int idSell);
}
