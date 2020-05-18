package demo.testcontainers.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import grails.util.GrailsWebMockUtil
import groovy.transform.CompileStatic
import org.grails.plugins.testing.GrailsMockHttpServletRequest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Ignore
import spock.lang.Specification

import javax.sql.DataSource
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

/*
   iquinto: session must be mocked or inject in this TRAIT for controller IT
 */
@CompileStatic
abstract class BaseControllerIntegrationSpecification extends Specification{


    private static final Set<HikariDataSource> datasourcesForCleanup = new HashSet<>()
    private final static String DB_QUERY = new File('src/integration-test/resources/files/base.sql').text

    protected static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer()
            .withDatabaseName("demo")
            .withUsername("isagani")
            .withPassword("isagani")

    /**
     *  by: iquinto
     *  is executed at the beginning of every test class
     *  instantiate container, create connection and migrate database
     */
    def setupSpec() {
        startPostgresIfNeeded()
        ['JDBC_CONNECTION_URL' : POSTGRES.getJdbcUrl(),
         'JDBC_CONNECTION_USER': POSTGRES.getUsername(),
         'JDBC_CONNECTION_PASSWORD': POSTGRES.getPassword(),
         'JDBC_CONNECTION_DRIVER': POSTGRES.getDriverClassName(),
         'JDBC_CONNECTION_DIALECT': 'org.hibernate.dialect.PostgresSQLDialect'   // for postgres Database
        ].each { k, v ->
            System.setProperty(k, v)
        }

        migrateDatabase()
    }

    /**
     *  by: iquinto
     *  is executed at the end of every test class
     *
     */
    def cleanupSpec() {
        datasourcesForCleanup.each {
            println "[BASE-INTEGRATION-TEST] - Closing ${it.getJdbcUrl()}..."
            it.close()
        }

        /* This is to stop the testcontainer instance
            if (POSTGRES.isRunning()) {
                println "[BASE-INTEGRATION-TEST] - Stopping Postgres..."
                POSTGRES.stop()
            }
        */
    }

    private static void startPostgresIfNeeded() {
        if (!POSTGRES.isRunning()) {
            println "[BASE-INTEGRATION-TEST] - Postgres is not started. Running..."
            POSTGRES.start()
        }

    }

    protected void migrateDatabase() throws SQLException {
        println"[BASE-INTEGRATION-TEST] - Migrating DataBase..."
        DataSource ds = getDataSource(POSTGRES)
        Statement statement = ds.getConnection().createStatement()
        statement.execute(DB_QUERY)
    }

    /**
     * by: iquinto
     * @param query
     * @throws SQLException
     * methotd to run a native sql
     * we can use this method during test execution
     */
    void executeQuery(String query) throws SQLException {
        DataSource ds = getDataSource(POSTGRES)
        Statement statement = ds.getConnection().createStatement()
        statement.execute(query)
    }

    ResultSet performQueryResult(String sql) throws SQLException {
        DataSource ds = getDataSource(POSTGRES);
        Statement statement = ds.getConnection().createStatement()
        statement.execute(sql)
        ResultSet resultSet = statement.getResultSet()
        resultSet.next()
        return resultSet
    }

    /**
     * by: iquinto
     * @param container
     * @return
     * HikariConfig plugin
     * create connection from a given DB container
     */
    DataSource getDataSource(JdbcDatabaseContainer container) {
        HikariConfig hikariConfig = new HikariConfig()
        hikariConfig.setJdbcUrl(container.getJdbcUrl())
        hikariConfig.setUsername(container.getUsername())
        hikariConfig.setPassword(container.getPassword())
        hikariConfig.setDriverClassName(container.getDriverClassName())
        final HikariDataSource dataSource = new HikariDataSource(hikariConfig)
        datasourcesForCleanup.add(dataSource)
        return dataSource
    }

    @Autowired
    WebApplicationContext ctx

    void setup() {
        MockHttpServletRequest request = new GrailsMockHttpServletRequest(ctx.servletContext)
        MockHttpServletResponse response = new GrailsMockHttpServletResponse()
        GrailsWebMockUtil.bindMockWebRequest(ctx, request, response)
        currentRequestAttributes.setControllerName(controllerName)
    }

    @Ignore
    abstract String getControllerName()

    @Ignore
    GrailsWebRequest getCurrentRequestAttributes() {
        return (GrailsWebRequest) RequestContextHolder.currentRequestAttributes()
    }

    void cleanup() {
        RequestContextHolder.resetRequestAttributes()
    }

    @Ignore
    def autowire(Class clazz) {
        def bean = clazz.newInstance()
        ctx.autowireCapableBeanFactory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false)
        bean
    }


    @Ignore
    def autowire(def bean) {
        ctx.autowireCapableBeanFactory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false)
        bean
    }



}