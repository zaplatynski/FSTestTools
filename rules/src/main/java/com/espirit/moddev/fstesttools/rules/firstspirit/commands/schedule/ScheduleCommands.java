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

package com.espirit.moddev.fstesttools.rules.firstspirit.commands.schedule;

import com.espirit.moddev.fstesttools.rules.firstspirit.utils.command.FsConnRuleCommand;

import de.espirit.firstspirit.access.AdminService;
import de.espirit.firstspirit.access.UserService;
import de.espirit.firstspirit.access.project.Project;
import de.espirit.firstspirit.access.project.TemplateSet;
import de.espirit.firstspirit.access.schedule.DeployTask;
import de.espirit.firstspirit.access.schedule.FileTarget;
import de.espirit.firstspirit.access.schedule.GenerateTask;
import de.espirit.firstspirit.access.schedule.RunState;
import de.espirit.firstspirit.access.schedule.ScheduleEntry;
import de.espirit.firstspirit.access.schedule.ScheduleEntryControl;
import de.espirit.firstspirit.access.schedule.ScheduleEntryRunningException;
import de.espirit.firstspirit.access.schedule.ScheduleStorage;
import de.espirit.firstspirit.access.schedule.ScheduleTask;
import de.espirit.firstspirit.access.schedule.TaskResult;
import de.espirit.firstspirit.admin.FileTargetImpl;
import de.espirit.firstspirit.server.scheduler.FileTargetDTO;
import de.espirit.firstspirit.server.scheduler.deploy.FileDeployTargetFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.fail;


/**
 * The enum Schedule commands.
 */
public enum ScheduleCommands implements FsConnRuleCommand<ScheduleParameters, ScheduleResult> {

