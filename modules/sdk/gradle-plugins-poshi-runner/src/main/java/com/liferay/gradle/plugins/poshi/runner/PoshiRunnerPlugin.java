/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.gradle.plugins.poshi.runner;

import com.liferay.gradle.util.FileUtil;
import com.liferay.gradle.util.GradleUtil;
import com.liferay.gradle.util.OSDetector;
import com.liferay.gradle.util.StringUtil;
import com.liferay.gradle.util.Validator;

import groovy.lang.Closure;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.UncheckedIOException;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.file.CopySpec;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileTree;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.reporting.DirectoryReport;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskOutputs;
import org.gradle.api.tasks.testing.JUnitXmlReport;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.tasks.testing.TestTaskReports;
import org.gradle.api.tasks.testing.logging.TestLoggingContainer;
import org.gradle.process.ExecSpec;
import org.gradle.util.CollectionUtils;
import org.gradle.util.GUtil;

/**
 * @author Andrea Di Giorgi
 */
public class PoshiRunnerPlugin implements Plugin<Project> {

	public static final String DOWNLOAD_WEB_DRIVER_BROWSER_BINARY_TASK_NAME =
		"downloadWebDriverBrowserBinary";

	public static final String EVALUATE_POSHI_CONSOLE_TASK_NAME =
		"evaluatePoshiConsole";

	public static final String EXECUTE_PQL_QUERY_TASK_NAME = "executePQLQuery";

	public static final String EXPAND_POSHI_RUNNER_TASK_NAME =
		"expandPoshiRunner";

	public static final String POSHI_RUNNER_CONFIGURATION_NAME = "poshiRunner";

	public static final String POSHI_RUNNER_RESOURCES_CONFIGURATION_NAME =
		PoshiRunnerResourcesPlugin.POSHI_RUNNER_RESOURCES_CONFIGURATION_NAME;

	public static final String RUN_POSHI_TASK_NAME = "runPoshi";

	public static final String SIKULI_CONFIGURATION_NAME = "sikuli";

	public static final String VALIDATE_POSHI_TASK_NAME = "validatePoshi";

	public static final String WRITE_POSHI_PROPERTIES_TASK_NAME =
		"writePoshiProperties";

	@Override
	public void apply(Project project) {
		GradleUtil.applyPlugin(project, BasePlugin.class);

		final PoshiRunnerExtension poshiRunnerExtension =
			GradleUtil.addExtension(
				project, "poshiRunner", PoshiRunnerExtension.class);

		_addConfigurationPoshiRunner(project, poshiRunnerExtension);
		_addConfigurationSikuli(project, poshiRunnerExtension);

		_addConfigurationPoshiRunnerResources(project);

		final JavaExec executePQLQueryTask = _addTaskExecutePQLQuery(project);
		final JavaExec evaluatePoshiConsoleTask = _addTaskEvaluatePoshiConsole(
			project);

		_addTaskExpandPoshiRunner(project);

		final Task downloadWebDriverBrowserBinaryTask =
			_addTaskDownloadWebDriverBrowserBinary(
				project, poshiRunnerExtension);

		final Test runPoshiTask = _addTaskRunPoshi(project);
		final JavaExec validatePoshiTask = _addTaskValidatePoshi(project);
		final JavaExec writePoshiPropertiesTask = _addTaskWritePoshiProperties(
			project);

		project.afterEvaluate(
			new Action<Project>() {

				@Override
				public void execute(Project project) {
					Properties poshiProperties = _getPoshiProperties(
						poshiRunnerExtension);

					_configureTaskDownloadWebDriverBrowserBinary(
						downloadWebDriverBrowserBinaryTask, poshiProperties);
					_configureTaskExecutePQLQuery(
						executePQLQueryTask, poshiProperties,
						poshiRunnerExtension);
					_configureTaskEvaluatePoshiConsole(
						evaluatePoshiConsoleTask, poshiProperties,
						poshiRunnerExtension);
					_configureTaskRunPoshi(
						runPoshiTask, poshiProperties, poshiRunnerExtension);
					_configureTaskValidatePoshi(
						validatePoshiTask, poshiProperties,
						poshiRunnerExtension);
					_configureTaskWritePoshiProperties(
						writePoshiPropertiesTask, poshiProperties,
						poshiRunnerExtension);
				}

			});
	}

