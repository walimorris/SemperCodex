package com.smart.peepingbill.models;

import com.smart.peepingbill.models.impl.SmartSystemNetworkImpl;

/**
 * Defines the interface for {@link com.smart.peepingbill.models.impl.SnapShotImpl}.
 */
public interface SnapShot {

    /**
     * Get SnapShot fileType.
     * @return {@link String}
     */
    String getFileType();

    /**
     * Get system's json representation.
     * @return {@link String}
     */
    SmartSystemNetworkImpl getSystemJson();

    /**
     * Get if codex snapshot directory exists on user system.
     * @return boolean
     */
    boolean getCodexSnapShotDirExists();

    /**
     * Get path to codex snapshot directory.
     * @return {@link String}
     */
    String getCodexSnapShotDirectoryPath();

    /**
     * Writes snapshot to filesystem.
     */
    void writeSnapShot();
}
