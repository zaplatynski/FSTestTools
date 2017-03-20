# FSTestTools

Project to support FirstSpirit module developers with writing unit test ([JUnit](http://junit.org/) w/ [Mockito](http://site.mockito.org/)) for FirstSpirit components such as executables.

## How to use

### Step 1

First you need to install the Maven artifacts locally in your Maven repository:

```shell
mvn clean install -Dci.version=Your-Version
```

_Your-Version_ could be anything like _1.2.3_.
The build will create following artifacts:
* mocking-_Your-Version_.jar,
* rules-_Your-Version_.jar and
* tests-_Your-Version_.jar

You can obmitt *Step 1* if you have access to e-Spirit's artifactory.

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

### More Information

Regarding unit test philosophy the `BaseMockingContext` is a [fake](https://www.martinfowler.com/articles/mocksArentStubs.html) which produces [mocks](https://www.martinfowler.com/articles/mocksArentStubs.html) (of agent or service implementations) with the help of the [Mockito](http://site.mockito.org/) library.
So it acts as a test drop-in replacement for the real FirstSpirit `BaseContext`.
But it is not only a simple mock factory because it returns always the same mock object instance.
So the mocks are always [singletons](https://en.wikipedia.org/wiki/Singleton_pattern).

## More help needed?
If there are any further questions regarding the *FSTestTools* please go to the [FirstSpirit Community](https://community.e-spirit.com/) and post them there.

## Disclaimer

FirstSpirit and this project are developed by the [e-Spirit AG](http://www.e-spirit.com).
The head office of the e-Spirit AG is in Dortmund, Germany.

Use this project and provided binaries at your own risk.
