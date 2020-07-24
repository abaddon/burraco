# Burraco Card game
![Kotlin CI](https://github.com/abaddon/burraco/workflows/Kotlin%20CI/badge.svg?branch=master_kotlin&event=push) [![codecov](https://codecov.io/gh/abaddon/burraco/branch/master_kotlin/graph/badge.svg?token=I5MUK1OLTQ)](https://codecov.io/gh/abaddon/burraco/branch/master_kotlin) 

![Scala CI](https://github.com/abaddon/burraco/workflows/Scala%20CI/badge.svg?branch=master&event=push) [![codecov](https://codecov.io/gh/abaddon/burraco/branch/master/graph/badge.svg?token=I5MUK1OLTQ)](https://codecov.io/gh/abaddon/burraco) 

![Status](https://img.shields.io/static/v1?label=Status&message=Work%20In%20Progess&color=yellow)

### branches
- [master](https://github.com/abaddon/burraco/tree/master) (default branch) : CQRS version written in Kotlin
- [versions/scala_cqs](https://github.com/abaddon/burraco/tree/versions/scala_cqs) : CQS version written in Scala
- [versions/kotlin_cqs](https://github.com/abaddon/burraco/tree/versions/kotlin_cqs) CQS version written in Kotlin

## Todo List
Game context:
- [x] Implement Burraco game command model
- [ ] Implement Burraco game read model
- [ ] Implement Burraco game API

Score Board context:
- [ ] Implement Score Board context
- [ ] Implement Score Board context API

Player context:
- [ ] Implement Players context
- [ ] Implement Players context API

Clients
- [ ] Implement Web client
- [ ] Implement Mobile client

## Architecture
Based on:
* [CQRS - Command Query Responsibility Segregation](https://martinfowler.com/bliki/CQRS.html)

* [Hexagonal_architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software))
* [Domain Driven Design](https://martinfowler.com/tags/domain%20driven%20design.html)

![architecture schema](./docs/architecture-cqrs.jpg)

# API documentation
- [Postmand API documentation](https://documenter.getpostman.com/view/11592805/T1DpCxrz?version=latest)