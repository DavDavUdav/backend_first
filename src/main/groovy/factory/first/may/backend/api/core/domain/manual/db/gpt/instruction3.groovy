package factory.first.may.backend.api.core.domain.manual.db.gpt
/**Класс для обращения ко второй базе данных выглядит следующим образом:

 ```java
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.annotation.Qualifier;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.stereotype.Component;

 @Component
  public class SecondDatabaseDao {

  private final JdbcTemplate jdbcTemplate;

 @Autowired
  public SecondDatabaseDao(@Qualifier("secondJdbcTemplate") JdbcTemplate jdbcTemplate) {
  this.jdbcTemplate = jdbcTemplate;
  }

  // Запрос к базе данных
  public void someQuery() {
  jdbcTemplate.query("SELECT * FROM some_table", rs -> {
  // обработка данных
  });
  }

  }
  ```

  В данном примере используется `JdbcTemplate` для выполнения запросов к базе данных.

  Пример вызова из main:

  ```java
  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.context.ApplicationContext;

 @SpringBootApplication
  public class MyApp {

  public static void main(String[] args) {
  ApplicationContext context = SpringApplication.run(MyApp.class, args);

  SecondDatabaseDao secondDao = context.getBean(SecondDatabaseDao.class);
  secondDao.someQuery();
  }

  }
  ```

  В данном примере создается экземпляр класса `SecondDatabaseDao`
  с помощью контейнера внедрения зависимостей Spring.
  Затем вызывается метод `someQuery()`, который выполнит запрос к базе данных.
*/