package factory.first.may.backend

import factory.first.may.backend.models.Workshop
import factory.first.may.backend.request_models.request.PersonRequest
import factory.first.may.backend.request_models.request.WorkshopRequest
import factory.first.may.backend.services.PersonService
import factory.first.may.backend.services.WorkshopsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import java.beans.Statement
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.time.Duration

@SpringBootApplication
class BackendApplication {

    @Autowired
    WorkshopsService workshopsService

    @Autowired
    PersonService personService

    static void main(String[] args) {
        SpringApplication.run(BackendApplication, args)
        println 'Server started'
    }
}