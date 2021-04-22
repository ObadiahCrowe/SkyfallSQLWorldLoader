package io.skyfallsdk.sqlloader.loader;

import io.skyfallsdk.SkyfallServer;
import io.skyfallsdk.concurrent.PoolSpec;
import io.skyfallsdk.concurrent.ThreadPool;
import io.skyfallsdk.world.Dimension;
import io.skyfallsdk.world.World;
import io.skyfallsdk.world.generate.WorldGenerator;
import io.skyfallsdk.world.loader.AbstractWorldLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SQLWorldLoader extends AbstractWorldLoader {

    private final WorldDatabaseHandler database;

    public SQLWorldLoader(SkyfallServer server, Path baseDir) {
        super(server, baseDir);

        this.database = new WorldDatabaseHandler(this);
    }

    SkyfallServer getServer() {
        return this.server;
    }

    @Override
    public void unloadAll() {

    }

    @Override
    public CompletableFuture<Optional<World>> load(Path path) throws IOException {
        return CompletableFuture.supplyAsync(() -> this.database.get(path.getFileName()), ThreadPool.createForSpec(PoolSpec.WORLD));
    }

    @Override
    public CompletableFuture<Void> unload(String name) {
        return null;
    }

    @Override
    public CompletableFuture<Void> unload(World world) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<World>> get(String name) {
        return null;
    }

    @Override
    public CompletableFuture<World> create(String name, Dimension dimension, WorldGenerator generator) throws IOException {
        return null;
    }
}
