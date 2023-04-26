package factory.first.may.backend.services

import factory.first.may.backend.api.errors.CustomAppException
import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Workshop
import factory.first.may.backend.repositories.WorkshopRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
// register WorkshopService as a Service Spring component
class WorkshopsService {
    @Autowired
    // tell Spring to inject value from Spring component
    WorkshopRepository workshopRepository

    //@Autowired
    //PersonService personService

    List findAll() {
        workshopRepository.findAll().asList()
    }

    Workshop addOne(Workshop workshop) {
        if (workshop == null) {
            throw new CustomAppException('Не найден цех по заданному id')
        }
        workshopRepository.save(workshop)
    }

    Workshop findById(long id) {
        workshopRepository.findById(id).orElse(null)
    }


    Workshop findByIdOrError(long id) {
        workshopRepository.findById(id).orElseThrow({
            new EntityNotFoundException()
        })
    }
}
