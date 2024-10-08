// SPDX-License-Identifier: Apache-2.0
// SPDX-FileCopyrightText: 2024 Jiuyang Liu <liu@jiuyang.me>
package oscc.dwbb.interface.DW01_add

import chisel3._
import chisel3.experimental.SerializableModuleParameter
import upickle.default

object Parameter {
  implicit def rw: default.ReadWriter[Parameter] =
    upickle.default.macroRW[Parameter]
}
case class Parameter(width: Int) extends SerializableModuleParameter {
  require(width >= 1)
}
class Interface(parameter: Parameter) extends Bundle {
  val A:   UInt = Input(UInt(parameter.width.W))
  val B:   UInt = Input(UInt(parameter.width.W))
  val CI:  UInt = Input(UInt(1.W))
  val SUM: UInt = Output(UInt(parameter.width.W))
  val CO:  UInt = Output(UInt(1.W))
}
