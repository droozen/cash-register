package com.roozen.register.command;

import com.roozen.register.services.SqlService;
import org.easymock.EasyMock;
import org.junit.Test;

public class ClientTest {

    @Test
    public void testInit() {
        final String[] inputCommands = {"init"};

        SqlService mockService = EasyMock.createMock(SqlService.class);
        mockService.init();

        EasyMock.replay(mockService);

        // EXECUTE
        Client.executeCommands(inputCommands, mockService);

        // VERIFY
        EasyMock.verify(mockService);
    }

}
