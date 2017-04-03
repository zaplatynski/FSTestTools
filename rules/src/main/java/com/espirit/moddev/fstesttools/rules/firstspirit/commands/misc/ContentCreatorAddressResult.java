/*
 * ********************************************************************
 * Copyright (C) 2017 e-Spirit AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ********************************************************************
 */

package com.espirit.moddev.fstesttools.rules.firstspirit.commands.misc;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCmdResultBean;

import java.net.URI;

/**
 * The type Content creator address result.
 */
public class ContentCreatorAddressResult implements FsConnRuleCmdResultBean {

    private final String contentCreatorUrl;

    /**
     * Instantiates a new Content creator address result.
     *
     * @param contentCreatorUrl the content creator url
     */
    public ContentCreatorAddressResult(String contentCreatorUrl) {
        this.contentCreatorUrl = contentCreatorUrl;
    }

    /**
     * Gets content creator address.
     *
     * @return the content creator address
     */
    public String getContentCreatorAddress() {
        return contentCreatorUrl;
    }

    /**
     * Gets content creator uri.
     *
     * @return the content creator uri
     */
    public URI getContentCreatorUri() {
        return URI.create(contentCreatorUrl);
    }
}