    RUN_SCHEDULE{
        @Override
        public ScheduleResult execute(final ScheduleParameters parameters) {
            final Project project = getProject(parameters);
            final UserService userService = project.getUserService();
            final AdminService ad = userService.getConnection().getService(AdminService.class);
            RunState scheduleRunState = RunState.SUCCESS;
            try {
                final ScheduleEntry scheduleEntry = ad.getScheduleStorage().getScheduleEntry(project, parameters.getEntryName());
                LOGGER.info("Start execution of schedule entry '{}'...", parameters.getEntryName());
                final ScheduleEntryControl control = scheduleEntry.execute();
                control.awaitTermination();
                final StringBuilder sb = new StringBuilder();
                scheduleRunState = getRunState(control, sb);
                logResult(parameters.getEntryName(), scheduleRunState, sb);
            } catch (final ScheduleEntryRunningException e) {
                LOGGER.error("Schedule '" + parameters.getEntryName() + "' caused an error: "+e.getMessage(), e);
                fail(e.toString());
            } catch (@SuppressWarnings("squid:S2221") final Exception e) {
                LOGGER.error("Perhaps the given schedule entry does not exist: '" + parameters.getEntryName() + "': " + e.getMessage(), e);
                fail(e.toString());
            }
            return new ScheduleResult(scheduleRunState);
        }

        private RunState getRunState(final ScheduleEntryControl control, final StringBuilder sb) {
            RunState scheduleResult = RunState.SUCCESS;
            final List<TaskResult> taskResults = control.getState().getTaskResults();
            for (final TaskResult result : taskResults) {
                sb.append("\t");
                sb.append(result.getTask().getName());
                sb.append(":\t");
                sb.append(result.getState());
                sb.append("\n");
                switch (result.getState()) {
                    case NOT_STARTED:
                    case ABORTED:
                    case SUCCESS:
                        break;
                    default:
                        scheduleResult = result.getState();
                }
            }
            return scheduleResult;
        }

        private void logResult(final String entryName, final RunState scheduleResult, final StringBuilder sb) {
            final String arg2 = sb.toString();
            switch (scheduleResult) {
                case ERROR:
                case FINISHED_WITH_ERRORS:
                    LOGGER.error("Finished execution of schedule entry '{}' with errors:\n{}", entryName, arg2);
                    break;
                case NOT_STARTED:
                case ABORTED:
                case SUCCESS:
                    LOGGER.info("Finished execution of schedule entry '{}' with success:\n{}", entryName, arg2);
                    break;
                default:
                    LOGGER.warn("Finished execution of schedule entry '{}' with warnings:\n{}", entryName, arg2);
            }
        }
    },
    CHG_DEPLOY_DIR {
        @Override
        public ScheduleResult execute(final ScheduleParameters parameters) {
            final Project project = getProject(parameters);
            boolean found = false;
            if (project != null) {
                final UserService userService = project.getUserService();
                final AdminService ad = userService.getConnection().getService(AdminService.class);
                final ScheduleEntry scheduleEntry = ad.getScheduleStorage().getScheduleEntry(project, parameters.getEntryName());
                if (scheduleEntry != null) {
                    final List<ScheduleTask> tasks = scheduleEntry.getTasks();
                    if(modifyAllTasks(parameters, tasks)){
                        found = true;
                    }
                }
            }
            if (!found) {
                LOGGER.warn("Did not change deploy path to '{}'!", parameters.getDeployDir().getAbsolutePath());
            }
            return ScheduleResult.VOID;
        }

        private boolean modifyAllTasks(final ScheduleParameters parameters, final List<ScheduleTask> tasks) {
            boolean found = false;
            for (final ScheduleTask task : tasks) {
                if (task instanceof DeployTask) {
                    final DeployTask deployTask = (DeployTask) task;
                    final FileDeployTargetFactory factory = new FileDeployTargetFactory();
                    final FileTargetDTO dto = new FileTargetDTO();
                    final String absolutePath = parameters.getDeployDir().getAbsolutePath();
                    dto.setTargetDirectory(absolutePath);
                    dto.setAppendDateToDirectoryName(false);
                    deployTask.setTarget(factory.create(deployTask, dto));
                    found = true;
                    LOGGER.info("Changed deploy path to '{}'", absolutePath);
                }
            }
            return found;
        }
    },
    GENERATE_SCHEDULE{
        @Override
        public ScheduleResult execute(final ScheduleParameters parameters) {
            final Project project = getProject(parameters);
            if (project != null) {
                final UserService userService = project.getUserService();
                final AdminService ad = userService.getConnection().getService(AdminService.class);

                try {
                    // create scheduler
                    final ScheduleStorage scheduleStorage = ad.getScheduleStorage();
                    final ScheduleEntry scheduleEntry = scheduleStorage.createScheduleEntry(parameters.getEntryName());
                    scheduleEntry.setProject(project);

                    // add tasks
                    scheduleEntry.getTasks().add(createGenerateTask(scheduleEntry, project, parameters));
                    scheduleEntry.getTasks().add(createDeployTask(scheduleEntry, parameters));

                    // save scheduler
                    scheduleEntry.save();
                    scheduleEntry.unlock();
                } catch (final RuntimeException e) {
                    LOGGER.error("Failed to create default scheduler!", e);
                }
            }
            return ScheduleResult.VOID;
        }
        
        private GenerateTask createGenerateTask(final ScheduleEntry scheduleEntry, final Project project, final ScheduleParameters configuration) {
            GenerateTask generateTask = null;
            try {
                generateTask = scheduleEntry.createTask(GenerateTask.class);
                generateTask.setDeleteDirectory(configuration.isGenerateDeleteDirectory());
                generateTask.setGenerateFlag(project.getMasterLanguage(), getTemplateSet(project, "html"), true);
                generateTask.setUrlPrefix(configuration.getGenerateUrlPrefix());
            } catch (final RuntimeException e) {
                LOGGER.error("Failed to create generate task!", e);
            }
            return generateTask;
        }

        private DeployTask createDeployTask(final ScheduleEntry scheduleEntry, final ScheduleParameters configuration) {
            DeployTask deployTask = null;
            try {
                deployTask = scheduleEntry.createTask(DeployTask.class);
                deployTask.setType(configuration.getDeployTaskType());

                final FileTarget target = deployTask.createTarget(FileTargetImpl.class);
                target.setPath("/tmp");
                target.setAppendDateToDirectoryName(false);
                deployTask.setTarget(target);
            } catch (final RuntimeException e) {
                LOGGER.error("Failed to create deploy task!", e);
            }
            return deployTask;
        }

        private TemplateSet getTemplateSet(final Project project, final String name) {
            for (final TemplateSet templateSet : project.getTemplateSets()) {
                if (templateSet.getUid().equals(name)) {
                    return templateSet;
                }
            }
            return null;
        }
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleCommands.class);

    private static Project getProject(final ScheduleParameters parameters) {
        return parameters.getConnection().getProjectByName(parameters.getProjectName());
    }
}
