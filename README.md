# Seek MAX - Take Home Assignment

## Tasks
### Scenario
At Seek Asia we want to re haul our candidate experience for Job Applications.
Your team is tasked to build a mobile app (iOS or Android) (it is up to you which framworks, languages etc you want to use) to connect to an already existing Backend.


Please refer to the following list of tasks and finish as many as you can within 4 hours maximum, due to time limits if you take a shortcut or create a tech debt maintain a todo list of what could've been done better.


The tasks are not priorised in any particular order.


Some of the tasks might require backend work, it's up to you to focus on tasks that exhibit your strength, feel free to mock the backend implementation if you find that better use of your time, and highlight that in your todo.


Pay more attention to detail and quality, rather than finishing as many tasks as possible. Document your approach in a meaningful way, so that other developers later can easily understand it.

Use a source code management strategy that serves the purpose for submission. (For example via github repository)

### Requirements
* Add a login screen to authenticate a user with the application
  * Needed Input fields: Username & Password and a Button
* The logged-in session needs to be stored
  * The form of the Session will be a JWT-Token
* Add Logout capabilities
* Create a `My Profile` Tab, via which candidates can adjust profile attributes (for example name)
  * Also requires Backend Work
* Add Password Change Capabilities
  * Also requires Backend Work
* Show list of active (where the status is `PUBLISHED`) job ads (with either pagination or lazy loading) (refer to `/design/JobList.png`)
* When clicking on an available job, show a new screen with more details and the option to apply
* Add an indicator to both the list and the details screen, whether the currently authenticated user already applied to a specific job
* Add search capabilities
  * Also requires Backend Work
* Tab for `Show My Applications`, which should show all the current authenticated users applications (need to create a new backend endpoint as well)
  * Also requires Backend Work

### Helpful Resources
Colors: `/design/ColorDesign.png`
Fonts: `/design/TextDesign.png`

## Backend

A basic backend implementation have been provided in the attached backend folder with a GraphQL -> NodeJS Rest API -> MongoDB setup.

The backend can be ran through docker.

### Prerequisites
1. Install Docker and docker-compose

### Run
```shell
docker-compose up -d
```

### Existing Logins
```shell
user1: Seeker1123
user2: Seeker2123
user3: Seeker3123
```

### Documentation
You can refer to the GraphQL Documentation in `backend/graphql/doc/index.html` (open in a browser of your choice)