package test.com.espirit.moddev.fstesttools.rules.firstspirit;

import com.espirit.moddev.fstesttools.rules.firstspirit.SchedulerConfiguration;

import de.espirit.firstspirit.access.schedule.DeployTask;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * SchedulerConfiguration Tester.
 */
public class SchedulerConfigurationTest {

    private SchedulerConfiguration testling;

    @Before
    public void before() throws Exception {
        testling = new SchedulerConfiguration();
    }

    /**
     * Method: isGenerateDeleteDirectory().
     */
    @Test
    public void testIsGenerateDeleteDirectory() throws Exception {
        assertTrue("Default true", testling.isGenerateDeleteDirectory());
    }

    /**
     * Method: setGenerateDeleteDirectory(final boolean generateDeleteDirectory).
     */
    @Test
    public void testSetGenerateDeleteDirectory() throws Exception {
        testling.setGenerateDeleteDirectory(false);

        assertFalse("set to false", testling.isGenerateDeleteDirectory());
    }

    /**
     * Method: getDeployTaskType().
     */
    @Test
    public void testGetDeployTaskType() throws Exception {
        assertThat("expect default", testling.getDeployTaskType(), is(DeployTask.Type.Full));
    }

    /**
     * Method: setDeployTaskType(final DeployTask.Type deployTaskType).
     */
    @Test
    public void testSetDeployTaskType() throws Exception {
        testling.setDeployTaskType(DeployTask.Type.Incremental);

        assertThat("set incremental", testling.getDeployTaskType(), is(DeployTask.Type.Incremental));
    }

    /**
     * Method: getGenerateUrlPrefix().
     */
    @Test
    public void testGetGenerateUrlPrefix() throws Exception {
        assertThat("expect default", testling.getGenerateUrlPrefix(), is("http://$address$"));
    }

    /**
     * Method: setGenerateUrlPrefix(final String generateUrlPrefix).
     */
    @Test
    public void testSetGenerateUrlPrefix() throws Exception {
        testling.setGenerateUrlPrefix("http://www.e-spirit.com");

        assertThat("expect new address", testling.getGenerateUrlPrefix(), is("http://www.e-spirit.com"));
    }


} 
