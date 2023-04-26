package factory.first.may.backend.services

import factory.first.may.backend.models.Sells
import factory.first.may.backend.repositories.SellsRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service // register SellsService as a Service Spring component
class SellsService {
    @Autowired // tell Spring to inject value from Spring component
    SellsRepository sellsRepository

    @Autowired
    PersonService personService

    List findAll() {
        sellsRepository.findAll(Sort.by(Sort.Order.desc('time'))).asList()
    }

    Sells findById(long id) {
        sellsRepository.findById(id).orElse(null)
    }

    Sells findByIdOrError(long id) {
        sellsRepository.findById(id).orElseThrow({
            new EntityNotFoundException()
        })
    }

    Sells save(Sells sells) {
        sells.isResolved = false
        sellsRepository.save(sells)
    }

    Sells update(Sells sells, long id) {
        def persisted = findByIdOrError(id)
        // update entity's values
        persisted.with {
            title = sells.title
            location = sells.location
            time = sells.time
        }
        sellsRepository.save(persisted)
    }

    Sells assignPerson(long id, long personId) {
        def sells = findByIdOrError(id)
        def person = personService.findByIdOrError(personId)

        sells.persones.add(person)
        sellsRepository.save(sells)
    }

    Sells removePerson(long id, long personId) {
        def sells = findByIdOrError(id)
        def person = personService.findByIdOrError(personId)

        sells.persones.remove(person)
        sellsRepository.save(sells)
    }

    Sells resolve(long id) {
        def sells = findByIdOrError(id)
        sells.isResolved = true

        sellsRepository.save(sells)
    }

    Sells deleteById(long id) {
        def sells = findByIdOrError(id)
        sellsRepository.delete(sells)
        sells
    }
}