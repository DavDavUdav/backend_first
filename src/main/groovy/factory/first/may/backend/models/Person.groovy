package factory.first.may.backend.models

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import groovyjarjarantlr4.v4.runtime.misc.NotNull
import jakarta.persistence.*


@Entity
class Person {
    @Id
    Long idPerson

    @Column(nullable = false)
    int serviceNumber

    @Column(nullable = false)
    String fullName

    @Column(nullable = true)
    Date dateStart
    @Column(nullable = true)
    Date dateEnd

    @Column(nullable = true)
    int rating

    @Column(nullable = true)
    boolean isProduction

    @Column(nullable = true)
    Date birthday

    int idWorkshop

    @NotNull
    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JsonIgnore
    Workshop workshop


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Person)) return false

        Person person = (Person) o

        if (serviceNumber != person.serviceNumber) return false

        return true
    }

    int hashCode() {
        return (serviceNumber != null ? serviceNumber.hashCode() : 0)
    }
}