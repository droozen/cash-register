package com.roozen.register.main;

import com.roozen.register.service.ItemLoader;
import com.roozen.register.service.SqlService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.eq;

public class ClientTest {

    Client client;

    @Before
    public void setUp() {
        client = new Client();
    }

    @Test
    public void testInit() throws Exception {
        client.setAction("init");

        SqlService mockService = EasyMock.createMock(SqlService.class);
        mockService.init();
        client.sqlService = mockService;

        EasyMock.replay(mockService);

        // EXECUTE
        client.execute();

        // VERIFY
        EasyMock.verify(mockService);
    }

    @Test
    public void testItems() throws Exception {
        final String testFileSource = "testFileSource";
        client.setAction("items");
        client.setFile(testFileSource);

        ItemLoader mockLoader = EasyMock.createMock(ItemLoader.class);
        mockLoader.loadItems(eq(testFileSource), eq(true));
        client.itemLoader = mockLoader;

        EasyMock.replay(mockLoader);

        // EXECUTE
        client.execute();

        // VERIFY
        EasyMock.verify(mockLoader);
    }

    @Test
    public void testItems_noHeader() throws Exception {
        final String testFileSource = "testFileSource";
        client.setAction("items");
        client.setFile(testFileSource);
        client.setIncludeHeader(false);

        ItemLoader mockLoader = EasyMock.createMock(ItemLoader.class);
        mockLoader.loadItems(eq(testFileSource), eq(false));
        client.itemLoader = mockLoader;

        EasyMock.replay(mockLoader);

        // EXECUTE
        client.execute();

        // VERIFY
        EasyMock.verify(mockLoader);
    }
}
