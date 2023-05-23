package factory.first.may.backend.services

import factory.first.may.backend.api.core.errors.CustomAppException
import factory.first.may.backend.api.core.errors.CustomNotFoundException
import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Sell
import factory.first.may.backend.repositories.SellRepository
import factory.first.may.backend.request_models.request.SellRequest
import org.springframework.beans.factory.annotation.Autowired
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
        sellsRepository.findAll().asList()
    }

    Sell findByIdSellOrError(int id) {
        Sell sell = sellsRepository.findByIdSell(id)
        if (sell == null) throw new CustomNotFoundException('Не найдена продажа по id = ' + id)
        sell
    }

    Sell update(SellRequest sellRequest) {
        Sell sell = findByIdSellOrError(sellRequest.idSell.toInteger())
        if (sell == null) throw new CustomNotFoundException('Не найдена продажа по id = ' + sellRequest.idSell)

        //Добавляем новую запись которая является дублем старой, но с пометкой
        //(isPrimary == false), что он не является первичной записью
        addOneSecondary(sell)
        if (sellRequest.idSell != null) {
            sell.setId(sellRequest.idSell)
            sell.setIdSell(sellRequest.idSell.toInteger())
        }
        if (sellRequest.sum != null)
            sell.setSum(sellRequest.sum)
        if (sellRequest.dateSell != null)
            sell.setDateSell(sellRequest.dateSell)
        if (sellRequest.idPerson != null) {
            Person newPerson = personService.findByServiceNumber(sellRequest.idPerson.toInteger())
            if (newPerson == null) throw new CustomNotFoundException('Не найден person id = ' + sellRequest.idPerson)
            sell.setPerson(newPerson)
        }
        sellsRepository.save(sell)
    }

    Sell addOne(SellRequest sell) {
        if (sell.sum == null ||
                sell.dateSell == null ||
                sell.idPerson == null) {
            throw new CustomAppException('Заполнены не все обязательные поля sum,dateSell,idPerson')
        }
        Person person = personService.findByServiceNumber(sell.idPerson.toInteger())
        if (person == null) throw new CustomNotFoundException('Не найден person id = ' + sell.idPerson)
        Sell newSell = new Sell(
                sum: sell.sum,
                dateSell: sell.dateSell,
                isPrimary: true,
                person: person
        )
        //Если есть idSell, то назначаеме его, иначе создаем запись и задаем idSell == id
        if (sell.idSell != null) {
            newSell.setIdSell(sell.idSell.toInteger())
            sellsRepository.save(newSell)
        } else {
            Sell afterSaveSell = sellsRepository.save(newSell)
            afterSaveSell.setIdSell(afterSaveSell.id.toInteger())
            sellsRepository.save(afterSaveSell)
        }
    }

    Sell addOneSecondary(Sell sell) {
        Sell newSell = new Sell(
                id: sell.id,
                idSell: sell.idSell,
                sum: sell.sum,
                dateSell: sell.dateSell,
                primarySellId: sell.idSell,
                isPrimary: false,
                person: sell.person
        )
        newSell.setIsPrimary(false)
        sellsRepository.save(newSell)
    }

    Sell deleteById(long id) {
        Sell sell = findByIdSellOrError(id.toInteger())
        sellsRepository.delete(sell)
        sell
    }
}