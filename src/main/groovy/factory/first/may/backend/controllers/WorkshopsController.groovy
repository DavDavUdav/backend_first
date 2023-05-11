package factory.first.may.backend.controllers


import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Workshop
import factory.first.may.backend.request_models.request.Id
import factory.first.may.backend.request_models.request.WorkshopRequest
import factory.first.may.backend.services.WorkshopsService
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping('workshop')
@Transactional
class WorkshopsController {
    @Autowired
    WorkshopsService workshopService

    @GetMapping('')
    List findAll() {
        workshopService.findAll()
    }

    @GetMapping('/findByIdWorkshop')
    Workshop findOne(@RequestBody Id id) {
        workshopService.findByIdWorkshop(id.id)
    }

    @PostMapping('/create')
    String addOne(@RequestBody Workshop workshop) {
        workshopService.addOne(workshop)
    }

    @PutMapping('/update')
    Workshop update(@RequestBody WorkshopRequest workshopRequest) {
        workshopService.update(workshopRequest)
    }

    @DeleteMapping('/delete')
    Workshop deleteById(@RequestBody Id id) {
        workshopService.deleteById(id.id)
    }
}