	private Configuration _addConfigurationPoshiRunner(
		final Project project,
		final PoshiRunnerExtension poshiRunnerExtension) {

		Configuration configuration = GradleUtil.addConfiguration(
			project, POSHI_RUNNER_CONFIGURATION_NAME);

		configuration.defaultDependencies(
			new Action<DependencySet>() {

				@Override
				public void execute(DependencySet dependencySet) {
					_addDependenciesPoshiRunner(project, poshiRunnerExtension);
				}

			});

		configuration.setDescription(
			"Configures Poshi Runner for this project.");
		configuration.setVisible(false);

		return configuration;
	}

	private Configuration _addConfigurationPoshiRunnerResources(
		Project project) {

		ConfigurationContainer configurationContainer =
			project.getConfigurations();

		Configuration configuration = configurationContainer.maybeCreate(
			POSHI_RUNNER_RESOURCES_CONFIGURATION_NAME);

		configuration.setDescription(
			"Configures the Poshi Runner resources for this project.");
		configuration.setVisible(false);

		return configuration;
	}

	private Configuration _addConfigurationSikuli(
		final Project project,
		final PoshiRunnerExtension poshiRunnerExtension) {

		Configuration configuration = GradleUtil.addConfiguration(
			project, SIKULI_CONFIGURATION_NAME);

		configuration.defaultDependencies(
			new Action<DependencySet>() {

				@Override
				public void execute(DependencySet dependencySet) {
					_addDependenciesSikuli(project, poshiRunnerExtension);
				}

			});

		configuration.setDescription("Configures Sikuli for this project.");
		configuration.setVisible(false);

		return configuration;
	}

	private void _addDependenciesPoshiRunner(
		Project project, PoshiRunnerExtension poshiRunnerExtension) {

		GradleUtil.addDependency(
			project, POSHI_RUNNER_CONFIGURATION_NAME, "com.liferay",
			"com.liferay.poshi.runner", poshiRunnerExtension.getVersion());
	}

	private void _addDependenciesSikuli(
		Project project, PoshiRunnerExtension poshiRunnerExtension) {

		String bitMode = OSDetector.getBitmode();

		if (bitMode.equals("32")) {
			bitMode = "x86";
		}
		else {
			bitMode = "x86_64";
		}

		String os = "linux";

		if (OSDetector.isApple()) {
			os = "macosx";
		}
		else if (OSDetector.isWindows()) {
			os = "windows";
		}

		String classifier = os + "-" + bitMode;

		GradleUtil.addDependency(
			project, SIKULI_CONFIGURATION_NAME, "org.bytedeco.javacpp-presets",
			"opencv", poshiRunnerExtension.getOpenCVVersion(), classifier,
			true);
	}

	private Task _addTaskDownloadWebDriverBrowserBinary(
		final Project project, PoshiRunnerExtension poshiRunnerExtension) {

		Task task = GradleUtil.addTask(
			project, DOWNLOAD_WEB_DRIVER_BROWSER_BINARY_TASK_NAME, Task.class);

		task.doLast(
			new Action<Task>() {

				@Override
				public void execute(Task task) {
					File webDriverDir = _getWebDriverDir(project);

					project.delete(webDriverDir);

					project.copy(
						new Action<CopySpec>() {

							@Override
							public void execute(CopySpec copySpec) {
								File file = _getWebDriverBrowserBinaryFile(
									project,
									_getPoshiProperties(poshiRunnerExtension));

								String fileName = file.getName();

								if (fileName.endsWith(".zip")) {
									copySpec.from(project.zipTree(file));
								}
								else {
									copySpec.from(project.tarTree(file));
								}

								copySpec.into(webDriverDir);
							}

						});
				}

			});

		return task;
	}

	private JavaExec _addTaskEvaluatePoshiConsole(Project project) {
		JavaExec javaExec = GradleUtil.addTask(
			project, EVALUATE_POSHI_CONSOLE_TASK_NAME, JavaExec.class);

		javaExec.setClasspath(_getPoshiRunnerClasspath(project));
		javaExec.setDescription("Evaluate the console output errors.");
		javaExec.setGroup("verification");
		javaExec.setMain(
			"com.liferay.poshi.runner.PoshiRunnerConsoleEvaluator");

		return javaExec;
	}

