package io.skyfallsdk.sqlloader.loader;

import io.skyfallsdk.sqlloader.SQLLoaderExpansion;
import io.skyfallsdk.sqlloader.database.AbstractDatabaseHandler;
import io.skyfallsdk.world.World;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class WorldDatabaseHandler extends AbstractDatabaseHandler {

    private static final String INIT_SCRIPT = "CREATE TABLE IF NOT EXISTS world_data (" +
      "id                   BIGINT(20)              AUTO_INCREMENT," +
      "level_name           VARCHAR(64)             NOT NULL," +
      "is_hardcore          BIT                     NOT NULL," +
      "generate_structures  BIT                     NOT NULL," +
      "is_raining           BIT                     NOT NULL," +
      "is_thundering        BIT                     NOT NULL," +
      "gamemode             VARCHAR(9)              NOT NULL," +
      "generator_version    INT                     NOT NULL," +
      "rain_time            INT                     NOT NULL," +
      "spawn_x              DOUBLE                  NOT NULL," +
      "spawn_y              DOUBLE                  NOT NULL," +
      "spawn_z              DOUBLE                  NOT NULL," +
      "thunder_time         INT                     NOT NULL," +
      "game_version         INT                     NOT NULL," +
      "last_played          BIGINT                  NOT NULL," +
      "random_seed          BIGINT                  NOT NULL," +
      "world_time           BIGINT                  NOT NULL," +
      "generator_name       VARCHAR(256)            NOT NULL," +
      "PRIMARY KEY (id)" +
      ");";

    private final SQLWorldLoader loader;

    public WorldDatabaseHandler(SQLWorldLoader loader) {
        this.loader = loader;

        this.loader.getServer().getScheduler().submit(() -> {
            this.loader.getServer().getExpansion(SQLLoaderExpansion.class).getLogger().info("Creating 'world_data' database table...");
            try (Connection connection = this.getConnection(); PreparedStatement statement = connection.prepareStatement(INIT_SCRIPT)) {
                statement.execute();
                this.loader.getServer().getExpansion(SQLLoaderExpansion.class).getLogger().info("Successfully created 'world_data' database table...");
            } catch (SQLException e) {
                this.loader.getServer().getExpansion(SQLLoaderExpansion.class).getLogger().error(e);
            }
        });
    }

    public Optional<World> get(Path path) {
        return null;
    }
}
