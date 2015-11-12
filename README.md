# cash-register
Simple experimental cash register application.  

===== SETUP =====
1. Install a mysql server locally  
2. Set up 'root' user on mysql with password 'password'  
3. create register database on mysql with: create database register;  
4. Initialize the database by running main class {com.roozen.register.Client} with arguments {--client.action=init}  
5. Load items into the database by running main class {com.roozen.register.Client} with arguments {--client.action=items --client.file=<path/to/items.csv>}  
6. Kick off the application by running main class {com.roozen.register.Server}  
7. ???  
8. Profit  
  
   Alternatively, if you don't want to provide the command line arguments, you could include a properties file on the
classpath for spring boot with the client.<property> properties supplied in the file. If the items file does not include
a header row, specify this with the property client.includeHeader=false. Otherwise, the first line in the file will be
skipped.
  
=================

REST URLs follow the format: controller/<controller name>/<optional: descriptor>/<action>  
For example: controller/order/find  