module com.smart.peepingbill {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.apache.commons.lang3;
    requires gson;
    requires org.slf4j;
    requires java.compiler;
    requires java.sql;
    requires java.annotation;
    requires org.json;
    requires unirest.java;
    requires javax.inject;
    requires org.jetbrains.annotations;
    requires itextpdf;

    opens com.smart.peepingbill to javafx.fxml;
    exports com.smart.peepingbill;
    exports com.smart.peepingbill.models.impl to org.mongodb.bson;
    exports com.smart.peepingbill.controllers;
    opens com.smart.peepingbill.controllers to javafx.fxml;
}