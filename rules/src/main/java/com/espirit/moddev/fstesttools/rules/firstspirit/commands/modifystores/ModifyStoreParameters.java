package com.espirit.moddev.fstesttools.rules.firstspirit.commands.modifystores;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdParamBean;

import de.espirit.firstspirit.access.Connection;

import javax.inject.Inject;

/**
 * The type Modify store parameters.
 */
public class ModifyStoreParameters implements FsConnRuleCmdParamBean{

    private final String projectName;
    private final String name;
    private String channel;
    private String content;

    @Inject
    @SuppressWarnings("squid:S3306")
    private Connection connection;

    /**
     * Instantiates a new Modify store parameters.
     *
     * @param projectName the project name
     * @param name        the name
     */
    public ModifyStoreParameters(final String projectName, final String name) {
        this.projectName = projectName;
        this.name = name;
    }

    /**
     * Instantiates a new Modify store parameters.
     *
     * @param projectName the project name
     * @param uid         the uid
     * @param channel     the channel
     * @param content     the content
     */
    public ModifyStoreParameters(final String projectName, final String uid, final String channel, final String content) {
        this(projectName, uid);
        this.channel = channel;
        this.content = content;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
