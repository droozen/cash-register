package com.roozen.register.main;

import com.roozen.register.service.SqlService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {

    Client client;

    @Before
    public void setUp() {
        client = new Client();
    }

    @Test
    public void testInit() throws Exception {
        final String[] inputCommands = {"init"};

        SqlService mockService = EasyMock.createMock(SqlService.class);
        mockService.init();
        client.sqlService = mockService;

        EasyMock.replay(mockService);

        // EXECUTE
        client.executeCommands(inputCommands);

        // VERIFY
        EasyMock.verify(mockService);
    }

}
