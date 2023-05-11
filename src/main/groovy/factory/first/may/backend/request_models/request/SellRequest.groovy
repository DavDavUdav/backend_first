package factory.first.may.backend.request_models.request

import com.fasterxml.jackson.annotation.JsonInclude

class SellRequest {
    int oldSellId

    Long id

    int idSell

    double sum

    Date dateSell

    boolean isPrimary = true

    @JsonInclude(JsonInclude.Include.NON_NULL)
    int primarySellId

    int idPerson
}