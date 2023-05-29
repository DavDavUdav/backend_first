package factory.first.may.backend.controllers

import factory.first.may.backend.models.Person
import factory.first.may.backend.request_models.request.Id
import factory.first.may.backend.request_models.request.PersonRequest
import factory.first.may.backend.services.PersonService
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

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

    @GetMapping('/fillDB')
    String fillDB() {
        personService.fillDB()
    }

    @GetMapping('/findById')
    Person findOne(@RequestBody Id id) {
        personService.findByIdOrError(id.id)
    }

    @GetMapping('/findByServiceNumber')
    Person findOneByServiceNumber(@RequestBody Id id) {
        personService.findByServiceNumber((id.id))
    }

    @PostMapping('/create')
    Person addOne(@RequestBody PersonRequest personRequest) {
        personService.addOne(personRequest)
    }

    @PostMapping('/addOneFrom1c')
    Person addOneFrom1c(@RequestBody PersonRequest personRequest) {
        personService.addOneFrom1c(personRequest)
    }

    @PutMapping('/update')
    Person update(@RequestBody PersonRequest personRequest) {
        personService.update(personRequest)
    }

    @DeleteMapping('/deleteById')
    Person deleteById(@RequestBody Id id) {
        personService.deleteById(id.id)
    }

    @DeleteMapping('/deleteByServiceNumber')
    Person deleteByServiceNumber(@RequestBody Id id) {
        personService.deleteByServiceNumber(id.id)
    }
}