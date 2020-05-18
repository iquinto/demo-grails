package demo.testcontainers.services

import demo.Band
import demo.BandService
import demo.testcontainers.utils.BaseContainerSpecification
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

import org.hibernate.SessionFactory
import org.testcontainers.spock.Testcontainers
import spock.lang.IgnoreIf

@IgnoreIf({ System.getProperty('geb.env') })
@Integration
@Rollback
@Testcontainers
class BandServiceSpec extends BaseContainerSpecification {

    BandService bandService
    SessionFactory sessionFactory

    void "test count objects in DB"() {
        when:
        List<Band> bandList = Band.findAll()

        then:
        println "TEST 1 Names : ${bandList.name}"
        bandList.size() > 0
    }

    void "test get object in DB"() {
        when:
        Band band = Band.findByName("Metallica")

        then:
        println "TEST 2  Name : ${band.name}"
        band != null
    }

    void "test create and modify an object"() {
        given:
        String str = "Bon Jovi"

        when:
        bandService.save(new Band(name: str , yearFormed: "1998", yearDissolution: "2018", style: "Rock", origin: "US"))
        sessionFactory.currentSession.flush()

        then:
        bandService.count() == old (Band.count()) + 1

        when:
        def b = Band.findByName(str)
        b.name = "HOLA"
        bandService.save(b)
        sessionFactory.currentSession.flush()

        then:
        println "TEST 3  Name : ${b.name}"
        b.name == "HOLA"
    }

    void "test delete"() {
        given:
        println " TEST 4  Before delete names : ${Band.findAll().name}"
        Long bandId = bandService.get(1).id

        when:
        bandService.delete(bandId)
        sessionFactory.currentSession.flush()

        then:
        println " TEST 4  After delete names : ${Band.findAll().name}"
        bandService.count() == old(Band.count()) - 1
    }


}
