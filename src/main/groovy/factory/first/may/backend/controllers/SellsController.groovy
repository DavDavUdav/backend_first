package factory.first.may.backend.controllers

import factory.first.may.backend.models.Sell
import factory.first.may.backend.request_models.request.Id
import factory.first.may.backend.request_models.request.SellRequest
import factory.first.may.backend.services.SellsService
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping('/findByIdSell')
    Sell findByIdSell(@RequestBody Id id) {
        sellsService.findByIdSellOrError(id.id)
    }

    @PostMapping('/create')
    Sell create(@RequestBody SellRequest sellRequest) {
        sellsService.addOne(sellRequest)
    }

    @PutMapping('/update')
    Sell update(@RequestBody SellRequest sellRequest) {
        sellsService.update(sellRequest)
    }

    @DeleteMapping('/delete')
    Sell deleteById(@RequestBody Id id) {
        sellsService.deleteById(id.id)
    }
}