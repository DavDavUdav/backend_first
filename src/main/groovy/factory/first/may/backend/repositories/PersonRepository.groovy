package factory.first.may.backend.repositories

import factory.first.may.backend.models.Person
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

interface PersonRepository extends CrudRepository<Person, Long> {}
