package net.minecraft.data;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HashCache
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Path path;
    private final Path cachePath;
    private int hits;
    private final Map<Path, String> oldCache = Maps.newHashMap();
    private final Map<Path, String> newCache = Maps.newHashMap();
    private final Set<Path> keep = Sets.newHashSet();

    public HashCache(Path pPath, String pCacheFileName) throws IOException
    {
        this.path = pPath;
        Path path = pPath.resolve(".cache");
        Files.createDirectories(path);
        this.cachePath = path.resolve(pCacheFileName);
        this.walkOutputFiles().forEach((p_123959_) ->
        {
            this.oldCache.put(p_123959_, "");
        });

        if (Files.isReadable(this.cachePath))
        {
            IOUtils.readLines(Files.newInputStream(this.cachePath), Charsets.UTF_8).forEach((p_123950_) ->
            {
                int i = p_123950_.indexOf(32);
                this.oldCache.put(pPath.resolve(p_123950_.substring(i + 1)), p_123950_.substring(0, i));
            });
        }
    }

    public void purgeStaleAndWrite() throws IOException
    {
        this.removeStale();
        Writer writer;

        try
        {
            writer = Files.newBufferedWriter(this.cachePath);
        }
        catch (IOException ioexception)
        {
            LOGGER.warn("Unable write cachefile {}: {}", this.cachePath, ioexception.toString());
            return;
        }

        IOUtils.writeLines(this.newCache.entrySet().stream().map((p_123944_) ->
        {
            return (String)p_123944_.getValue() + " " + this.path.relativize(p_123944_.getKey());
        }).collect(Collectors.toList()), System.lineSeparator(), writer);
        writer.close();
        LOGGER.debug("Caching: cache hits: {}, created: {} removed: {}", this.hits, this.newCache.size() - this.hits, this.oldCache.size());
    }

    @Nullable
    public String getHash(Path pFilePath)
    {
        return this.oldCache.get(pFilePath);
    }

    public void putNew(Path pFilePath, String pHash)
    {
        this.newCache.put(pFilePath, pHash);

        if (Objects.equals(this.oldCache.remove(pFilePath), pHash))
        {
            ++this.hits;
        }
    }

    public boolean had(Path pFilePath)
    {
        return this.oldCache.containsKey(pFilePath);
    }

    public void keep(Path pFilePath)
    {
        this.keep.add(pFilePath);
    }

    private void removeStale() throws IOException
    {
        this.walkOutputFiles().forEach((p_123957_) ->
        {
            if (this.had(p_123957_) && !this.keep.contains(p_123957_))
            {
                try
                {
                    Files.delete(p_123957_);
                }
                catch (IOException ioexception)
                {
                    LOGGER.debug("Unable to delete: {} ({})", p_123957_, ioexception.toString());
                }
            }
        });
    }

    private Stream<Path> walkOutputFiles() throws IOException
    {
        return Files.walk(this.path).filter((p_123955_) ->
        {
            return !Objects.equals(this.cachePath, p_123955_) && !Files.isDirectory(p_123955_);
        });
    }
}
