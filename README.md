# Requester

# About
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![JavaFX](https://img.shields.io/badge/javafx-%23FF0000.svg?style=for-the-badge&logo=javafx&logoColor=whit)

Requester is a Java application designed to perform HTTP requests to URLs. It allows you to easily perform requests with parameters and visualize the responses in a clean way.

# Requirements
- <a href="https://www.oracle.com/java/technologies/javase/jdk20-archive-downloads.html">Java 20.0.1</a>
- <a href="https://gluonhq.com/products/javafx/">JavaFX SDK 17.0.10</a>
- <a href="https://search.maven.org/artifact/com.google.code.gson/gson/2.10/jar">Gson 2.10</a>

# Performing HTTP requests
  - ## GET
    - Select the "GET" method, insert the URL and click "Send". The response area will be update with an indented ```JSON``` object.
      
      ![image](https://github.com/docafavarato/requester/assets/98183878/d5750f90-ff25-41cb-8591-24e1e1a120b5)

  - ## POST
    - Select the "POST" method, insert the URL and click "Send". Additionally, it is possible to add body params in the shape of a ```JSON``` object at the "Body params" area.

      ![image](https://github.com/docafavarato/requester/assets/98183878/d03f498a-4a2e-4a28-b4ad-e73032bd374e)

  - ## PUT
    - Select the "PUT" method, insert the URL and click "Send". As in the "POST" section, it is also possible to add body params in the shape of ```JSON``` object.

      ![image](https://github.com/docafavarato/requester/assets/98183878/f73835f7-414d-415f-9dc9-cba980e7a987)

  - ## DELETE
    - Select the "DELETE" method, insert the URL and click "Send".
