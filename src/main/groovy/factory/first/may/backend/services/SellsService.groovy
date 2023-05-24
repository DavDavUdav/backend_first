package factory.first.may.backend.services

import factory.first.may.backend.api.core.errors.CustomAppException
import factory.first.may.backend.api.core.errors.CustomNotFoundException
import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Sell
import factory.first.may.backend.repositories.SellRepository
import factory.first.may.backend.request_models.request.GetSellsRequest
import factory.first.may.backend.request_models.request.SellRequest
import factory.first.may.backend.request_models.request.WorkshopRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.text.DateFormat
import java.text.SimpleDateFormat

@Service
// register SellsService as a Service Spring component
class SellsService {
    @Autowired
    // tell Spring to inject value from Spring component
    SellRepository sellsRepository

    @Autowired
    PersonService personService

    List<Sell> findAll(GetSellsRequest getSellsRequest) {
        if (getSellsRequest.primaryOnly != null || getSellsRequest.dateStart != null) {
            return findByPrimaryAndDate(getSellsRequest)
        }
        sellsRepository.findAll().asList()
    }

    List<Sell> findByPrimaryAndDate(GetSellsRequest getSellsRequest) {
        List<Sell> resultList = []
        //statement =con.prepareStatement("select * from sell where is_primary = ? and date_sell > ?");
        //statement.setString(1, getSellsRequest.primaryOnly);
        //statement.setString(2, getSellsRequest.dateStart);

        String dbURL = "jdbc:sqlserver://srv-intermech;databaseName=backend_first_may_dining_room_db;integratedSecurity=false;encrypt=true;trustServerCertificate=true;CharacterSet=UTF-8;";
        String username = "search";
        String password = "srh";
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        Connection conn;

        try {

            conn = DriverManager.getConnection(dbURL, username, password);

            if (conn != null) {
                System.out.println("Connected");

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String sql = 'select * from sell where is_primary = ? and date_sell > ?'

        if (conn != null) {
            java.sql.Statement statement = conn.prepareStatement(sql);

            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            // Quoted "Z" to indicate UTC, no timezone offset
            df.setTimeZone(tz);
            String nowAsISO = df.format(getSellsRequest.dateStart);
            statement.setString(1, getSellsRequest.primaryOnly);
            statement.setString(2, nowAsISO);
            ResultSet result = statement.executeQuery();

            int count = 0;

            while (result.next()) {
                resultList.add(findByIdSellOrError(result.getInt(3)))
                //Person person = personService.findByServiceNumberOrNull(result.getInt(7))
                //println result.getDate(2)
                //resultList.add(
                //        Sell(
                //                id: result.getInt(1),
                //                idSell: result.getInt(3),
                //                sum: result.getDouble(6),
                //                dateSell: result.getDate(2),
                //                person: person,
                //        )
                //)
            }
            println(count)
            String output = "Sells processed #%d ";
            println(String.format(output, ++count));
            return resultList
        }
        return []
    }


    Sell findByIdSellOrError(int id) {
        Sell sell = sellsRepository.findByIdSell(id)
        if (sell == null) throw new CustomNotFoundException('Не найдена продажа по id = ' + id)
        sell
    }

    Sell update(SellRequest sellRequest) {
        if (sellRequest.idSell == null) {
            throw new CustomNotFoundException('Не найдена продажа по id = ' + sellRequest.idSell)
        }
        Sell sell = findByIdSellOrError(sellRequest.idSell.toInteger())
        if (sell == null) throw new CustomNotFoundException('Не найдена продажа по id = ' + sellRequest.idSell)

        //Добавляем новую запись которая является дублем старой, но с пометкой
        //(isPrimary == false), что продажа не является первичной записью
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
                person: person,
                idWorkshop: person.workshop.idWorkshop
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