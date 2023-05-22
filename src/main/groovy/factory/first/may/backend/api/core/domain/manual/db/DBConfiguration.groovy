/**package factory.first.may.backend.api.core.domain.manual.db

 import factory.first.may.backend.request_models.request.PersonRequest
 import org.springframework.beans.factory.annotation.Autowired
 import org.springframework.context.annotation.Bean
 import org.springframework.context.annotation.Configuration
 import org.springframework.jdbc.core.JdbcTemplate
 import org.springframework.jdbc.core.RowMapper
 import org.springframework.jdbc.datasource.DriverManagerDataSource

 import javax.sql.DataSource
 import java.sql.ResultSet
 import java.sql.SQLException


 /**
 Для создания запроса к БД в Spring Framework для парсинга данных необходимо использовать JdbcTemplate,
 который предоставляет удобный интерфейс для выполнения SQL запросов и маппинга результатов.
 Для работы с базой данных MSSQL создаем конфигурационный класс,
 который настроит подключение к базе данных:
 */
/**
 @Configuration
  class DBConfiguration {@Bean
  DataSource dataSource() {
  DriverManagerDataSource dataSource = new DriverManagerDataSource()
  dataSource.setUrl("jdbc:sqlserver://srv-intermech;databaseName=proiz")
  dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
  dataSource.setUsername("search")
  dataSource.setPassword("srh")
  print 'connect active'
  return dataSource
  }

 @Autowired
  private JdbcTemplate jdbcTemplate

  /** В этом примере мы используем DriverManagerDataSource для хранения настроек подключения к MSSQL
  базе данных. Вы можете изменить параметры подключения в соответствии с вашей конфигурацией.
  После этого можно создавать запросы к базе данных. Например, для выборки всех записей из таблицы users: */
/**
 static List<PersonRequest> getAllUsers() {
 JdbcTemplate jdbcTemplate = new JdbcTemplate()
 jdbcTemplate.setDataSource(DBConfiguration.dataSource())
 String sql = "SELECT tab_n," +
 "(select fam+' ' + nam+' '+snm) as fio," +
 "nam" +
 ",snm" +
 ",rab.kod_prof" +
 ",dt_rozd AS date_birthday" +
 ",dts AS date_start" +
 ",dtu AS date_dismissals," +
 "re.kod_prof," +
 "re.name_kat," +
 "pdr as pdr_id," +
 "pdr.name_pdr as pdr_name," +
 "pdr.shortname as pdr_shortname" +
 "FROM spr_rab rab" +
 "inner join (SELECT [kod_prof]" +
 "      ,prof.[kod_kat]" +
 "      ,[tarif_oklad]," +
 "  kat.name_kat" +
 "  FROM [proiz].[dbo].[eko_kat] prof" +
 "  join eko_spr_kateg kat on kat.kod_kat=prof.kod_kat) re" +
 "on rab.kod_prof = re.kod_prof" +
 "inner join (select kod_pdr,name_pdr,shortname from proiz.dbo.eko_st_spr_pdr) pdr on cast(pdr.kod_pdr as Integer) = rab.pdr"
 println sql
 List<PersonRequest> users = jdbcTemplate.query(sql, new PersonRowMapper())
 users.each { key, value ->
 println value[key]
 }

 return users
 }
 }


 /**
 В этом примере мы используем метод query,
 который выполняет SQL запрос к базе данных и возвращает список объектов,
 полученных из записей таблицы. Также мы передаем UserRowMapper,
 который сопоставляет результаты запроса с объектами User.
 UserRowMapper:
 */
/**
 class PersonRowMapper implements RowMapper<PersonRequest> {

 PersonRequest mapRow(ResultSet resultSet, int rowNum) throws SQLException {
 PersonRequest person = new PersonRequest()
 person.setIdPerson(resultSet.getLong("tab_n"))
 person.setServiceNumber(resultSet.getLong("tab_n").toInteger())
 person.setFullName(resultSet.getString("fio").trim())
 person.setDateStart(resultSet.getDate("date_start"))
 person.setDateEnd(resultSet.getDate("date_dismissals"))
 person.setIdWorkshop(resultSet.getLong("idWorkshop"))
 return person
 }
 }

 */