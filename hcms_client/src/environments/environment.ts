export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:9087/api' // adjust to your AuthService URL
};


/*
1. http://192.168.29.115:9087 or http://localhost:9087 -> HTTP accessible only within connected LANs
2. ngroq given public url -> https://tumultuous-proaction-libbie.ngrok-free.dev -> HTTPS widely accessible

Browser forbids HTTPS frontend -> HTTP backend -> does not work
Browser allows HTTP frontend -> HTTPS backend -> works
HTTPS frontend -> HTTPS backend -> also works (ideal for production)
*/
