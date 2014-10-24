This folder holds the scripts used by the DBMSProject application. Data Definition scripts are to be located in ~/scripts/DDL, while Data Manipulation scripts are to be located in ~/scripts/DML.

Within the ~/scripts/DDL folder are two .txt files, Initialize.txt and Clear.txt. These are there for the user to modify at their will and will govern which DDL scripts should be executed upon a successful connection to the database and upon a successful disconnection from the database respectively. To add a script to be executed simply add a new line with the name of the sql file to be executed. Note that the extension .sql is optional as the application ensures that the extension is present during execution. To remove a script simply delete the line in question. Do note that whitespace between script names or trailing after the final script name may hinder the application. For example, the following could lead to complications: (\r = carriage return \w = any amount of whitespace)

Bad Example 1:
--------------------------------
first.sql\r
\r\w
second.sql

--------------------------------
Bad Example 2:
--------------------------------
first.sql\r
\w
--------------------------------

It is important to remember that the order in which scripts are listed matters when those scripts contain dependencies, such as foreign key references, and that table drop scripts should be executed in the reverse order that they were defined to minimize the likelihood that a foreign key restraint will be violated.

Within the ~/scripts/DML folder is where the user's query scripts go. Most scripts can be successfully run through this dynamic script loading system however there are limitations. At this time queries that require user input cannot be handled dynamically and must be hard coded. Also scripts containing PL/SQL syntax appear to have problems as well so it is best to avoid those scripts with this system.

There may be non-sql files in ~/scripts/DML however they are of no consequence to the user and are merely for debugging purposes.