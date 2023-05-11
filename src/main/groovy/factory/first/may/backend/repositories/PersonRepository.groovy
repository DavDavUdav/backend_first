package factory.first.may.backend.repositories

import factory.first.may.backend.models.Person
import org.springframework.data.repository.CrudRepository

interface PersonRepository extends CrudRepository<Person, Long> {
    Person findByServiceNumber(int serviceNumber);
}
