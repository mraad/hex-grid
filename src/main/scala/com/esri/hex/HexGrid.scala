package com.esri.hex

/**
  * RowCol case class
  *
  * @param row the row
  * @param col the column
  */
case class HexRowCol(var row: Long = 0L, var col: Long = 0L) {
  /**
    * Convert the row/col pair into a packed long.
    * The upper 32 bits contain the row value, and the lower 32 bits contain the column value.
    * Make sure the row and col values are positive for this to work "right" - define an origin value to HexGrid
    * That is at the lower left corner of your space domain.
    *
    * Very important! both row and col _have_ to be positive - make sure that the origX/Y of the hex grid are set to the min X/Y of the input set.
    *
    * @return packed row/col value.
    */
  def toLong = (row << Integer.SIZE) | col

  /**
    * Convert the row/col pair to a text
    */
  def toText = s"$row:$col"
}

/**
  * Trait to extend a class to define an x/y pair.
  */
trait HexXY {
  var x = 0.0
  var y = 0.0
}

case class Hex00() extends HexXY

/**
  * The HexGrid
  *
  * @param size  the size of hexagon from its center to its top vertex.
  * @param origX optional horizontal origin. Default value is 0.0 -  However, make sure that it is set to the min horizontal value of your input set.
  * @param origY optional vertical origin. Default value is 0.0 - However, make sure that it is set to the min vertical value of your input set.
  */
case class HexGrid(size: Double, origX: Double = 0.0, origY: Double = 0.0) {

  private val COS_30 = Math.cos(30.0 * Math.PI / 180.0)
  private val TWO_PI = Math.PI * 2.0
  private val PI_OVER_180 = Math.PI / 180.0

  private val cellH = size * COS_30
  private val cellV = size / 2.0

  private val skipH = 2.0 * cellH
  private val skipV = 3.0 * cellV

  private def proceedToNeighbor(x: Double, y: Double, center: HexXY, rowcol: HexRowCol) = {
    val deg = azimuthInDegrees(x, y, center)
    var newRow = 0L
    var newCol = 0L
    if (deg > 300.0) {
      if (rowcol.row % 2L != 0L) {
        newCol = rowcol.col
      }
      else {
        newCol = rowcol.col - 1L
      }
      newRow = rowcol.row + 1L
    }
    else if (deg > 240.0) {
      newRow = rowcol.row
      newCol = rowcol.col - 1L
    }
    else if (deg > 180.0) {
      if (rowcol.row % 2L != 0L) {
        newCol = rowcol.col - 1L
      }
      else {
        newCol = rowcol.col
      }
      newRow = rowcol.row - 1L
    }
    else if (deg > 120.0) {
      if (rowcol.row % 2L != 0L) {
        newCol = rowcol.col + 1L
      }
      else {
        newCol = rowcol.col
      }
      newRow = rowcol.row - 1L
    }
    else if (deg > 60.0) {
      newRow = rowcol.row
      newCol = rowcol.col + 1L
    }
    else {
      if (rowcol.row % 2L != 0L) {
        newCol = rowcol.col + 1L
      }
      else {
        newCol = rowcol.col
      }
      newRow = rowcol.row + 1L
    }
    rowcol.row = newRow
    rowcol.col = newCol
  }

  private def azimuthInDegrees(x: Double, y: Double, hexXY: HexXY) = {
    val az = Math.atan2(x - hexXY.x, y - hexXY.y) // Checked with Kerry C - this is correct as all is reversed due to azimuth
    if (az < 0.0) {
      (az + TWO_PI) / PI_OVER_180
    } else {
      az / PI_OVER_180
    }
  }

  private def isInside(x: Double, y: Double, hexXY: HexXY) = {
    val qx = (x - hexXY.x).abs
    val qy = (y - hexXY.y).abs
    if (qx > cellH || qy > size)
      false
    else {
      val qq = qx / cellH + qy / cellV - 2.0
      if (qq <= 0.0)
        true
      else
        qq < 0.0000001
    }
    // if (qx > cellH || qy > size) false else qx / cellH + qy / cellV <= 2.0
  }

  def convertRowColToHexXY(row: Long, col: Long, hexXY: HexXY): HexXY = {
    val xOffs = if (row % 2L != 0L) cellH else 0.0
    hexXY.x = col * skipH + xOffs + origX
    hexXY.y = row * skipV + origY
    hexXY
  }

  def convertRowColToHexXY(row: Long, col: Long): HexXY = {
    convertRowColToHexXY(row, col, Hex00())
  }

  def convertRowColToHexXY(rowcol: HexRowCol, hexXY: HexXY): HexXY = {
    convertRowColToHexXY(rowcol.row, rowcol.col, hexXY)
  }

  def convertRowColToHexXY(rowcol: HexRowCol): HexXY = {
    convertRowColToHexXY(rowcol.row, rowcol.col, Hex00())
  }

  def convertXYToRowCol(x: Double, y: Double) = {
    val row = Math.floor((y - origY) / skipV).toLong
    val col = Math.floor((x - origX) / skipH).toLong
    val tempXY = Hex00()
    val rowcol = HexRowCol(row, col)
    convertRowColToHexXY(rowcol, tempXY)
    var g = 10 // Guard against infinite loop for conditions when orig X/Y is _not_ at min X/Y of input set
    while (g > 0 && !isInside(x, y, tempXY)) {
      g -= 1
      proceedToNeighbor(x, y, tempXY, rowcol)
      convertRowColToHexXY(rowcol, tempXY)
    }
    rowcol
  }

  def convertHexXYToRowCol(hexXY: HexXY) = {
    convertXYToRowCol(hexXY.x, hexXY.y)
  }

}
