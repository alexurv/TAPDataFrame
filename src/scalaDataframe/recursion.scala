package scalaDataframe

import scala.jdk.CollectionConverters._
import javaDataframe.factory.{CSVFactory, Dataframe}

import scala.annotation.tailrec

class DataFrame[T]{
  private val df:Dataframe[T] = new Dataframe[T](new CSVFactory(), "cities.csv")

  def getColumn(label:String):List[T] = df.getColumn(label).asScala.toList

}

object Run extends scala.App{

  val df = new DataFrame

  // A numeric column
  val listF = df.getColumn("LonS").map((s: String) => s.toInt)

  // A String column
  val listS = df.getColumn("State")

  // stack recursion
  def listFilterMap[T](f:T => Boolean, f2:T => T, l:List[T]) : List[T] = l match {
    case Nil => Nil
    case x :: xs => if (f(x)) f2(x)::listFilterMap(f, f2, xs)
                    else x::listFilterMap(f, f2, xs)
  }

  // tail recursion (uses an auxiliary function so that listFilterMap definition won't change)
  def listFilterMap2[T](f:T => Boolean) (f2:T => T) (l:List[T]) : List[T] = {
    @tailrec
    def listFilterMapAccum(f:T => Boolean, f2:T => T, l:List[T], accum: List[T]): List[T] = {
      l match {
        case Nil => accum
        case x :: xs => if (f(x)) listFilterMapAccum(f, f2, xs, f2(x)::accum)
                        else listFilterMapAccum(f, f2, xs, x::accum)
      }
    }
    listFilterMapAccum(f, f2, l, Nil)
  }

  // Round the values of a float-type column that are greater than a value (e.g., 100)
  val num: Float = 100

  def greater = (elem: Int) => elem.toFloat > num

  def round = (elem: Int) => elem.toFloat.round

  println(listFilterMap(greater, round, listF))


  // Replace a certain word from a string-type column on the elements that contain that word
  val str : String = "NOT A STATE"

  def compare = (elem:String) => elem.equals("OH")

  def replacei = (elem:String) => elem.replace(elem, str)

  // Partial parametrization
  var replaceState = listFilterMap2(compare) (replacei) _
  println(replaceState(listS))

  //println(listFilterMap2(compare) (replacei) (listS))

  class T
}

