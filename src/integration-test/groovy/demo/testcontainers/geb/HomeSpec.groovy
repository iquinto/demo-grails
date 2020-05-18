package demo.testcontainers.geb

import demo.testcontainers.utils.GebSpecification
import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Requires

@Integration
@Requires({ sys['geb.env'] })
@Slf4j
class HomeSpec extends GebSpecification {


    def 'visit me'() {
        when:
        go '/billboard'

        then:
        true
    }


}
