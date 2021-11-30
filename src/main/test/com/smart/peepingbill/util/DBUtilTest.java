package com.smart.peepingbill.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.smart.peepingbill.models.impl.User;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class DBUtilTest {

    @Mock DBUtil testDB;
    @Mock MongoClient mongoClient;
    @Mock MongoDatabase mongoDatabase;
    @Mock MongoCollection<?> mongoCollection;
    @Mock CodecProvider codecProvider;
    @Mock CodecRegistry codecRegistry;
    @Mock CodecRegistries registries;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    // Database active, close.
    void tearDown() {
        DBUtil.getInstance().close();
    }

    @Test
    void getInstance() {
        Assertions.assertNotNull(DBUtil.getInstance());
        DBUtil.getInstance().close();
    }
    @Test
    void usersDatabaseConnect() {
        try (MockedStatic<PojoCodecProvider> provider = Mockito.mockStatic(PojoCodecProvider.class)) {
            provider.when(() -> PojoCodecProvider.builder().register(User.class).build()).thenReturn(codecProvider);
        }

    }

    @Test
    void insertNewUser() {
    }

    @Test
    void findUser() {
    }

    @Test
    void close() {
    }
}