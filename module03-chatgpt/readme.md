# Todo Items Application

This is a simple web application that allows users to manage todo items. Users can create, read, update, and delete todo items using RESTful API endpoints.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Gradle

## Prerequisites

Before running the application, make sure you have the following prerequisites installed:

- Java Development Kit (JDK) 8 or higher
- MySQL Server
- Git (optional)

## Getting Started

To run the application, follow these steps:

1. Clone the repository (if you haven't done so already):
git clone <repository-url>
2. Navigate to the project directory:
cd todo-items-application
3. Open the application.properties file located in the src/main/resources directory.
4. Configure the MySQL database connection properties:
spring.datasource.url=jdbc:mysql://localhost:3306/todo_items_database?useSSL=false
spring.datasource.username=root
spring.datasource.password=root
Replace todo_items_database with the name of your MySQL database, and provide the appropriate username and password.
5. Save the changes to the application.properties file.
6. Build the application using Gradle:
./gradlew build
7. Run the application:
./gradlew bootRun
8. The application should now be running on http://localhost:8080. You can access the API endpoints using a tool like cURL or a web browser.

## API Endpoints

Here are the available API endpoints for managing todo items:

- **Create a Todo Item (POST):** `http://localhost:8080/todo-items`
- **Read All Todo Items (GET):** `http://localhost:8080/todo-items`
- **Read a Todo Item by ID (GET):** `http://localhost:8080/todo-items/{id}`
- **Update a Todo Item (PUT):** `http://localhost:8080/todo-items/{id}`
- **Delete a Todo Item (DELETE):** `http://localhost:8080/todo-items/{id}`

Replace `{id}` with the actual ID of the todo item you want to interact with.


Contributing
Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

License
This project is licensed under the MIT License.
Please make sure to adapt the instructions to match the Gradle build commands and project structure of your specific application.

* Development process feedback  
I tried my best to write 0 lines of code manually, so it took a lot of time to get fully working system  
However, ChatGPT was able to create 60-70% of work almost in no time, about 10% of time spent in general  
The most challenging part is tuning solution because model 3.5 has not unlimited (and not enough) context memory  
If using this model to ASSIST coding but not for full development, it would probably take about 2 hours to complete this task

1. Was it easy to complete the task using AI?  
* It wasn't hard, not really easy though because it takes something to make prompts clear and effective

2. How long did task take you to complete? (Please be honest, we need it to gather anonymized statistics)  
* To create more or less working solution +- 1 hour, to tune everything about 5 more. So it's about 6 hours  

3. Was the code ready to run after generation? What did you have to change to make it usable?  
* Usually code is ready to run, however, errors occur if ChatGPT for some reason doesn't consider other components

4. Which challenges did you face during completion of the task?  
* I tried to make solution using only ChatGPT, which sometimes has limited power. For example there were issues I noticed immediately,
but model wasn't even close to consider those reasons, so I had to explicitly tell / give hints for it to come up with solution

5. Which specific prompts you learned as a good practice to complete the task?  
* Personally I've discovered that it's very convenient to give model full components' codebase to create solution which complies with existing code
