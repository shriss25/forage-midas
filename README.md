# Forage JPMorgan Chase Midas Core Internship Project 

This project is part of the [JPMorgan Chase Software Engineering Virtual Experience](https://www.theforage.com/) on Forage. It simulates real-world development tasks at a financial institution, focusing on Kafka, Spring Boot, REST APIs, and H2 database.

---

##  Tasks Completed

###  Task 1 – Data Feed Interface
- Set up the project and verified task flow.

###  Task 2 – Kafka Integration
- Implemented Kafka consumer to receive transactions.
- Embedded Kafka used for testing.

###  Task 3 – Database Integration
- Used H2 in-memory database with Spring Data JPA.
- Validated and saved transactions with sender/recipient updates.

###  Task 4 – Incentive API Integration
- Connected external incentive REST API to add reward logic.
- Stored incentive amounts alongside each transaction.

###  Task 5 – Balance API
- Exposed `/balance` GET endpoint to retrieve user balance.
- Deployed on port `33400`.

---

##  Technologies Used

- Java 17
- Spring Boot
- Apache Kafka
- H2 Database
- REST APIs
- Maven

---

##  Project Structure

src/
├── main/
│ ├── java/com/jpmc/midascore/
│ ├── entity/
│ ├── repository/
│ ├── foundation/
│ └── resources/
├── test/
└── docker-compose.yml


---

##  How to Run

1. Clone the repo
2. Run `docker-compose up -d` to start Kafka and Zookeeper
3. Start incentive API JAR
4. Run the application with `mvn spring-boot:run`
5. Use Postman or browser to hit: `http://localhost:33400/balance?userId=1`

---

##  Author

- **Shrishti** – 3rd Year B.Tech CSE @ Delhi Technological University  
- GitHub: [@shriss25](https://github.com/shriss25)

---

##  Note

This project is a simulation and is not for production use.
