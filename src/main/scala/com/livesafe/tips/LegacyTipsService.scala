package com.livesafe.tips

import scala.concurrent.Future
import scala.io.Source

object LegacyTipsService {

  import scala.concurrent.ExecutionContext.Implicits.global

  def fetchLegacy1Tips: Future[String] = Future { Source.fromResource("legacy-1-tips.csv").mkString }
  def fetchLegacy2Tips: Future[String] = Future { Source.fromResource("legacy-2-tips.csv").mkString }

}
