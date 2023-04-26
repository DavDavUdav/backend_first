package factory.first.may.backend.controllers

import factory.first.may.backend.models.Sells
import factory.first.may.backend.services.SellsService
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
@RequestMapping('sells')
@Transactional
class SellsController {
    @Autowired
    SellsService sellsService

    @GetMapping('')
    List findAll() {
        sellsService.findAll()
    }

    @GetMapping('{id}')
    Sells findById(@PathVariable long id) {
        sellsService.findById(id)
    }

    @PostMapping('')
    Sells create(@RequestBody Sells sells) {
        sellsService.save(sells)
    }

    @PutMapping('{id}')
    Sells update(@RequestBody Sells sells, @PathVariable long id) {
        sellsService.update(sells, id)
    }

    @PostMapping('{id}/person/{personId}')
    Sells assignPerson(@PathVariable long id, @PathVariable long personId) {
        sellsService.assignPerson(id, personId)
    }

    @DeleteMapping('{id}/person/{personId}')
    Sells removePerson(@PathVariable long id, @PathVariable long personId) {
        sellsService.removePerson(id, personId)
    }

    @PostMapping('{id}')
    Sells resolve(@PathVariable long id) {
        sellsService.resolve(id)
    }

    @DeleteMapping('{id}')
    Sells deleteById(@PathVariable long id) {
        sellsService.deleteById(id)
    }
}