	private JavaExec _addTaskExecutePQLQuery(Project project) {
		JavaExec javaExec = GradleUtil.addTask(
			project, EXECUTE_PQL_QUERY_TASK_NAME, JavaExec.class);

		javaExec.args(Collections.singleton("executePQLQuery"));

		javaExec.setClasspath(_getPoshiRunnerClasspath(project));
		javaExec.setDescription("Execute the PQL query.");
		javaExec.setGroup("verification");
		javaExec.setMain("com.liferay.poshi.runner.PoshiRunnerCommandExecutor");

		return javaExec;
	}

	private Copy _addTaskExpandPoshiRunner(final Project project) {
		Copy copy = GradleUtil.addTask(
			project, EXPAND_POSHI_RUNNER_TASK_NAME, Copy.class);

		Closure<Void> closure = new Closure<Void>(project) {

			@SuppressWarnings("unused")
			public FileTree doCall() {
				Configuration configuration = GradleUtil.getConfiguration(
					project, POSHI_RUNNER_CONFIGURATION_NAME);

				Iterator<File> iterator = configuration.iterator();

				while (iterator.hasNext()) {
					File file = iterator.next();

					String fileName = file.getName();

					if (fileName.startsWith("com.liferay.poshi.runner-")) {
						return project.zipTree(file);
					}
				}

				return null;
			}

		};

		copy.from(closure);

		copy.into(_getExpandedPoshiRunnerDir(project));

		return copy;
	}

	@SuppressWarnings("rawtypes")
	private Test _addTaskRunPoshi(Project project) {
		final Test test = GradleUtil.addTask(
			project, RUN_POSHI_TASK_NAME, Test.class);

		test.dependsOn(
			BasePlugin.CLEAN_TASK_NAME +
				StringUtil.capitalize(RUN_POSHI_TASK_NAME),
			DOWNLOAD_WEB_DRIVER_BROWSER_BINARY_TASK_NAME,
			EXPAND_POSHI_RUNNER_TASK_NAME);

		test.include("com/liferay/poshi/runner/PoshiRunner.class");
		test.setClasspath(_getPoshiRunnerClasspath(project));
		test.setDefaultCharacterEncoding(StandardCharsets.UTF_8.toString());
		test.setDescription("Execute tests using Poshi Runner.");
		test.setGroup("verification");
		test.setScanForTestClasses(false);
		test.setTestClassesDirs(
			project.files(_getExpandedPoshiRunnerDir(project)));

		TaskOutputs taskOutputs = test.getOutputs();

		taskOutputs.upToDateWhen(
			new Spec<Task>() {

				@Override
				public boolean isSatisfiedBy(Task task) {
					return false;
				}

			});

		TestLoggingContainer testLoggingContainer = test.getTestLogging();

		testLoggingContainer.setShowStandardStreams(true);

		PluginContainer pluginContainer = project.getPlugins();

		pluginContainer.withId(
			"com.liferay.test.integration",
			new Action<Plugin>() {

				@Override
				public void execute(Plugin plugin) {
					test.dependsOn(_START_TESTABLE_TOMCAT_TASK_NAME);

					Task task = GradleUtil.getTask(
						test.getProject(), _STOP_TESTABLE_TOMCAT_TASK_NAME);

					task.mustRunAfter(test);
				}

			});

		return test;
	}

	private JavaExec _addTaskValidatePoshi(Project project) {
		JavaExec javaExec = GradleUtil.addTask(
			project, VALIDATE_POSHI_TASK_NAME, JavaExec.class);

		javaExec.setClasspath(_getPoshiRunnerClasspath(project));
		javaExec.setDescription("Validates the Poshi files syntax.");
		javaExec.setGroup("verification");
		javaExec.setMain("com.liferay.poshi.core.PoshiValidation");

		return javaExec;
	}

	private JavaExec _addTaskWritePoshiProperties(Project project) {
		JavaExec javaExec = GradleUtil.addTask(
			project, WRITE_POSHI_PROPERTIES_TASK_NAME, JavaExec.class);

		javaExec.setClasspath(_getPoshiRunnerClasspath(project));
		javaExec.setDescription("Write the Poshi properties files.");
		javaExec.setGroup("verification");
		javaExec.setMain("com.liferay.poshi.core.PoshiContext");

		return javaExec;
	}

