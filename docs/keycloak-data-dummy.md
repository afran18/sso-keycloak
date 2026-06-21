# Clients
1) App A - http://localhost:3000
2) App B - http://localhost:3001

# Client Secrets:
    - App A : 
    - App B : 

# Users:
| username | email              | password    | id                                   |
|----------|--------------------|-------------|--------------------------------------|
| afran    | afran@testmail.com |  | f75271af-d205-4222-89a6-bba93ce2451f |

## OIDC URL
    **Configuration** : http://localhost:8080/realms/company-realm/.well-known/openid-configuration
    **JWKS URL** : http://localhost:8080/realms/company-realm/protocol/openid-connect/certs

## Login URL App A
- http://localhost:8081/auth/login