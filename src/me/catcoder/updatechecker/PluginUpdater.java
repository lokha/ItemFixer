package me.catcoder.updatechecker;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents the updater of Bukkit plugin from GitHub commits.
 *
 * @author CatCoder
 */
public class PluginUpdater {

    //Base GitHubAPI URL
    public static final String BASE_URL = "https://api.github.com/";
    //Using for parsing input responses
    public static final Gson GSON = new Gson();

    //Target plugin
    private final Plugin plugin;
    //Plugin version
    private int currentVersion;
    //GitHub repo
    private final String repositoryUrl;

    public PluginUpdater(Plugin plugin, String user, String repository) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL.");
        this.plugin = plugin;
        this.repositoryUrl = "/".concat(user.concat("/").concat(repository).concat("/"));
    }

    /**
     * Check for new updates by GitHub API.
     *
     * @return - result of checking (UPDATE_FOUND, UPDATE_NOT_FOUND)
     * @throws UpdaterException - for any unhandled exceptions.
     */
    public UpdaterResult checkUpdates() throws UpdaterException {
        if (this.currentVersion == 0) currentVersion = parseVersion();
        try {
            URL url = new URL("https://api.github.com/".concat("repos").concat(this.repositoryUrl).concat("commits"));
            JsonObject[] objects = (JsonObject[])GSON.fromJson(new BufferedReader(new InputStreamReader(url.openStream(), Charsets.UTF_8.name())), JsonObject[].class);
            if (objects.length == 0) {
                throw new UpdaterException("commits is empty", this);
            }

            JsonObject object = objects[0];
            String[] toParse = object.get("commit").getAsJsonObject().get("message").getAsString().replace(".", "").split("\\n");
            if (toParse.length == 0) {
                throw new UpdaterException("commits is empty", this);
            }

            int version = Integer.parseInt(toParse[0]);
            if (version != this.currentVersion) {
                return UpdaterResult.UPDATE_FOUND;
            }
        } catch (MalformedURLException var6) {
            throw new UpdaterException(var6.getMessage(), this);
        } catch (IOException var7) {
            throw new UpdaterException("unhandled error " + var7.getMessage(), this);
        } catch (NumberFormatException var8) {
        }

        return UpdaterResult.UPDATE_NOT_FOUND;
    }

    /**
     * Parse local version.
     *
     * @return parsed double number.
     */
    private int parseVersion() throws UpdaterException {
        try {
            return Integer.parseInt(this.plugin.getDescription().getVersion().replace(".", ""));
        } catch (NumberFormatException var2) {
            throw new UpdaterException("cannot parse version " + var2.getMessage(), this);
        }
    }

    /**
     * Target plugin.
     *
     * @return plugin.
     */
    public Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Current version of plugin.
     *
     * @return - version number
     */
    public int getCurrentVersion() {
        return this.currentVersion;
    }
}
