package factory.first.may.backend.controllers


import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Workshop
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

    @GetMapping('{id}')
    Workshop findOne(@PathVariable String id) {
        println id
        workshopService.findById(Long.parseLong(id))
    }

    @PostMapping('/create/')
    String addOne(@RequestBody Workshop workshop) {
        workshopService.addOne(workshop)
        return 'Создание прошло успешно'
    }

    @PostMapping('')
    Workshop save(@RequestBody Workshop person) {
        workshopService.save(person)
    }

    @PutMapping('{id}')
    Workshop update(@RequestBody Workshop person, @PathVariable long id) {
        workshopService.update(person, id)
    }

    @DeleteMapping('{id}')
    Workshop deleteById(@PathVariable long id) {
        workshopService.deleteById(id)
    }
}