# Singleton Coverage Concern

### Overview
- Document contains project setup instructions and coverage concern details related singleton implementation

### Requirements
- Docker

## Getting Started

### Setup
Run the following commands:

- Terminal 1

    ```Cosnole
    git clone https://github.com/Shawn-Armstrong/SingletonCoverage
    cd SingletonCoverage
    docker compose build
    docker compose up
    ```

- Terminal 2
      
    ```console
    docker compose exec java17 sh
    ./gradlew build
    ```

### Generate coverage report
- Run tests and generate report with the following command:
    
  ```console
  ./gradlew test jacocoTestReport
  ```
- Report is saved in `../app/build/jacocoHtml/index.html` and can be opened in any browser

## Problem

### Background
- Project contains `MySingletonBean.java` which implements the singleton pattern using the classical double check locking approach
- The false branch of the nested null check is the only uncovered line; flow is case 3


<kbd><img src="https://github.com/user-attachments/assets/d39471a4-e12c-4a71-a68b-2ea25432d550" width="500"></kbd>



### Case 1
- Trivial case with single thread representing happy path
  
```mermaid
sequenceDiagram
    participant Thread1
    participant SingletonClass

    Thread1 ->> SingletonClass: Is SingletonClass null?
    Note over SingletonClass: Instance is null
    Thread1 ->> SingletonClass: Thread1 enters synchronized block
    Thread1 ->> SingletonClass: Is instance null?
    Note over SingletonClass: Instance is still null
    Thread1 ->> SingletonClass: Call constructor
    SingletonClass ->> SingletonClass: Initialize instance
    SingletonClass ->> SingletonClass: Assign instance to static field
    Thread1 ->> SingletonClass: Exit synchronized block
    Thread1 -->> SingletonClass: Return instance
 ```
    
### Case 2
- Trivial case with two threads representing sequential execution

```mermaid
sequenceDiagram
    participant Thread1
    participant Thread2
    participant SingletonClass

    Thread1 ->> SingletonClass: Is SingletonClass null?
    Note over SingletonClass: Instance is null
    Thread1 ->> SingletonClass: Thread1 enters synchronized block
    Thread1 ->> SingletonClass: Is instance null?
    Note over SingletonClass: Instance is still null
    Thread1 ->> SingletonClass: Call constructor
    SingletonClass ->> SingletonClass: Initialize instance
    SingletonClass ->> SingletonClass: Assign instance to static field
    Thread1 ->> SingletonClass: Exit synchronized block
    Thread1 -->> SingletonClass: Return instance
    Thread2 ->> SingletonClass: Is SingletonClass null?
    Note over SingletonClass: Instance is not null
    Thread2 -->> SingletonClass: Return existing instance
```

### Case 3
- Contentious case, two threads that arrive at critical section at nearly identical times
- thread1 enters first, thread2 yields
- This is the only case where the nested null check evaluates to false
    
```mermaid    
sequenceDiagram
    participant Thread1
    participant Thread2
    participant SingletonClass

    Thread1 ->> SingletonClass: Is SingletonClass null?
    Note over SingletonClass: Instance is null
    Thread1 ->> SingletonClass: Enters synchronized block
    Thread2 ->> SingletonClass: Is SingletonClass null?
    Note over SingletonClass: Instance is null
    Thread2 ->> SingletonClass: Attempts to enter synchronized block<br>Block is occupied<br>Thread2 yields
    Thread1 ->> SingletonClass: Is instance null?
    Note over SingletonClass: Instance is still null
    Thread1 ->> SingletonClass: Call constructor
    SingletonClass ->> SingletonClass: Initialize instance
    SingletonClass ->> SingletonClass: Assign instance to static field
    Thread1 ->> SingletonClass: Exit synchronized block
    Thread2 ->> SingletonClass: Enter synchronized block<br>Is SingletonClass null?
    Note over SingletonClass: Instance is not null
    Thread1 -->> SingletonClass: Return first instance
    Thread2 -->> SingletonClass: Return existing instance
 ```

