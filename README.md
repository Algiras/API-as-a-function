# API as a function 

We will delve into modelling an API using pure functions and try to solve all the problems we encounter on the way.

Request/Response model we will be using can be found [here](/src/main/scala/other/Base.scala).

Repository for presentation at Wix.com.

## Order of tutorial

- [FirstTry](/src/main/scala/FirstTry.scala) ([tests](/src/test/scala/FirstTryTest.scala))
- [AsyncRequest](/src/main/scala/AsyncRequest.scala) ([tests](/src/test/scala/AsyncRequestTest.scala))
- [Combine](/src/main/scala/Combine.scala) ([tests](/src/test/scala/CombineTest.scala))
- [ComposeHttpApp](/ComposeRequestHandler.scala) ([tests](/src/test/scala/ComposeHttpAppTest.scala))
- [ComposeHttpRoutes](/src/main/scala/ComposeHttpRoutes.scala) ([tests](/src/test/scala/ComposeHttpRoutesTest.scala))
- [IOService](/HandlerUsingIO.scala) ([tests](/src/test/scala/IOServiceTest.scala))
- [GeneralizeService](/GeneralizeHandler.scala) ([tests](/src/test/scala/GeneralizeServiceTest.scala))

## Prerequisite readings

- [Partial Function](https://www.scala-lang.org/api/current/scala/PartialFunction.html)
- [Monad](https://typelevel.org/cats/typeclasses/monad.html)
- [Monad Transformers](http://eed3si9n.com/herding-cats/monad-transfomers.html)
- [OptionT](https://typelevel.org/cats/datatypes/optiont.html)
- [Kleisli](https://typelevel.org/cats/datatypes/kleisli.html)
- [SemigroupK](https://typelevel.org/cats/typeclasses/semigroupk.html)

## Links

- [Original talk](https://www.youtube.com/watch?v=urdtmx4h5LE) this presentation is based on 
- [http4s](https://http4s.org/) approach this presentation is based on
- Libraries used:
  - [Cats](https://typelevel.org/cats/typeclasses.html)
  - [Cats Effects](https://typelevel.org/cats-effect/datatypes/)
  - [Monix](https://monix.io/)
- Compiler plugins:
  - [Kind Projector](https://github.com/non/kind-projector)
  
