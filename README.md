[![Build status](https://dev.azure.com/joamart/CuscaCasa/_apis/build/status/CuscaCasa-Android-CI)](https://dev.azure.com/joamart/CuscaCasa/_build/latest?definitionId=10)

# Cusca Casa

This is an Android App coded in Kotlin to reach out to an Azure SQL Database containing readings emitted by an IoT Sensor, and present a series of visual charts and useful information.

This is a sister project to the https://github.com/lokijota/Azure-RPi-BME680 repo, which describes the IoT Sensor, what information is being uploaded, and how it is stored.

## Configuration

For the code to compile a file called `connection_string.xml` must be created in the folder `\app\src\main\res\values` with the connection string to the Azure SQL Database. This must have the following format:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <item name="connection_string" type="string">jdbc:jtds:sqlserver://YOURSERVER.database.windows.net:1433;DatabaseName=YOURDATABASE;user=YOURUSER;password=YOURPASSWORD;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;</item>
</resources>
```

You can obtain this connection string from the Azure Portal. Remember to add the right prefix as per the example above.
