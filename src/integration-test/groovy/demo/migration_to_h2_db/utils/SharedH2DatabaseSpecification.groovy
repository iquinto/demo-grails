package demo.migration_to_h2_db.utils

import groovy.sql.Sql
import spock.lang.Shared
import spock.lang.Specification

/**
 *  by: iquinto
 *  configuration of tescontainers (DB) for IT
 */
abstract class SharedH2DatabaseSpecification extends Specification{
    private static final String H2_BACKUP_LOCATION = 'src/integration-test/resources/files/base.sql'
    private static final String URL = 'jdbc:h2:file:./src/integration-test/resources/db/testH2DB;MODE=PostgreSQL;AUTO_SERVER=TRUE'
    private static final String USER = 'sa'
    private static final String PASSWORD = ''
    private static final String DRIVERCLASSNAME = 'org.h2.Driver'

    @Shared Sql sql


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
        Sql sql = connectToSql()
        sql.execute("DROP ALL OBJECTS")
        sql.close()
        new File('src/integration-test/resources/db').deleteDir()
    }

    private void loadH2DB() {
        println "Loading H2 from backup location."
        sql = connectToSql()
        sql.execute("RUNSCRIPT FROM ?", [H2_BACKUP_LOCATION] as List<Object>)
    }

    private Sql connectToSql() {
        Sql.newInstance(URL, USER, PASSWORD, DRIVERCLASSNAME)
    }

}