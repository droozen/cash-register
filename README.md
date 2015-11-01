# cash-register
Simple experimental cash register application.

===== SETUP =====
1. Install a mysql server locally
2. Set up 'root' user on mysql with password 'password'
3. create register database on mysql with: create database register;
4. Initialize the database by running main class {com.roozen.register.main.Client} with arguments {init}
5. Load items into the database by running main class {com.roozen.register.main.Client} with arguments {items <path/to/items.csv>}
6. Kick off the application by running main class {com.roozen.register.main.Server}
7. ???
8. Profit

=================

REST URLs follow the format: controller/<controller name>/<optional: descriptor>/<action>
For example: controller/order/find