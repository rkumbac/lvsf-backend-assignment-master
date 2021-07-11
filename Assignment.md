## Assignment
The purpose of this is simply to get a small sample of some of your work, and have something concrete to discuss next time we see you.

We respect and appreciate your time: please do not spend more than a few hours of your time on this.
Feel free to turn in what you have at the end of that time, and provide a brief explanation of what you would do next, given more time.

## Task
Your task is to finish implementing two server endpoints for getting 'tips'.

The tips are sourced from two different legacy systems, which are unfortunately rather inconsistent in their naming and representation.
The requirement is to serve them up efficiently, in a single format, as JSON. The exact JSON schema is up to you.

The legacy tip data can be found as CSV files in the resources folder.
We've made the data available via the `LegacyTipsService`, however, so
you will not need to write code the load these files: you can imagine the data is coming directly from legacy service calls.
These services return raw CSV payloads of their tips, as Strings.

To keep things simple, you can assume that the legacy data will not change
during the course of your application's lifetime. No need to worry about
fetching updated data or change-tracking.

There are a finite number of tip types:
- disturbance
- suspicious activity
- bad smell
- arson
- violence

A tip consists of 4 fields:
- a universally unique identifier
- a description
- submission date
- a type

The two endpoints to implement are 
- get tips sorted by submission date (newest first) (`GET /tips`)
- get tip by id (`GET /tips/:id`)

We've provided a in-memory `Repository` class which can be used to 'store' the tips.
It simulates programming against a database or cache service by providing an asynchronous interface for reading and writing data.

The project dependencies, HTTP server code, and bootstrapping code are already
in place, along with some very basic unit tests.

Use the "FIXME" comments littered around the project as guides to direct your attention.

Feel free to alter any part of the code provided if you feel it is warranted.

## Running the project
 ```
 $ sbt run
 ```
from the root of the repository. This will fetch all dependencies and
start the HTTP service on port 8080.

## Testing the project
 ```
 $ sbt test
 ```
from the root of the repository.

## Submission
For submission, ensure your changes are committed to your current local branch and run the following:
```
$ git format-patch origin/master
```
This will generate one .patch file per new commit. Please zip these files up into a single archive and provide this as your submission.
