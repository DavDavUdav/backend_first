package factory.first.may.backend.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class Sells {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idSell

    double sum

    Date dateSell

    boolean isPrimary = false

    @ManyToOne( // tell persistence provider 'person' is many-to-many relation with Sells
            fetch = FetchType.EAGER // always fetch values when Disaster entity is loaded
    )
    Person person
}
