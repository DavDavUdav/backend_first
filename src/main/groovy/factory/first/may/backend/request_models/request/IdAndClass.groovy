package factory.first.may.backend.request_models.request

import factory.first.may.backend.models.Person
import factory.first.may.backend.models.Sell
import factory.first.may.backend.models.Workshop

abstract class IdAndClass {
    int id;
    Object object;

}

class IdAndSell extends IdAndClass {
    public int id;
    public SellRequest sellRequest;
}

class IdAndPerson extends IdAndClass {
    int id;
    Person person;
}

class IdAndWorkshop extends IdAndClass {
    int id;
    Workshop workshop;
}