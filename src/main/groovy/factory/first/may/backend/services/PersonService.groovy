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

    Person findByIdOrError(long id) {
        personRepository.findById(id).orElseThrow({
            new CustomNotFoundException('Не найден пользователь с id = ' + id)
        })
    }

    Person addOne(PersonRequest personRequest) {
        Workshop workshop = workshopService.findByIdWorkshop(personRequest.idWorkshop.toInteger());
        if (workshop == null) {
            throw new CustomAppException('Не найден цех по заданному id')
        }
        if (personRequest.idPerson == null ||
                personRequest.serviceNumber == null ||
                personRequest.fullName == null ||
                personRequest.dateStart == null ||
                personRequest.dateEnd == null ||
                personRequest.age == null ||
                personRequest.idWorkshop == null
        ) {
            throw new CustomAppException('Не заполнены обязательные поля \n' +
                    'idPerson\n fullName\n dateStart\n dateEnd\n age\n idWorkshop\n' +
                    'проверьте заполние полей')
        }
        Person newPerson = new Person(
                idPerson: personRequest.idPerson,
                serviceNumber: personRequest.serviceNumber,
                fullName: personRequest.fullName,
                dateStart: personRequest.dateStart,
                dateEnd: personRequest.dateEnd,
                age: personRequest.age,
                idWorkshop: personRequest.idWorkshop,
                workshop: workshop,
        )
        personRepository.save(newPerson)
    }


    Person update(PersonRequest requestPerson) {
        Person person = findByIdOrError(requestPerson.idPerson)
        if (requestPerson.serviceNumber != null) {
            person.setServiceNumber(requestPerson.serviceNumber.toInteger())
        }
        if (requestPerson.fullName != null) {
            person.setFullName(requestPerson.fullName)
        }
        if (requestPerson.dateStart != null) {
            person.setDateStart(requestPerson.dateStart)
        }
        if (requestPerson.dateEnd != null) {
            person.setDateEnd(requestPerson.dateEnd)
        }
        if (requestPerson.age != null) {
            person.setAge(requestPerson.age.toInteger())
        }
        if (requestPerson.rating != null) {
            person.setRating(requestPerson.rating.toInteger())
        }
        if (requestPerson.idWorkshop != null) {
            Workshop workshop = workshopService.findByIdWorkshop(requestPerson.idWorkshop.toInteger());
            if (workshop == null) {
                throw new CustomNotFoundException('Не найден юзер по заданному id = ' + requestPerson.idPerson)
            }
            person.setWorkshop(workshop)
        }
        personRepository.save(person)
    }

    Person deleteById(long id) {
        Person person = findByIdOrError(id)
        personRepository.delete(person)
        person
    }

    Person deleteByServiceNumber(long id) {
        Person person = findByServiceNumber(id.toInteger())
        personRepository.delete(person)
        person
    }

    Person findByServiceNumber(int serviceNumber) {
        Person person = personRepository.findByServiceNumber(serviceNumber);
        if (person == null) {
            throw new CustomNotFoundException('Не найден юзер по заданному id = ' + serviceNumber)
        }
        person
    }
}