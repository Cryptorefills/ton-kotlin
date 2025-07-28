@file:Suppress("PackageDirectoryMismatch")

package org.ton.kotlin.cell

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.DataCell
import org.ton.cell.VirtualCell

public interface CellContext {
    public fun loadCell(cell: Cell): DataCell

    public fun finalizeCell(builder: CellBuilder): Cell

    public companion object {
        public val EMPTY: CellContext = object : CellContext {

            override fun loadCell(cell: Cell): DataCell {
             // special‑case a truly empty cell (0 bits, 0 refs)
                if (cell.bits.isEmpty() && cell.refs.isEmpty()) {
                    return CellBuilder
                        .createCell { /* no bits, no refs */ }
                         as DataCell
                }
                if (cell is DataCell) return cell
                if (cell is VirtualCell && cell.cell is DataCell) return cell.cell
                throw IllegalArgumentException("Can't load ${cell::class} $cell")
            }

            override fun finalizeCell(builder: CellBuilder): Cell {
                return builder.build()
            }
        }
    }
}
