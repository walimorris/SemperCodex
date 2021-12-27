package com.smart.peepingbill.models.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smart.peepingbill.models.SnapShot;
import com.smart.peepingbill.util.NetworkUtil;
import com.smart.peepingbill.util.constants.PeepingConstants;
import javafx.concurrent.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    private Path codexSnapShotJsonDirectory;
    private Path codexSnapShotPdfDirectory;
    private Path codexSnapShotTxtDirectory;

    public SnapShotImpl(String fileType, SmartSystemNetworkImpl systemJson) {
        this.fileType = fileType;
        this.systemJson = systemJson;
        this.codexSnapShotDirectory = Paths.get(StringUtils.join(NetworkUtil.getUserHomeDirectory(),
                PeepingConstants.CODEX_SNAP_SHOT_DIRECTORY));

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
    public String getCodexSnapShotJsonDirectoryPath() {
        return codexSnapShotJsonDirectory.toString();
    }

    @Override
    public String getCodexSnapShotPdfDirectoryPath() {
        return codexSnapShotPdfDirectory.toString();
    }

    @Override
    public String getCodexSnapShotTxtDirectoryPath() {
        return codexSnapShotTxtDirectory.toString();
    }

    @Override
    public boolean getCodexSnapShotDirExists() {
        return snapShotDirExists;
    }

    @Override
    public void writeSnapShot() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                String filePrefix = StringUtils.join(NetworkUtil.getSystemLocalDate(), "_", NetworkUtil.getSystemLocalTime());
                Path fileName = Path.of(StringUtils.join(getCodexSnapShotJsonDirectoryPath(), "/", filePrefix,
                        PeepingConstants.JSON_FILE_TYPE));
                try {
                    Files.writeString(fileName, prettyPrintSystemJSON());
                } catch (IOException e) {
                    LOG.error("Error writing json snapshot to file: {}", e.getMessage());
                }
                LOG.info("{} written to system", fileName);
                return null;
            }
        };
        new Thread(task).start();
    }

    /**
     * The <b>user/home/CodexSnapShots</b> directory is checked for existence. If it does not exist,
     * it is created along with the <b>/user/home/CodexSnapShots/JSONSnapShots,
     * /user/home/CodexSnapShots/PDFSnapShots, /user/home/CodexSnapShots/TXTSnapShots</b> directories.
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
                this.codexSnapShotJsonDirectory = Paths.get(StringUtils.join(codexSnapShotDirectory.toString(), PeepingConstants.CODEX_SNAP_SHOT_JSON_DIRECTORY));
                Files.createDirectory(codexSnapShotJsonDirectory);

                // create pdf directory
                this.codexSnapShotPdfDirectory = Paths.get(StringUtils.join(codexSnapShotDirectory.toString(), PeepingConstants.CODEX_SNAP_SHOT_PDF_DIRECTORY));
                Files.createDirectory(codexSnapShotPdfDirectory);

                // create txt directory
                this.codexSnapShotTxtDirectory = Paths.get(StringUtils.join(codexSnapShotDirectory.toString(), PeepingConstants.CODEX_SNAP_SHOT_TXT_DIRECTORY));
                Files.createDirectory(codexSnapShotTxtDirectory);
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
