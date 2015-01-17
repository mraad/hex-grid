package com.esri.hex

import org.scalatest.FlatSpec

/**
  */
class HexRowColSpec extends FlatSpec {

  "HexRowCol(10,20)" should "return a row of 10 and a col of 20" in {
    val rowcol = HexRowCol(10, 20).toLong
    assertResult(10) {
      rowcol asRow
    }
    assertResult(20) {
      rowcol asCol
    }
  }

  "HexRowCol(123,456)" should "return a HexRowCol(123,456)" in {
    assertResult(HexRowCol(123,456)) {
      HexRowCol(123, 456).toLong.asRowCol()
    }
  }
}
