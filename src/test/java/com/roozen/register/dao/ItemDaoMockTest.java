package com.roozen.register.dao;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

import static org.junit.Assert.*;

public class ItemDaoMockTest {

    MockJdbcTemplate mockTemplate;
    ItemDao itemDao;

    @Test
    public void testFindAllItems() {
        final String expectedSql = "findAllItemsSql";

        mockTemplate = new MockJdbcTemplate(new org.apache.tomcat.jdbc.pool.DataSource());
        mockTemplate.setExpectedSql(expectedSql);

        itemDao = new ItemDao();
        itemDao.setFindAllItemSql(expectedSql);
        itemDao.jdbcTemplate = mockTemplate;


        // EXECUTE
        itemDao.findAllItems();

        // VERIFY
        assertTrue(mockTemplate.wasCalled());
    }


    class MockJdbcTemplate extends NamedParameterJdbcTemplate {

        private String sql;
        private boolean called;

        public MockJdbcTemplate(DataSource dataSource) {
            super(dataSource);
        }

        public void setExpectedSql(String sql) {
            this.sql = sql;
        }

        public boolean wasCalled() {
            return this.called;
        }

        @Override
        public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
            assertEquals(this.sql, sql);
            assertNotNull(rch);
            this.called = true;
        }
    }
}
