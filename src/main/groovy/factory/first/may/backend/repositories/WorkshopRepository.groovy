package factory.first.may.backend.repositories


import factory.first.may.backend.models.Workshop
import org.springframework.data.repository.CrudRepository

interface WorkshopRepository extends CrudRepository<Workshop, Long> {

    Workshop findByIdWorkshop(int idWorkshop);
}