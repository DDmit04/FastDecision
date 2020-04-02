# About FastDecision

FastDecision is a web application for fast online votings.
[Project link](https://fastdecision.herokuapp.com)

# App functionality

* Create votings with your own title and vote options.
* Remove your unsuccessful voitngs.
* Vote in real time.
* Protect votings with unic voting key.
* Input voting keys onse and save them in cookies.
* Search votings by it's title.
* See most popular and newest votings.
* Authorize by google and github OAuth
* Save votings on your account.
* Hide your own votings from other users.
* Light and darck themes.

# Tecnologies

* Backend - Spring boot
* Frontend - Vue.js + vuetify
* Database - PostgreSQL

# Documentation

Clone [git submodule repo](https://github.com/DDmit04/FastDecisionDocs/tree/f06c9d3bd2391f834de8badc2472beefaf4d53b3) 'docs' and run index.html (javadoc and styleguide) or just oprn files on browser

# Run localy

```
git clone git@github.com:DDmit04/FastDecision.git
```

To start frontend hot-reload server:

```
cd FastDecision
yarn install
yarn start
```

By default it running on http://localhost:8002

You can configure port in webpack.dev.js
```
devServer: {
        contentBase: './dist',
        compress: true,
        port: 8002,
        allowedHosts: [
            'localhost:9002'
        ],
        stats: 'minimal',
        clientLogLevel: 'error',
    },
```

Before start spring boot app don't forget configure application.yaml for your database:
```
 datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost/votingsdb}
    username: ${SPRING_DATASOURCE_USERNAME:postgres}
    password: ${SPRING_DATASOURCE_PASSWORD:sa}
  jpa:
    generate-ddl: false
    show-sql: false
    hibernate.ddl-auto: validate
    properties:
      hibernate:
        jdbc:
          lob:
            non_contextual_creation: true
```

And if you need - server port to run backend:
```
server:
  port: 9002
```
<strong>New Port must be added to allowedHosts in webpack.dev.js</strong>

Then start spring boot application in IDE and app will be running on http://localhost:9002 (by default)