	private void _configureTaskDownloadWebDriverBrowserBinary(
		Task task, Properties poshiProperties) {

		task.onlyIf(
			new Spec<Task>() {

				@Override
				public boolean isSatisfiedBy(Task task) {
					return _isDownloadWebDriverBrowserBinary(poshiProperties);
				}

			});
	}

	private void _configureTaskEvaluatePoshiConsole(
		JavaExec javaExec, Properties poshiProperties,
		PoshiRunnerExtension poshiRunnerExtension) {

		_populateSystemProperties(
			javaExec.getSystemProperties(), poshiProperties,
			javaExec.getProject(), poshiRunnerExtension);
	}

	private void _configureTaskExecutePQLQuery(
		JavaExec javaExec, Properties poshiProperties,
		PoshiRunnerExtension poshiRunnerExtension) {

		_populateSystemProperties(
			javaExec.getSystemProperties(), poshiProperties,
			javaExec.getProject(), poshiRunnerExtension);
	}

	private void _configureTaskRunPoshi(
		Test test, Properties poshiProperties,
		PoshiRunnerExtension poshiRunnerExtension) {

		_configureTaskRunPoshiBinResultsDir(test);
		_configureTaskRunPoshiReports(test);

		Project project = test.getProject();

		if (FileUtil.exists(project, ".env")) {
			try {
				Properties properties = new Properties();

				properties.load(new FileInputStream(project.file(".env")));

				test.environment((Map)properties);
			}
			catch (IOException ioException) {
				throw new UncheckedIOException(ioException);
			}
		}

		_populateSystemProperties(
			test.getSystemProperties(), poshiProperties, test.getProject(),
			poshiRunnerExtension);
		_populateWebDriverSystemProperties(test, poshiProperties);
	}

	private void _configureTaskRunPoshiBinResultsDir(Test test) {
		if (test.getBinResultsDir() != null) {
			return;
		}

		Project project = test.getProject();

		test.setBinResultsDir(
			project.file("test-results/binary/" + RUN_POSHI_TASK_NAME));
	}

	private void _configureTaskRunPoshiReports(Test test) {
		Project project = test.getProject();

		TestTaskReports testTaskReports = test.getReports();

		DirectoryReport directoryReport = testTaskReports.getHtml();

		if (directoryReport.getDestination() == null) {
			directoryReport.setDestination(project.file("tests"));
		}

		JUnitXmlReport jUnitXmlReport = testTaskReports.getJunitXml();

		jUnitXmlReport.setOutputPerTestCase(true);

		if (jUnitXmlReport.getDestination() == null) {
			jUnitXmlReport.setDestination(project.file("test-results"));
		}
	}

	private void _configureTaskValidatePoshi(
		JavaExec javaExec, Properties poshiProperties,
		PoshiRunnerExtension poshiRunnerExtension) {

		_populateSystemProperties(
			javaExec.getSystemProperties(), poshiProperties,
			javaExec.getProject(), poshiRunnerExtension);
	}

	private void _configureTaskWritePoshiProperties(
		JavaExec javaExec, Properties poshiProperties,
		PoshiRunnerExtension poshiRunnerExtension) {

		_populateSystemProperties(
			javaExec.getSystemProperties(), poshiProperties,
			javaExec.getProject(), poshiRunnerExtension);
	}

	private String _getBrowserType(Properties poshiProperties) {
		String browserType = poshiProperties.getProperty("browser.type");

		if (Validator.isNull(browserType)) {
			return "chrome";
		}

		return browserType;
	}

	private String _getChromeDriverURL(String chromeDriverVersion) {
		StringBuilder sb = new StringBuilder();

		sb.append("https://chromedriver.storage.googleapis.com/");

		sb.append(chromeDriverVersion);

		sb.append("/chromedriver_");

		if (OSDetector.isApple()) {
			sb.append("mac64");
		}
		else if (OSDetector.isWindows()) {
			sb.append("win32");
		}
		else {
			sb.append("linux64");
		}

		sb.append(".zip");

		return sb.toString();
	}

