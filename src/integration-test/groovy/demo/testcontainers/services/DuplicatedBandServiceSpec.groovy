package demo.testcontainers.services

import demo.Band
import demo.BandService
import demo.testcontainers.utils.BaseContainerSpecification
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.hibernate.SessionFactory
import org.testcontainers.spock.Testcontainers
import spock.lang.IgnoreIf

@IgnoreIf({ System.getProperty('geb.env') })
@Integration
@Rollback
@Testcontainers
class DuplicatedBandServiceSpec extends BaseContainerSpecification {

    BandService bandService
    SessionFactory sessionFactory

    void "test list objects "() {
        when:
        List<Band> bandList = Band.findAll()

        then:
        println "A ===========> ${Band.findAll().name.sort()}"
        bandList.size() > 0
    }


    void "test create"() {
        given:
        String str = "A NEW_BAND"

        when:
        bandService.save(new Band(name: str , yearFormed: "1998", yearDissolution: "2018", style: "Rock", origin: "US"))
        sessionFactory.currentSession.flush()

        then:
        println "B ===========> ${Band.findAll().name.sort()}"
        bandService.count() == old (Band.count()) + 1

    }

    void "test list objects after saving and the  object must not persist"() {
        when:
        List<Band> bandList = Band.findAll()

        then:
        println "C ===========> ${Band.findAll().name.sort()}"
        bandList.size() > 0
    }

    void "test modify and verify an object"() {
        given:
        println "D.1 ===========> ${Band.findAll().name.sort()}"
        Band band = Band.findByName("Bad Company")

        when:
        band.name = "A NEW NAME"
        bandService.save(band)
        sessionFactory.currentSession.flush()

        then:
        println "D.2 ===========> ${Band.findAll().name.sort()}"
        band.name == "A NEW NAME"
    }

    void "test list objects after updating and the  object must not persist"() {
        when:
        List<Band> bandList = Band.findAll()

        then:
        println "E ===========> ${Band.findAll().name.sort()}"
        bandList.size() > 0
    }

}
