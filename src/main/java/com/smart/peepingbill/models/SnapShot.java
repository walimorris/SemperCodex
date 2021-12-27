package com.smart.peepingbill.models;

import com.smart.peepingbill.models.impl.SmartSystemNetworkImpl;

/**
 * Defines the interface for {@code com/smart/peepingbill/models/SnapShot.java}.
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
     * Get path to codex snapshot json directory.
     * @return {@link String}
     */
    String getCodexSnapShotJsonDirectoryPath();

    /**
     * Get path to codex snapshot pdf directory.
     * @return {@link String}
     */
    String getCodexSnapShotPdfDirectoryPath();

    /**
     * Get path to codex snapshot txt directory.
     * @return {@link String}
     */
    String getCodexSnapShotTxtDirectoryPath();

    /**
     * Writes snapshot to filesystem.
     */
    void writeSnapShot();
}
