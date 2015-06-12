Main.java needs to be parameterized

In createConfiguration(...) we need to provide the Azure subscription ID

In getAccessTokenFromUserCredentials(...) in the AuthenticationContext the second string needs to be the Windows Azure AD Graph API Endpoint (in the old portal  Active Directory -> Microsoft -> View Endpoints)

In getAccessTokenFromUserCredentials(...) we need to provide the clientID and clientSecret in the ClientCredential constructor for our application. You generate these by creating a new application (in the old portal Active Directory -> Microsoft -> Applications search for SDKAuthTest and click Configure, pull the ClientID and create yourself a key)

