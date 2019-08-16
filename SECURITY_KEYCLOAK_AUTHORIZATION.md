# Overview

Suppose that we have to restrict access to user in MMF as follows.

| User | Position in the Company | MMF configuration | Paris plant | London plant |
| ------ | ------ | ------ | ------ | ------ |
| Richard | Plant production engineer | none | Can change plants equipments | Can only search plants equipments |
| John | Florida regional manager | Can only search configuration data | Can change plants equipments | Can change plants equipments |
| Bob | Support administrator | Can change MMF configuration (e.g. System Parameters) | none | none |

In this way, KeyCloak must be configured as follow:

| User | KeyCloak roles |
| ------ | ------ |
| Richard | mmf_admin_EUROPE_paris mmf_user_EUROPE_london |
| John | mmf_admin_EUROPE |
| Bob | mmf_config_admin |

One example of this assignment, can be viewed in the screenshot bellow:

![role admin](security_Authentication/RichardUser.png)

The user must also be insert in the mmf table as follows:

```
INSERT INTO mmf.adm_user_usr(
	usr_id, usr_id_usg, usr_login, usr_password, usr_name, usr_locale)
	VALUES (10, 1, 'richard', '', 'richard', 'en');
```

## KeyCloak role structure

Taking the example above, the following text will show how the roles mmf_admin_EUROPE_paris, mmf_user_EUROPE_london and mmf_config_admin are defined.

The first kind of users area: "admin" and "user". The admin has access to all scopes and the user are not allowed to change the database. 

In addition, users permission may differ according to its locations. Also, some entities are not location-dependent, such as MMF configuration (e.g. System Parameters).

In this way, MMF has the following role types:

* Location roles - Represents one location the groups an entity (e.g. Equipment)
* Operation roles - Represents which operations/scopes are possible to be done
* Location-dependent roles - Represents location and operations/scopes roles
    
### Operation roles

For this type of role, the operations are defined as attributes of role using the pattern "scope.\<resource>.\<scope>", as described in the examples bellow.

* **Configuration for role_admin** with all possible scopes defined
  ![role admin](security_Authentication/RolesAttributes/KeycloakRoleAdmin.png)
    
* **Configuration for role_user** with scopes that does not change the database
  ![role user](security_Authentication/RolesAttributes/KeycloakRolUser.png)

### Location roles

For this type of role, the location must be defined in the "location" attribute. This value must be obtained by the location id from the MMF database.

* location_EUROPE_paris  
 ![location_EUROPE_paris](security_Authentication/RolesAttributes/location_europe_paris.png)

* location_EUROPE_london
 ![location_EUROPE_paris](security_Authentication/RolesAttributes/location_europe_london.png)

One can also create a composite role and group locations. In the example bellow, location_EUROPE includes both location_EUROPE_paris and location_EUROPE_london.

* location_europe   
 ![location_europe](security_Authentication/RolesAttributes/location_EUROPE.png)

### Location-dependent roles

For this type of role, the role must associate:
* At least one location role
* At least one operation role

The pictures bellow show example of using different relations

* mmf_admin_EUROPE_paris with associated roles: role_admin and location_EUROPE_paris
	
 ![admin miami](security_Authentication/CompositeRoles/AdminEuropeParis.png)

* mmf_user_EUROPE_london with associated roles: role_user and location_EUROPE_london
	
 ![admin miami](security_Authentication/CompositeRoles/UserEuropeLondon.png)
             