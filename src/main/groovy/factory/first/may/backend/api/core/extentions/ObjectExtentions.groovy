package factory.first.may.backend.api.core.extentions

import groovy.json.JsonBuilder

import groovy.transform.CompileStatic

/** Класс ObjectExtensions.groovy, который будет добавлять новые методы всем объектам:
 * Здесь мы определили четыре метода:

 * isNull - проверяет объект на null и, если он null, возвращает значение по умолчанию;
 * requireNonNull - проверяет объект на null и, если он null, кидает NullPointerException с заданным сообщением;
 * assertNonNull - проверяет объект на null и, если он null, кидает AssertionError с заданным сообщением;
 * requireNonNull - просто проверяет объект на null и, если он null, кидает NullPointerException без сообщения.*/
@CompileStatic
class ObjectExtensions {

    static <T> T isNull(T obj, T defaultVal) {
        obj ?: defaultVal
    }

    static <T> T requireNonNull(T obj, String message) {
        Objects.requireNonNull(obj, message)
    }

    static <T> T requireNonNull(T obj) {
        Objects.requireNonNull(obj)
    }

    static <T> T assertNonNull(T obj, String message) {
        assert obj != null: message
        obj
    }

    static <T> T print(T obj) {
        println new JsonBuilder(obj).toPrettyString()
        obj
    }
}