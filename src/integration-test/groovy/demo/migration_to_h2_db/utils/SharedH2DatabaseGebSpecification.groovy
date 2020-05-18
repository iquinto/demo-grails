package demo.migration_to_h2_db.utils


import geb.spock.GebReportingSpec
import groovy.sql.Sql
import org.testcontainers.spock.Testcontainers

/**
 *  by: iquinto
 *  configuration of tescontainers (DB) for IT
 */
@Testcontainers
abstract class SharedH2DatabaseGebSpecification extends GebReportingSpec{
    private static final String H2_BACKUP_LOCATION = 'src/integration-test/resources/files/h2.sql'
    private static final String URL = 'jdbc:h2:file:./src/integration-test/resources/files/testH2DB;AUTO_SERVER=TRUE'
    private static final String USER = 'sa'
    private static final String PASSWORD = ''
    private static final String DRIVERCLASSNAME = 'org.h2.Driver'

    void setupSpec() {
        loadH2DB()
        ['JDBC_CONNECTION_URL' : URL,
         'JDBC_CONNECTION_USER': USER,
         'JDBC_CONNECTION_PASSWORD': PASSWORD,
         'JDBC_CONNECTION_DRIVER': DRIVERCLASSNAME
        ].each { k, v ->
            println "Datasource >>>> $k :  $v"
            System.setProperty(k, v)
        }
    }

    void cleanupSpec() {
        println "Cleaning  H2 database."
        Sql sql = connectToSql()
        sql.execute("DROP ALL OBJECTS")
    }

    private void loadH2DB() {
        println "Loading H2 from backup location."
        Sql sql = connectToSql()
        sql.execute("RUNSCRIPT FROM ?", [H2_BACKUP_LOCATION] as List<Object>)
    }

    private Sql connectToSql() {
        Sql.newInstance(URL, USER, PASSWORD, DRIVERCLASSNAME)
    }

}