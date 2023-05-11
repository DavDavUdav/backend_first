package factory.first.may.backend.controllers

import factory.first.may.backend.models.Person
import factory.first.may.backend.request_models.request.Id
import factory.first.may.backend.request_models.request.PersonRequest
import factory.first.may.backend.services.PersonService
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
@RequestMapping('person')
@Transactional
class PersonController {
    @Autowired
    PersonService personService

    @GetMapping('')
    List findAll() {
        personService.findAll()
    }

    @GetMapping('/findById')
    Person findOne(@RequestBody Id id) {
        personService.findById(id.id)
    }

    @GetMapping('/findByServiceNumber')
    Person findOneByServiceNumber(@RequestBody Id id) {
        personService.findByServiceNumber((id.id))
    }

    @PostMapping('/create')
    Person addOne(@RequestBody Person person) {
        personService.addOne(person)
    }

    @PutMapping('/update')
    Person update(@RequestBody PersonRequest personRequest) {
        personService.update(personRequest)
    }

    @DeleteMapping('{id}')
    Person deleteById(@PathVariable long id) {
        personService.deleteById(id)
    }
}