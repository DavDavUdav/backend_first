package factory.first.may.backend.services

import factory.first.may.backend.api.errors.CustomAppException
import factory.first.may.backend.api.errors.CustomNotFoundException
import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Sell
import factory.first.may.backend.repositories.SellRepository
import factory.first.may.backend.request_models.request.SellRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
// register SellsService as a Service Spring component
class SellsService {
    @Autowired
    // tell Spring to inject value from Spring component
    SellRepository sellsRepository

    @Autowired
    PersonService personService

    List<Sell> findAll() {
        sellsRepository.findAll(Sort.by(Sort.Order.desc('dateSell')) as Closure)
    }

    Sell findByIdSell(int id) {
        Sell sell = sellsRepository.findByIdSell(id)
        if (sell == null) throw new CustomNotFoundException('Не найдена продажа по id = ' + id)
        sell
    }

    Sell update(SellRequest sellRequest) {
        Sell sell = findByIdSell(sellRequest.oldSellId.toInteger())
        if (sell == null) throw new CustomNotFoundException('Не найдена продажа по id = ' + sellRequest.oldSellId);
        if (sellRequest.idPerson == null) throw new CustomNotFoundException('Не найден person id = ' + sellRequest.oldSellId);
        Person newPerson = personService.findByIdOrError(sellRequest.idPerson)
        //Добавляем новую запись которая является дублем старой, но с пометкой
        //(isPrimary == false), что он не является первичной записью
        addOneSecondary(sell)
        Sell newSell = new Sell(
                id: sellRequest.id,
                idSell: sellRequest.idSell,
                sum: sellRequest.sum,
                isPrimary: true,
                dateSell: sellRequest.dateSell,
                person: newPerson,
        )
        sell.with { newSell }
        sellsRepository.save(sell)
    }

    Sell addOne(Sell sell) {
        if (sell.idSell == null ||
                sell.sum == null ||
                sell.isPrimary == null ||
                sell.dateSell == null ||
                sell.person == null) {
            throw new CustomAppException('Заполнены не все обязательные поля idSell,sum,isPrimary,dateSell,person')
        }
        Sell newPerson = new Sell(
                id: sell.id,
                idSell: sell.idSell,
                sum: sell.sum,
                dateSell: sell.dateSell,
                isPrimary: sell.isPrimary,
                person: sell.person
        )
        sellsRepository.save(newPerson)
    }

    Sell addOneSecondary(Sell sell) {
        Sell newPerson = new Sell(
                id: sell.id,
                idSell: sell.idSell,
                sum: sell.sum,
                dateSell: sell.dateSell,
                primarySellId: sell.idSell,
                isPrimary: false,
                person: sell.person
        )
        sellsRepository.save(newPerson)
    }

    Sell deleteById(long id) {
        Sell sell = findByIdSell(id.toInteger())
        sellsRepository.delete(sell)
        sell
    }
}