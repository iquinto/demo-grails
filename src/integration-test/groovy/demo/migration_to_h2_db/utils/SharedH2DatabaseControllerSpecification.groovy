package demo.migration_to_h2_db.utils


import grails.util.GrailsWebMockUtil
import groovy.sql.Sql
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
import spock.lang.Ignore
import spock.lang.Specification



/*
   iquinto: session must be mocked or inject in this TRAIT for controller IT
 */

@CompileStatic
abstract class SharedH2DatabaseControllerSpecification extends Specification{
    private static final String H2_BACKUP_LOCATION = 'src/integration-test/resources/files/base.sql'
    private static final String URL = 'jdbc:h2:file:./src/integration-test/resources/db/testH2DB;MODE=PostgreSQL;AUTO_SERVER=TRUE'
    private static final String USER = 'sa'
    private static final String PASSWORD = ''
    private static final String DRIVERCLASSNAME = 'org.h2.Driver'

    @Autowired
    WebApplicationContext ctx

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