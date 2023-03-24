# Alpha Flash Select Java Sample

This is a sample java client for Alpha Flash Select.

## Configuration

Service credentials should be placed into a file called 'credentials.properties', with a _username_ and 
_password_ property set.

```properties
username=XXXXX
password=XXXX
```

## Running the Sample

The sample can be run with the included gradle wrapper:

    ./gradlew run

## Dependencies

| Name                  | Purpose               | Links
| --                    | --                    | --
| Jackson               | JSON Serialization    | [Source](https://github.com/FasterXML/jackson)
| Apache HTTP Client    | Http Requests         | [Website](https://hc.apache.org/httpcomponents-client-5.0.x/)

## Code

 * [DTO Package](src/main/java/com/alphaflash/select/dto) - This contains classes to be unmarshalled from the API. 
 * [STOMP Package](src/main/java/com/alphaflash/select/stomp) - This is a simple core-java com.alphaflash.select.stomp client. 
 * [com.alphaflash.select.Main Class](src/main/java/com/alphaflash/select/Main.java) - This contains the main method. 