	private String _getChromeDriverVersion(
		Project project, String chromeBinaryPath) {

		if (chromeBinaryPath == null) {
			chromeBinaryPath = "/usr/bin/google-chrome";

			if (OSDetector.isApple()) {
				chromeBinaryPath =
					"/Applications/Google Chrome.app/Contents/MacOS" +
						"/Google Chrome";
			}
			else if (OSDetector.isWindows()) {
				chromeBinaryPath =
					"C:\\Program Files (x86)\\Google\\Chrome\\Application" +
						"\\chrome.exe";

				String bitMode = OSDetector.getBitmode();

				if (bitMode.equals("64")) {
					String chrome64BitBinaryPath =
						"C:\\Program Files\\Google\\Chrome\\Application" +
							"\\chrome.exe";

					if (Files.exists(Paths.get(chrome64BitBinaryPath))) {
						chromeBinaryPath = chrome64BitBinaryPath;
					}
				}
			}

			if (Files.notExists(Paths.get(chromeBinaryPath))) {
				throw new IllegalArgumentException(
					"Unable to find a Google Chrome binary. Manually set " +
						"\"browser.chrome.bin.file\" in \"poshi.properties\" " +
							"to a Google Chrome or Chromium binary.");
			}
		}

		if (OSDetector.isWindows()) {
			chromeBinaryPath = chromeBinaryPath.replace("/", "\\");

			chromeBinaryPath = chromeBinaryPath.replace("\\", "\\\\");
		}

		final String finalChromeBinaryPath = chromeBinaryPath;

		final ByteArrayOutputStream byteArrayOutputStream =
			new ByteArrayOutputStream();

		project.exec(
			new Action<ExecSpec>() {

				@Override
				public void execute(ExecSpec execSpec) {
					System.out.println(
						"Using Google Chrome binary at " +
							finalChromeBinaryPath);

					if (OSDetector.isWindows()) {
						execSpec.commandLine(
							"cmd", "/c",
							"wmic datafile where name=\"" +
								finalChromeBinaryPath +
									"\" get Version /value");
					}
					else {
						execSpec.commandLine(
							finalChromeBinaryPath, "--version");
					}

					execSpec.setStandardOutput(byteArrayOutputStream);
				}

			});

		String chromeVersionOutput = byteArrayOutputStream.toString();

		Matcher matcher = _chromeVersionPattern.matcher(chromeVersionOutput);

		if (matcher.find()) {
			String chromeMajorVersion = matcher.group("chromeMajorVersion");

			if (_chromeDriverVersions.containsKey(chromeMajorVersion)) {
				return _chromeDriverVersions.get(chromeMajorVersion);
			}
		}

		return _DEFAULT_CHROME_DRIVER_VERSION;
	}

	private File _getExpandedPoshiRunnerDir(Project project) {
		return new File(project.getBuildDir(), "poshi-runner");
	}

	private String _getGeckoDriverURL(String geckoDriverVersion) {
		StringBuilder sb = new StringBuilder();

		sb.append("https://github.com/mozilla/geckodriver/releases/download/v");

		sb.append(geckoDriverVersion);

		sb.append("/geckodriver-v");

		sb.append(geckoDriverVersion);

		sb.append("-");

		if (OSDetector.isApple()) {
			sb.append("macos.tar.gz");
		}
		else if (OSDetector.isWindows()) {
			sb.append("win32.zip");
		}
		else {
			sb.append("linux64.tar.gz");
		}

		return sb.toString();
	}

	private File _getPoshiExtPropertiesFile(File poshiPropertiesFile) {
		String fileName = poshiPropertiesFile.getName();

		int pos = fileName.lastIndexOf('.');

		if (pos <= 0) {
			return new File(
				poshiPropertiesFile.getParentFile(), fileName + "-ext");
		}

		String extension = fileName.substring(pos + 1);

		String shortFileName = fileName.substring(
			0, fileName.length() - extension.length() - 1);

		return new File(
			poshiPropertiesFile.getParentFile(),
			shortFileName + "-ext." + extension);
	}

