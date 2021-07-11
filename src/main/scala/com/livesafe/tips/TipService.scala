package com.livesafe.tips

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.livesafe.tips.TipService.Tip

import scala.collection.mutable.ListBuffer

trait TipService {

  def getTips: Future[List[Tip]]
  def getTip(id: UUID): Future[Tip]

}

class TipServiceImpl extends TipService {

  /**
   * FIXME: Replace this with your implementation. This stub is provided as an example of how to load the legacy data from [[LegacyTipsService]]
   *
   * @return
   */
  def getTips: Future[List[Tip]] = {

    val legacyTipsData: ListBuffer[Tip] = ListBuffer()

    val rawLegacy1TipsDataMap : Map[String, String] = Map[String, String](
      "tipid" -> "tipId",
      "Type" -> "tipType",
      "created" -> "createdDt",
      "msg" -> "message"
    )

    val rawLegacy2TipsDataMap  : Map[String, String] = Map[String, String](
      "tipId" -> "tipId",
      "type" -> "tipType",
      "createdAt" -> "createdDt",
      "message" -> "message"
    )

    def createTipObj(fieldMap: Map[String, String], fieldNames : Array[String], tipRecord: String) : Tip = {
      tipRecord.split(",")
        .map(s => {
          val tipObj : Tip = new Tip()
          for(i <- 0 to fieldNames.length) {
            val fieldName: String = fieldMap.get(fieldNames(i)).toString
            if (fieldName == "tipId") {
              tipObj.setTipId(s)
            } else if (fieldName == "tipType") {
              tipObj.setTipType(s)
            } else if (fieldName == "createdDt") {
              tipObj.setCreatedDt(s)
            } else if (fieldName == "message") {
              tipObj.setMessage(s)
            }
          }
          tipObj
        })
        .head
    }

    def loadRawLegacyData(fieldMap: Map[String, String], legacyTipsData: Future[String]): Future[List[Tip]] = {

      legacyTipsData.map(s => {
        val data = s.splitAt(1)
        val fieldNames = data._1.split(",")
        val fieldValuesList = data._2.split("\n").toList

        fieldValuesList.map(fv => createTipObj(fieldMap, fieldNames, fv))
      })
    }

    val rawLegacy1TipsData = loadRawLegacyData(rawLegacy1TipsDataMap, LegacyTipsService.fetchLegacy1Tips)
    val rawLegacy2TipsData = loadRawLegacyData(rawLegacy2TipsDataMap, LegacyTipsService.fetchLegacy2Tips)

    legacyTipsData += rawLegacy1TipsData
    legacyTipsData += rawLegacy2TipsData

    legacyTipsData.sortBy(rec => rec.createdDt).reverse

    Future(legacyTipsData.toList)

  }

  /**
   * FIXME: Replace this with your implementation. This stub is provided as an example of how to load the legacy data from [[LegacyTipsService]]
   *
   * @return
   */
  def getTip(id: UUID): Future[Tip] = {
    val legacyTipsData: ListBuffer[Tip] = ListBuffer()

    val rawLegacy1TipsDataMap : Map[String, String] = Map[String, String](
      "tipid" -> "tipId",
      "Type" -> "tipType",
      "created" -> "createdDt",
      "msg" -> "message"
    )

    val rawLegacy2TipsDataMap  : Map[String, String] = Map[String, String](
      "tipId" -> "tipId",
      "type" -> "tipType",
      "createdAt" -> "createdDt",
      "message" -> "message"
    )

    def getTipObj(fieldMap: Map[String, String], fieldNames : Array[String], tipRecord: String, id: String) : Tip = {
      var tipObj : Tip = null
      tipRecord.split(",")
        .map(s => {
          for(i <- 0 to fieldNames.length) {
            if(fieldMap.get(fieldNames(i)).toString == "tipId" && s == id) {
              tipObj = new Tip()
              for(j <- 0 to fieldNames.length) {
                val fieldName: String = fieldMap.get(fieldNames(j)).toString
                if (fieldName == "tipId") {
                  tipObj.setTipId(s)
                } else if (fieldName == "tipType") {
                  tipObj.setTipType(s)
                } else if (fieldName == "createdDt") {
                  tipObj.setCreatedDt(s)
                } else if (fieldName == "message") {
                  tipObj.setMessage(s)
                }
              }
            }
          }
        }).head
      return tipObj
    }

    def findTip(fieldMap: Map[String, String], legacyTipsData: Future[String], id: String): Future[Tip] = {

      legacyTipsData.map(s => {
        val data = s.splitAt(1)
        val fieldNames = data._1.split(",")
        val fieldValuesList = data._2.split("\n").toList

        fieldValuesList.map(fv => getTipObj(fieldMap, fieldNames, fv, id)).head
      })
    }

    val rawLegacy1TipsData = findTip(rawLegacy1TipsDataMap, LegacyTipsService.fetchLegacy1Tips, id.toString)
    if (rawLegacy1TipsData != null)
      rawLegacy1TipsData
    else {
      val rawLegacy2TipsData = findTip(rawLegacy2TipsDataMap, LegacyTipsService.fetchLegacy2Tips, id.toString)
      rawLegacy2TipsData
    }
  }

}

object TipService {
  /**
   * FIXME: We need a proper data model for 'Tip'!
   */
  type Tip = TipData
}

class TipData(var tipId: String, var tipType: String, var createdDt : String, var message: String) {
  def getTipId: String = tipId
  def getTipType: String = tipType
  def getCreatedDt: String = createdDt
  def getMessage: String = message

  def setTipId(tipId: String): Unit = {
    this.tipId = tipId
  }

  def setTipType(tipType: String): Unit = {
    this.tipType = tipType
  }

  def setCreatedDt(createdDt: String): Unit = {
    this.createdDt = createdDt
  }

  def setMessage(message: String): Unit = {
    this.message = message
  }

  def this() {
    this("", "", "", "")
  }
}