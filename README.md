> Personal implementation and offline validation: see [LAB5_PROGRESS.md](LAB5_PROGRESS.md). Live API/team activities are not claimed complete.

# Lab 5: Using APIs in Java

## Overview:
In this lab, you and your team will learn to work with web API calls.
With this technical programming skill in hand, your team will be one step closer
to implementing your project.

> Interacting with web APIs can be a bit tricky at first,
> but if your team works together then everyone should have a good understanding
> of how to make web API calls by the end of this lab.

## Background: What Is a Web API and How Do I Use One?

A call to a web API is similar to a method call, but it is made over the internet.
You send a request to a server and receive a response, often in JSON format.
For example, there is a web API for cat facts called "meowfacts" which is 
accessible at https://meowfacts.herokuapp.com.
If you navigate to this URL in your web browser, you will be returned one random cat fact.
Below is one such cat fact received from the API:

```json
{
  "data": [
    "In ancient Egypt, when a family cat died, all family members would shave their eyebrows as a sign of mourning."
  ]
}
```

Just like in a Java method call, web API calls can have input parameters.
These are often passed as **query parameters** in the URL.

For instance, the meowfacts API can return multiple cat facts at once when
you provide a count parameter.
To do so, you append "?count=x" to the meowfacts API URL where `x` is the 
number of cat facts you would like to request.

For example, by navigating to https://meowfacts.herokuapp.com/?count=5 in 
your web browser, you will be returned five facts.
Below is an example response from the API:
```json
{
  "data": [
    "Cats can not taste sweetness.",
    "A group of cats is called a clowder.",
    "Cats can get tapeworms from eating mice. If your cat catches a mouse it is best to take the prize away from it.",
    "You check your cats pulse on the inside of the back thigh, where the leg joins to the body. Normal for cats: 110-170 beats per minute.",
    "The worlds richest cat is worth $13 million after his human passed away and left her fortune to him."
  ]
}
```

