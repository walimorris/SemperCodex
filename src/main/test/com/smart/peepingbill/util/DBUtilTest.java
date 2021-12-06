package com.smart.peepingbill.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.smart.peepingbill.models.impl.User;
import com.smart.peepingbill.util.constants.PeepingConstants;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@RunWith(MockitoJUnitRunner.class)
class DBUtilTest {
    private static final Logger LOG = LoggerFactory.getLogger(DBUtilTest.class);

    private static final String USER_TEST_DATABASE = "test";
    private static final String USER_TEST_COLLECTION = "userTest";
    private static final String USER_TEST_COLLECTION_NAMESPACE = "test.userTest";

    private static MongoClient mongoClient;
    private static ConnectionString connectionString;
    private static MongoDatabase database;
    private static MongoCollection<User> userTestCollection;

    @BeforeAll
    static void setUp() {
        Properties properties = new Properties();
        StringBuilder propertyFileStr = new StringBuilder();

        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(User.class).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

        try (Reader propertyFile = new FileReader(String.valueOf(Path.of(String.valueOf(Paths.get(PeepingConstants.APPLICATION_PROPERTIES)))));
             BufferedReader applicationPropertyReader = new BufferedReader(propertyFile)) {

            // build property file string
            propertyFileStr.append(propertyFile);
            properties.load(applicationPropertyReader);
            properties.getProperty(PeepingConstants.MONGO_CONNECTION_STRING);
            connectionString = new ConnectionString(properties.getProperty(PeepingConstants.MONGO_CONNECTION_STRING));
        } catch (InvalidPathException | IOException e) {
            System.out.println(e.getMessage());
        }

        // Test DB should take User Object
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToSslSettings(builder -> builder.enabled(true))
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(USER_TEST_DATABASE).withCodecRegistry(pojoCodecRegistry);
    }

    @AfterAll
    static void tearDownAll() {
        // We should remove all test user creations in database
        User testUser = new User(PeepingConstants.TEST_USER_NAME_SUCCESS_1, PeepingConstants.TEST_PASS_SUCCESS_1);
        userTestCollection = database.getCollection(USER_TEST_COLLECTION, User.class);
        userTestCollection.findOneAndDelete(Filters.eq("userName", testUser.getUserName()));

        // close client
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Test
    void getInstance() {
        Assertions.assertNotNull(DBUtil.getInstance());
        DBUtil.getInstance().close();
    }
    @Test
    void usersDatabaseConnect() {
        // Test database is connected
        userTestCollection = database.getCollection(USER_TEST_COLLECTION, User.class);
        Assertions.assertEquals(USER_TEST_DATABASE, database.getName());
        Assertions.assertEquals(userTestCollection.getNamespace().toString(), USER_TEST_COLLECTION_NAMESPACE);
    }

    @Test
    void insertNewUser() {
        // insert users in Test DB
        User testUser = new User(PeepingConstants.TEST_USER_NAME_SUCCESS_1, PeepingConstants.TEST_PASS_SUCCESS_1);

        userTestCollection = database.getCollection(USER_TEST_COLLECTION, User.class);
        User result = userTestCollection.find(Filters.eq("userName", testUser.getUserName())).first();
        ReasonUtil.Reason reason = ReasonUtil.getCreateUserReason(); 
        String success = null; 
        
        // reason should be a 'create user' reason and null to insert new user
        if (reason.isCreateUserReason() && result != null) {
            success = PeepingConstants.TRUE;
        } else {
            if (reason.isCreateUserReason() && result == null) {
                success = PeepingConstants.FALSE;
            }
        }
        // insert user
        Assertions.assertEquals(success, PeepingConstants.FALSE);
        userTestCollection.insertOne(testUser);

        // retest that user is found after insertion
        User finalResult = userTestCollection.find(Filters.eq("userName", testUser.getUserName())).first();
        Assertions.assertNotNull(finalResult);
    }

    @Test
    void findUser() {}

    @Test
    void close() {}
}