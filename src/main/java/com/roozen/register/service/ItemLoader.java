package com.roozen.register.service;

import com.roozen.register.model.Item;
import com.roozen.register.service.resource.Resource;
import com.roozen.register.service.resource.ResourceFactory;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class ItemLoader {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ResourceFactory resourceFactory;

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

    private Collection<Item> parseItemsFromFile(String fileSource, boolean header) throws Exception {
        final Set<Item> items = new HashSet<>();

        Resource resource = getFileResource(fileSource);
        if (header) {
            resource.readBlock();
        }

        String line;
        while ((line = resource.readBlock()) != null) {
            String[] split = line.split(",");
            if (split.length != 2) throw new RuntimeException("Invalid file format");
            if (!NumberUtils.isNumber(split[1]))
                throw new RuntimeException("Invalid file format. Price must be number.");

            items.add(new Item(split[0], Double.parseDouble(split[1])));
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

    private void writeItemsToDatabse(Collection<Item> items) {

    }
}
