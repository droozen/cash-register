package com.roozen.register.init;

import com.roozen.register.model.Item;
import com.roozen.register.init.resource.Resource;
import com.roozen.register.init.resource.ResourceFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.*;

@Service
public class ItemLoader {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    ResourceFactory resourceFactory;

    private String insertItemSql;

    public void setInsertItemSql(String insertItemSql) {
        this.insertItemSql = insertItemSql;
    }

    /**
     * Load items from a file in csv format: name,price
     * TODO: Configure a control file interface that will define the format of the csv file.
     *
     * @param fileSource
     * @param header
     * @throws Exception
     */
    public void loadItems(String fileSource, boolean header) throws Exception {
        Collection<Item> items = parseItemsFromFile(fileSource, header);
        writeItemsToDatabse(items);
    }

    /**
     * Load items into memory from file.
     * TODO: How big do we expect the files to be? If it is large enough, we may need to read/write the items in buffered blocks.
     *
     * @param fileSource
     * @param header
     * @return
     * @throws Exception
     */
    private Collection<Item> parseItemsFromFile(String fileSource, boolean header) throws Exception {
        final Set<Item> items = new HashSet<>();

        Resource resource = getFileResource(fileSource);
        if (header) {
            resource.readBlock();
        }

        String line;
        int count = 0;
        while (StringUtils.isEmpty(line = resource.readBlock()) == false) {
            String[] split = line.split(",");
            if (split.length != 2) throw new RuntimeException("Invalid file format");
            if (!NumberUtils.isNumber(split[1].trim()))
                throw new RuntimeException("Invalid file format. Price must be number.");

            items.add(new Item(count, split[0].trim(), Double.parseDouble(split[1].trim())));
            count++;
        }

        resource.close();

        return items;
    }

    private Resource getFileResource(String fileSource) {
        try {
            return resourceFactory.getFileResource(fileSource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("File Not Found. Failed to load items.");
        }
    }

    /**
     * This schema assumes all items are new.
     * If we need to consider updates, we'll need to extend our approach.
     *
     * @param items
     */
    private void writeItemsToDatabse(Collection<Item> items) {
        Map<String, Object>[] batchParams = new HashMap[items.size()];

        int index = 0;
        for (Item item : items) {
            batchParams[index] = getParams(item);
            index++;
        }

        jdbcTemplate.batchUpdate(insertItemSql, batchParams);
    }

    private Map<String, Object> getParams(Item item) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", item.getId());
        params.put("name", item.getName());
        params.put("price", item.getPrice());
        return params;
    }
}
