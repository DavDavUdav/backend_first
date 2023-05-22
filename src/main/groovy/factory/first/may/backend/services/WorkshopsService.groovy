package factory.first.may.backend.services

import factory.first.may.backend.api.core.errors.CustomAppException
import factory.first.may.backend.api.core.errors.CustomNotFoundException
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

    Workshop addOne(WorkshopRequest workshopRequest) {
        if (workshopRequest.idWorkshop == null ||
                workshopRequest.name == null ||
                workshopRequest.shortName == null) {
            throw new CustomAppException('Не заполнены обязательные поля \n' +
                    'idWorkshop\n name\n shortName' +
                    'проверьте заполнение полей')
        }
        Workshop workshop = new Workshop(
                id: workshopRequest.idWorkshop,
                idWorkshop: workshopRequest.idWorkshop,
                name: workshopRequest.name,
                shortName: workshopRequest.shortName
        )

        workshopRepository.save(workshop)
        workshop
    }

    Workshop findByIdWorkshop(int id) {
        Workshop workshop = workshopRepository.findByIdWorkshop(id)
        if (workshop == null) {
            throw new CustomNotFoundException('Не найден цех по заданному id')
        }
        workshop
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
        Workshop workshop = findByIdWorkshop(workshopRequest.idWorkshop)
        if (workshop == null) throw new CustomNotFoundException('Не найден цех по id = ' + workshopRequest.idWorkshop);
        workshop.setName(workshopRequest.name)
        workshop.setShortName(workshopRequest.shortName)

        workshopRepository.save(workshop)
        workshop
    }
}
