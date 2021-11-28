package com.smart.peepingbill.util;

import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.smart.peepingbill.models.impl.User;
import com.smart.peepingbill.util.constants.PeepingConstants;
import org.apache.commons.lang3.StringUtils;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Defines the code for database operations on the {@link com.smart.peepingbill.PeepingBillApplication}.
 * DBUtil utilizes the Singleton Pattern to ensure a single instance of DBUtil is accessible for a
 * single purpose.
 */
public class DBUtil implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(DBUtil.class);

    private static DBUtil instance;
    private ConnectionString connectionString;
    private MongoCollection<User> userCollection;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private DBUtil() {}

    public static DBUtil getInstance() {
        if (instance == null) {
            instance = new DBUtil();
            return instance;
        }
        return instance;
    }

    /**
     * Connects to smart Database.
     */
    public void usersDatabaseConnect() {
        LOG.info("DatabaseConnection_Attempting to connect to database [smart].");
        loadSmartDatabaseConnectionString();
        try {
            // Configure pojoCodecProvider. Automatic setting applies Codecs to any class and its properties.
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(User.class).build();

            // Add pojoCodecProvider to a CodecRegistry which encodes our POJO data.
            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

            // Configure our mongoClient, database and collection to use the Codecs in the codec registry.
            MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
                    .codecRegistry(pojoCodecRegistry)
                    .build();
            mongoClient = MongoClients.create(clientSettings);
            database = mongoClient.getDatabase(PeepingConstants.USER_DATABASE).withCodecRegistry(pojoCodecRegistry);
            userCollection = database.getCollection(PeepingConstants.USER_COLLECTION, User.class);
            LOG.info("DatabaseConnection_Connected to database ' {} '", database.getName());
        } catch (MongoClientException e) {
            LOG.error("DatabaseConnection_Unable to connect to database ' {} ' : {}", database.getName(), e.getMessage());
        }
    }

    /**
     * Inserts new user into users collection in Smart database.
     * @param user {@link User}
     * @return {@link String} of success on successful insertion, missing property if username or password
     * was not correctly filled out when creating new user or user_unavailable if user already exists.
     */
    public String insertNewUser(User user, ReasonUtil.Reason reason) {
        String isUserExist;
        StringBuilder message = new StringBuilder();
        if (StringUtils.isNotEmpty(user.getUserName()) && StringUtils.isNotEmpty(user.getPassword())) {
            isUserExist = findUser(user, reason);

            // case: user creation options: returns true if user found and false otherwise
            if (isUserExist.equals(PeepingConstants.FALSE)) {
                try {
                    userCollection.insertOne(user);
                    message.append(PeepingConstants.SUCCESS);
                    LOG.info("UserCreation_user_created ' {} ' : {}", user.getUserName(), PeepingConstants.SUCCESS);
                    return message.toString();
                } catch (MongoClientException e) {
                    LOG.info("Error connecting to database and creating user ' {} ' : {}", user.getUserName(), e.getMessage());
                }
            }
            LOG.info("UserCreation_user_unavailable for creation : action unavailable");
            return message.append(PeepingConstants.USER_UNAVAILABLE).toString();
        }
        LOG.info("UserCreation_missing_property for user creation : action unavailable");
        return message.append(PeepingConstants.MISSING_PROPERTY).toString();
    }

    /**
     * Used for login, finds user in user collection of Smart database.
     * @param user {@link User}
     * @return boolean
     */
    public String findUser(User user, ReasonUtil.Reason reason) {
        String message = PeepingConstants.FALSE;
        User findUser = userCollection.find(Filters.eq("userName", user.getUserName())).first();
        LOG.info("Searching for user ' {} ' in ' {} ' database", user.getUserName(), database.getName());

        // find user for user creation process
        if (reason.isCreateUserReason() && findUser != null) {
            return PeepingConstants.TRUE;
        } else {
            if (reason.isCreateUserReason() && findUser == null) {
                return message;
            }
        }

        // find user for user login process
        if (reason.isLoginUserReason() && findUser != null) {
            // user has been found - follow up with password match
            message = StringUtils.equals(findUser.getPassword(), user.getPassword()) ? PeepingConstants.TRUE
                    : PeepingConstants.INVALID_PASSWORD_ENTRY;
            LOG.info("LOGIN SUCCESS MESSAGE: ' {} '", message);
        }
        return message;
    }

    /**
     * Close current {@link MongoClient} connected to open Smart database.
     */
    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    /**
     * Loads application properties for mongo database connection and setup.
     */
    private void loadSmartDatabaseConnectionString() {
        Properties properties = new Properties();
        StringBuilder propertyFileStr = new StringBuilder();

        // Run with Try With resources design as Reader and BufferedReader will auto-close.
        try (Reader propertyFile = new FileReader(String.valueOf(Path.of(String.valueOf(Paths.get(PeepingConstants.APPLICATION_PROPERTIES)))));
            BufferedReader applicationPropertyReader = new BufferedReader(propertyFile)) {

                // build property file string
                propertyFileStr.append(propertyFile);
                properties.load(applicationPropertyReader);
                properties.getProperty(PeepingConstants.MONGO_CONNECTION_STRING);
                this.connectionString = new ConnectionString(properties.getProperty(PeepingConstants.MONGO_CONNECTION_STRING));
        } catch (InvalidPathException | IOException e) {
            LOG.error("Error loading given path ' {} '", propertyFileStr);
        }
    }
}
