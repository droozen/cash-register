package com.roozen.register.main;

import com.roozen.register.service.ItemLoader;
import com.roozen.register.service.SqlService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ClientTest {

    Client client;

    @Before
    public void setUp() {
        client = new Client();
    }

    @Test
    public void testInit() throws Exception {
        // SETUP
        client.setAction("init");

        SqlService mockService = mock(SqlService.class);
        client.sqlService = mockService;

        // EXECUTE
        client.execute();

        // VERIFY
        verify(mockService).init();
    }

    @Test
    public void testItems() throws Exception {
        final String testFileSource = "testFileSource";

        // SETUP
        client.setAction("items");
        client.setFile(testFileSource);

        ItemLoader mockLoader = mock(ItemLoader.class);
        client.itemLoader = mockLoader;

        // EXECUTE
        client.execute();

        // VERIFY
        verify(mockLoader).loadItems(testFileSource, true);
    }

    @Test
    public void testItems_noHeader() throws Exception {
        final String testFileSource = "testFileSource";

        // SETUP
        client.setAction("items");
        client.setFile(testFileSource);
        client.setIncludeHeader(false);

        ItemLoader mockLoader = mock(ItemLoader.class);
        client.itemLoader = mockLoader;

        // EXECUTE
        client.execute();

        // VERIFY
        verify(mockLoader).loadItems(testFileSource, false);
    }
}
