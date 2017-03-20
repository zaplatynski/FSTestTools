package com.espirit.moddev.fstesttools.tests;


import com.espirit.moddev.fstesttools.rules.logging.InitLog4jLoggingRule;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasXPath;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


/**
 * The type Module xml parent test is used as base class for own module.xml tests. <div> Prerequisites:<br> <ol> <li>In the root of the class loader
 * there must be a file module.xml (e.g. src/main/resources)</li> <li>In the root of the test class loader there must be a file called
 * moduleXmlTest.properties<br> (e.g. src/main/resources) with the following content for example: <code> version=${project.version}<br>
 * name=${project.artifactId}<br> displayName=${project.name}<br> description=${project.artifactId}<br> </code></li> <li>Enable Maven's resource
 * filtering as follows: <code> &lt;testResources&gt;<br> &lt;testResource&gt;<br> &lt;directory&gt;src/test/resources&lt;/directory&gt;<br>
 * &lt;filtering&gt;true&lt;/filtering&gt;<br> &lt;includes&gt;<br> &lt;include&gt;*.properties&lt;/include&gt;<br> &lt;/includes&gt;<br>
 * &lt;/testResource&gt;<br> &lt;testResource&gt;<br> &lt;directory&gt;src/test/resources&lt;/directory&gt;<br>
 * &lt;filtering&gt;false&lt;/filtering&gt;<br> &lt;excludes&gt;<br> &lt;exclude&gt;*.properties&lt;/exclude&gt;<br> &lt;/excludes&gt;<br>
 * &lt;/testResource&gt;<br> &lt;/testResources&gt;<br> </code> </li> </ol> </div>
 */
@SuppressWarnings("squid:S00112")
public abstract class AbstractModuleXmlTest {

    /**
     * The constant LOGGING_RULE.
     */
    @ClassRule
    public static final InitLog4jLoggingRule LOGGING_RULE = new InitLog4jLoggingRule(Level.DEBUG);

    public static final String TEST_PROPERTIES = "moduleXmlTest.properties";
    public static final String EXPECTED_MSG = "Expected specific value";
    public static final String EXPECTED_XPATH = "Expected XPath";

    /**
     * The Error Collector.
     */
    @Rule
    public ErrorCollector errors = new ErrorCollector();

    private Logger logger;
    private Node moduleXML;
    private Properties pomProperties;

    public Node getModuleXML() {
        return moduleXML;
    }

    public Properties getPomProperties() {
        return new Properties(pomProperties);
    }

    /**
     * Gets class loader.
     *
     * @return the class loader
     */
    protected abstract ClassLoader getTestClassLoader();

    @NotNull
    protected String getRelativeTestPropertiesPath() {
        return TEST_PROPERTIES;
    }

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {

        logger = LoggerFactory.getLogger(getClass());

        final File file = new File(getTestClassLoader().getResource("module.xml").toURI());
        final String content = FileUtils.readFileToString(file);
        moduleXML = createXMLfromString(content);

        pomProperties = new Properties();
        populateProperties();
    }

    protected void populateProperties() {
        try (InputStream inputStream = getTestClassLoader().getResourceAsStream(getRelativeTestPropertiesPath())) {
            pomProperties.load(inputStream);
        } catch (final Exception e) {
            logger
                .warn("Loading of '" + getRelativeTestPropertiesPath() + "' failed. Try to load fallback '{}': {}", TEST_PROPERTIES, e);
            try (InputStream inputStreamFallback = getTestClassLoader().getResourceAsStream(TEST_PROPERTIES)) {
                pomProperties.load(inputStreamFallback);
            } catch (final Exception fatalError) {
                logger.error("Fatal error loading properties file '{}': {}", TEST_PROPERTIES, fatalError);
                fail(fatalError.toString());
            }
        }
    }

    private static Node createXMLfromString(final String xmlString) throws Exception {
        return DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(new ByteArrayInputStream(xmlString.getBytes()))
            .getDocumentElement();
    }

    /**
     * Test if there is a version.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIfThereIsAVersion() throws Exception {
        assertThat(EXPECTED_XPATH, moduleXML, hasXPath("/module/version"));
    }

    /**
     * Test if version is equal to pom version.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIfVersionIsEqualToPomVersion() throws Exception {
        final String expectedVersion = pomProperties.getProperty("version");
        assertThat(EXPECTED_MSG, moduleXML, hasXPath("/module/version", equalTo(expectedVersion)));
    }

    /**
     * Test if there is a display name.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIfThereIsADisplayName() throws Exception {
        assertThat(EXPECTED_XPATH, moduleXML, hasXPath("/module/displayname"));
    }

    /**
     * Test if display name is equal to project name.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIfDisplayNameIsEqualToProjectName() throws Exception {
        final String expectedName = pomProperties.getProperty("displayName");
        assertThat("Expected specific value: " + expectedName, moduleXML, hasXPath("/module/displayname", equalTo(expectedName)));
    }

    /**
     * Test if there is a name.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIfThereIsAName() throws Exception {
        assertThat(EXPECTED_XPATH, moduleXML, hasXPath("/module/name"));
    }

    /**
     * Test if name is equal to pO mname.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIfNameIsEqualToPOMname() throws Exception {
        final String expectedName = pomProperties.getProperty("name");
        assertThat(EXPECTED_MSG, moduleXML, hasXPath("/module/name", equalTo(expectedName)));
    }

    /**
     * Test if there is a description.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIfThereIsADescription() throws Exception {
        assertThat(EXPECTED_XPATH, moduleXML, hasXPath("/module/description"));
    }

    /**
     * Test if description is equal to artifact id.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIfDescriptionIsEqualToPomValue() throws Exception {
        final String expectedDescription = getPomProperties().getProperty("description");
        assertThat(EXPECTED_MSG, getModuleXML(), hasXPath("/module/description", equalTo(expectedDescription)));
    }

    /**
     * Test if there is a description.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIfThereIsAVendor() throws Exception {
        assertThat(EXPECTED_XPATH, moduleXML, hasXPath("/module/vendor"));
    }

    /**
     * Test if description is equal to artifact id.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIfVendorIsEqualToPomValue() throws Exception {
        final String expectedDescription = getPomProperties().getProperty("vendor");
        assertThat(EXPECTED_MSG, getModuleXML(), hasXPath("/module/vendor", equalTo(expectedDescription)));
    }

    /**
     * Test all classes.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAllClasses() throws Exception {
        final XPath xPath = XPathFactory.newInstance().newXPath();
        final NodeList nodes = (NodeList) xPath.evaluate("//class",
                                                         moduleXML, XPathConstants.NODESET);
        logger.info("Number of classes: {}", nodes.getLength());
        loadClasses(nodes);
    }

    /**
     * Test all configurables.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAllConfigurables() throws Exception {
        final XPath xPath = XPathFactory.newInstance().newXPath();
        final NodeList nodes = (NodeList) xPath.evaluate("//configurable",
                                                         moduleXML, XPathConstants.NODESET);
        logger.info("Number of configurables: {}", nodes.getLength());
        loadClasses(nodes);
    }

    /**
     * Load classes from string by ClassLoader to check if they are found.
     */
    private void loadClasses(final NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); ++i) {
            final Node clazz = nodes.item(i);
            logger.info("Check if '{}' is existent ...", clazz.getTextContent());
            try {
                Class.forName(clazz.getTextContent());
            } catch (final Exception e) {
                errors.addError(e);
            }
        }
    }

}
