## flights

Project is build using [Maven build tool](https://maven.apache.org/). 
To build project please install Maven and run following command from project root folder.

`mvn clean install`

To run program navigate to \target folder and run command

`java -jar flights-0.9.jar --server.address=<HOST> --server.port=<PORT> --server.servlet.context-path=<CONTEXT>`

or just 

`java -jar flights-0.9.jar`

to run program with default host: `localhost:8080` and with context path: `/api` 

Default values for host, port and context could also be set in '\src\main\resources\application.properties' file. 
