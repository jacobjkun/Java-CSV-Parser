## Purpose
This project was created as part of the MS3 jr coding challenge.

The purpose of this project is to import a .csv file and transfer all good entries to an SQLite database of the same name as the .csv file using Java. Meanwhile, the bad entries will be written to a new .csv file called <csvName>-bad.csv and tallied in a log file <csvName>.log


## Approach and Assumptions
General Approach:

Because this project has to be able to efficently function with very large datasets, it imports the .csv file then splits the lines from the .csv at the commas into string arrays. Using those string arrays, it then creates prepared statements and adds them to batch instructions that would execute for every 1000 instructions. This allows the program to excute much more efficently then trying to run an sql command for every line, one at a time.

Assumptions for this project:

Based on the available data in the .csv file, there was no column with completly unique data values, so I assumed that it was acceptable for this database to have multiple entries with the same data. As such, there is no primary key set since that would not allow entries to share data values within the column. I also assumed that I was not to supposed to manipulate the data (like adding a unique identifier) since there was no mention of such in the given instructions.

## How to use
Step 1: Go to the SQLite download page: https://www.sqlite.org/download.html and download precompiled binaries from the Windows section.

Step 2: Download sqlite-shell-win32-*.zip and sqlite-dll-win32-*.zip 

Step 3: Create the folder C:\sqlite and unzip both above files to the sqlite folder (after which you should have sqlite3.def, sqlite3.dll and sqlite3.exe in the folder) 

Step 4: Add C:\sqlite in your PATH environment variable 

Step 5: Download the latest sqlite-jdbc driver from https://bitbucket.org/xerial/sqlite-jdbc/downloads/ and place it in the C:\sqlite folder (latest version is sqlite-jdbc-3.30.1.jar as time of writing, also included under the lib folder of this project)

Step 6: Create the folder db in C:\sqlite so that the file path C:\sqlite\db\ exists (this is where the Java code will create the db from the .csv file)

Step 7: Compile and execute parser.java

Step 8: The program will then prompt the user to enter the file path to the .csv file (example C:\users\yourUserName\downloads\), and then the name of the .csv file itself (including the .csv file extension), after that, the program should finish execution on its own.

Note: The badEntries .csv and .log file should be written out to the directory in which the Java program is being executed.


## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies
- `test`: contains sample output incase the program fails to compile/run on some one else's computer

## Dependency Management

The `JAVA DEPENDENCIES` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-pack/blob/master/release-notes/v0.9.0.md#work-with-jar-files-directly).


This java code is reliant on the sqlite-jdbc-3.30.1.jar which is included in the lib folder of this project.


