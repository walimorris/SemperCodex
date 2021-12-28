package com.smart.peepingbill.util;

import com.smart.peepingbill.util.constants.PeepingConstants;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Defines the code for {@code com/smart/peepingbill/util/FileUtil.java} Util class. This Util
 * is used for various convenience file system functionality such as getting the user's home
 * directory, the CodexSnapShot directory and more.
 *
 * @author Wali Morris<walimmorris@gmail.com>
 * created on 2021/12/28
 */
public class FileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Utilizes System properties to return user's home directory.
     * @return {@link String}
     */
    @Nullable
    public static String getUserHomeDirectory() {
        return System.getProperty(PeepingConstants.SYSTEM_USER_HOME);
    }

    /**
     * Returns the {@link Path} to CodexSnapShot directory.
     * @return {@link Path}
     */
    @NotNull
    public static Path getCodexSnapShotDirectory() {
        return Paths.get(StringUtils.join(FileUtil.getUserHomeDirectory(),
                PeepingConstants.CODEX_SNAP_SHOT_DIRECTORY));
    }

    /**
     * Returns the {@link Path} to CodexSnapShot directory.
     * @return {@link Path}
     */
    @NotNull
    public static Path getCodexSnapShotJsonDirectory() {
        return Paths.get(StringUtils.join(getCodexSnapShotDirectory(), "/", PeepingConstants.CODEX_SNAP_SHOT_JSON_DIRECTORY));
    }

    /**
     * Returns the CodexSnapShot directory path as string.
     * @return {@link String}
     */
    public static String getCodexSnapShotDirectoryPathAsString() {
        return String.valueOf(getCodexSnapShotDirectory());
    }

    /**
     * Returns the CodexSnapShot directory path as String.
     * @return {@link String}
     */
    public static String getCodexSnapShotJsonDirectoryAsString() {
        return String.valueOf(getCodexSnapShotJsonDirectory());
    }

    /**
     * SnapShotFiles are created with date-time as prefix to the file type. Creates date-time
     * prefix utilizing functions from {@link NetworkUtil}.
     * @return {@link String}
     *
     * @see NetworkUtil#getSystemLocalDate()
     * @see NetworkUtil#getSystemLocalTime()
     */
    public static String getSnapShotFilePrefix() {
        return StringUtils.join(NetworkUtil.getSystemLocalDate(), "_", NetworkUtil.getSystemLocalTime());
    }
}
