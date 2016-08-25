package com.esri.hex

import org.scalatest.Matchers._
import org.scalatest._

/**
  */
class HexRowColSpec extends FlatSpec {

  "HexRowCol(10,20)" should "return a row of 10 and a col of 20" in {
    val rowcol = HexRowCol(10, 20).toLong
    rowcol.asRow shouldBe 10
    rowcol.asCol shouldBe 20
  }

  "HexRowCol(123,456)" should "return a HexRowCol(123,456)" in {
    assertResult(HexRowCol(123, 456)) {
      HexRowCol(123, 456).toLong.asRowCol()
    }
  }
}
