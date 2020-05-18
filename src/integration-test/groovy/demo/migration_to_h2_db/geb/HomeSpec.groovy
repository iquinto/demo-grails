package demo.migration_to_h2_db.geb

import demo.migration_to_h2_db.utils.SharedH2DatabaseGebSpecification
import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Requires

@Integration
@Requires({ sys['geb.env'] })
@Slf4j
class HomeSpec extends SharedH2DatabaseGebSpecification {


    def 'visit me'() {
        when:
        go '/billboard'

        then:
        true
    }


}
