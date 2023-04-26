package factory.first.may.backend.controllers

import factory.first.may.backend.models.Person
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

    @GetMapping('{id}')
    Person findOne(@PathVariable String id) {
        println id
        personService.findById(Long.parseLong(id))
    }

    @PostMapping('/create/')
    Person addOne(@RequestBody Person person) {
        personService.addOne(person, person.idWorkshop)
    }

    @PostMapping('')
    Person save(@RequestBody Person person) {
        personService.save(person)
    }

    @PutMapping('{id}')
    Person update(@RequestBody Person person, @PathVariable long id) {
        personService.update(person, id)
    }

    @DeleteMapping('{id}')
    Person deleteById(@PathVariable long id) {
        personService.deleteById(id)
    }
}