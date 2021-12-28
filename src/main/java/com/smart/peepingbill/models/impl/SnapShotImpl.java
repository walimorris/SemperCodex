package com.smart.peepingbill.models.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smart.peepingbill.models.SnapShot;
import com.smart.peepingbill.util.FileUtil;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.concurrent.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Defines the code for {@code com/smart/peepingbill/models/impl/SnapShotImpl.java} class.
 * A SnapShot can be defined as the current local area network and its devices' json data
 * being recorded and saved locally as a <b>SnapShot</b> in various file types. This class
 * allows the user of a SnapShot to decide which file type the current system json should
 * be saved as, .json, .pdf, .txt. etc.<br>
 * <br>The codex snapshot directory can be found at <b>/user/home/CodexSnapShots</b> directory.
 *
 * @author Wali Morris<walimmorris@gmail.com>
 * created on 2021/12/26
 */
public class SnapShotImpl implements SnapShot {
    public static final Logger LOG = LoggerFactory.getLogger(SnapShotImpl.class);

    private final String fileType;
    private final SmartSystemNetworkImpl systemJson;
    private boolean snapShotDirExists;
    private final Path codexSnapShotDirectory;

    public SnapShotImpl(String fileType, SmartSystemNetworkImpl systemJson) {
        this.fileType = fileType;
        this.systemJson = systemJson;
        this.codexSnapShotDirectory = FileUtil.getCodexSnapShotDirectory();

        this.snapShotDirExists = codexSnapDirExists();
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    @Override
    public SmartSystemNetworkImpl getSystemJson() {
        return systemJson;
    }

    @Override
    public String getCodexSnapShotDirectoryPath() {
        return codexSnapShotDirectory.toString();
    }

    @Override
    public boolean getCodexSnapShotDirExists() {
        return snapShotDirExists;
    }

    @Override
    public void writeSnapShot() {
        if (fileType.equalsIgnoreCase(PeepingConstants.JSON)) {
            initJsonSnapShotTask();
        }
    }

    /**
     * Initializes a {@link Task} to create a .json file of the current system network as
     * a {@link org.json.JSONObject}.
     */
    public void initJsonSnapShotTask() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                String filePrefix = FileUtil.getSnapShotFilePrefix();
                Path fileName = Path.of(StringUtils.join(FileUtil.getCodexSnapShotJsonDirectory(), "/", filePrefix,
                        PeepingConstants.JSON_FILE_TYPE));
                try {
                    Files.writeString(fileName, prettyPrintSystemJSON());
                } catch (IOException e) {
                    LOG.error("Error writing json snapshot to file '{}': {}", fileName, e.getMessage());
                }
                LOG.info("{} written to system", fileName);
                return null;
            }
        };
        new Thread(task).start();
    }

    /**
     * The <b>user/home/CodexSnapShots</b> directory is checked for existence. If it does not exist,
     * it is created along with the <b>/user/home/CodexSnapShots/JSONSnapShots</b> directory.
     * @return boolean
     */
    private boolean codexSnapDirExists() {
        // check if file exists, if not create it
        if (Files.exists(codexSnapShotDirectory)) {
            snapShotDirExists = true;
        } else {
            // create snapshot directories
            try {
                Files.createDirectory(codexSnapShotDirectory);
                // create json snap directory
                Files.createDirectory(FileUtil.getCodexSnapShotJsonDirectory());
            } catch (IOException e) {
                LOG.error("Error creating Codex SnapShot Directories: {}", e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Returns pretty print system json.
     * @return {@link String}
     */
    private String prettyPrintSystemJSON() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(systemJson);
    }
}
