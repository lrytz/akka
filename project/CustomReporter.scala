package akka

import sbt._
import Keys._
import KeyRanks.DTask
import xsbti.{Reporter, Problem, Position, Severity}

object CustomReporter {
  private lazy val compilerReporter = TaskKey[xsbti.Reporter](
    "compilerReporter",
    "Experimental hook to listen (or send) compilation failure messages.",
    DTask
  )

  val ignoreWarnings = Seq(
    compilerReporter in (Compile, compile) :=
      new xsbti.Reporter {
        private val buffer = collection.mutable.ArrayBuffer.empty[Problem]
        def reset(): Unit = buffer.clear()
        def hasErrors: Boolean = buffer.exists(_.severity == Severity.Error)
        def hasWarnings: Boolean = buffer.exists(_.severity == Severity.Warn)
        def printSummary(): Unit = {

          print("\033c")
          if (problems.nonEmpty) {
            problems.foreach{ p =>
              println("=====================================================")
              println(p.position)
              println(p.message)
              println()
              println()
            }
          }
        }
        def problems: Array[Problem] = buffer.toArray

        def log(problem: Problem): Unit = {
          if (problem.severity == Severity.Error) {
            buffer.append(problem)
          }
        }
        def log(pos: Position, msg: String, sev: Severity): Unit = {
          log(new Problem {
            def category: String = "foo"
            def severity: Severity = sev
            def message: String = msg
            def position: Position = pos
          })
        }
        def comment(pos: xsbti.Position, msg: String): Unit = ()
      }
  )
}
