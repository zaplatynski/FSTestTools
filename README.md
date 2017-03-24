# FS Test Tools

This is a project to support FirstSpirit module developers with writing unit test ([JUnit](http://junit.org/) w/ [Mockito](http://site.mockito.org/)) for FirstSpirit components such as executables, services or the module.xml deployment descriptor.

## How to use

### Step 1

There are two possibilities to get the FS Test Tools installed:
- *Use Binary Release* or
- *Build Release yourself*

You can skip *Use Binary Release* if you have access to e-Spirit's artifactory.

#### Using Binary Releases

Use the GitHub release here to install them:
```
mvn install:install-file -Dfile=<path-to-file> -DpomFile=<path-to-pomfile-of-file>
```
For instance (if the version is 1.2.3):
```
mvn install:install-file -Dfile=mocking-1.2.3.jar -DpomFile=mocking-1.2.3.pom
```
See [official Maven mini guide](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html) for more information

#### Build Release yourself

First you need to install the Maven artifacts locally in your Maven repository:

```shell
mvn clean install -Dci.version=Your-Version
```

_Your-Version_ could be anything like _1.2.3_.
The build will create following artifacts:
* mocking-_Your-Version_.jar,
* rules-_Your-Version_.jar and
* tests-_Your-Version_.jar

### Step 2

Then you can add them inside your own project's pom.xml dependency section (e.g. for the _mocking_ artifact):

```xml
...
<dependencies>
    ...
    <dependency>
        <groupId>com.espirit.moddev.fstesttools</groupId>
        <artifactId>mocking</artifactId>
        <version>Your-Version</version>
        <scope>test</scope>
    </dependency>
    ...
</dependencies>
...
```
You can replace _Your-Version_ with _LATEST_ if you have access to e-Spirit's artifactory.

### Step 3

#### Using Mocking Contexts

Now you can use the e.g. the `MockingBaseContext` inside your unit tests:

```java
public class MyTest {
    ...
    private BaseContext context;

    @Before
    public void setUp() throws Exception {
        context = new MockingBaseContext(Locale.ENGLISH, BaseContext.Env.PREVIEW);
        ...
    }
    
    @Test
    public void testSomeThing() throws Execption {
        // Use normal FS API to get mocks of classes such as agents:
        FormAgent formAgent = context.requireSpecialist(FormAgent.TYPE);
        // Use Mockito to tell what the mock should do
        when(formAgent.anyMethod()).thenReturn(...);
        ...
    }
    ...
}
```

#### Using The JUnit Rules

At the moment there are only two types of Junit Rules available:
* Two rules to support you with logging
* One rule to support you with FirstSpirit client actions

**Logging rules**

If both logging rules are used, then the logging system is initialized (`InitLog4jLoggingRule`) and in the log there a message at the beginning and the end of each test (`LogTestMethodNameRule`).
This is usefull if there are many log lines and it is needed to know where a tests starts and stops. 

```java
public class MyTest {
    
    @ClassRule
    public static InitLog4jLoggingRule loggingRule = new InitLog4jLoggingRule();
    
    @Rule
    public LogTestMethodNameRule rule = new LogTestMethodNameRule();
    
    @Test
    public void testSomeThing() throws Execption {
        
        ...
    }
    ...
}
```

Example log output:
```
1 [main] INFO MyTest  - Start of 'testSomeThing'...
...
5 [main] INFO MyTest  - Successful termination of 'testSomeThing'!
```

**FirstSpirit Connection Rule**

The FirstSpirit Connection Rule is a FirstSpirit client for integration tests.
The rule maintains the connection, e.g. opens and close it according to test life cycle.
The prerequisite is a running FirstSpirit server.
By default the host `localhost` with port `8000` and user `Admin` with password `Admin` is used.
Then the rule can be used to execute a schedule, import zips (e.g. exported templates) or modify the content of (page) templates.
The feature set of the rule is not final or complete but can be extended easliy.
If you like to have some new functionality you are welcome to provide it by adding a so-called command (see next paragraph below).

```java
public class MyTest {
    
    @Rule
    public FirstSpiritConnectionRule rule = new FirstSpiritConnectionRule();
    
    @Test
    public void testSomeThing() throws Execption {
        MyParameters parameters = new MyParameters(...);
        MyResult result = rule.invokeCommand("nameOfCommand", parameters);
        ...
    }
    ...
}
```

All available commands can be found in the package [com.espirit.moddev.fstesttools.rules.firstspirit.commands](rules/src/main/java/com/espirit/moddev/fstesttools/rules/firstspirit/commands).

**Create Your Own FirstSpirit Connection Rule Commands**

There is small example whithin the test package [com/espirit/moddev/fstesttools/rules/firstspirit/commands](rules/src/test/java/com/espirit/moddev/fstesttools/rules/firstspirit/commands) which is used by the class [FirstSpiritConnectionRuleTest](rules/src/test/java/com/espirit/moddev/fstesttools/rules/firstspirit/FirstSpiritConnectionRuleTest.java).

Basically you need to implement these tree classes where the command class must be in the package `com.espirit.moddev.fstesttools.rules.firstspirit.commands` (the rule will scan for it) or below:
```java
public class MyParameters implements FsConnRuleCmdParamBean {
    
    @javax.inject.Inject
    private Connection firstSpiritConnection;
    
    // Or:
    //@Inject
    //private BaseContext baseContext;
    
    // Or:
    //@Inject
    //private SpecialistBroker broker;
    
    ...

    @Override
    public String getProjectName() {
        //If no project is needed return here null
        return null;
    }

    ...
}

public class MyResult implements FsConnRuleCmdResultBean {
    ...
}

public class MyCommand implements FsConnRuleCommand<MyParameters, MyResult>{
    
    @Override
    public String name() {
        return "Foobar";
    }

    @Override
    public MyResult execute(final MyParameters parameters) {
        ...
        return new MyResult(...);
    }
}
```

With *dependency injection* (see [JSR 330](https://www.jcp.org/en/jsr/detail?id=330) or `@javax.inject.Inject` annotation above) the FirstSpirit Connection Rule will provide a `Connection`, a `BaseContext`, a `SpecialistBroker` or a `GenerationContext` which will be the foundation of your command.

#### Using The Module-Xml-Test

The parent class `AbstractModuleXmlTest` expects a `module.xml` in the root of the class path.
Futhermore there must be a file called `moduleXmlTest.properties` in the test resource which must be filtered too (for instance):
```ini
name=${project.groupId}.${project.artifactId}
displayName=${project.name}
description=${project.name} (${project.artifactId})
version=${project.version}
vendor=${organization.name}
```

If the pom's variables or properties do not apply at your project because you use static strings then use static values inside the `moduleXmlTest.properties`.
The basic idea behind the pom's variables is to have one place to maintain a module's name, display name etc.
To enable filtering just add this to your `pom.xml`:

```xml
<project>
    ...
    <build>
        <testResources>
            <testResource>
                <directory>src/test/resources</directory>
                <filtering>true</filtering>
                <includes>
                    <include>*.properties</include>
                </includes>
            </testResource>
            <testResource>
                <directory>src/test/resources</directory>
                <filtering>false</filtering>
                <excludes>
                    <exclude>*.properties</exclude>
                </excludes>
            </testResource>
        </testResources>
        ...
    </build>
    ...
</project>
```

After this you need to subclass the `AbstractModuleXmlTest` with a `ModuleXmlTest` and implement the class loader method:

```java
public class ModuleXmlTest extends AbstractModuleXmlTest {

    @Override
    protected ClassLoader getTestClassLoader() {
        return getClass().getClassLoader();
    }

    // Additional Tests are possible, for instance
    @Test
    public void testThatConstanceValuesEqualsNamesInModuleXml() throws Exception {
        assertThat("Expected specific value", getModuleXML(),
                   hasXPath("/module/components/project-app/name", equalTo("MyTechnicalProjectAppName")));

    }
}
```
This done you get some basic tests for free:
* Name
* Display name
* Version
* Vendor
* Existance of full qualified class names of class and configurable XML tags

Of cause you can add some own test as you like by using the Hamcrest XPath Matchers (see above).

## More Information

Regarding unit test philosophy the `MockingBaseContext` is a [fake](https://www.martinfowler.com/articles/mocksArentStubs.html) which produces [mocks](https://www.martinfowler.com/articles/mocksArentStubs.html) (of agent or service implementations) with the help of the [Mockito](http://site.mockito.org/) library.
So it acts as a test drop-in replacement for the real FirstSpirit `BaseContext`.
But it is not only a simple mock factory because it returns always the same mock object instance.
So the mocks are always [singletons](https://en.wikipedia.org/wiki/Singleton_pattern).

### More help needed?

If there are any further questions regarding the *FS Test Tools* please go to the [FirstSpirit Community Developers Aera](https://community.e-spirit.com/community/developer) and post them there.

## Disclaimer

FirstSpirit and this project are developed by the [e-Spirit AG](http://www.e-spirit.com).
The head office of the e-Spirit AG is in Dortmund, Germany.

Use this project and provided binaries at your own risk.
