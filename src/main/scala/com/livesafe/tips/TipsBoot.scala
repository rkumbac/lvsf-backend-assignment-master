package com.livesafe.tips

object TipsBoot extends App {

  /**
   * Fixme: Provide your own tip service
   */
  val tipService = new TipServiceImpl()

  val tipHttpService = new TipsHttpService(tipService)

  tipHttpService.start()

}
