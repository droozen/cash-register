package com.roozen.register.controller;

import com.roozen.register.dao.ItemDao;
import com.roozen.register.dao.OrderDao;
import com.roozen.register.main.Server;
import com.roozen.register.model.Order;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Server.class})
@WebIntegrationTest
public class OrderControllerTest {

    @Autowired
    OrderController orderController;

    String baseUrl = "http://localhost:8080/";

    RestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setUp() throws Exception {
        // @Transactional daos cannot be appropriately tested in this context,
        // since the transaction manager will commit before it gets back to the test method.
        // Therefore, here we have to rely on mocks. We'll need another strategy to test the SQL.
        orderController.orderDao = EasyMock.createMock(OrderDao.class);
        orderController.itemDao = EasyMock.createMock(ItemDao.class);
    }

    @Test
    public void testCreateOrder() {
        final Order expectedOrder = new Order(123);

        EasyMock.expect(orderController.orderDao.createNewOrder()).andReturn(expectedOrder);
        EasyMock.replay(orderController.orderDao);

        // EXECUTE
        String json = restTemplate.getForObject(baseUrl + "controller/order/create", String.class, new HashMap<>());

        // VERIFY
        EasyMock.verify(orderController.orderDao);

        System.out.println(json);
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> jsonMap = parser.parseMap(json);

        assertEquals(123, jsonMap.get("orderId"));
        assertNull(jsonMap.get("orderNumber"));
        assertNotNull(jsonMap.get("timestamp"));
        assertEquals(0.0, (Double) jsonMap.get("subTotal"), 0.01);
        assertEquals(0.0, (Double) jsonMap.get("totalTax"), 0.01);
        assertEquals(0.0, (Double) jsonMap.get("grandTotal"), 0.01);
        assertEquals("INPROGRESS", jsonMap.get("statusCode"));
        assertTrue(((Collection) jsonMap.get("lineItems")).isEmpty());
        assertNull(jsonMap.get("tenderRecord"));
    }

}