While web APIs can be accessed by navigating to their URLs in your web browser, 
they can (and usually are) accessed programmatically.
In this lab you will use the `OkHttp` library to access a web API developed for 
this course (see 
https://square.github.io/okhttp/ for documentation of the `OkHttp` library).

A minimal code example for accessing the meowfacts API discussed above using 
the `OkHttp` library is provided below and in class `GetExample` in the `example` package.

* Try running `GetExample` at this point. If the project doesn't run,
  please see the advice at the bottom of the README. This class demonstrates making
  two calls to the API. One call with no parameters, and a second call with a parameter.

```java
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetExample {
  final OkHttpClient client = new OkHttpClient();

  String run(String url) throws IOException {
    Request request = new Request.Builder()
        .url(url)
        .build();

    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

  public static void main(String[] args) throws IOException {
    GetExample example = new GetExample();
    String response = example.run("https://meowfacts.herokuapp.com");
    System.out.println(response);
  }
}
```

The output of the above will be a single cat fact in JSON format as we saw when
making the call through our web browser. This JSON formatted response from the API
could then be parsed and used by our program.

[GetExample](src/main/java/example/GetExample.java) also includes an overloaded version of the `run` method that
demonstrates passing in a value for the `count` parameter:

```java
public String run(String baseURL, String paramKey, String paramValue) throws IOException {
        HttpUrl url = HttpUrl.parse(baseURL).newBuilder()
                .addQueryParameter(paramKey, paramValue)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
```

The code is _almost_ the same, except that we need to add the `count` parameter onto the
URL before making our request to the API. This is done using the `HttpUrl` class and its methods. 

> In IntelliJ, you can hover over the various `OkHttp` related classes and methods
> to read their documentation and learn more about how they work.

Now that we know the basics of making calls to web APIs,
we are ready to get started on today's lab!

## Task 0: Fork and clone this repo

Have one member of your team fork this repo and invite the others to collaborate on their fork.
You can do this in your repo on GitHub under `Settings -> Collaborators`.
This will allow you to push branches to a common repo and then use pull requests to contribute
your code and review. To prevent others from pushing directly to the main branch,
we recommend you set branch protection rules on GitHub.

Below is an example of what the settings might look like after
adding branch protection rules:

![image of branch protection rules for main
with the requirement of two approvers to
merge in pull requests.](images/branch_protection_rules.png "branch protection rules")

After everyone is invited to your team's repository, everyone should clone 
the repository so that they have a local copy.

## Task 1: Your API Token (token)

In this lab, we will be working with the Grade API. Unlike the meowfacts API, this API
requires a user to provide a `token` in order to use it. This is common for many APIs, as
it is necessary to limit access and monitor API usage.

In order to use the Grade API, you will need to sign up with a username and obtain an API token.
To do so, we are going to make a simple request to the Grade API.

You can do this either through your web browser or using `OkHttp` in the [GetToken](src/main/java/example/GetToken.java) class: 

* If using your web browser, enter `https://csc207-20269-vm.teach.cs.toronto.edu/signUp?username=USERNAME`
with USERNAME replaced with the username you want to use.

* If you want to use OkHttp, complete the TODO in `GetToken.main`.

> Throughout this lab, you can refer to [the Grade API Postman Workspace](https://www.postman.com/cloudy-astronaut-813156/csc207-apis-2026)
> for the documentation of all API requests related to the Grade API.
> Specifically, to learn more about the SignUp API request,
> refer to [this section of the workspace](https://www.postman.com/cloudy-astronaut-813156/csc207-apis-2026/request/rq6uy9x/1-1-sign-up-when-a-username-is-available).
> The documentation on Postman includes code snippets. You can set the language to `Java - OkHttp` to get
> code snippets compatible with OkHttp. If you sign in with a Postman account, you can also run example API
> calls through Postman. This isn't necessary to complete the lab activities though.

After you have obtained the response, you need to save your token somewhere, as you will need it
throughout the rest of the lab.

The response will look something like:

```json
{"environment_variables":"token=<YOUR_UNIQUE_TOKEN>",
   "message":"User signed up successfully...",
   "status_code":200,
   "token":"<YOUR_UNIQUE_TOKEN>"}
```

* Create a file called `token.txt` beside `pom.xml` in the top-level folder of your project. 
* Copy and paste the value you found for the `token` field in that file, as well as your username,
  so that you don't forget them.

> This file is indicated in the `.gitignore` for your project, so its name will
> appear highlighted in IntelliJ to indicate that git will ignore the file (it won't be version 
> controlled). **This can be useful to ensure that you don't accidentally share private information
> (like personal API tokens) or configurations specific to your local environment when working on a
> team project.**

Now that you have your API token, the last step is to get your program to use it. To do so, we
are going to set an `environment variable` in the run configuration.

* Try running `Application` (in [Application](src/main/java/app/gui/Application)).

When you start the program, you will see that it says `your token is null` (since we didn't set it yet).

* Stop the program and go to `Run -> Edit Configurations...`.

* Open the Run Configuration for `Application` and find the `Environment Variables:`
   field.
   - Note: If you don't see this Run Configuration listed:
      - create a new Run Configuration of type `Application` (use the +
        in the top left corner of the window).
      - where it says "Main class", type `app.gui.Application`.

* In that field, paste the text `token=TOKEN`, with `TOKEN` replaced with your token. Your token should also be in your `token.txt` file.
    Example: `token=6SgDAt8XpnQYTDPt4vHcPCCKJ2ppLg1C`.
* Click `Apply` and then `OK`.
* Now, rerun the program and you should see your `token` displayed.
* Click on the `Log Grade` button and in the next screen, enter `207` as the course, enter a valid grade for this course,
  and click `Log`. You should see a popup telling you that your grade was successfully entered.
  You can then check your grade by using the `Get Grade` menu and specifying your username and `207` for the course.

You are now ready to use the program!
Make sure everyone on your team has successfully completed the steps above before you move on.

> An alternative to manually setting environment variables as was done above is to
> set up a `.env` file. Your team may find using a `.env` file convenient when working on
> your projects if you use an API requiring a token.

## Task 2: Forming a team

As a team-building exercise, you will now work together using this application.
In this program, team members can view one another's grades.

1. Choose a team name. Make it something unique to your team, as other teams will also
   be picking team names and duplicate names aren't allowed in the Grade API.

2. Have one member of your team form a team with the name your team chose
   by clicking the `Form a team` button, entering the team name in the text field, and pressing `Submit`.

3. Each other member of the team should then join the team using the `Join a team` menu. Make sure you see the popup
   confirming that you successfully joined the team.

4. Try looking up the grade another team member entered for `207` using the `Get a Grade` menu.

Now that you are all on the team, there are a couple coding tasks for your team to work on.

***

> Note: If your team finds it convenient to work on parts of this lab on a common machine,
> you can create different run configurations (copy an existing one) which each use a different
> `token` environment variable. Then you can run multiple instances of the program and
> enter requests as different users!

***

## Task 3: Coding the Get Average Grade feature

While this program has some useful core functionality which is provided by the Grade API,
there are certain things that the Grade API can't currently do for us.

1. Go to `My Team` menu, enter the course name as `207` and click on `Get average grade`.
   You will notice that the functionality for `Get Average Grade` isn't implemented yet
   — it will display `Average Grade: 0.0`. 
2. As a team, your goal is to work together to implement this feature and confirm that it works.
3. Trace the execution flow from the `Get Average Grade` button in
   [Application.java](src/main/java/app/gui/Application.java) until you reach
   [GetAverageGradeUseCase](src/main/java/usecase/GetAverageGradeUseCase.java).
   You may use either debugger breakpoints or manual code tracing.
4. Complete Task 3a in [GetAverageGradeUseCase](src/main/java/usecase/GetAverageGradeUseCase.java)
   and Task 3b in [MongoGradeDataBase](src/main/java/api/MongoGradeDataBase.java).
   The comments in the starter code provide hints.

You can run the provided tests to check if your "Task 3a" `GetAverageGradeUseCase` logic is
implemented correctly, _before_ you finish implementing the "Task 3b" `MongoGradeDataBase.getMyTeam`
method:

`GetAverageGradeUseCaseMockitoTest` (mocked tests using Mockito)

`GetAverageGradeUseCaseTest` (mocked tests using a stub database)

> These tests make use of a technique called mocking (or stubbing), where instead of calling
> the _actual_ API we use fake data instead. This is possible because of how the program is designed,
> with the `GradeDataBase` interface allowing us to replace any variable with reference type `GradeDataBase`
> with our own dummy implementation.

## Task 4: Coding the Get Top Grade feature

In this task, you are required to build a feature that will allow the user to get the top grade on
their team for a given course.

1. Similar to `Get Average Grade`, you will need to implement the `Get Top Grade` feature.
2. However, in this feature, the `Get Top Grade` usecase is already implemented in the `GetTopGradeUseCase` class.
3. You will need to implement the UI for this feature by adding a button `Get Top Grade` in the `My Team` menu.
You can draw inspiration from the `Get Average Grade` button.

## Task 5 Bonus

If your team finishes early and has extra time, we encourage you to think of an
additional use case that can be implemented using this Grade API.
Implement this use case end-to-end, from the UI layer to the UseCase layer, and see if you can get it working.
This will be good practice for when your team starts developing your own course project. 

For example, new users are currently created by directly calling the Grade API.
It might be nice if that functionality were incorporated into the application itself.

***

## Note about Authentication

We are using a very simple authentication method: passing our API `token` in the request headers.
When you use other real-world APIs in the future, you will encounter more sophisticated authentication techniques.

## Further Reading: Tools for Exploring APIs (Optional)

You can also use tools like Postman and Hoppscotch to experiment with making calls to web APIs.
These tools are useful since some web API requests can not be directly made through your web browser.
When later working on your project or future projects, you may find such tools
to be quite convenient when first exploring and learning about a new web API.

The following explains the steps of how one could have used Hoppscotch to obtain a token for the Grade API.

1. Go to https://hoppscotch.io. This is a tool that can be used to quickly interact with APIs.
2. Beside the word GET,
   replace `https://echo.hoppscotch.io/` with `https://csc207-20269-vm.teach.cs.toronto.edu/signUp`.

Next, we need to specify the username which we want to use. To do this, we add a parameter.

3. In the Parameters tab, choose `+ Add new` and set the parameter name to `username`.
4. For its value, choose whatever username you want to use. Make note of the name you choose,
   as you'll use it during the lab. (Don't worry, you can repeat this process with a new username
   if you need to later!)
5. Press the `Send` button to make the request. You may receive an error that the request didn't
   go through. Scroll down and select `proxy` as the middleware and press `Send` again. You should
   now see the result of the request.
6. If the username had been used before, you will see a response indicating that.
   Choose a new username and send the request again.
7. If the request is successfully sent, you will see a response that contains a `status_code` of
   value `200`. In this response, you will get two important fields: `token` and
   `environment_variables`.

## Troubleshooting

You should be able to compile and run the project, but it is possible that the Maven
project didn't automatically build. If you have errors that won't let you run the code in the
next step, you may need to reload the Maven project. You can do this by right-clicking on the
`pom.xml` file in your project. In the context menu, choose `Maven -> Sync Project`. This _should_
resolve any errors. You may also need to go to `Project Structure...` and select your project JDK.
If errors persist, you may need to restart IntelliJ or unlink and relink the Maven project.
