//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.catcoder.updatechecker;

import java.util.logging.Level;

public class UpdaterException extends Exception {
    private static final long serialVersionUID = -7371026016860990753L;
    private final String message;
    private final PluginUpdater updater;

    public UpdaterException(String message, PluginUpdater updater) {
        super(message);
        this.message = message;
        this.updater = updater;
    }

    public void print() {
        this.updater.getPlugin().getLogger().log(Level.SEVERE, String.format(this.printableMessage(), this.updater.getPlugin().getName(), this.message));
    }

    public String printableMessage() {
        return "[PluginUpdater]: Error of plugin %s (%s)";
    }
}
