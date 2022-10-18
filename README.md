# Project Setup Guide for Windows

## Prerequisites

1. Java 1.8

Refer to the [Java 1.8 installation guide](https://www.java.com/en/download/help/windows_manual_download.html) for Windows.

2. Maven 3.8

Refer to the [Maven installation guide](https://maven.apache.org/install.html) for Windows.

3. Mysql 8

Refer to the [Mysql installation guide](https://www3.ntu.edu.sg/home/ehchua/programming/sql/MySQL_HowTo.html) for Windows.

Open cmd, 'npm install -g @angular/cli'

## Run the Application Locally
1. Open cmd, navigate to the directory where you want to download the source code.

2. Run 'git clone https://github.com/YaoAtTNTS/assignment-backend.git' to download the source code.

3. Run 'cd ./assignment-backend' to navigate to the project root dir.

4. Run 'mvn clean package' 

5. Run 'cd ./target' to navigate to the project jar package dir.

6. Run 'java -jar assignment-1.0-SNAPSHOT' to start the application.

7. Keep the cmd program on.

## Create DB schema and table
There are 2 ways.
### 1. Use MySQL Workbench.
1.1 Connect to your mysql server.

1.2 Run 'CREATE SCHEMA `assignment` DEFAULT CHARACTER SET utf8;' to create the schema.

1.3 Open the sql script file in the /db dir under the project root dir. Run the sql script to create the employee table.

### 2. Use cmd. 
2.1 Run 'mysql -uroot -p'

2.2 Enter your password. You will enter into mysql command line.

2.3 Run sql script 'CREATE SCHEMA `assignment` DEFAULT CHARACTER SET utf8' to create the schema;

2.4 Run sql script 'source /your/project/dir/db/assignment_employee.sql' to create the employee table.