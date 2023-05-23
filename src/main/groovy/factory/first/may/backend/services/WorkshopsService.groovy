package factory.first.may.backend.services

import factory.first.may.backend.api.core.errors.CustomAppException
import factory.first.may.backend.api.core.errors.CustomNotFoundException
import factory.first.may.backend.models.Workshop
import factory.first.may.backend.repositories.WorkshopRepository
import factory.first.may.backend.request_models.request.WorkshopRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.time.Duration

@Service
// register WorkshopService as a Service Spring component
class WorkshopsService {
    @Autowired
    // tell Spring to inject value from Spring component
    WorkshopRepository workshopRepository

    List findAll() {
        return workshopRepository.findAll().asList()
    }

    Workshop addOne(WorkshopRequest workshopRequest) {
        if (workshopRequest.idWorkshop == null ||
                workshopRequest.name == null ||
                workshopRequest.shortName == null) {
            throw new CustomAppException('Не заполнены обязательные поля: ' +
                    'idWorkshop = '+workshopRequest.idWorkshop+',' +
                    ' name = '+workshopRequest.name+',' +
                    ' shortName = '+workshopRequest.shortName+', ' +
                    'проверьте заполнение полей ')
        }
        Workshop workshop = new Workshop(
                id: workshopRequest.idWorkshop,
                idWorkshop: workshopRequest.idWorkshop,
                name: workshopRequest.name,
                shortName: workshopRequest.shortName
        )

        workshopRepository.save(workshop)
        workshop
    }

    Workshop findByIdWorkshop(int id) {
        Workshop workshop = workshopRepository.findByIdWorkshop(id)
        if (workshop == null) {
            throw new CustomNotFoundException('Не найден цех по заданному id = '+id)
        }
        workshop
    }

    Workshop deleteById(int id) {
        Workshop workshop = findByIdWorkshop(id)
        if (workshop == null) {
            throw new CustomNotFoundException('Не найден цех по заданному id = '+id)
        }
        workshopRepository.delete(workshop)
        workshop
    }

    Workshop update(WorkshopRequest workshopRequest) {
        Workshop workshop = findByIdWorkshop(workshopRequest.idWorkshop)
        if (workshop == null) throw new CustomNotFoundException('Не найден цех по id = ' + workshopRequest.idWorkshop);
        workshop.setName(workshopRequest.name)
        workshop.setShortName(workshopRequest.shortName)

        workshopRepository.save(workshop)
        workshop
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

        String sql = 'select kod_pdr,name_pdr,shortname from proiz.dbo.eko_st_spr_pdr'

        if (conn != null) {
            java.sql.Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);

            int count = 0;

            while (result.next()) {
                String idWorkshop = result.getString(1);
                String name = result.getString(2);
                String shortName = result.getString(3);

                WorkshopRequest workshopRequest = new WorkshopRequest(
                        idWorkshop: new Integer(idWorkshop.trim()),
                        name: name,
                        shortName: shortName
                );
                addOne(workshopRequest);
            }
            println(count)
            String output = "Workshops processed #%d ";
            println(String.format(output, ++count));
        }
        workshopRepository.findAll().asList()
        //personRepository.findAll(Sort.by('id_person')).asList()
    }
}
