package helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to read customer data from JSON files
 */
public class JsonReader {
    private static final Logger logger = LoggerFactory.getLogger(JsonReader.class);
    private static final String DEFAULT_INPUT_DIR = "input";
    private static final String DEFAULT_JSON_FILE = "customers.json";

    private final ObjectMapper objectMapper;
    private final String inputDirectory;

    public JsonReader() {
        this(DEFAULT_INPUT_DIR);
    }

    public JsonReader(String inputDirectory) {
        this.objectMapper = new ObjectMapper();
        this.inputDirectory = inputDirectory;
    }

    /**
     * Reads customer data from the default JSON file and returns a map
     *
     * @return Map of customer ID to Customer object
     */
    public Map<String, Customer> readCustomers() {
        return readCustomers(DEFAULT_JSON_FILE);
    }

    /**
     * Reads customer data from a specific JSON file and returns a map
     *
     * @param jsonFileName The name of the JSON file to read
     * @return Map of customer ID to Customer object
     */
    public Map<String, Customer> readCustomers(String jsonFileName) {
        try {
            Path filePath = Paths.get(inputDirectory, jsonFileName);

            if (!Files.exists(filePath)) {
                logger.warn("JSON file not found at: {}", filePath);
                logger.info("Creating default customer JSON file...");
                createDefaultCustomerFile(filePath);
            }

            String jsonContent = Files.readString(filePath);
            logger.info("Reading customer data from: {}", filePath);

            // Parse JSON array of customers
            List<Customer> customers = objectMapper.readValue(jsonContent, new TypeReference<List<Customer>>() {
            });

            // Convert to map with customer ID as key
            Map<String, Customer> customerMap = new HashMap<>();
            for (int i = 0; i < customers.size(); i++) {
                Customer customer = customers.get(i);
                String customerId = "customer_" + (i + 1);
                customerMap.put(customerId, customer);
                logger.info("Loaded customer: {} - {} {}", customerId, customer.getFirstName(), customer.getLastName());
            }

            logger.info("Successfully loaded {} customers", customerMap.size());
            return customerMap;

        } catch (IOException e) {
            logger.error("Error reading customer JSON file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read customer data", e);
        }
    }

    /**
     * Gets the first customer from the JSON file
     *
     * @return The first customer from the JSON file
     */
    public Customer getFirstCustomer() {
        Map<String, Customer> customers = readCustomers();
        if (customers.isEmpty()) {
            throw new RuntimeException("No customers found in JSON file");
        }
        return customers.get("customer_1");
    }

    /**
     * Gets the first customer as a map of attributes
     *
     * @return Map of customer attributes (key-value pairs)
     */
    public Map<String, String> getFirstCustomerAsMap() {
        Customer firstCustomer = getFirstCustomer();
        return firstCustomer.getAttributesMap();
    }

    /**
     * Creates a default customer JSON file with one sample customer
     */
    private void createDefaultCustomerFile(Path filePath) throws IOException {
        // Create input directory if it doesn't exist
        Files.createDirectories(filePath.getParent());

        // Create default customer
        Customer defaultCustomer = new Customer();

        // Create JSON array with one customer
        String jsonContent = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(List.of(defaultCustomer));

        // Write to file
        Files.write(filePath, jsonContent.getBytes());
        logger.info("Created default customer file at: {}", filePath);
    }
}
