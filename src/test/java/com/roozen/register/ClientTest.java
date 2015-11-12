package com.roozen.register;

import com.roozen.register.init.ItemLoader;
import com.roozen.register.init.SqlLoader;
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

        SqlLoader mockService = mock(SqlLoader.class);
        client.sqlLoader = mockService;

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
