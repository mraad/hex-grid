package com.esri

/**
  */
package object hex {

  implicit class LongImplicits(l: Long) {

    private val MASK = (1L << Integer.SIZE) - 1L

    /**
     * HexRowCol(10,20).toLong toRowCol
     * @return a RowCol instance from a long
     */
    implicit def toRowCol(): HexRowCol = {
      HexRowCol(l toRow(), l toCol())
    }

    /**
     * val rowcol = HexRowCol(10,20).toLong
     * val row = packed toRow // 10
     * @return the row value as the upper 32 bits of the supplied long value
     */
    implicit def toRow(): Long = {
      (l >> Integer.SIZE) & MASK
    }

    /**
     * val rowcol = HexRowCol(10,20).toLong
     * val col = packed toCol // 20
     * @return the col value as the lower 32 bits of the supplied long value
     */
    implicit def toCol(): Long = {
      l & MASK
    }
  }

}
