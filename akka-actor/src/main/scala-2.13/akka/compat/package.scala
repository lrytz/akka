/*
 * Copyright (C) 2018 Lightbend Inc. <https://www.lightbend.com>
 */

package akka

package object compat {
  implicit class IteratorExtensions[T](private val it: Iterator[T]) extends AnyVal {
    // TODO: remove when 2.13.0-M5 is out
    def partition(p: T ⇒ Boolean): (Iterator[T], Iterator[T]) = {
      val (a, b) = it.duplicate
      (a.filter(p), b.filterNot(p))
    }
  }
}