	private Properties _getPoshiProperties(
		PoshiRunnerExtension poshiRunnerExtension) {

		Properties poshiProperties = new Properties();

		List<File> poshiPropertiesFiles = new ArrayList<>();

		poshiPropertiesFiles.add(poshiRunnerExtension.getPoshiPropertiesFile());

		poshiPropertiesFiles.addAll(
			poshiRunnerExtension.getPoshiPropertiesFiles());

		for (File poshiPropertiesFile : poshiPropertiesFiles) {
			if (poshiPropertiesFile != null) {
				if (poshiPropertiesFile.exists()) {
					poshiProperties.putAll(
						GUtil.loadProperties(poshiPropertiesFile));
				}

				File poshiExtPropertiesFile = _getPoshiExtPropertiesFile(
					poshiPropertiesFile);

				if (poshiExtPropertiesFile.exists()) {
					poshiProperties.putAll(
						GUtil.loadProperties(poshiExtPropertiesFile));
				}
			}
		}

		return poshiProperties;
	}

	private FileCollection _getPoshiRunnerClasspath(Project project) {
		Configuration poshiRunnerConfiguration = GradleUtil.getConfiguration(
			project, POSHI_RUNNER_CONFIGURATION_NAME);

		Configuration poshiRunnerResourcesConfiguration =
			GradleUtil.getConfiguration(
				project, POSHI_RUNNER_RESOURCES_CONFIGURATION_NAME);

		Configuration sikuliConfiguration = GradleUtil.getConfiguration(
			project, SIKULI_CONFIGURATION_NAME);

		return project.files(
			poshiRunnerConfiguration, poshiRunnerResourcesConfiguration,
			sikuliConfiguration);
	}

	private File _getWebDriverBrowserBinaryFile(
		Project project, Properties poshiProperties) {

		String url = null;

		String browserType = _getBrowserType(poshiProperties);

		if (browserType.equals("chrome")) {
			String chromeBinaryPath = poshiProperties.getProperty(
				"browser.chrome.bin.file");

			url = _getChromeDriverURL(
				_getChromeDriverVersion(project, chromeBinaryPath));
		}

		if (browserType.equals("firefox")) {
			url = _getGeckoDriverURL(_DEFAULT_GECKO_DRIVER_VERSION);
		}

		if (Validator.isNull(url)) {
			throw new RuntimeException("Unable to get browser driver URL");
		}

		try {
			return FileUtil.get(project, url, null);
		}
		catch (IOException ioException) {
			throw new UncheckedIOException(ioException);
		}
	}

	private String _getWebDriverBrowserBinaryName(Properties poshiProperties) {
		return _webDriverBrowserBinaryNames.get(
			_getBrowserType(poshiProperties));
	}

	private String _getWebDriverBrowserBinaryPropertyName(
		Properties poshiProperties) {

		return _webDriverBrowserBinaryPropertyNames.get(
			_getBrowserType(poshiProperties));
	}

	private File _getWebDriverDir(Project project) {
		return new File(project.getBuildDir(), "webdriver");
	}

	private boolean _isDownloadWebDriverBrowserBinary(
		Properties poshiProperties) {

		String webDriverBrowserBinaryPropertyName =
			_getWebDriverBrowserBinaryPropertyName(poshiProperties);

		if (poshiProperties.containsKey(webDriverBrowserBinaryPropertyName)) {
			return false;
		}

		return Validator.isNull(
			System.getProperty(webDriverBrowserBinaryPropertyName));
	}

	private void _populateSystemProperties(
		Map<String, Object> gradleSystemProperties, Properties poshiProperties,
		Project project, PoshiRunnerExtension poshiRunnerExtension) {

		gradleSystemProperties.putAll(
			poshiRunnerExtension.getPoshiProperties());

		File baseDir = poshiRunnerExtension.getBaseDir();

		if ((baseDir != null) && baseDir.exists()) {
			gradleSystemProperties.put(
				"test.base.dir.name", project.relativePath(baseDir));
		}

		List<String> testNames = poshiRunnerExtension.getTestNames();

		if (!testNames.isEmpty()) {
			gradleSystemProperties.put(
				"test.name", CollectionUtils.join(",", testNames));
		}

		ExtensionContainer extensionContainer = project.getExtensions();

		ExtraPropertiesExtension extraPropertiesExtension =
			extensionContainer.getExtraProperties();

		Map<String, Object> properties =
			extraPropertiesExtension.getProperties();

		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			String propertyName = entry.getKey();

			String[] array = propertyName.split("(?=\\p{Upper})");

			if (Objects.equals(array[0], "poshi") && (array.length > 1)) {
				StringBuilder sb = new StringBuilder();

				for (int i = 1; i < array.length; i++) {
					sb.append(array[i].toLowerCase());
					sb.append('.');
				}

				sb.setLength(sb.length() - 1);

				gradleSystemProperties.put(sb.toString(), entry.getValue());
			}
		}

