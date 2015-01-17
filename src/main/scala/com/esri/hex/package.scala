package com.esri

/**
  */
package object hex {

  implicit class LongConversions(l: Long) {

    private val MASK = (1L << Integer.SIZE) - 1L

    /**
     * HexRowCol(10,20).toLong asRowCol
     * @return a RowCol instance from a long
     */
    implicit def asRowCol(): HexRowCol = {
      HexRowCol(l asRow(), l asCol())
    }

    /**
     * val rowcol = HexRowCol(10,20).toLong
     * val row = packed asRow // 10
     * @return the row value as the upper 32 bits of the supplied long value
     */
    implicit def asRow(): Long = {
      (l >> Integer.SIZE) & MASK
    }

    /**
     * val rowcol = HexRowCol(10,20).toLong
     * val col = packed asCol // 20
     * @return the col value as the lower 32 bits of the supplied long value
     */
    implicit def asCol(): Long = {
      l & MASK
    }
  }

}
