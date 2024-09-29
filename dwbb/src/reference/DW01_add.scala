// SPDX-License-Identifier: Apache-2.0
// SPDX-FileCopyrightText: 2024 Jiuyang Liu <liu@jiuyang.me>
package oscc.dwbb.reference

import chisel3._
import org.chipsalliance.dwbb.interface.DW01_add

class DW01_add(parameter: DW01_add.Parameter) extends ReferenceModule(new DW01_add.Interface(parameter), parameter) {
  val sum: UInt = Wire(UInt((parameter.width + 1).W))
  sum := io.A +& io.B +& io.CI
  io.SUM := sum(parameter.width - 1, 0)
  io.CO := sum(parameter.width)
}
