// SPDX-License-Identifier: Apache-2.0
// SPDX-FileCopyrightText: 2024 Jiuyang Liu <liu@jiuyang.me>
package oscc.dwbb.wrapper.DW01_add

import chisel3.experimental.IntParam
import oscc.dwbb.interface.DW01_add._
import oscc.dwbb.wrapper.WrapperModule

import scala.collection.immutable.SeqMap

class DW01_add(parameter: Parameter)
    extends WrapperModule[Interface, Parameter](
      new Interface(parameter),
      parameter,
      p => SeqMap("width" -> IntParam(p.width))
    )
