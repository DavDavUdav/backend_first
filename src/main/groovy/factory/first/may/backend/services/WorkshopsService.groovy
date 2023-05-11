package factory.first.may.backend.services

import factory.first.may.backend.api.errors.CustomAppException
import factory.first.may.backend.api.errors.CustomNotFoundException
import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Sell
import factory.first.may.backend.models.Workshop
import factory.first.may.backend.repositories.WorkshopRepository
import factory.first.may.backend.request_models.request.WorkshopRequest
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
        return workshopRepository.findAll().asList()
    }

    Workshop addOne(Workshop workshop) {
        if (workshop == null) {
            throw new CustomAppException('Не найден цех по заданному id')
        }
        return workshopRepository.save(workshop)
    }

    Workshop findByIdWorkshop(int id) {
        Workshop _workshop = workshopRepository.findByIdWorkshop(id)
        if (_workshop == null) {
            throw new CustomNotFoundException('Не найден цех по заданному id')
        }
        return _workshop
    }

    Workshop deleteById(int id) {
        Workshop workshop = findByIdWorkshop(id)
        if (workshop == null) {
            throw new CustomNotFoundException('Не найден цех по заданному id')
        }
        workshopRepository.delete(workshop)
        workshop
    }

    Workshop update(WorkshopRequest workshopRequest) {
        Workshop workshop = findByIdWorkshop(workshopRequest.oldWorkshopId.toInteger())
        if (workshop == null) throw new CustomNotFoundException('Не найден цех по id = ' + workshopRequest.oldWorkshopId);
        Workshop newWorkshop = new Workshop(
                id: workshopRequest.id,
                idWorkshop: workshopRequest.idWorkshop,
                name: workshopRequest.name,
                typeOfWorkshop: workshopRequest.typeOfWorkshop,
        )
        workshop.with { newWorkshop }

        return workshopRepository.save(workshop)
    }
}
