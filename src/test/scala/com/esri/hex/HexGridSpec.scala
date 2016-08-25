package com.esri.hex

import org.scalatest.Matchers._
import org.scalatest._

/**
  */
class HexGridSpec extends FlatSpec {

  val cos = Math.cos(30.0 * Math.PI / 180.0)
  val size = 100.0
  val hexGrid = HexGrid(size)

  "Point(10,10)" should "return (0,0)" in {
    val rowcol = hexGrid.convertXYToRowCol(10, 10)
    val pointXY = hexGrid.convertRowColToHexXY(rowcol)
    assert(0 === pointXY.x)
    assert(0 === pointXY.y)
  }

  "Point(110,0)" should "be in the cell on the right" in {
    val rowcol = hexGrid.convertXYToRowCol(110, 10)
    val pointXY = hexGrid.convertRowColToHexXY(rowcol)
    assertResult(2 * size * cos) {
      pointXY.x
    }
    assertResult(0) {
      pointXY.y
    }
  }

  "Point(0,110)" should "be in the top right cell" in {
    val rowcol = hexGrid.convertXYToRowCol(0, 110)
    assertResult(1) {
      rowcol.row
    }
    assertResult(0) {
      rowcol.col
    }
    val pointXY = hexGrid.convertRowColToHexXY(rowcol)
    assertResult(size * cos) {
      pointXY.x
    }
    assertResult(size + size / 2.0) {
      pointXY.y
    }
  }

  it should "-7859566.073085553, 5097327.411990407" in {
    val hexGrid = HexGrid(50.0)
    val rowcol = hexGrid.convertXYToRowCol(-7859566.073085553, 5097327.411990407)
    rowcol should not be null
    rowcol.row shouldBe 67965
    rowcol.col shouldBe -90755
  }

  it should "-7859566.074, 5097327.411" in {
    val hexGrid = HexGrid(50.0)
    val rowcol = hexGrid.convertXYToRowCol(-7859566.074, 5097327.411)
    rowcol should not be null
    rowcol.row shouldBe 67964
    rowcol.col shouldBe -90754
  }
}
