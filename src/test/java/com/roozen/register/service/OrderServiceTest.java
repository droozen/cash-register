package com.roozen.register.service;

import com.roozen.register.dao.ItemDao;
import com.roozen.register.dao.OrderDao;
import com.roozen.register.Server;
import com.roozen.register.model.Order;
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
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Server.class})
@WebIntegrationTest
public class OrderServiceTest {

    @Autowired
    OrderService orderController;

    String baseUrl = "http://localhost:8080/";

    RestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setUp() throws Exception {
        // @Transactional daos cannot be appropriately tested in this context,
        // since the transaction manager will commit before it gets back to the test method.
        // Therefore, here we have to rely on mocks. We'll need another strategy to test the SQL.
        orderController.orderDao = mock(OrderDao.class);
        orderController.itemDao = mock(ItemDao.class);
    }

    @Test
    public void testCreateOrder() {
        final Order expectedOrder = new Order(123);

        when(orderController.orderDao.createNewOrder()).thenReturn(expectedOrder);

        // EXECUTE
        String json = restTemplate.getForObject(baseUrl + "service/order/create", String.class, new HashMap<>());

        // VERIFY
        verify(orderController.orderDao).createNewOrder();

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
