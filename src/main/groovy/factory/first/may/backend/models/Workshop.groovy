package factory.first.may.backend.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne


@Entity
// tell persistence provider 'Workshop' is a persistence entity
class Workshop {
    @JsonIgnore
    @Id
    // tell persistence provider 'id' is primary key
    //@GeneratedValue( // tell persistence provider that value of 'id' will be generated
    //        strategy = GenerationType.IDENTITY // use RDBMS unique id generator
    //)
    Long id

    int idWorkshop

    String name

    String shortName
}