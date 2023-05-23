package factory.first.may.backend.services


import factory.first.may.backend.api.core.errors.CustomAppException
import factory.first.may.backend.api.core.errors.CustomNotFoundException
import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Workshop
import factory.first.may.backend.repositories.PersonRepository
import factory.first.may.backend.request_models.request.PersonRequest
import factory.first.may.backend.request_models.request.WorkshopRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.time.Duration

@Service
class PersonService {
    @Autowired
    PersonRepository personRepository
    @Autowired
    WorkshopsService workshopService

    List findAll() {
        personRepository.findAll().asList()
        //personRepository.findAll(Sort.by('id_person')).asList()
    }

    Person findByIdOrError(long id) {
        personRepository.findById(id).orElseThrow({
            new CustomNotFoundException('Не найден пользователь с id = ' + id)
        })
    }

    Person addOne(PersonRequest personRequest) {
        Workshop workshop = workshopService.findByIdWorkshop(personRequest.idWorkshop.toInteger());
        if (workshop == null) {
            throw new CustomAppException('Не найден цех по заданному id = ' + personRequest.idWorkshop.toInteger())
        }
        if (personRequest.idPerson == null ||
                personRequest.serviceNumber == null ||
                personRequest.fullName == null ||
                personRequest.idWorkshop == null
        ) {
            throw new CustomAppException('Не заполнены обязательные поля :' +
                    'idPerson =  ' + personRequest.idPerson +
                    ' ,fullName =  ' + personRequest.fullName +
                    ' ,idWorkshop = ' + personRequest.idWorkshop +
                    ' проверьте заполние полей')
        }
        Person newPerson = new Person(
                idPerson: personRequest.idPerson,
                serviceNumber: personRequest.serviceNumber,
                fullName: personRequest.fullName,
                //dateStart: personRequest.dateStart,
                //dateEnd: personRequest.dateEnd,
                //rating: personRequest.rating,
                //birthday: personRequest.birthday,
                idWorkshop: personRequest.idWorkshop,
                workshop: workshop,
        )
        if (personRequest.dateStart != null) {
            newPerson.setDateStart(personRequest.dateStart)
        }
        if (personRequest.dateEnd != null) {
            newPerson.setDateEnd(personRequest.dateEnd)
        }
        if (personRequest.birthday != null) {
            newPerson.setBirthday(personRequest.birthday)
        }
        if (personRequest.rating != null) {
            newPerson.setRating(personRequest.rating)
        }

        personRepository.save(newPerson)
    }


    Person update(PersonRequest requestPerson) {
        Person person = findByIdOrError(requestPerson.idPerson)
        if (requestPerson.serviceNumber != null) {
            person.setServiceNumber(requestPerson.serviceNumber.toInteger())
        }
        if (requestPerson.fullName != null) {
            person.setFullName(requestPerson.fullName)
        }
        if (requestPerson.dateStart != null) {
            person.setDateStart(requestPerson.dateStart)
        }
        if (requestPerson.dateEnd != null) {
            person.setDateEnd(requestPerson.dateEnd)
        }
        if (requestPerson.birthday != null) {
            person.setBirthday(requestPerson.birthday)
        }
        if (requestPerson.rating != null) {
            person.setRating(requestPerson.rating.toInteger())
        }
        if (requestPerson.idWorkshop != null) {
            Workshop workshop = workshopService.findByIdWorkshop(requestPerson.idWorkshop.toInteger());
            if (workshop == null) {
                throw new CustomNotFoundException('Не найден юзер по заданному id = ' + requestPerson.idPerson)
            }
            person.setWorkshop(workshop)
        }
        personRepository.save(person)
    }

    Person deleteById(long id) {
        Person person = findByIdOrError(id)
        personRepository.delete(person)
        person
    }

    Person deleteByServiceNumber(long id) {
        Person person = findByServiceNumber(id.toInteger())
        personRepository.delete(person)
        person
    }

    Person findByServiceNumber(int serviceNumber) {
        Person person = personRepository.findByServiceNumber(serviceNumber);
        if (person == null) {
            throw new CustomNotFoundException('Не найден юзер по заданному id = ' + serviceNumber)
        }
        person
    }

    Person findByServiceNumberOrNull(int serviceNumber) {
        Person person = personRepository.findByServiceNumber(serviceNumber);
        person
    }

    List fillDB() {
        String dbURL = "jdbc:sqlserver://srv-intermech;databaseName=proiz;integratedSecurity=false;encrypt=true;trustServerCertificate=true;CharacterSet=UTF-8;";
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
        int count = 0;
        int countUpdate = 0;

        //MSSQL Server
        String sql = "USE proiz SELECT tab_n,fam,nam ,snm ,rab.kod_prof ,dt_rozd AS date_birthday ,dts AS date_start ,dtu AS date_dismissals, re.kod_prof, re.name_kat, pdr as pdr_id, pdr.name_pdr as pdr_name, pdr.shortname as pdr_shortname FROM [proiz].[dbo].[spr_rab] rab inner join (SELECT [kod_prof]       ,prof.[kod_kat]       ,[tarif_oklad],   kat.name_kat   FROM [proiz].[dbo].[eko_kat] prof   join eko_spr_kateg kat on kat.kod_kat=prof.kod_kat) re on rab.kod_prof = re.kod_prof inner join (select kod_pdr,name_pdr,shortname from proiz.dbo.eko_st_spr_pdr) pdr on cast(pdr.kod_pdr as Integer) = rab.pdr";

        if (conn != null) {
            java.sql.Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);



            while (result.next()) {

                String serviceNumber = result.getString(1)
                String name = (result.getString(2) + " " + result.getString(3) + " " + result.getString(4)).trim();
                String dateBirthday = result.getDate(6);
                String dateStart = result.getDate(7);
                String dateEnd = result.getDate(8);
                String idWorkshop = result.getString(11);

                //String fullname = result.getString("fullname");
                //String email = result.getString("email");

                //String output = "User #%d: %s - %s ";
                //println(String.format(output, ++count, name, pass));
                String newDateAdded = "2018-11-11 09:30:31"
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd")
                Date dateAdded = dateFormat.parse(newDateAdded)
                PersonRequest personRequest = new PersonRequest(
                        idPerson: new Integer(serviceNumber.trim()),
                        serviceNumber: new Integer(serviceNumber.trim()),
                        fullName: name.trim(),
                        dateStart: dateStart == null ? null : dateFormat.parse(dateStart),
                        dateEnd: dateEnd == null ? null : dateFormat.parse(dateEnd),
                        birthday: dateBirthday == null ? null : dateFormat.parse(dateBirthday),
                        idWorkshop: new Integer(idWorkshop.trim()),
                );
                if (findByServiceNumberOrNull(new Integer(serviceNumber.trim())) == null){
                    addOne(personRequest)
                    count++
                }else{
                    update(personRequest)
                    countUpdate++
                }


            }

        }
        println(count)
        String output = "Users processed ${count+countUpdate}, updated ${countUpdate}, created ${count} ";
        println(output);


        personRepository.findAll().asList()
        //personRepository.findAll(Sort.by('id_person')).asList()
    }

}