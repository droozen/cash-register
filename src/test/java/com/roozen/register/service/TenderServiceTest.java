package com.roozen.register.service;

import com.roozen.register.dao.OrderDao;
import com.roozen.register.Server;
import com.roozen.register.model.Item;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Server.class})
@WebIntegrationTest
public class TenderServiceTest {

    @Autowired
    TenderService tenderController;

    String baseUrl = "http://localhost:8080/";

    RestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setUp() throws Exception {
        // @Transactional daos cannot be appropriately tested in this context,
        // since the transaction manager will commit before it gets back to the test method.
        // Therefore, here we have to rely on mocks. We'll need another strategy to test the SQL.
        tenderController.orderDao = mock(OrderDao.class);
    }

    @Test
    public void testChange() throws Exception {
        final Integer orderId = 1;
        final Order expectedOrder = new Order(orderId);
        final Item item = new Item(1, "Pizza", 14.99);
        expectedOrder.addLineItem(item);

        when(tenderController.orderDao.findOrder(1)).thenReturn(expectedOrder);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "service/tender/change")
                .queryParam("order", orderId)
                .queryParam("tender", 20.00);

        // EXECUTE
        String json = restTemplate.getForObject(builder.toUriString(), String.class, new HashMap<>());

        // VERIFY
        verify(tenderController.orderDao).findOrder(1);

        System.out.println(json);
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> jsonMap = parser.parseMap(json);

        assertEquals(1, jsonMap.get("orderId"));

        Map<String, Object> tenderMap = (Map<String, Object>) jsonMap.get("tenderRecord");

        assertEquals(20.00, (Double) tenderMap.get("amountTendered"), 0.01);
        assertEquals(5.01, (Double) tenderMap.get("changeGiven"), 0.01);
        assertNotNull(tenderMap.get("timestamp"));
    }
}