		if (poshiProperties != null) {
			Enumeration<String> enumeration =
				(Enumeration<String>)poshiProperties.propertyNames();

			while (enumeration.hasMoreElements()) {
				String key = enumeration.nextElement();

				String value = poshiProperties.getProperty(key);

				gradleSystemProperties.put(key, value);
			}
		}

		Properties systemProperties = System.getProperties();

		for (Object object : systemProperties.keySet()) {
			String key = (String)object;

			if (key.equals("user.dir")) {
				continue;
			}

			gradleSystemProperties.put(key, System.getProperty(key));
		}
	}

	private void _populateWebDriverSystemProperties(
		Test test, Properties poshiProperties) {

		String webDriverBrowserBinaryPropertyName =
			_getWebDriverBrowserBinaryPropertyName(poshiProperties);

		if (poshiProperties.containsKey(webDriverBrowserBinaryPropertyName)) {
			return;
		}

		String webDriverBrowserBinaryPropertyValue = System.getProperty(
			webDriverBrowserBinaryPropertyName);

		if (Validator.isNull(webDriverBrowserBinaryPropertyValue)) {
			webDriverBrowserBinaryPropertyValue =
				_getWebDriverDir(test.getProject()) + "/" +
					_getWebDriverBrowserBinaryName(poshiProperties);

			if (OSDetector.isWindows()) {
				webDriverBrowserBinaryPropertyValue =
					webDriverBrowserBinaryPropertyValue + ".exe";
			}
		}

		if (Validator.isNotNull(webDriverBrowserBinaryPropertyValue)) {
			Map<String, Object> systemProperties = test.getSystemProperties();

			systemProperties.put(
				webDriverBrowserBinaryPropertyName,
				webDriverBrowserBinaryPropertyValue);
		}
	}

	private static final String _DEFAULT_CHROME_DRIVER_VERSION = "2.37";

	private static final String _DEFAULT_GECKO_DRIVER_VERSION = "0.31.0";

	private static final String _START_TESTABLE_TOMCAT_TASK_NAME =
		"startTestableTomcat";

	private static final String _STOP_TESTABLE_TOMCAT_TASK_NAME =
		"stopTestableTomcat";

	private static final Map<String, String> _chromeDriverVersions =
		new HashMap<String, String>() {
			{
				put("86", "86.0.4240.22");
				put("87", "87.0.4280.20");
				put("88", "88.0.4324.96");
				put("89", "89.0.4389.23");
				put("90", "90.0.4430.24");
				put("91", "91.0.4472.101");
				put("92", "92.0.4515.107");
				put("93", "93.0.4577.63");
				put("94", "94.0.4606.61");
				put("95", "95.0.4638.17");
				put("96", "96.0.4664.45");
				put("97", "97.0.4692.71");
				put("98", "98.0.4758.102");
				put("99", "99.0.4844.51");
				put("100", "100.0.4896.60");
				put("101", "101.0.4951.41");
				put("102", "102.0.5005.61");
				put("103", "103.0.5060.134");
				put("104", "104.0.5112.79");
				put("105", "105.0.5195.52");
				put("106", "106.0.5249.61");
				put("107", "107.0.5304.62");
				put("108", "108.0.5359.71");
			}
		};
	private static final Pattern _chromeVersionPattern = Pattern.compile(
		"[A-z=\\s]+(?<chromeMajorVersion>[0-9]+)\\.");
	private static final Map<String, String> _webDriverBrowserBinaryNames =
		new HashMap<String, String>() {
			{
				put("chrome", "chromedriver");
				put("firefox", "geckodriver");
			}
		};
	private static final Map<String, String>
		_webDriverBrowserBinaryPropertyNames = new HashMap<String, String>() {
			{
				put("chrome", "webdriver.chrome.driver");
				put("firefox", "webdriver.gecko.driver");
			}
		};

}