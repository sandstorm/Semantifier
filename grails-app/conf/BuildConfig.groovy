grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenCentral()
        mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
		compile('com.hp.hpl.jena:jena:2.6.4') {
			excludes 'slf4j-log4j12', 'log4j', 'slf4j-api'
		}


		compile('ws.palladian:palladian:0.8-SNAPSHOT') {
			 excludes "log4j", "stax", "jaxen", "weka", "lucene-core", "lucene-memory", "lucene-analyzers", "rome", "lingpipe", "junit", "xml-apis", "xercesImpl", "trove", "twitter4j-core", "prefuse", "bonecp", "snakeyaml", "jwbf", "boilerpipe", "nekohtml", "mysql-connector-java", "commons-validator", "h2", "commons-configuration", "slf4j-api"
		}
		compile('commons-configuration:commons-configuration:1.6')
        // runtime 'mysql:mysql-connector-java:5.1.5'
    }

    plugins {
        compile ":hibernate:$grailsVersion"
        compile ":jquery:1.6.1.1"
        compile ":resources:1.0"

        build ":tomcat:$grailsVersion"
    }
}
