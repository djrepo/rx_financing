## Building and Running

In order to build and test application run:
mvn clean package
Once jar file is created you can run it
java target/assignment-factoredInvoice-0.0.1-SNAPSHOT.jar

## Maven profiles:
* local - this profile is active by default, it activates spring profile h2-basic
* dev - this profile can be activated by -P in command e.g. mvn clean package -Pdev, it activates spring profile psql-basic

## Database connections :
In these files are defined jdbc connections application-h2.yml application-psql.yml application-h2test.yml
* h2 - database is stored in file test.mv.db, you are free to manually remove this file whenever you need fresh empty db
* h2test - database is stored in memory, after application finish data are lost
* psql - preinstalled postgresSql database, you need to edit this file according to your environment

## Data Seeding :
Program at startup create database schema and fill example data to created tables.
* basic - this seeding option is small, it use 15 invoices, 3 Purchasers, 3 Creditors
* pst - this seeding option is bigger for performance testing, it use 200 000 invoices, 1000 purchasers, 1000 Creditors
        it is created by randomly generated data

## Spring profile activation:
Activation is done in application.yml file with property spring.profiles.active. You can edit this property manually or maven can substitute this value in maven build
* h2-basic - this profile will use h2 db connection and data set option basic
* h2-pst - this profile will use h2 db connection and data set option pst
* psql-basic - this profile will use psql db connection and data set option basic
* psql-pst - this profile will use psql db connection and data set option pst
* h2test-basic - this profile will use h2test db connection and data set option basic,
                 it is active for integration test and it will prevent normal run of application,
                 instead of seeding data and running financing by application itself, junit will define what to do

## Batch settings:
Invoice table can be huge therefore program is financing invoices in a smaller batches.
You can change batch size with property crx.batch-size in application.yml file.

## Solution descriptions :
Method FinancingService.finance() is the entry point where entire process of financing Invoices started.
Process need reference data. In order to speed it up reference data are loaded to cache. Each time method is called cache is invalidated and reloaded again.
# Caching (dis)advantage :
* advantage - Thanks to cache mechanism it looks that process can finance high volume of invoice in less then few minutes.
* disadvantage - It is needed to prevent users of doing changes to reference data in short time window.
                 We can for example run process (scheduled it) at night time once a day.

Method FinancingService.finance() consist of two parts
* creating cache
* main batch loop

# Main batch loop
At the very beginning app is look how many invoices are not financed (totalNotFinancedInvoices). Goal is to finance this amount and not to go any further.
This approach will prevent application to run in endless loop - because new invoices can be appending to the invoice table.
Cached reference data need to be invalidate from time to time we cannot run finance for a longer period of time.
Invoices are received ordered by id so we will load the oldest one first.




