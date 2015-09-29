package com.mapr.db.samples.rest;

import com.mapr.db.samples.rest.helper.MaprDBHelper;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.servlet.config.ServletScanner;
import com.wordnik.swagger.servlet.listing.ApiDeclarationServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;

public class Main {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws Exception{

        LOG.info("================================================");
        LOG.info("   Starting MapR-DB Sample Application");
        LOG.info("================================================\n\n");

        Server server = new Server(8080);


        ServletHolder sh = new ServletHolder(ServletContainer.class);
        // Set the package where the services reside
        sh.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "com.mapr.db.samples.rest.api");
        sh.setInitParameter("javax.ws.rs.Application", MaprDBHelper.class.getName());

        sh.setInitOrder(1); // force loading at startup

        initSwagger();

        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.setResourceBase(new File(Main.class.getResource("/static").toURI()).getAbsolutePath());

        context.addServlet(sh, "/api/*");
        context.addServlet(ApiDeclarationServlet.class, "/api-docs/*");
        context.addServlet(DefaultServlet.class, "/*");

        context.addServlet(sh, "/api/*");
        context.addServlet(ApiDeclarationServlet.class, "/api-docs/*");
        context.addServlet(DefaultServlet.class, "/*");

        server.start();

        LOG.info("================================================");
        LOG.info("   Application Links");
        LOG.info("================================================\n\n");
        LOG.info("> http://localhost:8080/ \t\t\t(Web Application)");
        LOG.info("> http://localhost:8080/swagger \t\t(REST API)");

        server.join();




    }


    private static void initSwagger() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setBasePath("http://localhost:8080/api");
        ServletScanner scanner = new ServletScanner();
        scanner.setResourcePackage("com.mapr.db.samples.rest.api");
        ScannerFactory.setScanner(scanner);
    }


}
