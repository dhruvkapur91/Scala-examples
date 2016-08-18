package dynamic

object TryingDynamic extends App {

  import scala.language.dynamics

  class Animal extends Dynamic {
    def _select_(name:String) = name
    def _invoke_(name:String, args:Any*) = _select_(name)

    def selectDynamic(name : String) = name
  }

  println {
    new Animal().foo
  }

}
