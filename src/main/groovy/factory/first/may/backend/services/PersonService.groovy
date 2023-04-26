package factory.first.may.backend.services

import factory.first.may.backend.api.errors.CustomAppException
import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Workshop
import factory.first.may.backend.repositories.PersonRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonService {
    @Autowired
    PersonRepository personRepository
    @Autowired
    WorkshopsService workshopService

    List findAll() {
        personRepository.findAll().asList()
        //personRepository.findAll(Sort.by('id_person')).asList()
    }

    Person findById(long id) {
        personRepository.findById(id).orElse(null)
    }

    Person findByIdOrError(long id) {
        personRepository.findById(id).orElseThrow({
            new EntityNotFoundException()
        })
    }

    Person addOne(Person person, long workshopId) {
        Workshop workshop = workshopService.findById(workshopId as long);
        println workshop
        if (workshop == null) {
            throw new CustomAppException('Не найден цех по заданному id')
        }
        Person newPerson = new Person(
                idPerson: person.idPerson,
                serviceNumber: person.serviceNumber,
                fullName: person.fullName,
                dateStart: person.dateStart,
                dateEnd: person.dateEnd,
                isProduction: person.isProduction,
                age: person.age,
                idWorkshop: person.idWorkshop,
                workshop: workshop,
        )
        personRepository.save(newPerson)
    }

    Person save(Person person) {
        // assign person to every abilities
        person.workshop?.each { it.person = person }
        personRepository.save(person)
    }

    Person update(Person person, long id) {
        Person persisted = findByIdOrError(id)
        persisted.with {
            name = person.name
        }
        def toBeRemoved = []
        // find values to update & delete
        persisted.abilities.each {
            def a = person.abilities.find { it2 -> it2.id == it.id }
            if (a == null) toBeRemoved.add(it)
            else it.name = a.name
        }
        persisted.abilities.removeAll(toBeRemoved)
        // assign persisted entity as the person
        person.abilities.each {
            if (it.id == null) {
                it.person = persisted
                persisted.abilities.add(it)
            }
        }

        personRepository.save(persisted)
    }

    Person deleteById(long id) {
        def person = findByIdOrError(id)
        personRepository.delete(person)
        person
    }
}