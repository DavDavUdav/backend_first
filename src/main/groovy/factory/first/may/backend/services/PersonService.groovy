package factory.first.may.backend.services

import factory.first.may.backend.api.errors.CustomAppException
import factory.first.may.backend.api.errors.CustomNotFoundException
import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Workshop
import factory.first.may.backend.repositories.PersonRepository
import factory.first.may.backend.request_models.request.PersonRequest
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
            new CustomNotFoundException('Не найден пользователь с id = ' + id)
        })
    }

    Person addOne(Person person) {
        Workshop workshop = workshopService.findByIdWorkshop(person.idWorkshop);
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


    Person update(PersonRequest requestPerson) {
        Person person = findByIdOrError(requestPerson.oldPersonId)
        Workshop workshop = workshopService.findByIdWorkshop(requestPerson.idWorkshop);
        if (workshop == null) {
            throw new CustomAppException('Не найден цех по заданному id')
        }
        Person newPerson = new Person(
                idPerson: requestPerson.oldPersonId,
                serviceNumber: requestPerson.serviceNumber,
                fullName: requestPerson.fullName,
                dateStart: requestPerson.dateStart,
                dateEnd: requestPerson.dateEnd,
                rating: requestPerson.rating,
                isProduction: requestPerson.isProduction,
                age: requestPerson.age,
                idWorkshop: requestPerson.idWorkshop,
                workshop: workshop
        )
        person.with { newPerson }
        personRepository.save(person)
    }

    Person deleteById(long id) {
        def person = findByIdOrError(id)
        personRepository.delete(person)
        person
    }

    Person findByServiceNumber(int serviceNumber) {
        return personRepository.findByServiceNumber(serviceNumber);
    }
}