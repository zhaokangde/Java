# Keycloak
## Basic Configuration

Follow the steps below to config Keycloak:

*Requirements: Java JDK 8 or higher*

1. Download Keycloak [here](https://www.keycloak.org/downloads.html) (version 5.0.0 recommended) and extract it
2. By default, the DBMS (Data Base Management System) of the Keycloak is [H2](https://www.h2database.com/), but it is recommended to switch to [PostgreSQL](https://www.postgresql.org/), however, if it is not of your interest, skip to next step
    1. In **PostgreSQL**, create new database called **keycloak** to store Keycloak program data
    2. Download JDBC Driver [here](https://jdbc.postgresql.org/download.html) compatible with your version of PostgreSQL DBMS and JDK
    3. In "{downloaded-keycloak-directory}\modules\system\layers\base\org\\" directory, create new directory called **postgresql** and in this new directory create another new directory called **main**
    4. Paste the JDBC Driver (JAR file) downloaded previously in **main** directory
    5. Create the new XML file called **module.xml** in the same directory and paste the follow content into it, replacing the path value **postgresql-42.2.5.jar** for the JDBC Driver file name:
        ```
        <?xml version="1.0" encoding="UTF-8"?>
        <module name="org.postgresql" xmlns="urn:jboss:module:1.5" >
          <resources>
            <resource-root path="postgresql-42.2.5.jar"/>
          </resources>
          <dependencies>
             <module name="javax.api"/>
             <module name="javax.transaction.api"/>
           </dependencies>
        </module>
        ```
    6. In "{downloaded-keycloak-directory}\standalone\configuration\standalone.xml", add new ```driver``` tag as shown below in ```drivers``` tag
        ```
        <driver name="postgresql" module="org.postgresql">
            <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
        </driver>
        ```
    7. Still in **standalone.xml** file, add new ```datasource``` tag like shown below in ```datasources``` tag (Modify the ```connection-url```, ```user-name``` and ```password``` if necessary)
        ```
        <datasource jta="true" jndi-name="java:jboss/datasources/PostgreSQLDS" pool-name="PostgreSQLDS" enabled="true" use-java-context="true">
            <connection-url>jdbc:postgresql://localhost:5432/keycloak</connection-url>
            <driver>postgresql</driver>
            <transaction-isolation>TRANSACTION_READ_COMMITTED</transaction-isolation>
            <pool>
                <min-pool-size>10</min-pool-size>
                <max-pool-size>100</max-pool-size>
                <prefill>true</prefill>
            </pool>
            <security>
                <user-name>postgres</user-name>
                <password>*****</password>
            </security>
            <statement>
                <prepared-statement-cache-size>32</prepared-statement-cache-size>
                <share-prepared-statements>true</share-prepared-statements>
            </statement>
        </datasource>
        ```
    8. Finally, still in **standalone.xml** file, find ```<spi name="connectionsJpa">``` tag and modify ```<property name="dataSource" value="java:jboss/datasources/KeycloakDS"/>``` to ```<property name="dataSource" value="java:jboss/datasources/PostgreSQLDS"/>```
3. Run "{downloaded-keycloak-directory}/bin/standalone.sh"
4. Access ```http://localhost:8080/auth```
5. Create Keycloak's User and Password
6. In **Administration Console**, use your credentials created above
7. On top-left side, click on **Master > Add Realm**
8. Click on **Select File** and choose file **config/realm-export.json** and click on **Create**
   1. Make sure that **Web Origins** is valid
   2. Make sure that **Root URL** is valid
9. In **Clients**, select **mmf-adm-service** and in the **Credentials** tab, click on **Regenerate Secret**
    1. Copy the generated secret and replace the **keycloak.credentials.secret** key value in the file "{downloaded-source-directory}\mmf-adm\adm-service\src\main\resources\application.properties":
        ```
        keycloak.credentials.secret=899c4d49-8ab2-441f-a3d4-b5afc19ab5b9
        ```
    2. Repeat the process for **mmf-master-service** and **mmf-report-service**.
10. Create **User Federation** Provider and import **Users**:
    1. In **User Federation**, select **ldap** option in **Add provider...** drop-down list in the middle of the screen or the top right corner.
    2. Configure your LDAP Provider with your configuration, similar to the one shown in the sample image below. For more details, learn **[Keycloak User Storage Federation Documentation](https://www.keycloak.org/docs/5.0/server_admin/index.html#_user-storage-federation)**.
        ![success-syncronization-ldap-users](../docs/keycloak_configuration/img/ldap-configuration-example.png)
    3. Click in **Synchronize all users** button and check that the message below will appear indicating that users have been successfully imported:
        ![success-syncronization-ldap-users](../docs/keycloak_configuration/img/success-syncronization-ldap-users.png)
    4. Make sure that users have been imported by clicking the **View all users** button in **Users** menu option.

NOTICE: The imported **realm-export.json** have LDAP configurations of the MMF development environment, it can be used to help your own environment.

## Enabling/Disabling Keycloak

To disable/enable keycloak:

1. mmf-backend
    1. Change the property **keycloak.enabled** to true or false in all application.properties files located at "{module-path}\adm-service\src\main\resources\application.properties".
2. mmf-frontend
    1. Change the environment variable **VUE_APP_KEYCLOAK_ENABLE** to true or false at file "mmf-frontent/mmf-web/.env.development".
 
## Themes

Follow the steps below to deploy **siemens-mmf** login theme:

1. Copy **siemens-mmf** to "{downloaded-keycloak-directory}/themes/"
2. Access ```http://localhost:8080/auth```
3. In **Administration Console**, use your credentials to access
4. Go to **Clients**, and choose your Client that you want to change the login screen
5. In **Settings > Login Theme**, choose **siemens-mmf**
6. Save changes.