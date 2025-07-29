@file:Suppress("PackageDirectoryMismatch")

package org.ton.kotlin.cell

import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.DataCell
import org.ton.cell.VirtualCell
import org.ton.cell.CellDescriptor
import org.ton.cell.LevelMask
import kotlin.collections.emptyList

public interface CellContext {
    public fun loadCell(cell: Cell): DataCell

    public fun finalizeCell(builder: CellBuilder): Cell

    public companion object {
        public val EMPTY: CellContext = object : CellContext {
            override fun loadCell(cell: Cell): DataCell {
                // special-case a truly empty cell (0 bits, 0 refs)
                if (cell.isEmpty() && cell.refs.isEmpty()) {
                    // Create a proper DataCell with empty content
                    return DataCell(
                        descriptor = CellDescriptor.from(
                            levelMask = LevelMask(0),  // Zero level mask for empty cell
                            isExotic = false,
                            referenceCount = 0,
                            bitLength = 0
                        ),
                        bits = BitString.empty(),
                        refs = emptyList(),
                        hashes = emptyList()
                    )
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
