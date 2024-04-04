package se.thinkcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.thinkcode.infrastructure.DatabaseConnection;
import se.thinkcode.version.SqlVersionRepository;
import se.thinkcode.version.VersionRepository;
import se.thinkcode.version.VersionService;
import se.thinkcode.version.v1.VersionController;
import spark.Spark;

import static spark.Spark.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Waiting for server to start");

        handleExceptions();
        addRoutes();
        initializeDatabase();

        Spark.awaitInitialization();
        logger.info("Started on " + port());
    }

    private static void handleExceptions() {
        exception(Exception.class, (exception, request, response) -> {
            String message = exception.getMessage();

            logger.error(message, exception);
        });
    }

    private static void addRoutes() {
        get("/api/v1/version", (req, res) -> new VersionController(getVersionService()).handle(req, res));
    }

    private static void initializeDatabase() {
        new DatabaseConnection();
    }

    private static VersionService getVersionService() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        VersionRepository repository = new SqlVersionRepository(databaseConnection);

        return new VersionService(repository);
    }
}
