//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.catcoder.updatechecker;

public enum UpdaterResult {
    UPDATE_FOUND,
    UPDATE_NOT_FOUND;

    private UpdaterResult() {
    }

    public boolean hasUpdates() {
        return this.equals(UPDATE_FOUND);
    }
}
